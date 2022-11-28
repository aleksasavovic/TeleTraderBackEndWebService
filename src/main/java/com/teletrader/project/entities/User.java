package com.teletrader.project.entities;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;

import com.fasterxml.jackson.annotation.JsonIgnore;

import antlr.collections.impl.Vector;

@Entity(name="users")
public class User {
	@Id
	private String username;
	
	private String password;
	
	private double cash;
	
	@JsonIgnore
	@OneToMany(mappedBy = "user")
	private Set<LimitOrder> limitOrders=new HashSet<>();
	
	@JsonIgnore
	@OneToMany(mappedBy="user")
	private Set<UserStock> ownedStocks=new HashSet<>();
	
	
	
	//Getters and setters
	
	public String getUsername() {
		return username;
	}
	
	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public double getCash() {
		return cash;
	}

	public void setCash(double cash) {
		this.cash = cash;
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
