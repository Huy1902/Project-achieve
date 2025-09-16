package com.ducbrick.finance_tracker.services;

import com.ducbrick.finance_tracker.entities.User;
import com.ducbrick.finance_tracker.exceptions.NoLoggedInUserException;
import com.ducbrick.finance_tracker.repos.UserRepo;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LoggedInUserFinderService {
  private final Logger logger = LoggerFactory.getLogger(LoggedInUserFinderService.class);
  private final UserRepo userRepo;

  public int getId() {
    //Never NULL
    SecurityContext context = SecurityContextHolder.getContext();

    Authentication authentication = context.getAuthentication();

    if (authentication == null) {
      throw new NoLoggedInUserException("No user is logged in");
    }

    if (!(authentication.getPrincipal() instanceof UserDetails)) {
      logger.error("Current authentication principal is not of type UserDetails");
      throw new RuntimeException();
    }

    UserDetails userDetails = (UserDetails) authentication.getPrincipal();

    Optional<User> queryRes = userRepo.findByUsername(userDetails.getUsername());

    if (queryRes.isEmpty()) {
      logger.error("Unexcepted exception: Current user is not present in the database");
      throw new RuntimeException();
    }

    return queryRes.get().getId();
  }
}
