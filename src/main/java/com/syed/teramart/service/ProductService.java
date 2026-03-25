package com.syed.teramart.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.syed.teramart.entity.*;
import com.syed.teramart.repository.CategoryRepository;
import com.syed.teramart.repository.ProductImageRepository;
import com.syed.teramart.repository.ProductRepository;

@Service
public class ProductService {
	
	@Autowired
	private ProductRepository productRepository;
	
	@Autowired
	private ProductImageRepository productImageRepository;
	
	@Autowired
	private CategoryRepository categoryRepository;

	public List<Product> getProductByCategory(String categoryName) {
		if(categoryName != null && !categoryName.isEmpty()) {
			Optional<Category> categoryOptional = categoryRepository.findByCategoryName(categoryName);
			if(categoryOptional.isPresent()) {
				Category category = categoryOptional.get();
				return productRepository.findByCategory_CategoryId(category.getCategoryId());
			} else {
				throw new RuntimeException("Category not found!!");
			}
		} else {
			return productRepository.findAll();
		}
	}

	public List<String> getProductImages(Integer productId) {
		
		List<ProductImage> productImages = productImageRepository.findByProduct_ProductId(productId);
		List<String> imageUrls = new ArrayList<>();
		for(ProductImage image: productImages) {
			imageUrls.add(image.getImageUrl());
		}
		return imageUrls;
	}

}
