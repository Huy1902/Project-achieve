package com.ducbrick.finance_tracker.controllers;


import com.ducbrick.finance_tracker.dtos.CategoryStatDto;
import com.ducbrick.finance_tracker.dtos.StatRequestDto;
import com.ducbrick.finance_tracker.services.PieChartStatService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.time.LocalDate;
import java.util.List;

@RequiredArgsConstructor
@Controller
public class HomeController {

  private final PieChartStatService pieChartStatService;

  @GetMapping("/home")
  public String home() {
    return "home";
  }

  @GetMapping("/homeGraph")
  @ResponseBody
  public List<CategoryStatDto> homeGraph() {
    StatRequestDto statRequestDto = new StatRequestDto();
    statRequestDto.setStart(LocalDate.now());
    statRequestDto.setEnd(LocalDate.now());
    return pieChartStatService.get(statRequestDto);
  }
  
  @GetMapping
  public String redirect() {
    return "redirect:/home";
  }
}
