package com.photoreview.controller;

import com.photoreview.dto.ProfileUpdateDto;
import com.photoreview.model.User;
import com.photoreview.security.CustomUserDetails;
import com.photoreview.service.UserService;
import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/profile")
public class ProfileController {

    private final UserService userService;

    public ProfileController(UserService userService) {
        this.userService = userService;
    }

    private User getAuthenticatedUser(Authentication auth) {
        if (auth == null) return null;
        CustomUserDetails userDetails = (CustomUserDetails) auth.getPrincipal();
        return userService.findById(userDetails.getUser().getId());
    }

    @GetMapping
    public String showProfileForm(Authentication authentication, Model model) {
        User user = getAuthenticatedUser(authentication);
        ProfileUpdateDto dto = new ProfileUpdateDto();
        dto.setDisplayName(user.getDisplayName());
        dto.setBio(user.getBio());

        model.addAttribute("profileDto", dto);
        model.addAttribute("currentUser", user);
        return "profile";
    }

    @PostMapping
    public String updateProfile(
            @ModelAttribute("profileDto") @Valid ProfileUpdateDto dto,
            BindingResult result,
            Authentication authentication,
            Model model) {

        User user = getAuthenticatedUser(authentication);

        if (dto.getNewPassword() != null && !dto.getNewPassword().isEmpty()) {
            if (dto.getCurrentPassword() == null || dto.getCurrentPassword().isEmpty()) {
                result.rejectValue("currentPassword", "error.currentPassword", "Current password is required to change passwords.");
            }
            if (!dto.getNewPassword().equals(dto.getConfirmNewPassword())) {
                result.rejectValue("confirmNewPassword", "error.confirmNewPassword", "New passwords do not match.");
            }
        }

        if (result.hasErrors()) {
            model.addAttribute("currentUser", user);
            return "profile";
        }

        try {
            userService.updateProfile(user, dto);
        } catch (IllegalArgumentException e) {
            result.rejectValue("currentPassword", "error.currentPassword", e.getMessage());
            model.addAttribute("currentUser", user);
            return "profile";
        }

        return "redirect:/profile?success=true";
    }
}
