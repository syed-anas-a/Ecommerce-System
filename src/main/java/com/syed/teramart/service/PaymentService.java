package com.syed.teramart.service;

import java.math.BigDecimal;
import com.syed.teramart.repository.OrderRepository;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import com.razorpay.Utils;
import com.syed.teramart.dto.PaymentVerificationRequest;
import com.syed.teramart.entity.Order;
import com.syed.teramart.entity.OrderStatus;

@Service
public class PaymentService {

    private final OrderRepository orderRepository; 
	
	@Value("${razorpay.key_id}") private String razorpayKeyId;
	
	@Value("${razorpay.key_secret}") private String razorpayKeySecret;
	
	@Autowired
	private final RazorpayClient razorpayClient;  

	
	public PaymentService(RazorpayClient razorpayClient, OrderRepository orderRepository) throws RazorpayException {
		// TODO Auto-generated constructor stub
		this.razorpayClient = razorpayClient;
		// TODO Auto-generated constructor stub
		this.orderRepository = orderRepository;
	}	
	
	public String initiatePayment(BigDecimal amount, String orderId) throws RazorpayException {
		
		var orderRequest = new JSONObject();
		orderRequest.put("amount", amount.multiply(BigDecimal.valueOf(100)).intValue());
		orderRequest.put("currency", "INR");
		orderRequest.put("receipt", "receipt_"+orderId);
		
		com.razorpay.Order razorpayOrder = razorpayClient.orders.create(orderRequest);
		
		return razorpayOrder.get("id");
	}

	public void verifyPayment(PaymentVerificationRequest verificationRequest) throws RazorpayException {
		// TODO Auto-generated method stub
		
		var verifyRequest = new JSONObject();
		verifyRequest.put("razorpay_order_id", verificationRequest.getRazorpayOrderId());
		verifyRequest.put("razorpay_payment_id", verificationRequest.getRazorpayPaymentId());
		verifyRequest.put("razorpay_signature", verificationRequest.getRazorpaySignature());
		
		Order order = orderRepository.findByRazorpayOrderId(verificationRequest.getRazorpayOrderId())
				.orElseThrow(() -> new IllegalArgumentException("Invalid request"));
		
		try {
			Utils.verifyPaymentSignature(verifyRequest, razorpayKeySecret);
			order.setStatus(OrderStatus.SUCCESS);
			order.setRazorpayPaymentId(verificationRequest.getRazorpayPaymentId());
			orderRepository.save(order);
		}
		
		catch (RazorpayException e) {
			order.setStatus(OrderStatus.FAILED);
			orderRepository.save(order);
			throw e;
		}
		catch (Exception e) {
			throw e;
		}
		
	}
	

}
