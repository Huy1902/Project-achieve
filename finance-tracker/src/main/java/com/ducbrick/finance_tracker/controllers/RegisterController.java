package com.ducbrick.finance_tracker.controllers;

import com.ducbrick.finance_tracker.dtos.UserRegisterDetailsDto;
import com.ducbrick.finance_tracker.entities.User;
import com.ducbrick.finance_tracker.exceptions.UserAlreadyExistException;
import com.ducbrick.finance_tracker.services.RegisterService;
import com.ducbrick.finance_tracker.wrappers.SecurityUser;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


@Controller
@RequestMapping("/register")
@RequiredArgsConstructor
//@Validated
public class RegisterController {
  private final RegisterService registerService;

  @GetMapping
  public String registerForm(Model model) {
    //create new userDTO
    model.addAttribute("user", new UserRegisterDetailsDto());
    return "register";
  }

  @PostMapping
  public String register(@ModelAttribute("user") @Valid UserRegisterDetailsDto user, BindingResult result, Model model, RedirectAttributes redirectAttributes)
{
    if (result.hasErrors()) {
      model.addAttribute("user", user);
      return "register";
    }
    try {
      registerService.register(user);
    } catch(UserAlreadyExistException e) {
      result.addError(new ObjectError("user", e.getMessage()));
      model.addAttribute("user", user);
      return "register";
    }

    redirectAttributes.addFlashAttribute("successMessage", "Your account has been successfully registered");
    return "redirect:/login";
  }

  @GetMapping("/success")
  public String success(Model model) {
    return "success";
  }

}
