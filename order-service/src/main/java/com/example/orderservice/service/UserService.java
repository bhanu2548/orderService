package com.example.orderservice.service;

import com.example.orderservice.dto.LoginSuccessDTO;
import com.example.orderservice.entity.User;
import com.example.orderservice.repo.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;


@Service
public class UserService implements UserDetailsService {
	@Autowired
   UserRepository userRepository;

	
    public UserService(UserRepository userRepository) {
		super();
		this.userRepository = userRepository;
	}

	public User register(User user) {
        return userRepository.save(user);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> optionalUser = this.userRepository.findByUsername(username);
        if (!optionalUser.isPresent()) {
          // log.info("Invalid credentials");
            throw new UsernameNotFoundException("Invalid credentials");
        }
        return new org.springframework.security.core.userdetails.User(optionalUser.get().getUsername(), optionalUser.get().getPassword(),
                new ArrayList<>());
    }
}
