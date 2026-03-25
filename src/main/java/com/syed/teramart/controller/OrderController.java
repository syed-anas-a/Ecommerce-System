package com.syed.teramart.controller;


import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.syed.teramart.dto.OrderResponse;
import com.syed.teramart.entity.User;
import com.syed.teramart.service.OrderService;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@CrossOrigin(origins="http://localhost:5173", allowCredentials = "true")
@RequestMapping("api/orders")
public class OrderController {
	
	@Autowired
	private final OrderService orderService;
	
	public OrderController(OrderService orderService) {
		// TODO Auto-generated constructor stub
		this.orderService = orderService;
	}
	
	@PostMapping("/create")
	public ResponseEntity<?> createOrder(HttpServletRequest request) {
		
		try {
			
			User user = (User) request.getAttribute("authenticatedUser");
			
			if(user == null) {
				return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not authenticated");
			} 
			Map<String, Object> response = orderService.createOrder(user);
			
			return ResponseEntity.ok(response); 
		}
		
		catch(Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid request");
		}
	}
	
	@GetMapping("/")
	public ResponseEntity<?> getOrders(HttpServletRequest request) {
		
		try {
			User user = (User) request.getAttribute("authenticatedUser");
			System.out.println("User: " + user);
			
			List<OrderResponse> response = orderService.getOrders(user);
			
			return ResponseEntity.ok(response); 
		}
		catch(Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", e.getMessage())); 
		}
			
		
	}
	
}






