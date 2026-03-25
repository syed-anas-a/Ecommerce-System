package com.syed.teramart.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.syed.teramart.entity.*;
import com.syed.teramart.repository.*;

@Service
public class CartService {
	
	@Autowired
	private CartRepository cartRepository;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private ProductRepository productRepository;
	
	@Autowired
	private ProductImageRepository productImageRepository;

	// Adding product to cart
	public void addToCart(int userId, int productId, int quantity) {
		
		User user = userRepository.findById(userId)
				.orElseThrow(() -> new IllegalArgumentException("User not found with ID: " + userId));
		
		Product product = productRepository.findById(productId)
				.orElseThrow(() -> new IllegalArgumentException("Product not found with ID: "));
		
		// retrieve & check if the product already exists in cart
		Optional<CartItem> existingItem = cartRepository.findByUserAndProduct(userId, productId);
		
		// update quantity if exists
		if(existingItem.isPresent()) {
			CartItem cartItem = existingItem.get();
			cartItem.setQuantity(cartItem.getQuantity()+1);
			cartRepository.save(cartItem);
		} else { // add new product to cart
			CartItem newItem = new CartItem(user, product, quantity);
			cartRepository.save(newItem);
		}
	}

	// Get Cart Items for the user
	public Map<String, Object> getCartItems(int userId) {
		
		// Fetching cartItems for the user with productdetails
		List<CartItem> cartItems = cartRepository.findCartItemsWithProductDetailsByUserId(userId);
		
		// response map to hold the cart details
		Map<String, Object> response = new HashMap<>();
		User user = userRepository.findById(userId)
				.orElseThrow(() -> new IllegalArgumentException("User not found"));
		
		response.put("username", user.getUsername());
		response.put("role", user.getRole());
		
		// List to hold all product details
		List<Map<String, Object>> products = new ArrayList<>();
		int overallTotalPrice = 0;
		
		
		for(CartItem cartItem : cartItems) {
			Map<String, Object> productDetails = new HashMap<>();
			
			// get product details
			Product product = cartItem.getProduct();
			
			// get product image
			List<ProductImage> productImages = productImageRepository.findByProduct_ProductId(product.getProductId());
			String imageUrl = (productImages != null && !productImages.isEmpty())
					? productImages.get(0).getImageUrl()
					: "default-image-url";
			
			// add product details
			productDetails.put("product_id", product.getProductId());
			productDetails.put("image_url", imageUrl);
			productDetails.put("name", product.getName());
			productDetails.put("description", product.getDescription());
			productDetails.put("price_per_unit", product.getPrice());
			productDetails.put("quantity", cartItem.getQuantity());
			productDetails.put("total_price", cartItem.getQuantity() * product.getPrice().doubleValue());
			
			products.add(productDetails);
			
			overallTotalPrice += cartItem.getQuantity() * product.getPrice().doubleValue();
			
		} 
		
		// Final cart response
		Map<String, Object> cart = new HashMap<>();
		cart.put("products", products);
		cart.put("overall_total_price", overallTotalPrice);
		
		response.put("cart", cart);
		
		return response;
	}

	public void updateCartItemQuantity(int userId, int productId, int quantity) {
		
		
		User user = userRepository.findById(userId)
				.orElseThrow(() -> new IllegalArgumentException("User not found"));
		
		Product product = productRepository.findById(productId)
				.orElseThrow(() -> new IllegalArgumentException("Product not found"));
		
		
		Optional<CartItem> existingItem = cartRepository.findByUserAndProduct(user.getUserId(), product.getProductId());
		
		if(existingItem.isPresent()) {
			CartItem cartItem = existingItem.get();
			if(quantity == 0) {
				cartRepository.deleteCartItem(userId, productId); 
			} else {
				cartItem.setQuantity(quantity);
				cartRepository.save(cartItem);
			}
		}
		
	}

	public void deleteCartItem(int userId, int productId) {
		
		User user = userRepository.findById(userId)
				.orElseThrow(() -> new IllegalArgumentException("User not found"));
		
		Product product = productRepository.findById(productId)
				.orElseThrow(() -> new IllegalArgumentException("Product not found"));
		
		cartRepository.deleteCartItem(user.getUserId(), product.getProductId());
		
	}

}
