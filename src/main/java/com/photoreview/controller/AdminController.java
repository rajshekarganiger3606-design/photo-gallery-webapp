package com.photoreview.controller;

import com.photoreview.model.Category;
import com.photoreview.model.User;
import com.photoreview.security.CustomUserDetails;
import com.photoreview.service.CategoryService;
import com.photoreview.service.CommentService;
import com.photoreview.service.PhotoService;
import com.photoreview.service.RatingService;
import com.photoreview.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final UserService userService;
    private final PhotoService photoService;
    private final CommentService commentService;
    private final RatingService ratingService;
    private final CategoryService categoryService;

    public AdminController(UserService userService, PhotoService photoService, CommentService commentService, 
                           RatingService ratingService, CategoryService categoryService) {
        this.userService = userService;
        this.photoService = photoService;
        this.commentService = commentService;
        this.ratingService = ratingService;
        this.categoryService = categoryService;
    }

    private User getAuthenticatedUser(Authentication auth) {
        if (auth == null) return null;
        CustomUserDetails userDetails = (CustomUserDetails) auth.getPrincipal();
        return userService.findById(userDetails.getUser().getId());
    }

    @GetMapping("/dashboard")
    public String adminDashboard(Model model) {
        model.addAttribute("totalUsers", userService.countUsers());
        model.addAttribute("totalPhotos", photoService.countPhotos());
        model.addAttribute("totalComments", commentService.countComments());
        model.addAttribute("totalRatings", ratingService.countRatings());
        
        // Fetch popular photos and active user profiles
        model.addAttribute("popularPhotos", photoService.getMostPopularPhotos(5));
        model.addAttribute("usersList", userService.findAllUsers());

        return "admin/dashboard";
    }

    @GetMapping("/users")
    public String manageUsers(Model model) {
        model.addAttribute("users", userService.findAllUsers());
        return "admin/users";
    }

    @PostMapping("/users/{id}/status")
    public String toggleUserStatus(@PathVariable Long id, @RequestParam boolean enabled) {
        userService.changeUserStatus(id, enabled);
        return "redirect:/admin/users?success=status";
    }

    @PostMapping("/users/{id}/delete")
    public String deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return "redirect:/admin/users?success=delete";
    }

    @GetMapping("/photos")
    public String managePhotos(
            @RequestParam(defaultValue = "0") int page,
            Model model) {
        model.addAttribute("photos", photoService.getPhotos(null, null, "newest", page, 20).getContent());
        return "admin/photos";
    }

    @PostMapping("/photos/{id}/delete")
    public String deletePhotoByAdmin(@PathVariable Long id, Authentication authentication) {
        User admin = getAuthenticatedUser(authentication);
        photoService.deletePhoto(id, admin);
        return "redirect:/admin/photos?success=delete";
    }

    @GetMapping("/categories")
    public String manageCategories(Model model) {
        model.addAttribute("categories", categoryService.findAll());
        model.addAttribute("newCategory", new Category());
        return "admin/categories";
    }

    @PostMapping("/categories/add")
    public String addCategory(@ModelAttribute("newCategory") Category category, Model model) {
        try {
            categoryService.save(category);
        } catch (Exception e) {
            model.addAttribute("errorMessage", e.getMessage());
            model.addAttribute("categories", categoryService.findAll());
            return "admin/categories";
        }
        return "redirect:/admin/categories?success=add";
    }

    @PostMapping("/categories/{id}/delete")
    public String deleteCategory(@PathVariable Long id) {
        categoryService.delete(id);
        return "redirect:/admin/categories?success=delete";
    }

    @GetMapping("/comments")
    public String moderateComments(Model model) {
        model.addAttribute("comments", commentService.findAll());
        return "admin/comments";
    }

    @PostMapping("/comments/{id}/delete")
    public String deleteCommentByAdmin(@PathVariable Long id, Authentication authentication) {
        User admin = getAuthenticatedUser(authentication);
        commentService.deleteComment(id, admin);
        return "redirect:/admin/comments?success=delete";
    }
}
