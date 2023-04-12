package com.springboot.rideShare.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.springboot.rideShare.exception.ResourceNotFoundException;
import com.springboot.rideShare.model.LoginRequest;
import com.springboot.rideShare.model.User;
import com.springboot.rideShare.repository.UserRepository;

@RestController
@RequestMapping("/")
public class UserController {

	@Autowired
	private UserRepository userRepository;

	@GetMapping("/users")
	public List<User> getAllUsers() {
		return userRepository.findAll();
	}

	@PostMapping("/users")
	public User createUser(@RequestBody User user) {
		return userRepository.save(user);
	}

	@PostMapping("/login")
	public ResponseEntity<User> login(@RequestBody LoginRequest loginRequest) {
		User user = userRepository.findByEmail(loginRequest.getEmail());
		if (user == null) {
			throw new ResourceNotFoundException("No user found by username: " + loginRequest.getEmail());
		}
		user = userRepository.findByPassword(loginRequest.getPassword());
		if (user == null) {
			throw new ResourceNotFoundException("Password is wrong");
		}
		return ResponseEntity.ok(user);
	}

	@GetMapping("/users/{id}")
	public ResponseEntity<User> getUserbyId(@PathVariable Long id) {
		User user = userRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("No user of id: " + id));
		return ResponseEntity.ok(user);
	}

	@PutMapping("/users/{id}")
	public ResponseEntity<User> updateUserbyId(@PathVariable Long id, @RequestBody User userDetails) {
		User user = userRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("No user of id: " + id));
		user.setFirstName(userDetails.getFirstName());
		user.setLastName(userDetails.getLastName());
		user.setPhone(userDetails.getPhone());
		user.setEmail(userDetails.getEmail());
		user.setPassword(userDetails.getPassword());

		User updatedUser = userRepository.save(user);
		return ResponseEntity.ok(updatedUser);
	}

	@DeleteMapping("/users/{id}")
	public ResponseEntity<Map<String, Boolean>> deleteUser(@PathVariable Long id) {
		User user = userRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("No user of id: " + id));
		userRepository.delete(user);
		Map<String, Boolean> response = new HashMap<>();
		response.put("Deleted", Boolean.TRUE);
		return ResponseEntity.ok(response);
	}

}
