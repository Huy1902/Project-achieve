package com.ducbrick.finance_tracker.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserRegisterDetailsDto {
  @NotBlank(message = "Username must not be empty")
  @Size(min = 1, max = 128, message = "Username size must be between 1 and 128")
  private String username;

  @NotBlank(message = "Email must not be empty")
  @Email
  @Size(max = 128, message = "Email size must not exceed 128")
  private String email;

  @NotBlank(message = "Password must not be empty")
  @Size(max = 128)
  private String password;

}
