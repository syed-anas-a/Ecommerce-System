package com.syed.teramart.dto;

import java.math.BigDecimal;

public class OrderItemResponse {
	
	private int productId;
	private String productName;
	private String productImage;
	private int quantity;
	private BigDecimal unitPrice;
	private BigDecimal totalPrice;
	
	public OrderItemResponse() {
		// TODO Auto-generated constructor stub
	}

	public OrderItemResponse(int productId, String productName, int quantity, BigDecimal unitPrice,
			BigDecimal totalPrice, String productImage) {
		super();
		this.productId = productId;
		this.productName = productName;
		this.quantity = quantity;
		this.unitPrice = unitPrice;
		this.totalPrice = totalPrice;
	}

	public int getProductId() {
		return productId;
	}

	public void setProductId(int productId) {
		this.productId = productId;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public BigDecimal getUnitPrice() {
		return unitPrice;
	}

	public void setUnitPrice(BigDecimal unitPrice) {
		this.unitPrice = unitPrice;
	}

	public BigDecimal getTotalPrice() {
		return totalPrice;
	}

	public void setTotalPrice(BigDecimal totalPrice) {
		this.totalPrice = totalPrice;
	}

	public String getProductImage() {
		return productImage;
	}

	public void setProductImage(String productImage) {
		this.productImage = productImage;
	}
	
	

}
