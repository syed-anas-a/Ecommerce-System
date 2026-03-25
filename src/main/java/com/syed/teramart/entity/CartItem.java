package com.syed.teramart.entity;

import jakarta.persistence.*;

@Entity
@Table(name="cart_items")
public class CartItem{
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@ManyToOne
	@JoinColumn(name="user_id", nullable = false)
	private User user;
	
	@ManyToOne
	@JoinColumn(name="product_id", nullable = false)
	private Product product;
	
	@Column
	private Integer quantity;
	
	public CartItem() {
		// TODO Auto-generated constructor stub
	}

	public CartItem(Integer id, User user, Product product, Integer quantity) {  
		super();
		this.id = id;
		this.user = user;
		this.product = product;
		this.quantity = quantity;
	}

	public CartItem(User user, Product product, Integer quantity) {
		super();
		this.user = user;
		this.product = product;
		this.quantity = quantity;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer cartId) {
		this.id = cartId;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

}
