package com.example.orderservice.filter;

import java.io.IOException;
import java.util.Optional;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import com.example.orderservice.service.JWTService;
import com.example.orderservice.service.UserService;

@Component
public class AuthenticationFilter extends OncePerRequestFilter {
            @Autowired
	 JWTService jwtService;
            @Autowired
	    UserService userService;
	    
	    
	    public AuthenticationFilter(JWTService jwtService, UserService userService) {
			this.jwtService = jwtService;
			this.userService = userService;
		}

		@Override
	    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
	        Optional<String> optionalJWT = parseJwt(request);
	        if(!optionalJWT.isPresent()){
	            filterChain.doFilter(request, response);
	            return;
	        }
	        String username = jwtService.getUsernameFromToken(optionalJWT.get());

	        UserDetails userDetails = userService.loadUserByUsername(username);
	        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
	                userDetails, null, userDetails.getAuthorities());
	        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

	        SecurityContextHolder.getContext().setAuthentication(authentication);
	        filterChain.doFilter(request, response);
	    }

	    private Optional<String> parseJwt(HttpServletRequest request) {
	        String token = request.getHeader("Authorization");
	        if (StringUtils.hasText(token) && token.startsWith("Bearer ")) {
	            return Optional.of(token.substring(7));
	        }
	        if (StringUtils.hasText(request.getParameter("token"))) {
	            return Optional.of(request.getParameter("token"));
	        }
	        return Optional.empty();
	    }
}
