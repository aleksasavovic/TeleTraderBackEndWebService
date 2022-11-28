package com.teletrader.project.services;

import java.util.concurrent.CompletableFuture;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.teletrader.project.entities.Stock;
import com.teletrader.project.repositories.OrderJpaRepository;
import com.teletrader.project.repositories.StockJpaRepository;
import com.teletrader.project.repositories.UserJpaRepository;
import com.teletrader.project.repositories.UserStockJpaRepository;

@Service
public class StockService {
	@Autowired
	private UserJpaRepository userJpaRepository;
	@Autowired
	private OrderJpaRepository orderJpaRepository;
	@Autowired
	private StockJpaRepository stockJpaRepository;
	@Autowired
	private UserStockJpaRepository userStockJpaRepository;
	Logger logger = LoggerFactory.getLogger(StockService.class);
	
	@Async
	public void updateStockPrice(Stock stock, double newPrice) {
		if(stock.getPrice()!=newPrice) {
		stock.setPrice(newPrice);
		stockJpaRepository.save(stock);
		logger.warn("Stock update: Stock: "+stock.getId()+" price updated to "+newPrice);
		}
	}



	
}
