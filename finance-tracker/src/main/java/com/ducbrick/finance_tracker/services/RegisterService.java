package com.ducbrick.finance_tracker.services;

import com.ducbrick.finance_tracker.dtos.UserRegisterDetailsDto;
import com.ducbrick.finance_tracker.entities.User;
import com.ducbrick.finance_tracker.exceptions.UserAlreadyExistException;
import com.ducbrick.finance_tracker.repos.UserRepo;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Valid;
import jakarta.validation.Validator;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.util.Set;


@Service
@Validated
@RequiredArgsConstructor
public class RegisterService {
  private final Logger logger = LoggerFactory.getLogger(RegisterService.class);
  private final Validator validator;
  private final PasswordEncoder passwordEncoder;
  private final UserRepo userRepo;

  @Transactional
  public void register(@NotNull @Valid UserRegisterDetailsDto dto) throws UserAlreadyExistException {
    User user = User.builder()
            .username(dto.getUsername())
            .email(dto.getEmail())
            .password(passwordEncoder.encode(dto.getPassword()))
            .build();

    Set<ConstraintViolation<User>> violations = validator.validate(user);

    if (!violations.isEmpty()) {
      logger.error("Unexpected constraint violations: {}", violations);
      throw new RuntimeException();
    }

    if (userRepo.findByUsername(user.getUsername()).isPresent()) {
      throw new UserAlreadyExistException("A user with username " + user.getUsername() + " already exists");
    }

    if (userRepo.findByEmail(user.getEmail()).isPresent()) {
      throw new UserAlreadyExistException("A user with email " + user.getEmail() + " already exists");
    }

    userRepo.save(user);
  }
}
