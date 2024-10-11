package com.ss.heartlinkapi.login.jwt;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Collection;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.util.StreamUtils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ss.heartlinkapi.login.dto.CustomUserDetails;
import com.ss.heartlinkapi.login.dto.LoginDTO;
import com.ss.heartlinkapi.login.service.CustomUserDetailsService;

public class LoginFilter extends UsernamePasswordAuthenticationFilter {
	
	private final CustomUserDetailsService customUserDetailsService;
	private final PasswordEncoder passwordEncoder;
	private final AuthenticationManager authenticationManager;

	public LoginFilter(CustomUserDetailsService customUserDetailsService,PasswordEncoder passwordEncoder,AuthenticationManager authenticationManager) {
		this.customUserDetailsService = customUserDetailsService;
		this.passwordEncoder = passwordEncoder;
		this.authenticationManager = authenticationManager;
	}

	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
			throws AuthenticationException {

		LoginDTO loginDTO = new LoginDTO();
		try {
			ObjectMapper objectMapper = new ObjectMapper();
			ServletInputStream inputStream = request.getInputStream();
			String body = StreamUtils.copyToString(inputStream, StandardCharsets.UTF_8);
			loginDTO = objectMapper.readValue(body, LoginDTO.class);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		String loginId = loginDTO.getLoginId();
		String password = loginDTO.getPassword();
		
		UserDetails userDetails = customUserDetailsService.loadUserByUsername(loginId);
		
		if(userDetails instanceof CustomUserDetails) {
			CustomUserDetails customUserDetails = (CustomUserDetails) userDetails;
			if(!passwordEncoder.matches(password, customUserDetails.getPassword())) {
				throw new BadCredentialsException("비밀번호 불일치");
			}
			Collection<? extends GrantedAuthority> authorities = customUserDetails.getAuthorities();
			UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(loginId, password, authorities);
			return authenticationManager.authenticate(authToken);
		}else {
			throw new UsernameNotFoundException("UsernameNotFound : "+loginId);
		}
	}
	
	@Override
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
			Authentication authResult) throws IOException, ServletException {
		// TODO Auto-generated method stub
		super.successfulAuthentication(request, response, chain, authResult);
	}
	
	@Override
	protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException failed) throws IOException, ServletException {
		// TODO Auto-generated method stub
		super.unsuccessfulAuthentication(request, response, failed);
	}
	

}
