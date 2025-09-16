package com.ducbrick.finance_tracker.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NewCategoryDetailsDto {
  @NotBlank(message = "Category name must not be blank :(")
  @Size(max = 128)
  private String name;

  @NotBlank
  @Size(min = 7, max = 7)
  private String color;
}
