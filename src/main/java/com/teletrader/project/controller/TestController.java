package com.teletrader.project.controller;

import java.util.Set;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.teletrader.project.entities.LimitOrder;
import com.teletrader.project.entities.OrderBook;
import com.teletrader.project.entities.User;
import com.teletrader.project.entities.UserStock;
import com.teletrader.project.exceptions.ResourceNotFoundException;
import com.teletrader.project.repositories.OrderJpaRepository;
import com.teletrader.project.repositories.StockJpaRepository;
import com.teletrader.project.repositories.UserJpaRepository;
import com.teletrader.project.repositories.UserStockJpaRepository;
import com.teletrader.project.services.OrderService;

@RestController
public class TestController {
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
	

	@PostMapping("/test/user/{username}/stock/{stockId}/limitOrder")
	public void addLimitOrder(@PathVariable String username,@PathVariable Integer stockId ,@Valid @RequestBody LimitOrder limitOrder) {
		service.addLimitOrder(username, stockId, limitOrder);
	}
	
	
	@GetMapping("/test/stock/{stockId}/orderBook")
	public OrderBook getOrderBook(@PathVariable Integer stockId) {
		OrderBook orderBook=service.getOrderBook(stockId);
		if(orderBook==null)
			throw new ResourceNotFoundException("Stock  id-"+stockId+" does not exist");
		return orderBook;
	}


}
