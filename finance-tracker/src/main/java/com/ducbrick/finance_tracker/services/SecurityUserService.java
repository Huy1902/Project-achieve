package com.ducbrick.finance_tracker.services;

import com.ducbrick.finance_tracker.entities.User;
import com.ducbrick.finance_tracker.repos.UserRepo;
import com.ducbrick.finance_tracker.wrappers.SecurityUser;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

@RequiredArgsConstructor
public class SecurityUserService implements UserDetailsService {
  private final UserRepo userRepo;

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    Optional<User> user = userRepo.findByUsername(username);
    return user.map(SecurityUser::new).orElseThrow(() -> new UsernameNotFoundException("User not found"));
  }
}
