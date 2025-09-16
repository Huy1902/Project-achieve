package com.ducbrick.finance_tracker.entities;

import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "app_user")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class User {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @NotBlank
  @Size(min = 1, max = 128)
  private String username;

  @NotBlank
  @Email
  @Size(max = 128)
  private String email;

  @NotBlank
  @Size(max = 68)
  private String password;

  @OneToMany(mappedBy = "owner")
  private List<@NotNull @Valid Category> categories;
}
