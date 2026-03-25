package com.syed.teramart.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.syed.teramart.entity.Order;

import java.util.List;
import java.util.Optional;



public interface OrderRepository extends JpaRepository<Order, Integer> {

	Optional<Order> findByOrderId(Integer orderId);
	
	Optional<Order> findByRazorpayOrderId(String razorpayOrderId);

	@Query("SELECT o FROM Order o WHERE user.userId = :userId")
	List<Order> findAllByUserId(Integer userId);
	
	
}
