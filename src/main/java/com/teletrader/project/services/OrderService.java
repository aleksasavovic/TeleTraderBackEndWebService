package com.teletrader.project.services;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import com.teletrader.project.entities.LimitOrder;
import com.teletrader.project.entities.OrderBook;
import com.teletrader.project.entities.OrderBookElement;
import com.teletrader.project.entities.Stock;
import com.teletrader.project.entities.User;
import com.teletrader.project.entities.UserStock;
import com.teletrader.project.entities.UserStockCompositKey;
import com.teletrader.project.exceptions.DeficientResourcesException;
import com.teletrader.project.exceptions.InvalidOrderException;
import com.teletrader.project.exceptions.ResourceNotFoundException;
import com.teletrader.project.repositories.OrderJpaRepository;
import com.teletrader.project.repositories.StockJpaRepository;
import com.teletrader.project.repositories.UserJpaRepository;
import com.teletrader.project.repositories.UserStockJpaRepository;


@Service
public class OrderService {

	@Autowired
	private UserJpaRepository userJpaRepository;
	@Autowired
	private OrderJpaRepository orderJpaRepository;
	@Autowired
	private StockJpaRepository stockJpaRepository;
	@Autowired
	private UserStockJpaRepository userStockJpaRepository;
	@Autowired
	private StockService stockService;
	@Autowired
	private UserService userService;
	
	Logger logger = LoggerFactory.getLogger(OrderService.class);
	
	public User getUser(String username) {
		return userJpaRepository.findById(username).get();
	}
	
	public Set<UserStock> getUserOwnedStocks(String username){
		User u=userJpaRepository.findById(username).get();
		Set<UserStock> userStocks=u.getOwnedStocks();
		return userStocks;
		
	}
	
	public OrderBook getOrderBook(Integer stockId) {
		Optional<Stock> s=stockJpaRepository.findById(stockId);
		if(s.isEmpty())
			return null;
		CompletableFuture<List<OrderBookElement>>topSellOrders=  orderJpaRepository.getTop10SellOrdersForStock(stockId);
		CompletableFuture<List<OrderBookElement>>topBuyOrders=  orderJpaRepository.getTop10BuyOrdersForStock(stockId);
		OrderBook orderBook=null;
		try {
			orderBook = new OrderBook(topBuyOrders.get(),topSellOrders.get());
		} catch (InterruptedException e) {
			logger.warn(e.getMessage());
		} catch (ExecutionException e) {
			logger.warn(e.getMessage());
		}
		return orderBook;
	}
	
	public void addLimitOrder( String username, Integer stockId ,LimitOrder limitOrder) {
		User u=userJpaRepository.findById(username).get();
		Stock s=stockJpaRepository.findById(stockId).get();
		limitOrder.setStock(s);
		limitOrder.setUser(u);
		Integer id = orderJpaRepository.getMaxId()+1;
		limitOrder.setId(id);
		switch(limitOrder.getOrderType()) {
		//we have two cases for limit orders: BUY or SELL
		/*
		 * 
		 * ---------------------------------CHECK IF USER HAS SELL OFFER HIGHER THAN HIS NEW BUY OFFER AND VICE VERSA------------------------
		 * 
		 */
		case BUY:{
			if(limitOrder.getAmountToTrade()*limitOrder.getPriceToOrderAt()<=u.getCash()) {
			LimitOrder lowestSellOrderForUser= orderJpaRepository.getLowestSellOrderForUser(stockId, username);
			if(lowestSellOrderForUser==null || (lowestSellOrderForUser!=null && limitOrder.getPriceToOrderAt()<lowestSellOrderForUser.getPriceToOrderAt()))
				fillBuyLimitOrder(limitOrder);
			else throw new InvalidOrderException("user has sell order active under this price"); 
				
		
		}
			else throw new DeficientResourcesException("Not enough cash");
			break;
		}
		case SELL:{
			Optional<UserStock> userStockOptional= userStockJpaRepository.findById(new UserStockCompositKey(username,stockId));
			if(userStockOptional.isPresent() && limitOrder.getAmountToTrade()<=userStockOptional.get().getAmount()) {
			LimitOrder highestBuyOrderForUser= orderJpaRepository.getHighestBuyOrderForUser(stockId, username);
			if(highestBuyOrderForUser == null || (highestBuyOrderForUser !=null && limitOrder.getPriceToOrderAt()>highestBuyOrderForUser.getPriceToOrderAt()))
				fillSellLimitOrder(limitOrder);
			else throw new InvalidOrderException("user has buy order active over this price"); 
		}
		
			else throw new DeficientResourcesException("Not enough shares of stock id= "+stockId);
			break;
		}
	}
		
		
	}
	//We are trying to buy at lowest possible price
		// We are getting lowest not filled sell orders until of the conditions are met:
		//1: we fully fileld our buy order
		//2: There are no more appropriate sell orders :
		//    2.1 lowest buy order price is higher than the price we want to sell at
		//    2.2  no more sell orders at all
		//If buy order is not filled, we save it
		//Order we are trying to fill is referenced as currentOrder
	public void fillBuyLimitOrder(LimitOrder currentOrder) {
		while(currentOrder.getAmountToTrade()>0.0) {
			CompletableFuture cf1,cf2,cf3;
			LimitOrder sellingOrder= orderJpaRepository.getLowestSellOrder(1, currentOrder.getStock().getId());
			if(sellingOrder==null || sellingOrder.getPriceToOrderAt()>currentOrder.getPriceToOrderAt())
				break;
			else {
				stockService.updateStockPrice(currentOrder.getStock(),sellingOrder.getPriceToOrderAt());
				if(sellingOrder.getAmountToTrade()>=currentOrder.getAmountToTrade()) {
					
					cf1=userService.addStocksToUser(currentOrder.getAmountToTrade(), currentOrder.getUser(),currentOrder.getStock());
					cf2=userService.updateUserCash(sellingOrder.getPriceToOrderAt()*currentOrder.getAmountToTrade(),sellingOrder.getUser().getUsername());
					cf3=userService.updateUserCash(-1*sellingOrder.getPriceToOrderAt()*currentOrder.getAmountToTrade(),currentOrder.getUser().getUsername());
					
					sellingOrder.setAmountToTrade(sellingOrder.getAmountToTrade()-currentOrder.getAmountToTrade());
					currentOrder.setAmountToTrade(0);
					if(sellingOrder.getAmountToTrade()<=0.0)
						orderJpaRepository.deleteById(sellingOrder.getId());//this means lowest sell order is fully filled, so we can remove it
					else
						orderJpaRepository.save(sellingOrder);//if not fully filled, we update how much is left to be filled
				
				}
				else {
					cf1=userService.addStocksToUser(sellingOrder.getAmountToTrade(), currentOrder.getUser(),currentOrder.getStock());
					cf2=userService.updateUserCash(sellingOrder.getPriceToOrderAt()*sellingOrder.getAmountToTrade(),sellingOrder.getUser().getUsername());
					cf3=userService.updateUserCash(-1*sellingOrder.getPriceToOrderAt()*sellingOrder.getAmountToTrade(),currentOrder.getUser().getUsername());
					currentOrder.setAmountToTrade(currentOrder.getAmountToTrade()-sellingOrder.getAmountToTrade());
					
					orderJpaRepository.delete(sellingOrder);
				}
			}
			CompletableFuture.allOf(cf1,cf2,cf3).join();
		}
		if(currentOrder.getAmountToTrade()>0.0) {//if we didnt fill buy order, we save it to database
			orderJpaRepository.save(currentOrder);
			userService.updateUserCash(-1*currentOrder.getPriceToOrderAt()*currentOrder.getAmountToTrade(),currentOrder.getUser().getUsername());
		}
	} 
	//We are trying to sell for highest possible price
	// We are getting Highest not filled buy orders until of the conditions are met:
	//1: we fully fileld our sell order
	//2: There are no more appropriate buy orders :
	//    2.1 highest buy order price is lower than the price we want to sell at
	//    2.2  no more buy orders at all
	//If sell order is not filled, we save it
	//Order we are trying to fill is referenced as currentOrder
	public void fillSellLimitOrder(LimitOrder currentOrder) {
		while(currentOrder.getAmountToTrade()>0.0) {
			CompletableFuture cf1,cf2,cf3;
			LimitOrder buyOrder= orderJpaRepository.getHighestBuyOrder(0, currentOrder.getStock().getId());
			if(buyOrder==null || buyOrder.getPriceToOrderAt()<currentOrder.getPriceToOrderAt())
				break;
			else {
				
				stockService.updateStockPrice(currentOrder.getStock(),buyOrder.getPriceToOrderAt());
				if(buyOrder.getAmountToTrade()>=currentOrder.getAmountToTrade()) {
					
					cf1=userService.addStocksToUser(-1*currentOrder.getAmountToTrade(), currentOrder.getUser(),currentOrder.getStock());
					cf2=userService.addStocksToUser(currentOrder.getAmountToTrade(),buyOrder.getUser(),currentOrder.getStock());
					cf3=userService.updateUserCash(buyOrder.getPriceToOrderAt()*currentOrder.getAmountToTrade(),currentOrder.getUser().getUsername());
				
					buyOrder.setAmountToTrade(buyOrder.getAmountToTrade()-currentOrder.getAmountToTrade());
					currentOrder.setAmountToTrade(0);
					if(buyOrder.getAmountToTrade()<=0.0)
						orderJpaRepository.deleteById(buyOrder.getId());//this means highest buyOrder is fully filled, so we can remove it
					else
						orderJpaRepository.save(buyOrder);//if not fully filled, we update how much is left to be filled
				
				}
				else {
					cf1=userService.addStocksToUser(-1*buyOrder.getAmountToTrade(), currentOrder.getUser(),currentOrder.getStock());
					cf2=userService.updateUserCash(buyOrder.getPriceToOrderAt()*buyOrder.getAmountToTrade(),currentOrder.getUser().getUsername());
					cf3=userService.addStocksToUser(buyOrder.getAmountToTrade(), buyOrder.getUser(),currentOrder.getStock());
					currentOrder.setAmountToTrade(currentOrder.getAmountToTrade()-buyOrder.getAmountToTrade());
				
					orderJpaRepository.delete(buyOrder);
				}
			}
			CompletableFuture.allOf(cf1,cf2,cf3).join();
		}
		if(currentOrder.getAmountToTrade()>0.0) {//if we didnt fill sell order, we save it to database
			orderJpaRepository.save(currentOrder);
			userService.addStocksToUser(-1*currentOrder.getAmountToTrade(), currentOrder.getUser(),currentOrder.getStock());
		}
	}
	

	
	
}
