package com.teletrader.project.repositories;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.EntityGraph.EntityGraphType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.scheduling.annotation.Async;

import com.teletrader.project.entities.LimitOrder;
import com.teletrader.project.entities.LimitOrder.OrderType;
import com.teletrader.project.entities.OrderBook;
import com.teletrader.project.entities.OrderBookElement;
import com.teletrader.project.entities.Stock;
//findFirstBy is not supported in this version of JPA
public interface OrderJpaRepository extends JpaRepository<LimitOrder, Integer> {

	
		//Native sql query for getting Lowest Sell order
	   @Query(nativeQuery = true,
               value = "SELECT * FROM LIMIT_ORDER "
               		+ "WHERE ORDER_TYPE = :orderType and STOCK_ID = :stockId "
               		+ "ORDER BY PRICE_TO_ORDER_AT ASC, ID LIMIT 1")
	public LimitOrder getLowestSellOrder(@Param("orderType")int orderType,@Param("stockId")Integer stockId);
	   
	   //Native sql query or getting Highest Buy order
	   @Query(nativeQuery = true,
               value = "SELECT * FROM LIMIT_ORDER "
               		+ "WHERE ORDER_TYPE = :orderType and STOCK_ID = :stockId "
               		+ "ORDER BY PRICE_TO_ORDER_AT DESC, ID ASC LIMIT 1")
	public LimitOrder getHighestBuyOrder(@Param("orderType")int orderType,@Param("stockId")Integer stockId);
	   
	  //Native sql query for getting maximum curent id, used to get next id 
	 @Query(nativeQuery = true,
			 value="Select max(ID) from LIMIT_ORDER")
	public Integer getMaxId();
	
	 //Native sql query for getting highest buy order on given stock for given user, Used to check if new sell order is valid
	 @Query(nativeQuery = true,
             value = "SELECT * FROM LIMIT_ORDER "
             		+ "WHERE ORDER_TYPE = 0 and STOCK_ID = :stockId and user_username = :username "
             		+ "ORDER BY PRICE_TO_ORDER_AT DESC LIMIT 1")
	public LimitOrder getHighestBuyOrderForUser(@Param("stockId") Integer stockId,@Param("username") String username);	
	 
	 //Native sql query for getting lowest sell order on given stock for given  user, Used to check if new buy order is valid
	   @Query(nativeQuery = true,
               value = "SELECT * FROM LIMIT_ORDER "
            		   + "WHERE ORDER_TYPE = 1 and STOCK_ID = :stockId and user_username = :username "
               		+ "ORDER BY PRICE_TO_ORDER_AT DESC LIMIT 1")
	public LimitOrder getLowestSellOrderForUser(@Param("stockId") Integer stockId,@Param("username") String username);
	 
	   //Native sql querry used to create top 10 buy orders
	@Query(nativeQuery = true,
               value =" select sum(amount_to_trade) as amount, price_to_order_at as price"
               		+ " from limit_order"
               		+ " where stock_id = :stockId and order_type = 0 "
               		+ " group by price_to_order_at "
               		+ " order by price_to_order_at desc "
               		+ " limit 10")
	@Async
	public CompletableFuture<List<OrderBookElement>> getTop10BuyOrdersForStock(@Param("stockId") Integer stockId);
	   
	 //Native sql querry used to create top 10 sell orders
	   @Query(nativeQuery = true,
               value =" select sum(amount_to_trade) as amount, price_to_order_at as price"
               		+ " from limit_order"
               		+ " where stock_id = :stockId and order_type = 1 "
               		+ " group by price_to_order_at "
               		+ " order by price_to_order_at asc "
               		+ " limit 10")
	@Async
	public CompletableFuture<List<OrderBookElement>> getTop10SellOrdersForStock(@Param("stockId") Integer stockId);
	
	 
	   
	 /*  
	   @Async
	   void deleteById(Integer id);
	   @Async
	   <S extends LimitOrder> S save(LimitOrder limiOtder);*/
	   /*  @Query(nativeQuery = true,
       value = "SELECT * FROM LIMIT_ORDER "
    		   + "WHERE ORDER_TYPE = 0 and STOCK_ID = :stockId "
       		+ "ORDER BY PRICE_TO_ORDER_AT DESC ")
public List<LimitOrder> getTop10BuyOrdersForStock(@Param("stockId") Integer stockId);

@Query(nativeQuery = true,
       value =" select * "
       		+ "from limit_order"
       		+ " where stock_id = :stockId and order_type = 1 "
       		+ "order by price_to_order_at asc ")
public List<LimitOrder> getTop10SellOrdersForStock(@Param("stockId") Integer stockId);*/
	 /*
	  * public List<LimitOrder> findByOrderTypeAndStock_IdOrderByPriceToOrderAtDesc(OrderType orderType, Integer stockId);
	public List<LimitOrder> findByOrderTypeAndStock_IdOrderByPriceToOrderAt(OrderType orderType, Integer stockId);
	  */
}
