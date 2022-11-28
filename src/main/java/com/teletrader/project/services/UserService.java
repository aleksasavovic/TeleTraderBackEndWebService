package com.teletrader.project.services;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.teletrader.project.entities.Stock;
import com.teletrader.project.entities.User;
import com.teletrader.project.entities.UserStock;
import com.teletrader.project.entities.UserStockCompositKey;
import com.teletrader.project.repositories.OrderJpaRepository;
import com.teletrader.project.repositories.StockJpaRepository;
import com.teletrader.project.repositories.UserJpaRepository;
import com.teletrader.project.repositories.UserStockJpaRepository;

@Service
public class UserService {
	@Autowired
	private UserJpaRepository userJpaRepository;
	@Autowired
	private OrderJpaRepository orderJpaRepository;
	@Autowired
	private StockJpaRepository stockJpaRepository;
	@Autowired
	private UserStockJpaRepository userStockJpaRepository;
	Logger logger = LoggerFactory.getLogger(UserService.class);
	
	@Async
	public CompletableFuture<Void> addStocksToUser(double amount, User user, Stock stock) {

		UserStockCompositKey compositeKey= new UserStockCompositKey(user.getUsername(), stock.getId());
		Optional<UserStock> userStockOptional = userStockJpaRepository.findById(compositeKey);
		UserStock userStock=null;
		if(userStockOptional.isPresent()) {
			userStock=userStockOptional.get();
			userStock.setAmount(userStock.getAmount()+amount);
		}
		else
			userStock=new UserStock(compositeKey, user, stock, amount);
		//Because amount can be negative in case of sell order, we have to check is amount >0 and remove entity if it isnt
		if(userStock.getAmount()>0) {
			userStockJpaRepository.save(userStock);
			logger.warn("UserStock update: User "+user.getUsername()+" got "+amount+" shares of stock: "+stock.getId() );
		}
		else {
			userStockJpaRepository.delete(userStock);
			logger.warn("UserStock deleted: User "+user.getUsername()+", stock: "+stock.getId());
		}
		return CompletableFuture.completedFuture(null);
		}
	@Async
	public CompletableFuture<Void> updateUserCash(double cash, String username) {
		Optional<User> userOptional= userJpaRepository.findById(username);
		if(userOptional.isPresent()) {
			User user=userOptional.get();
			user.setCash(user.getCash()+cash);
			userJpaRepository.save(user);
			logger.warn("User updated: User "+user.getUsername()+" got his cash changed by "+cash);
		}
		return CompletableFuture.completedFuture(null);
	}
	
	
}