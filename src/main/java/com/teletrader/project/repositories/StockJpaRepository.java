package com.teletrader.project.repositories;

import java.util.List;
import java.util.concurrent.Future;

import org.springframework.data.jpa.repository.JpaRepository;

import com.teletrader.project.entities.Stock;


public interface StockJpaRepository extends JpaRepository<Stock, Integer> {

}
