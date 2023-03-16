package com.ltm.web.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true) // 2023-02-27  @PreAuthorize 를 사용하기 위해 필요
public class SecurityConfig {

	@Bean
	SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http
			.authorizeHttpRequests()
			.requestMatchers(new AntPathRequestMatcher("/**")).permitAll()//로그인 하지 않아도 모든 페이지에 접근가능 (추후 첫 메인페이지만 접근하도록 수정 예정)
			.antMatchers("/css/**", "/js/**", "/img/**").permitAll()

		.and()
			.formLogin()
			.loginPage("/member/login")
			.defaultSuccessUrl("/main")
			
		.and() //2023-02-27 추가
            .logout()
            .logoutRequestMatcher(new AntPathRequestMatcher("/member/logout"))
            .logoutSuccessUrl("/")
            .invalidateHttpSession(true)	
			
         .and() //2023-03-14 추가 csrf토큰 안넣어도 됨.
         	.csrf().disable()
			.cors()
		;
		
		return http.build();
		
	}
	@Bean
	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	@Bean
	AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception{
		return authenticationConfiguration.getAuthenticationManager();
	}

	
}
