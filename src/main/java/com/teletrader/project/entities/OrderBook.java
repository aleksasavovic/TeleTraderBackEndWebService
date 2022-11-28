package com.teletrader.project.entities;

import java.util.List;

import javax.persistence.ColumnResult;
import javax.persistence.SqlResultSetMapping;

//Contains top 10 BUY orders(highest 10) and top 10 SELL orders(lowest 10)
public class OrderBook {
	private List<OrderBookElement> buyOrders;
	private List<OrderBookElement> sellOrders;
	
	public OrderBook() {}

	public OrderBook(List<OrderBookElement> buyOrders, List<OrderBookElement> sellOrders) {
		super();
		this.buyOrders = buyOrders;
		this.sellOrders = sellOrders;
	}

	public List<OrderBookElement> getBuyOrders() {
		return buyOrders;
	}

	public void setBuyOrders(List<OrderBookElement> buyOrders) {
		this.buyOrders = buyOrders;
	}

	public List<OrderBookElement> getSellOrders() {
		return sellOrders;
	}

	public void setSellOrders(List<OrderBookElement> sellOrders) {
		this.sellOrders = sellOrders;
	}

	
	

}
