package com.ducbrick.finance_tracker.controllers;

import com.ducbrick.finance_tracker.dtos.CategoryDetailsDto;
import com.ducbrick.finance_tracker.dtos.NewCategoryDetailsDto;
import com.ducbrick.finance_tracker.dtos.NewTransactionDetailsDto;
import com.ducbrick.finance_tracker.exceptions.TransactionDependencyException;
import com.ducbrick.finance_tracker.services.CategoryPersistenceService;
import com.ducbrick.finance_tracker.services.CategoryFinderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
//@Validated
@RequiredArgsConstructor
@RequestMapping("/category")
public class CategoryController {
  private final Logger logger = LoggerFactory.getLogger(CategoryController.class);
  private final CategoryFinderService categoryFinderService;
  private final CategoryPersistenceService categoryPersistenceService;

  @GetMapping("/all")
  public String allCategory(Model model) {
    model.addAttribute("categories", categoryFinderService.findAll());
    if(!model.containsAttribute("categoryDTO")) {
      model.addAttribute("categoryDTO", CategoryDetailsDto.builder().build());
    }
    if(!model.containsAttribute("new_categoryDTO")) {
      model.addAttribute("new_categoryDTO", new NewCategoryDetailsDto());
    }
    return "category/allCategory";
  }


  //TODO: handle resubmitting from reloading page
  @PostMapping("/add")
  public String add(@ModelAttribute("new_categoryDTO") @Valid NewCategoryDetailsDto dto,
                    BindingResult result, RedirectAttributes redirectAttributes,
                    @RequestParam("redirectTo") String redirectTo) {
    if (result.hasErrors()) { //not working rn
        redirectAttributes.addFlashAttribute("new_categoryDTO", dto);
        redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.new_categoryDTO",
                result);
        redirectAttributes.addFlashAttribute("showCategoryModal", true);
        System.out.println("Add category failed");
      return "redirect:" + redirectTo;
    }
    categoryPersistenceService.add(dto);
    redirectAttributes.addFlashAttribute("CategoryAddSuccessMessage",
            "Category added successfully");
    return "redirect:" + redirectTo;
  }

  @PostMapping("/update")
  public String update(@ModelAttribute("categoryDTO") @Valid CategoryDetailsDto dto, BindingResult result
  , RedirectAttributes redirectAttributes) {
    if (result.hasErrors()) {
      redirectAttributes.addFlashAttribute("categoryDTO", dto);
      redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.categoryDTO",
              result);
      redirectAttributes.addFlashAttribute("showUpdateForm", true);

      return "redirect:/category/all";
    }
    categoryPersistenceService.update(dto);
    redirectAttributes.addFlashAttribute("CategoryUpdateSuccessMessage",
            "Category updated successfully.");
    return "redirect:/category/all";
  }

  @PostMapping("/delete")
  public String delete(@RequestParam int id, RedirectAttributes redirectAttributes) {
    try {
      categoryPersistenceService.delete(id);
    } catch (TransactionDependencyException e) {
      logger.info(e.getMessage());

      redirectAttributes.addFlashAttribute("CategoryDeleteErrorMessage",
              "Cannot delete category with existing transactions!");

      return "redirect:/category/all";
    }
    redirectAttributes.addFlashAttribute("CategoryDeleteSuccessMessage",
            "Successfully deleted category.");
    return "redirect:/category/all";
  }
}
