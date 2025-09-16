package com.ducbrick.finance_tracker.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class CategoryDetailsDto {
  @NotNull
  private Integer id;

  @NotBlank
  @Size(max = 128)
  private String name;

  @NotBlank
  @Size(min = 7, max = 7)
  private String color;
}
