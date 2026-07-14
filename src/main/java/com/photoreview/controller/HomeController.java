package com.photoreview.controller;

import com.photoreview.model.Photo;
import com.photoreview.model.User;
import com.photoreview.service.CategoryService;
import com.photoreview.service.PhotoService;
import com.photoreview.service.UserService;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import java.util.List;

@Controller
public class HomeController {

    private final PhotoService photoService;
    private final CategoryService categoryService;
    private final UserService userService;

    public HomeController(PhotoService photoService, CategoryService categoryService, UserService userService) {
        this.photoService = photoService;
        this.categoryService = categoryService;
        this.userService = userService;
    }

    @GetMapping({"/", "/home"})
    public String home(Model model) {
        // Fetch 3 latest uploads and 3 most popular
        Page<Photo> latestPage = photoService.getPhotos(null, null, "newest", 0, 3);
        List<Photo> popularPhotos = photoService.getMostPopularPhotos(3);

        model.addAttribute("latestPhotos", latestPage.getContent());
        model.addAttribute("popularPhotos", popularPhotos);
        return "home";
    }

    @GetMapping("/gallery")
    public String gallery(
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) String search,
            @RequestParam(defaultValue = "newest") String sort,
            @RequestParam(defaultValue = "0") int page,
            Model model) {

        int pageSize = 9; // Display 9 items per page
        Page<Photo> photoPage = photoService.getPhotos(categoryId, search, sort, page, pageSize);

        model.addAttribute("photos", photoPage.getContent());
        model.addAttribute("categories", categoryService.findAll());
        model.addAttribute("selectedCategory", categoryId);
        model.addAttribute("searchQuery", search);
        model.addAttribute("selectedSort", sort);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", photoPage.getTotalPages());

        return "gallery";
    }

    @GetMapping("/photographer/{id}")
    public String photographerProfile(@PathVariable Long id, Model model) {
        User photographer = userService.findById(id);
        if (photographer == null) {
            return "redirect:/error?status=404";
        }
        
        List<Photo> photos = photoService.findPhotosByPhotographer(id);
        // Compute total likes received and overall rating mean
        long totalLikes = photos.stream().mapToLong(Photo::getTotalLikes).sum();
        double avgRating = photos.stream().mapToDouble(Photo::getAverageRating).average().orElse(0.0);
        avgRating = Math.round(avgRating * 100.0) / 100.0;

        model.addAttribute("photographer", photographer);
        model.addAttribute("photos", photos);
        model.addAttribute("totalPhotos", photos.size());
        model.addAttribute("totalLikes", totalLikes);
        model.addAttribute("avgRating", avgRating);

        return "photographer-profile";
    }

    @GetMapping("/about")
    public String about() {
        return "about";
    }

    @GetMapping("/contact")
    public String contact() {
        return "contact";
    }

    @GetMapping("/dashboard")
    public String handleDashboardRedirect(Authentication authentication) {
        if (authentication == null) {
            return "redirect:/login";
        }
        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"));
        if (isAdmin) {
            return "redirect:/admin/dashboard";
        }
        return "redirect:/dashboard/home";
    }
}
