package com.photoreview.controller;

import com.photoreview.model.Comment;
import com.photoreview.model.Photo;
import com.photoreview.model.Rating;
import com.photoreview.model.User;
import com.photoreview.security.CustomUserDetails;
import com.photoreview.service.CommentService;
import com.photoreview.service.LikeService;
import com.photoreview.service.PhotoService;
import com.photoreview.service.RatingService;
import com.photoreview.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

@Controller
public class PhotoController {

    private final PhotoService photoService;
    private final CommentService commentService;
    private final LikeService likeService;
    private final RatingService ratingService;
    private final UserService userService;

    public PhotoController(PhotoService photoService, CommentService commentService, 
                           LikeService likeService, RatingService ratingService, UserService userService) {
        this.photoService = photoService;
        this.commentService = commentService;
        this.likeService = likeService;
        this.ratingService = ratingService;
        this.userService = userService;
    }

    private User getAuthenticatedUser(Authentication auth) {
        if (auth == null) return null;
        CustomUserDetails userDetails = (CustomUserDetails) auth.getPrincipal();
        return userService.findById(userDetails.getUser().getId());
    }

    @GetMapping("/photo-details/{id}")
    public String photoDetails(@PathVariable Long id, Authentication authentication, Model model) {
        Photo photo = photoService.findById(id);
        List<Comment> comments = commentService.findCommentsByPhoto(id);

        boolean hasLiked = false;
        Integer userRating = 0;

        User user = getAuthenticatedUser(authentication);
        if (user != null) {
            hasLiked = likeService.hasLiked(user.getId(), photo.getId());
            Optional<Rating> ratingOpt = ratingService.getUserRating(user.getId(), photo.getId());
            if (ratingOpt.isPresent()) {
                userRating = ratingOpt.get().getScore();
            }
        }

        model.addAttribute("photo", photo);
        model.addAttribute("comments", comments);
        model.addAttribute("hasLiked", hasLiked);
        model.addAttribute("userRating", userRating);
        model.addAttribute("currentUser", user);

        return "photo-details";
    }

    @PostMapping("/photos/{id}/like")
    public String toggleLike(@PathVariable Long id, Authentication authentication) {
        User user = getAuthenticatedUser(authentication);
        Photo photo = photoService.findById(id);
        likeService.toggleLike(user, photo);
        return "redirect:/photo-details/" + id;
    }

    @PostMapping("/photos/{id}/rate")
    public String ratePhoto(@PathVariable Long id, @RequestParam Integer score, Authentication authentication) {
        User user = getAuthenticatedUser(authentication);
        Photo photo = photoService.findById(id);
        try {
            ratingService.ratePhoto(user, photo, score);
        } catch (Exception e) {
            return "redirect:/photo-details/" + id + "?error=rating";
        }
        return "redirect:/photo-details/" + id;
    }

    @PostMapping("/comments/post/{photoId}")
    public String addComment(@PathVariable Long photoId, @RequestParam String content, Authentication authentication) {
        User user = getAuthenticatedUser(authentication);
        Photo photo = photoService.findById(photoId);
        try {
            commentService.addComment(user, photo, content);
        } catch (Exception e) {
            return "redirect:/photo-details/" + photoId + "?error=comment";
        }
        return "redirect:/photo-details/" + photoId;
    }

    @PostMapping("/comments/delete/{commentId}")
    public String deleteComment(@PathVariable Long commentId, @RequestParam Long photoId, Authentication authentication) {
        User user = getAuthenticatedUser(authentication);
        try {
            commentService.deleteComment(commentId, user);
        } catch (Exception e) {
            return "redirect:/photo-details/" + photoId + "?error=delete_comment";
        }
        return "redirect:/photo-details/" + photoId;
    }
}
