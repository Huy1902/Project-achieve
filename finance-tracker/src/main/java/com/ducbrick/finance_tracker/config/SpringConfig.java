package com.ducbrick.finance_tracker.config;

import com.ducbrick.finance_tracker.repos.UserRepo;
import com.ducbrick.finance_tracker.services.SecurityUserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SpringConfig {
  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http.formLogin(form -> form.loginPage("/login").permitAll().defaultSuccessUrl("/home", true))
        .httpBasic(Customizer.withDefaults())
        .authorizeHttpRequests(c -> {
          c.requestMatchers("/register").permitAll()
           .anyRequest().authenticated();
        })
//        .csrf(AbstractHttpConfigurer::disable)
        .logout((logout) -> logout.logoutSuccessUrl("/login?logout").permitAll());

    return http.build();
  }

  @Bean
  public UserDetailsService userDetailsService(UserRepo userRepo) {
    return new SecurityUserService(userRepo);
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }
}
