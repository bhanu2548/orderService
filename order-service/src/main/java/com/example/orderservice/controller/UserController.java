package com.example.orderservice.controller;

import com.example.orderservice.dto.LoginSuccessDTO;
import com.example.orderservice.entity.User;
import com.example.orderservice.service.JWTService;
import com.example.orderservice.service.UserService;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("user")
@CrossOrigin("*")
public class UserController {

	@Autowired
    UserService userService;
	@Autowired
    AuthenticationManager authenticationManager;
	@Autowired
    JWTService jwtService;
	@Autowired
    PasswordEncoder encoder;

    public UserController(UserService userService, AuthenticationManager authenticationManager, JWTService jwtService,
			PasswordEncoder encoder) {
		super();
		this.userService = userService;
		this.authenticationManager = authenticationManager;
		this.jwtService = jwtService;
		this.encoder = encoder;
	}

	@PostMapping("register")
    public User register(@RequestBody User user) {
        user.setPassword(encoder.encode(user.getPassword()));
        return userService.register(user);
    }

    @PostMapping("login")
    public LoginSuccessDTO login(@RequestBody User user) {
        authenticate(user.getUsername(), user.getPassword());
        final UserDetails userDetails = userService
                .loadUserByUsername(user.getUsername());

        final String token = jwtService.generateToken(userDetails);
        
        LoginSuccessDTO dto =new LoginSuccessDTO();
        dto.setJwtToken(token);
        return dto;
    }

    private void authenticate(String username, String password) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        } catch (DisabledException e) {
            throw new UsernameNotFoundException("USER_DISABLED", e);
        } catch (BadCredentialsException e) {
            throw new UsernameNotFoundException("INVALID_CREDENTIALS", e);
        }
    }
}
