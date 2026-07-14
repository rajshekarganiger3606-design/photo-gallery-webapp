package com.photoreview.controller;

import com.photoreview.dto.UserRegistrationDto;
import com.photoreview.service.UserService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new UserRegistrationDto());
        return "register";
    }

    @PostMapping("/register")
    public String registerUserAccount(
            @ModelAttribute("user") @Valid UserRegistrationDto registrationDto,
            BindingResult result,
            Model model) {

        if (result.hasErrors()) {
            return "register";
        }

        if (!registrationDto.getPassword().equals(registrationDto.getConfirmPassword())) {
            result.rejectValue("confirmPassword", "error.confirmPassword", "Passwords do not match");
            return "register";
        }

        try {
            userService.registerUser(registrationDto);
        } catch (IllegalArgumentException e) {
            result.rejectValue("email", "error.email", e.getMessage());
            return "register";
        }

        return "redirect:/login?success=true";
    }
}
