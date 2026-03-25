package com.syed.teramart.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.syed.teramart.entity.*;
import com.syed.teramart.service.ProductService;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@CrossOrigin(origins="http://localhost:5174", allowCredentials="true")
@RequestMapping("/api/products")
public class ProductController {
	
	@Autowired
	private ProductService productService;
	
	@GetMapping
	public ResponseEntity<Map<String, Object>> getProducts(
			@RequestParam(required=false) String category,
			HttpServletRequest request
			) {
		
		try {
			
			// Retreive authenticated user from request attr. set by filter
			User authenticatedUser = (User) request.getAttribute("authenticatedUser");
			if(authenticatedUser == null) {
				return ResponseEntity.status(401).body(Map.of("error", "Unauthorized access")); 
			}
			
			// fetch products based on category
			List<Product> products = productService.getProductByCategory(category);
			
			// Build Response
			Map<String, Object> response = new HashMap<>();
			
			// add userinfo
			Map<String, String> userInfo = new HashMap<>();
			userInfo.put("name", authenticatedUser.getUsername());
			userInfo.put("role", authenticatedUser.getRole().name());
			response.put("user", userInfo);
			
			// Build product list & add products
			List<Map<String, Object>> productList = new ArrayList<>();
			for(Product product: products) {
				Map<String, Object> thisProduct = new HashMap<>();
				thisProduct.put("product_id", product.getProductId());
				thisProduct.put("name", product.getName());
				thisProduct.put("description", product.getDescription());
				thisProduct.put("price", product.getPrice());
				thisProduct.put("stock", product.getStock());
				
				// Fetch product image
				List<String> images = productService.getProductImages(product.getProductId());
				thisProduct.put("images", images);
				productList.add(thisProduct); 
			}
			response.put("products", productList);
			return ResponseEntity.ok(response);
			
			
		}
		catch (Exception e) {
			return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
		}
	}

}
