package com.syed.teramart.controller;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.syed.teramart.service.UserService;
import com.syed.teramart.entity.*;

@RestController
@CrossOrigin(origins="http://localhost:5173", allowCredentials="true")
@RequestMapping("/api/auth")
public class UserController {
	
	private UserService service;
	
	public UserController(UserService service) {
		this.service = service;
	} 
	 
	@PostMapping("/register")
	public ResponseEntity<?> registerUser(@RequestBody User user) {
		User registeredUser = service.registerUser(user);
		try {
			return ResponseEntity.ok(Map.of("message", "Registration successfull", "user", registeredUser));
		}
		catch(Exception e) {
			return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
		}
	}

}
 