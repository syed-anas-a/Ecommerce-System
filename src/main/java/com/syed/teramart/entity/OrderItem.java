package com.syed.teramart.entity;

import java.math.BigDecimal;

import jakarta.persistence.*;

@Entity
@Table(name="order_items")
public class OrderItem {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "order_id", nullable = false)
	private Order order;
	
	@Column(name = "product_id", nullable = false)
	private Integer productId;
	
	@Column(name = "quantity", nullable = false)
	private Integer quantity;
	
	@Column(name = "unit_price", nullable = false)
	private BigDecimal unitPrice;
	
	@Column(name = "total_price", nullable = false)
	private BigDecimal totalPrice;
	
	public OrderItem() {
		// TODO Auto-generated constructor stub
	}

	public OrderItem(int id, Order order, Integer productId, Integer quantity, BigDecimal unitPrice,
			BigDecimal totalPrice) {
		super();
		this.id = id;
		this.order = order;
		this.productId = productId;
		this.quantity = quantity;
		this.unitPrice = unitPrice;
		this.totalPrice = totalPrice;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Order getOrder() {
		return order;
	}

	public void setOrder(Order order) {
		this.order = order;
	}

	public Integer getProductId() {
		return productId;
	}

	public void setProductId(Integer productId) {
		this.productId = productId;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
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

}
