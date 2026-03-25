package com.syed.teramart.service;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder; 
import org.springframework.stereotype.Service;

import com.syed.teramart.entity.User;
import com.syed.teramart.repository.UserRepository;

@Service
public class UserService {
	
	private final UserRepository repository;
	private BCryptPasswordEncoder passwordEncoder;

	public UserService(UserRepository repository) {
		// TODO Auto-generated constructor stub
		this.repository = repository;
		this.passwordEncoder = new BCryptPasswordEncoder();
	}
	
	public User registerUser(User user) {
		
		// check if the user already registered
		if(repository.findByUsername(user.getUsername()).isPresent()) {
			throw new RuntimeException("Username already exists");
		}
		if(repository.findByEmail(user.getEmail()).isPresent()) {
			throw new RuntimeException("Email already exists");
		}
		
		// encoding the user given password
		user.setPassword(passwordEncoder.encode(user.getPassword())); 
		
		return repository.save(user);
	}
}
