package com.syed.teramart.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.syed.teramart.dto.AddToCartRequest;
import com.syed.teramart.entity.User;
import com.syed.teramart.repository.UserRepository;
import com.syed.teramart.service.CartService;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@CrossOrigin(origins="http://localhost:5174", allowCredentials="true")
@RequestMapping("/api/cart")
public class CartController {

	@Autowired
	private CartService cartService;
	
	@Autowired
	private UserRepository userRepository;
	
	@PostMapping("/add")
	public ResponseEntity<?> addToCart(@RequestBody AddToCartRequest addToCart, HttpServletRequest request) {
		
		User authUser = (User) request.getAttribute("authenticatedUser");
		int productId = addToCart.getProductId();
		int quantity = addToCart.getQuantity();
		
		User user = userRepository.findById(authUser.getUserId())
				.orElseThrow(() -> new IllegalArgumentException("User not found with username: " + authUser));
		
		cartService.addToCart(user.getUserId(), productId, quantity);
		return ResponseEntity.status(HttpStatus.CREATED).build();
	}
	
	@PostMapping("/items")
	public ResponseEntity<Map<String, Object>> getCartItems(HttpServletRequest request) {
		
		User user = (User) request.getAttribute("authenticatedUser");
		Map<String, Object> cartItems = cartService.getCartItems(user.getUserId());
		
		return ResponseEntity.ok(cartItems);

	}
	
	@PutMapping("/update")
	public ResponseEntity<Void> updateCartItemQuantity(@RequestBody Map<String, Object> request) {
		
		String username = (String) request.get("username");
		int productId = (int) request.get("productId");
		int quantity = (int) request.get("quantity");
		
		User user = userRepository.findByUsername(username)
				.orElseThrow(() -> new IllegalArgumentException("User not found with username: " + username));
		
		cartService.updateCartItemQuantity(user.getUserId(), productId, quantity);
		return ResponseEntity.status(HttpStatus.OK).build();
	}
	
	@DeleteMapping("/delete")
	public ResponseEntity<Void> deleteCartItem(@RequestBody Map<String, Object> request) {
		
		String username = (String) request.get("username");
		int productId = (int) request.get("productId");
		
		User user = userRepository.findByUsername(username)
				.orElseThrow(() -> new IllegalArgumentException("User not found with username: " + username));
		
		cartService.deleteCartItem(user.getUserId(), productId);
		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	}

	
}

















