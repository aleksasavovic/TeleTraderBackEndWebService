package com.teletrader.project.entities;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Embeddable;

//primary key has to override hashCode and equals methods, and this composite key will be used as a primary key for UserStock Entity
@Embeddable
public class UserStockCompositKey implements Serializable {
	private String username;
	private Integer stockId;
	
	public UserStockCompositKey() {}
	
	public UserStockCompositKey(String username, Integer stockId) {
		super();
		this.username = username;
		this.stockId = stockId;
	}

	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public Integer getStockId() {
		return stockId;
	}
	public void setStockId(Integer stockId) {
		this.stockId = stockId;
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(stockId, username);
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		UserStockCompositKey other = (UserStockCompositKey) obj;
		return Objects.equals(stockId, other.stockId) && Objects.equals(username, other.username);
	}

	
	
	
}



