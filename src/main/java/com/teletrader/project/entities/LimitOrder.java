package com.teletrader.project.entities;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;

@Entity(name="limit_order")
public class LimitOrder {
	public static enum OrderType{
		BUY,SELL
	}
	@Id
	private Integer id;
	@DecimalMin("0.0")
	private double amountToTrade;
	@DecimalMin("0.0")
	private double priceToOrderAt;
	private OrderType orderType;
	
	public OrderType getOrderType() {
		return orderType;
	}

	public void setOrderType(OrderType orderType) {
		this.orderType = orderType;
	}

	@ManyToOne
	private Stock stock;
	 
	@ManyToOne
	private User user;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public double getAmountToTrade() {
		return amountToTrade;
	}

	public void setAmountToTrade(double amountToBuy) {
		this.amountToTrade = amountToBuy;
	}

	public double getPriceToOrderAt() {
		return priceToOrderAt;
	}

	public void setPriceToOrderAt(double priceToBuyAt) {
		this.priceToOrderAt = priceToBuyAt;
	}

	public Stock getStock() {
		return stock;
	}

	public void setStock(Stock stock) {
		this.stock = stock;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
	
	
}
