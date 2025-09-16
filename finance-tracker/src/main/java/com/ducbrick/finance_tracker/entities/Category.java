package com.ducbrick.finance_tracker.entities;

import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "category")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
public class Category {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @NotBlank(message = "Category name must not be blank.")
  @Size(max = 128, message = "Name's length must not exceed 128.")
  private String name;

  @NotBlank
  @Size(min = 7, max = 7)
  private String color;

  @ManyToOne
  @JoinColumn(name = "owner_id")
  @NotNull
  @Valid
  private User owner;

  @OneToMany(mappedBy = "category")
  private List<@NotNull @Valid Transaction> transactions;
}
