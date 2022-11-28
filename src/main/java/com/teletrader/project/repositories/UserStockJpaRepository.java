package com.teletrader.project.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.teletrader.project.entities.UserStock;
import com.teletrader.project.entities.UserStockCompositKey;


public interface UserStockJpaRepository extends JpaRepository<UserStock, UserStockCompositKey>{
	
}
