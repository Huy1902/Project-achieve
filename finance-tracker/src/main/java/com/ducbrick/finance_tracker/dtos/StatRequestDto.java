package com.ducbrick.finance_tracker.dtos;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Getter
@Setter
public class StatRequestDto {
  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
  @NotNull
  private LocalDate start;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
  @NotNull
  private LocalDate end;
}
