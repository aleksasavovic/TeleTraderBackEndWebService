package com.teletrader.project.repositories;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.scheduling.annotation.Async;

import com.teletrader.project.entities.User;

public interface UserJpaRepository extends JpaRepository<User, String>{
	
}
