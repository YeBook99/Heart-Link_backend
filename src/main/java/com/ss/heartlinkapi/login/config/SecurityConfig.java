package com.ss.heartlinkapi.login.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.ss.heartlinkapi.login.jwt.LoginFilter;
import com.ss.heartlinkapi.login.service.CustomUserDetailsService;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
	
	private final AuthenticationConfiguration authenticationConfiguration;
	private final CustomUserDetailsService customUserDetailsService;
	public SecurityConfig(AuthenticationConfiguration authenticationConfiguration,CustomUserDetailsService customUserDetailsService) {
		this.authenticationConfiguration = authenticationConfiguration;
		this.customUserDetailsService = customUserDetailsService;
	}
	
	@Bean
	public AuthenticationManager authenticationManager() throws Exception {
		return authenticationConfiguration.getAuthenticationManager();
	}

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .formLogin(login -> login.disable())
                .httpBasic(basic -> basic.disable())
                .sessionManagement(config -> config.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth.antMatchers("/user/join").permitAll()
                        .antMatchers("/user/idcheck").permitAll()
                        // 예능 전용
                        .antMatchers("/couple/**", "/admin/**").permitAll()
                        .anyRequest().authenticated());
        http.addFilterAt(new LoginFilter(customUserDetailsService, bCryptPasswordEncoder(), authenticationManager()), UsernamePasswordAuthenticationFilter.class);
        
			return http.build();	
	}
	
	@Bean
	public BCryptPasswordEncoder bCryptPasswordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	
	
}
