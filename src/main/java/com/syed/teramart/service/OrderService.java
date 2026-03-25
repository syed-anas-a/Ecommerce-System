package com.syed.teramart.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.syed.teramart.repository.ProductImageRepository;
import com.syed.teramart.repository.ProductRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.razorpay.RazorpayException;
import com.syed.teramart.dto.OrderItemResponse;
import com.syed.teramart.dto.OrderResponse;
import com.syed.teramart.entity.CartItem;
import com.syed.teramart.entity.Order;
import com.syed.teramart.entity.OrderItem;
import com.syed.teramart.entity.OrderStatus;
import com.syed.teramart.entity.Product;
import com.syed.teramart.entity.ProductImage;
import com.syed.teramart.entity.User;
import com.syed.teramart.repository.CartRepository;
import com.syed.teramart.repository.OrderRepository;

@Service
public class OrderService {
	
	@Value("${razorpay.key_id}")
	private String razorpayKeyId;
	
	@Autowired
	private final OrderRepository orderRepository;
	
	@Autowired
	private final PaymentService paymentService;
	
	@Autowired
	private final CartRepository cartRepository;
	
	@Autowired
	private final ProductRepository productRepository;
	
	@Autowired
    private final ProductImageRepository productImageRepository;
	
	
	public OrderService(OrderRepository orderRepository, CartRepository cartRepository, 
			PaymentService paymentService,ProductImageRepository productImageRepository, 
			ProductRepository productRepository) {
		// TODO Auto-generated constructor stub
		this.orderRepository = orderRepository;
		this.cartRepository = cartRepository;
		this.paymentService = paymentService;
		this.productImageRepository = productImageRepository;
		this.productRepository = productRepository;
	}
	
	public Map<String, Object> createOrder(User userRequest) throws RazorpayException {

		
		Order order = new Order();
		order.setUser(userRequest); 
		order.setCreatedAt(LocalDateTime.now());
		order.setStatus(OrderStatus.PENDING);
		
		List<OrderItem> orderItems = new ArrayList<>();
		BigDecimal totalAmount = BigDecimal.ZERO;
				
		List<CartItem> cartItems = cartRepository.findByUserId(userRequest.getUserId());
		
		if (cartItems.isEmpty()) return Map.of("message", "Empty Cart");
		
		for(CartItem item: cartItems) {
			
			OrderItem orderItem = new OrderItem();
			orderItem.setProductId(item.getProduct().getProductId());
			orderItem.setQuantity(item.getQuantity());
			orderItem.setUnitPrice(item.getProduct().getPrice());
			orderItem.setTotalPrice(orderItem.getUnitPrice().multiply(BigDecimal.valueOf(item.getQuantity())));
			totalAmount = totalAmount.add(orderItem.getTotalPrice());
			orderItems.add(orderItem);
			orderItem.setOrder(order);
		}
		
		order.setTotalAmount(totalAmount);
		order.setOrderItems(orderItems);
		orderRepository.save(order);
		cartRepository.clearCart(userRequest.getUserId()); 
		
		String razorpayOrderId = paymentService.initiatePayment(totalAmount, order.getOrderId().toString());
		
		Map<String, Object> response = new HashMap<>();
		response.put("orderId", order.getOrderId());
		response.put("razorpayOrderId", razorpayOrderId);
		response.put("razorpayKeyId", razorpayKeyId);
		response.put("amount", totalAmount);
		 
		return response;
	}

	public List<OrderResponse> getOrders(User user) { 

		// fetch all orders for the user
		System.out.println("User: " + user);
		List<Order> orders = orderRepository.findAllByUserId(user.getUserId());
		
		// Create a list of all orders
		List<OrderResponse> allOrdersResponse = orders.stream()
				.map(order -> buildOrderResponse(order))
				.toList();
		
		return allOrdersResponse;  
	}

	private OrderResponse buildOrderResponse(Order order) {
		// TODO Auto-generated method stub
		OrderResponse orderResponse = new OrderResponse();
		orderResponse.setOrderId(order.getOrderId());
		orderResponse.setUsername(order.getUser().getUsername());
		orderResponse.setStatus(order.getStatus().toString());
		orderResponse.setTotalAmount(order.getTotalAmount());
		
		List<OrderItemResponse> items = order.getOrderItems()
				.stream()
				.map(item -> buildOrderItemResponse(item))
				.toList(); 
		orderResponse.setItems(items);
		
		return orderResponse; 
	}

	private OrderItemResponse buildOrderItemResponse(OrderItem item) {
		// TODO Auto-generated method stub
		OrderItemResponse itemResponse = new OrderItemResponse();
		
		Product product = productRepository.findById(item.getProductId())
				.orElseThrow(() -> new IllegalArgumentException("Product not found"));
		List<ProductImage> productImages = productImageRepository.findByProduct_ProductId(product.getProductId());
		String imageUrl = productImages.isEmpty() ? null : productImages.get(0).getImageUrl();
		
		itemResponse.setProductId(item.getProductId());
		itemResponse.setProductName(product.getName());
		itemResponse.setProductImage(imageUrl);
		itemResponse.setQuantity(item.getQuantity());
		itemResponse.setUnitPrice(item.getUnitPrice());
		itemResponse.setTotalPrice(item.getTotalPrice());
		
		return itemResponse;
	}

}
