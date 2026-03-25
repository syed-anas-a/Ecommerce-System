package com.syed.teramart.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.syed.teramart.dto.LoginRequest;
import com.syed.teramart.entity.User;
import com.syed.teramart.service.AuthService;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@RestController
@CrossOrigin(origins="http://localhost:5173", allowCredentials="true")
@RequestMapping("/api/auth")
public class AuthController {
	
	private final AuthService authService;
	public AuthController(AuthService authService) {
		this.authService = authService;
	}
	
	@PostMapping("/login")
	public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest, HttpServletResponse response) {
		try { 
			User user = authService.authenticateUser(loginRequest.getUsername(), loginRequest.getPassword());
			String token = authService.generateToken(user);
			
			Cookie cookie = new Cookie("authToken", token);
			cookie.setHttpOnly(true);
			cookie.setSecure(false);
			cookie.setPath("/");
			cookie.setMaxAge(3600); // 1hr
			cookie.setDomain("localhost");
			response.addCookie(cookie);
			
			response.addHeader("Set-Cookie", 
					String.format("authToken=%s; HttpOnly; Path=/; Max-Age=3600; SameSite=None", token));
			
			Map<String, Object> responseBody = new HashMap<>();
			responseBody.put("message", "Login Successfull");
			responseBody.put("role", user.getRole().name());
			responseBody.put("username", user.getUsername());
			
			return ResponseEntity.ok(responseBody);
			
		}
		catch (Exception e) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Some error occurred"); 
		}
	}
	
	@PostMapping("/logout")
	public ResponseEntity<?> logout(HttpServletRequest request, HttpServletResponse response) {
		
		try {
			User user = (User) request.getAttribute("autheticatedUser");
			authService.logoutUser(user.getUserId());
			Cookie cookie = new Cookie("authToken", null);
			cookie.setHttpOnly(true);
			cookie.setMaxAge(0);
			cookie.setPath("/");
			response.addCookie(cookie);
			
			return ResponseEntity.ok(Map.of("message", "Logout Successfully"));
		}
		catch(Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("message", "Logout unsuccessfull"));
		}
		 
	}

}

