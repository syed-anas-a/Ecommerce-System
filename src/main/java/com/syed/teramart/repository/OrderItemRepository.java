package com.syed.teramart.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.syed.teramart.entity.OrderItem;

public interface OrderItemRepository extends JpaRepository<OrderItem, Integer> {
	
	@Query("SELECT oi FROM OrderItem oi WHERE oi.order.user.userId = :userId")
	List<OrderItem> findByUserId(int userId);
	
}
