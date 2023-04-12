package com.springboot.rideShare.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.springboot.rideShare.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
	User findByEmail(String username);

	User findByPassword(String password);
}