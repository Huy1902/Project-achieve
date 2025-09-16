package com.ducbrick.finance_tracker.controllers;

import com.ducbrick.finance_tracker.dtos.CategoryStatDto;
import com.ducbrick.finance_tracker.dtos.StatRequestDto;
import com.ducbrick.finance_tracker.services.PieChartStatService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@Controller
@RequiredArgsConstructor
@Validated
@RequestMapping("/statistics")
public class StatController {
  private final PieChartStatService pieChartStatService;

  @GetMapping
  public String statistics(Model model) {
    return "statistics";
  }

  @PostMapping("/pie")
  @ResponseBody
  public ResponseEntity<?> getStat(@RequestBody @NotNull @Valid StatRequestDto dto, BindingResult bindingResult) {
    // TODO: make the charts' speen fixed
    if(bindingResult.hasErrors()) {
      return ResponseEntity.badRequest().body(Map.of("message", "bad request, try again."));
    }
    return ResponseEntity.ok(pieChartStatService.get(dto));
  }
}
