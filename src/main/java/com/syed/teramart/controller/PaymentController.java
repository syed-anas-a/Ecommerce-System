package com.syed.teramart.controller;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.razorpay.RazorpayException;
import com.syed.teramart.dto.PaymentVerificationRequest;
import com.syed.teramart.service.PaymentService;


@RestController
@CrossOrigin(origins="http://localhost:5173", allowCredentials = "true")
@RequestMapping("api/payments")
public class PaymentController {
	
	private final PaymentService paymentService;
	
	public PaymentController(PaymentService paymentService) {
		// TODO Auto-generated constructor stub
		this.paymentService = paymentService;
	}
	
	@PostMapping("/verify")
	public ResponseEntity<?> verifyPayment(@RequestBody PaymentVerificationRequest verificationRequest) {
		
		try {
			paymentService.verifyPayment(verificationRequest);
		}
		catch (RazorpayException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", e.getMessage()));
		}
		catch (Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Some error occurred");
		}
		return ResponseEntity.ok("Payment successfull"); 
	} 
	
}
 