package com.ss.heartlinkapi.config;

import java.util.Collections;

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
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.ss.heartlinkapi.login.jwt.CustomLogoutFilter;
import com.ss.heartlinkapi.login.jwt.JWTFilter;
import com.ss.heartlinkapi.login.jwt.JWTUtil;
import com.ss.heartlinkapi.login.jwt.LoginFilter;
import com.ss.heartlinkapi.login.service.CustomLogoutService;
import com.ss.heartlinkapi.login.service.CustomUserDetailsService;
import com.ss.heartlinkapi.login.service.JWTService;
import com.ss.heartlinkapi.login.service.RefreshTokenService;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
	
	private final AuthenticationConfiguration authenticationConfiguration;
	private final CustomUserDetailsService customUserDetailsService;
	private final JWTUtil jwtUtil;
	private final JWTService jwtService;
	private final RefreshTokenService refreshTokenService;
	private final CustomLogoutService customLogoutService;

	public SecurityConfig(AuthenticationConfiguration authenticationConfiguration,
			CustomUserDetailsService customUserDetailsService, JWTUtil jwtUtil, JWTService jwtService,
			RefreshTokenService refreshTokenService, CustomLogoutService customLogoutService) {
		this.authenticationConfiguration = authenticationConfiguration;
		this.customUserDetailsService = customUserDetailsService;
		this.jwtUtil = jwtUtil;
		this.jwtService = jwtService;
		this.refreshTokenService = refreshTokenService;
		this.customLogoutService = customLogoutService;
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
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .sessionManagement(config -> config.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth.antMatchers("/user/join").permitAll()
                        .antMatchers("/user/idcheck").permitAll()
                        .antMatchers("/reissue").permitAll()
                        .antMatchers("/user/sms/**").permitAll()
                        //토큰 role값 검증 확인용
                        //.antMatchers("/user/check").hasRole("USER")
                        // 예능 전용
                        .antMatchers("/couple/**", "/admin/**", "/search/**").permitAll()
                        .antMatchers("/dm/**","/message","/report","/notifications/**").permitAll()
						.antMatchers("/img/**").permitAll()
						// 정훈 전용
						.antMatchers("/feed/**").permitAll()
						.antMatchers("/following/**").permitAll()
						.antMatchers("/like/**").permitAll()
                        .anyRequest().authenticated());
        http.addFilterBefore(new JWTFilter(jwtService), LoginFilter.class);
        http.addFilterAt(new LoginFilter(customUserDetailsService, bCryptPasswordEncoder(), authenticationManager(),jwtUtil,refreshTokenService), UsernamePasswordAuthenticationFilter.class);
        http.addFilterBefore(new CustomLogoutFilter(customLogoutService), LogoutFilter.class);
			return http.build();	
	}
	
	@Bean
	public BCryptPasswordEncoder bCryptPasswordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Bean
	public CorsConfigurationSource corsConfigurationSource() {
		
		CorsConfiguration configuration = new CorsConfiguration();
		
		configuration.setAllowedOrigins(Collections.singletonList("http://localhost:3000"));
		configuration.setAllowedMethods(Collections.singletonList("*"));
		configuration.setAllowCredentials(true);
		configuration.setAllowedHeaders(Collections.singletonList("*"));
		configuration.setMaxAge(3600L);
		configuration.setExposedHeaders(Collections.singletonList("Authorization"));
		
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
	    source.registerCorsConfiguration("/**", configuration);
	    
	    return source;
	}
	
}
