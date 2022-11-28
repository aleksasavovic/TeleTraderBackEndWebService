package com.teletrader.project.controller;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import javax.validation.Valid;
import javax.websocket.server.PathParam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.teletrader.project.entities.LimitOrder;
import com.teletrader.project.entities.LimitOrder.OrderType;
import com.teletrader.project.exceptions.ResourceNotFoundException;
import com.teletrader.project.entities.OrderBook;
import com.teletrader.project.entities.OrderBookElement;
import com.teletrader.project.entities.Stock;
import com.teletrader.project.entities.User;
import com.teletrader.project.entities.UserStock;
import com.teletrader.project.entities.UserStockCompositKey;
import com.teletrader.project.repositories.OrderJpaRepository;
import com.teletrader.project.repositories.StockJpaRepository;
import com.teletrader.project.repositories.UserJpaRepository;
import com.teletrader.project.repositories.UserStockJpaRepository;
import com.teletrader.project.security.SecurityUtil;
import com.teletrader.project.services.OrderService;

//Use TestController to test the app
@RestController
public class UserController {
	@Autowired
	private UserJpaRepository userJpaRepository;
	@Autowired
	private OrderJpaRepository orderJpaRepository;
	@Autowired
	private StockJpaRepository stockJpaRepository;
	@Autowired
	private UserStockJpaRepository userStockJpaRepository;
	@Autowired
	private OrderService service;

	@GetMapping("regular/info")
	public User getUser() {
		User u=SecurityUtil.getCurrentUser();
		return u;
	}
	@PostMapping("regular/stock/{stockId}/limitOrder")
	public void addLimitOrder(@PathVariable Integer stockId ,@Valid @RequestBody LimitOrder limitOrder) {
	
		String username=SecurityUtil.getCurrentUser().getUsername();
		service.addLimitOrder(username, stockId, limitOrder);
	

	}
	
	@GetMapping("regular/stock/{stockId}/orderBook")
	public OrderBook getOrderBook(@PathVariable Integer stockId) {
		OrderBook orderBook=service.getOrderBook(stockId);
		if(orderBook==null)
			throw new ResourceNotFoundException("Stock  id-"+stockId+" does not exist");
		return orderBook;
	}

	
}
