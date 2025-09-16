package com.ducbrick.finance_tracker.entities;

import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "transaction")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Transaction {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @NotBlank
  @Size(max = 256)
  private String name;

  @NotNull
  private BigDecimal value;

  private String notes;

  @NotNull
  private LocalDateTime timestamp;

  @NotNull
  @Valid
  @ManyToOne
  @JoinColumn(name = "category_id")
  private Category category;
}
