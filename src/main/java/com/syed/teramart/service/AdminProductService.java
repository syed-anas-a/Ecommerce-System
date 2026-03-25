package com.syed.teramart.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import com.syed.teramart.entity.Product;
import com.syed.teramart.entity.ProductImage;
import com.syed.teramart.entity.Category;
import com.syed.teramart.repository.CategoryRepository;
import com.syed.teramart.repository.ProductImageRepository;
import com.syed.teramart.repository.ProductRepository;

@Service
public class AdminProductService {
	
	ProductRepository productRepository;
	ProductImageRepository productImageRepository;
	CategoryRepository categoryRepository;
	
	public AdminProductService(ProductRepository productRepository, ProductImageRepository productImageRepository,
			CategoryRepository categoryRepository) {
		// TODO Auto-generated constructor stub
		this.productRepository = productRepository;
		this.productImageRepository = productImageRepository;
		this.categoryRepository = categoryRepository;
	}
	
	public Product addProduct(String name, String description, BigDecimal price, Integer stock, Integer categoryId, String imageUrl) {
		
		Category category = categoryRepository.findById(categoryId)
				.orElseThrow(() -> new IllegalArgumentException("Invalid category Id"));
		
		Product product = new Product();
		product.setName(name);
		product.setDescription(description);
		product.setPrice(price);
		product.setCategory(category);
		product.setCreatedAt(LocalDateTime.now());
		product.setUpdatedAt(LocalDateTime.now());
		
		Product newProduct = productRepository.save(product);
		
		if(imageUrl == null || imageUrl.isEmpty()) {
			throw new IllegalArgumentException("Image url is invalid");
		}
		
		ProductImage productImage = new ProductImage();
		productImage.setImageUrl(imageUrl);
		productImage.setProduct(newProduct);
		productImageRepository.save(productImage);
		
		return product;
	}

	public void deleteProduct(Integer productId) {
		// TODO Auto-generated method stub
		if(!productRepository.existsById(productId)) {
			throw new IllegalArgumentException("NO product doesn exist with id: " + productId);
		}
		productImageRepository.deleteByProductId(productId);
		productRepository.deleteById(productId);
		
	}

}
