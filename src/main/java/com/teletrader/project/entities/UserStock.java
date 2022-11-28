package com.teletrader.project.entities;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity(name="user_stock")
public class UserStock {
		@EmbeddedId
		private UserStockCompositKey userStockCompositKey;
		
		@ManyToOne
		@MapsId("username")
		@JsonIgnore
		@JoinColumn(name="user_username")
		private User user;
		
		@ManyToOne
		@MapsId("stockId")
		@JsonIgnore
		@JoinColumn(name="stock_id")
		private Stock stock;
		
		private double amount;
		
		//Constrtuctors
		public UserStock() {}
		public UserStock(UserStockCompositKey userStockCompositKey, User user, Stock stock, double amount) {
			super();
			this.userStockCompositKey = userStockCompositKey;
			this.user = user;
			this.stock = stock;
			this.amount = amount;
		}
		
		//Getters And setters
		public UserStockCompositKey getUserStockCompositKey() {
			return userStockCompositKey;
		}
		public void setUserStockCompositKey(UserStockCompositKey userStockCompositKey) {
			this.userStockCompositKey = userStockCompositKey;
		}
		public User getUser() {
			return user;
		}
		public void setUser(User user) {
			this.user = user;
		}
		public Stock getStock() {
			return stock;
		}
		public void setStock(Stock stock) {
			this.stock = stock;
		}
		public double getAmount() {
			return amount;
		}
		public void setAmount(double amount) {
			this.amount = amount;
		}
		  
}

