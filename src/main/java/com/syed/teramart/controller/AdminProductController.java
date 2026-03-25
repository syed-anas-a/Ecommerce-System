package com.syed.teramart.controller;

import java.math.BigDecimal;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.syed.teramart.dto.AddProductAdminRequest;
import com.syed.teramart.entity.Product;
import com.syed.teramart.service.AdminProductService;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/admin/products")
public class AdminProductController {
	
	AdminProductService adminProductService;
	
	public AdminProductController(AdminProductService adminProductService) {
		// TODO Auto-generated constructor stub
		this.adminProductService = adminProductService;
	}
	
	@PostMapping("/add")
	public ResponseEntity<?> addProduct(@RequestBody AddProductAdminRequest productRequest, HttpServletRequest request) {
		
		try {
			String name = productRequest.getName();
			String description = productRequest.getDescription();
			BigDecimal price = productRequest.getPrice();
			Integer stock = productRequest.getStock();
			Integer categoryId = productRequest.getCategoryId();
			String imageUrl = productRequest.getImageUrl();
			
			Product product = adminProductService.addProduct(name, description, price, stock, categoryId, imageUrl);
			
			return ResponseEntity.status(HttpStatus.CREATED).body(product);
			
		}
		catch (IllegalArgumentException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		}
		catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Unexpected error!!");
		}
		
	}
	
	@DeleteMapping("/delete")
	public ResponseEntity<?> deleteProduct(@RequestBody Map<String, Object> requestBody) {
		
		try {
			Integer productId = (Integer) requestBody.get("productId");
			adminProductService.deleteProduct(productId);
			return ResponseEntity.status(HttpStatus.OK).body("Product successfully deleted");
		}
		catch (IllegalArgumentException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
		}
		catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Unexpected error");
		}
		
	}

}
