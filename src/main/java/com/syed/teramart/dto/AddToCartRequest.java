package com.syed.teramart.dto;

public class AddToCartRequest {
	
	int productId;
	int quantity;
	
	public AddToCartRequest() {
		// TODO Auto-generated constructor stub
	}

	public AddToCartRequest(int productId, int quantity) {
		super();
		this.productId = productId;
		this.quantity = quantity;
	}

	public int getProductId() {
		return productId;
	}

	public void setProductId(int productId) {
		this.productId = productId;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

}
