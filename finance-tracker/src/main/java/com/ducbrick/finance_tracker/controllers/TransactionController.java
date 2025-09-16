package com.ducbrick.finance_tracker.controllers;

import com.ducbrick.finance_tracker.dtos.CategoryDetailsDto;
import com.ducbrick.finance_tracker.dtos.NewCategoryDetailsDto;
import com.ducbrick.finance_tracker.dtos.NewTransactionDetailsDto;
import com.ducbrick.finance_tracker.dtos.TransactionDetailsDto;
import com.ducbrick.finance_tracker.services.CategoryFinderService;
import com.ducbrick.finance_tracker.services.TransactionPersistenceService;
import com.ducbrick.finance_tracker.services.TransactionRetrievalService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Controller
//@Validated
@RequiredArgsConstructor
@RequestMapping("/transaction")
public class TransactionController {
  private final TransactionPersistenceService transactionPersistenceService;
  private final CategoryFinderService categoryFinderService;
  private final TransactionRetrievalService transactionRetrievalService;

  @GetMapping("/add")
  public String addTransaction(Model model) {
    model.addAttribute("categories", categoryFinderService.findAll());
    if(!model.containsAttribute("transaction_dto")) {
      model.addAttribute("transaction_dto", NewTransactionDetailsDto.builder().build());
    }
    /* for add category.
      in case of normal load, this attribute will be used to store the DTO for
      add category function.
      in case of add category failure, a flash attribute will be plugged in from the POST
      in CategoryController
    */
    if(!model.containsAttribute("new_categoryDTO")) {
      model.addAttribute("new_categoryDTO",  new NewCategoryDetailsDto());
    }
    return "transaction/add";
  }

  @PostMapping("/add")
  public String add(@ModelAttribute("transaction_dto") @NotNull @Valid NewTransactionDetailsDto dto,
                    BindingResult bindingResult, RedirectAttributes redirectAttributes) {
    if(bindingResult.hasErrors()) {
      redirectAttributes.addFlashAttribute("transaction_dto", dto);
      redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.transaction_dto",
              bindingResult);
//      System.out.println("add transaction failed!");
      return "redirect:/transaction/add";
    }
    transactionPersistenceService.add(dto);
    redirectAttributes.addFlashAttribute("TransactionAddSuccessMessage",
            "Transaction added successfully.");
    return "redirect:/transaction/add";
  }

  @GetMapping("/all")
  public String getAllTransactionsPage(Model model, @RequestParam(required = false, defaultValue = "1") @Positive Integer page) {
    int pageCount = transactionRetrievalService.getPageCount();
    model.addAttribute("pageCount", pageCount); //total page count
//    System.out.println(pageCount);
    if (pageCount == 0) {
      page = 1;
      model.addAttribute("transactions", Collections.emptyList());
    }
    else {
      List<TransactionDetailsDto> transactions = transactionRetrievalService.getPagedTransactions(page);
      model.addAttribute("transactions", transactions);
    }
    model.addAttribute("page", page); //the page requested by user using bottom navbar


    List<CategoryDetailsDto> categories = categoryFinderService.findAll();
    /* Map each DTO to its own ID, so template engine can get the color string of each ctgr */
    Map<Integer, CategoryDetailsDto> colorMap = categories.stream()
            .collect(Collectors.toMap(CategoryDetailsDto::getId, Function.identity()));
    model.addAttribute("categories", categories);
    model.addAttribute("colorMap", colorMap);

    if (!model.containsAttribute("transaction_edit_dto")) {
      NewTransactionDetailsDto tmp = NewTransactionDetailsDto.builder().build();
      model.addAttribute("transaction_edit_dto", TransactionDetailsDto.builder().details(tmp).build());
    }
    return "transaction/allTransaction";
  }


  @PostMapping("/update")
  public String update(@ModelAttribute("transaction_edit_dto") @NotNull @Valid TransactionDetailsDto dto,
                       BindingResult result,
                       @RequestParam(required = false, defaultValue = "1") @Positive Integer page,
                       RedirectAttributes redirectAttributes) {
    if (result.hasErrors()) {
      redirectAttributes.addFlashAttribute(
              "org.springframework.validation.BindingResult.transaction_edit_dto", result);
      redirectAttributes.addFlashAttribute("page", page); //the page requested by user using bottom navbar
      redirectAttributes.addFlashAttribute("transaction_edit_dto", dto);
      redirectAttributes.addFlashAttribute("showModal", true);
      return "redirect:/transaction/all?page" + page;
    }
    transactionPersistenceService.update(dto);
    redirectAttributes.addFlashAttribute("TransactionUpdateSuccessMessage",
            "Transaction updated successfully.");
    return "redirect:/transaction/all";
  }

  @PostMapping("/delete")
  public String delete(@RequestParam int id, RedirectAttributes redirectAttributes) {
    transactionPersistenceService.delete(id);
    redirectAttributes.addFlashAttribute("TransactionDeleteSuccessMessage",
            "Transaction deleted successfully.");
    return "redirect:/transaction/all";
  }

}
