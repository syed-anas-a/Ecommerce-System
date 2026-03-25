package com.syed.teramart.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class OrderResponse {
	
	private int orderId;
	private String username;
	private BigDecimal totalAmount;
	private String status;
	private LocalDateTime createdAt;
	private List<OrderItemResponse> items;
	
	public OrderResponse() {
		// TODO Auto-generated constructor stub
	}

	public OrderResponse(int orderId, String username, BigDecimal totalAmount, String status, LocalDateTime createdAt,
			List<OrderItemResponse> items) {
		super();
		this.orderId = orderId;
		this.username = username;
		this.totalAmount = totalAmount;
		this.status = status;
		this.createdAt = createdAt;
		this.items = items;
	}

	public int getOrderId() {
		return orderId;
	}

	public void setOrderId(int orderId) {
		this.orderId = orderId;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public BigDecimal getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(BigDecimal totalAmount) {
		this.totalAmount = totalAmount;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}

	public List<OrderItemResponse> getItems() {
		return items;
	}

	public void setItems(List<OrderItemResponse> items) {
		this.items = items;
	}
}
