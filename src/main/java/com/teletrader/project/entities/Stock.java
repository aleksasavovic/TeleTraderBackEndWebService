package com.teletrader.project.entities;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class Stock {
	@Id
	private Integer id;
	
	private double price;
	
	private String name;
	
	@JsonIgnore
	@OneToMany(mappedBy = "stock")
	private Set<LimitOrder> limitOrders=new HashSet<>();
	
	@JsonIgnore
	@OneToMany(mappedBy="stock")
	private Set<UserStock> ownedStocks =new HashSet<>();
	
	

	//Getters And setters
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Set<LimitOrder> getLimitOrders() {
		return limitOrders;
	}

	public void setLimitOrders(Set<LimitOrder> limitOrders) {
		this.limitOrders = limitOrders;
	}

	public Set<UserStock> getOwnedStocks() {
		return ownedStocks;
	}

	public void setOwnedStocks(Set<UserStock> ownedStocks) {
		this.ownedStocks = ownedStocks;
	}
}
