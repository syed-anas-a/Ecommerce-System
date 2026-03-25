package com.syed.teramart.dto;

import java.math.BigDecimal;

public class AddProductAdminRequest {
	
	String name;
	String description;
	BigDecimal price;
	Integer stock;
	Integer categoryId;
	String imageUrl;
	
	public AddProductAdminRequest() {
		// TODO Auto-generated constructor stub
	}

	public AddProductAdminRequest(String name, String description, BigDecimal price, Integer stock, Integer categoryId,
			String imageUrl) {
		super();
		this.name = name;
		this.description = description;
		this.price = price;
		this.stock = stock;
		this.categoryId = categoryId;
		this.imageUrl = imageUrl;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public Integer getStock() {
		return stock;
	}

	public void setStock(Integer stock) {
		this.stock = stock;
	}

	public Integer getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(Integer categoryId) {
		this.categoryId = categoryId;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

}
