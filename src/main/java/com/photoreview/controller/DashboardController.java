package com.photoreview.controller;

import com.photoreview.dto.PhotoUploadDto;
import com.photoreview.model.Comment;
import com.photoreview.model.Photo;
import com.photoreview.model.User;
import com.photoreview.security.CustomUserDetails;
import com.photoreview.service.CategoryService;
import com.photoreview.service.CommentService;
import com.photoreview.service.PhotoService;
import com.photoreview.service.UserService;
import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Controller
@RequestMapping("/dashboard")
public class DashboardController {

    private final PhotoService photoService;
    private final CategoryService categoryService;
    private final CommentService commentService;
    private final UserService userService;

    public DashboardController(PhotoService photoService, CategoryService categoryService, 
                               CommentService commentService, UserService userService) {
        this.photoService = photoService;
        this.categoryService = categoryService;
        this.commentService = commentService;
        this.userService = userService;
    }

    private User getAuthenticatedUser(Authentication auth) {
        if (auth == null) return null;
        CustomUserDetails userDetails = (CustomUserDetails) auth.getPrincipal();
        return userService.findById(userDetails.getUser().getId());
    }

    @GetMapping("/home")
    public String dashboardHome(Authentication authentication, Model model) {
        User user = getAuthenticatedUser(authentication);
        List<Photo> photos = photoService.findPhotosByPhotographer(user.getId());
        List<Comment> comments = commentService.findCommentsReceivedByPhotographer(user.getId());

        long totalLikes = photos.stream().mapToLong(Photo::getTotalLikes).sum();
        double avgRating = photos.stream().mapToDouble(Photo::getAverageRating).average().orElse(0.0);
        avgRating = Math.round(avgRating * 100.0) / 100.0;

        model.addAttribute("photos", photos);
        model.addAttribute("comments", comments);
        model.addAttribute("totalPhotos", photos.size());
        model.addAttribute("totalLikes", totalLikes);
        model.addAttribute("avgRating", avgRating);
        return "dashboard/home";
    }

    @GetMapping("/upload")
    public String showUploadForm(Model model) {
        model.addAttribute("photoDto", new PhotoUploadDto());
        model.addAttribute("categories", categoryService.findAll());
        return "dashboard/edit-photo"; // Reuse the edit-photo template with a check
    }

    @PostMapping("/upload")
    public String uploadPhoto(
            @ModelAttribute("photoDto") @Valid PhotoUploadDto photoDto,
            BindingResult result,
            Authentication authentication,
            Model model) {

        if (photoDto.getFile() == null || photoDto.getFile().isEmpty()) {
            result.rejectValue("file", "error.photoDto", "Image file is required.");
        }

        if (result.hasErrors()) {
            model.addAttribute("categories", categoryService.findAll());
            return "dashboard/edit-photo";
        }

        User user = getAuthenticatedUser(authentication);
        try {
            photoService.uploadPhoto(photoDto, user);
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Error saving photo: " + e.getMessage());
            model.addAttribute("categories", categoryService.findAll());
            return "dashboard/edit-photo";
        }

        return "redirect:/dashboard/home?success=upload";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Authentication authentication, Model model) {
        Photo photo = photoService.findById(id);
        User user = getAuthenticatedUser(authentication);

        // Check ownership
        if (!photo.getUploader().getId().equals(user.getId()) && 
            user.getRoles().stream().noneMatch(r -> r.getName().equals("ROLE_ADMIN"))) {
            return "redirect:/error?status=403";
        }

        PhotoUploadDto dto = new PhotoUploadDto();
        dto.setTitle(photo.getTitle());
        dto.setDescription(photo.getDescription());
        dto.setCategoryId(photo.getCategory() != null ? photo.getCategory().getId() : null);

        model.addAttribute("photoDto", dto);
        model.addAttribute("photoId", photo.getId());
        model.addAttribute("categories", categoryService.findAll());
        return "dashboard/edit-photo";
    }

    @PostMapping("/edit/{id}")
    public String editPhoto(
            @PathVariable Long id,
            @ModelAttribute("photoDto") @Valid PhotoUploadDto photoDto,
            BindingResult result,
            Authentication authentication,
            Model model) {

        if (result.hasErrors()) {
            model.addAttribute("photoId", id);
            model.addAttribute("categories", categoryService.findAll());
            return "dashboard/edit-photo";
        }

        User user = getAuthenticatedUser(authentication);
        try {
            photoService.updatePhoto(id, photoDto, user);
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Error updating photo: " + e.getMessage());
            model.addAttribute("photoId", id);
            model.addAttribute("categories", categoryService.findAll());
            return "dashboard/edit-photo";
        }

        return "redirect:/dashboard/home?success=edit";
    }

    @PostMapping("/delete/{id}")
    public String deletePhoto(@PathVariable Long id, Authentication authentication) {
        User user = getAuthenticatedUser(authentication);
        try {
            photoService.deletePhoto(id, user);
        } catch (Exception e) {
            return "redirect:/dashboard/home?error=delete";
        }
        return "redirect:/dashboard/home?success=delete";
    }
}
