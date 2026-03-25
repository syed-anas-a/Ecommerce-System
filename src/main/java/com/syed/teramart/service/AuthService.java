package com.syed.teramart.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.syed.teramart.entity.*;
import com.syed.teramart.repository.JWTTokenRepository;
import com.syed.teramart.repository.UserRepository;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.SignatureAlgorithm;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Optional;

@Service
public class AuthService {
	
	private final Key SIGNING_KEY;
	
	private final UserRepository userRepository;
	private final JWTTokenRepository jwtTokenRepository;
	private final BCryptPasswordEncoder passwordEncoder;
	
	public AuthService(UserRepository userRepository, 
			JWTTokenRepository jwtTokenRepository, 
			@Value("${jwt.secret}")String jwtSecret) {
		// TODO Auto-generated constructor stub
		this.userRepository = userRepository;
		this.jwtTokenRepository = jwtTokenRepository;
		this.passwordEncoder = new BCryptPasswordEncoder();
		
		if(jwtSecret.getBytes(StandardCharsets.UTF_8).length < 64) {
			throw new IllegalArgumentException("JWT_SECRET in application.properties must be at least 64 bytes long for HS512.");
		}
		this.SIGNING_KEY = Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));
		
	}
	
	public User authenticateUser(String username, String password) {
		User user = userRepository.findByUsername(username)
				.orElseThrow(()-> new RuntimeException("Invalid username"));
		
		if(!passwordEncoder.matches(password, user.getPassword())) {
			throw new RuntimeException("Invalid username or password");
		}
		
		return user; 
	}
	
	public String generateToken(User user) {
		String token;
		LocalDateTime now = LocalDateTime.now();
		
		JWTToken existingToken = jwtTokenRepository.findByUserId(user.getUserId()).orElse(null);
		
		if(existingToken != null && now.isBefore(existingToken.getExpiresAt())) {
			token = existingToken.getToken();
		} 
		else {
			token = generateNewToken(user);
		 	if(existingToken != null) {
				jwtTokenRepository.delete(existingToken);
			} 
		 	saveToken(user, token);
		}
		
		return token; 
	}

	private String generateNewToken(User user) {
		
		return Jwts.builder()
				.setSubject(user.getUsername())
				.claim("role", user.getRole().name())
				.setIssuedAt(new Date())
				.setExpiration(new Date(System.currentTimeMillis()+3600000)) //1hr
				.signWith(SIGNING_KEY, SignatureAlgorithm.HS512)
				.compact();
	}
	
	public void saveToken(User user, String token) {
		JWTToken jwtToken = new JWTToken(user, token, LocalDateTime.now().plusHours(1));
		jwtTokenRepository.save(jwtToken);
	}
	
	public boolean validateToken(String token) {
		
		try {
			System.err.println("VALIDATING TOKEN...");
			
			// Parsing and validating token
			Jwts.parserBuilder()
				.setSigningKey(SIGNING_KEY)
				.build()
				.parseClaimsJws(token);
			
			// Check if token exists in db and isn't expired
			Optional<JWTToken> jwtToken = jwtTokenRepository.findByToken(token);
			if(jwtToken.isPresent()) {
				System.err.println("Token Expiry: " + jwtToken.get().getExpiresAt());
				System.err.println("Current Time: " + LocalDateTime.now());
				return jwtToken.get().getExpiresAt().isAfter(LocalDateTime.now());
			}
			
			return false;
		}
		catch (Exception e) {
			System.err.println("Token validation failed: " + e.getMessage());
			return false;
		}
	}
	
	public String extractUsername(String token) {
		return Jwts.parserBuilder()
				.setSigningKey(SIGNING_KEY)
				.build()
				.parseClaimsJws(token)
				.getBody()
				.getSubject();
	}

	public void logoutUser(Integer userId) {
		// TODO Auto-generated method stub
		jwtTokenRepository.deleteByUser(userId);
		
	}
	
}






