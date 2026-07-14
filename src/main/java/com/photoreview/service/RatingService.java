package com.photoreview.service;

import com.photoreview.model.Rating;
import com.photoreview.model.Photo;
import com.photoreview.model.User;
import com.photoreview.repository.RatingRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
@Transactional
public class RatingService {

    private final RatingRepository ratingRepository;
    private final PhotoService photoService;

    public RatingService(RatingRepository ratingRepository, PhotoService photoService) {
        this.ratingRepository = ratingRepository;
        this.photoService = photoService;
    }

    public Rating ratePhoto(User user, Photo photo, Integer score) {
        if (score < 1 || score > 5) {
            throw new IllegalArgumentException("Rating score must be between 1 and 5.");
        }

        Optional<Rating> existingRating = ratingRepository.findByUserIdAndPhotoId(user.getId(), photo.getId());
        Rating rating;
        if (existingRating.isPresent()) {
            rating = existingRating.get();
            rating.setScore(score);
            rating.setRatedAt(LocalDateTime.now());
        } else {
            rating = new Rating(user, photo, score);
        }

        Rating savedRating = ratingRepository.save(rating);
        
        // Synchronously recompute average scores on target photo
        photoService.updateAverageRating(photo.getId());

        return savedRating;
    }

    public Optional<Rating> getUserRating(Long userId, Long photoId) {
        return ratingRepository.findByUserIdAndPhotoId(userId, photoId);
    }

    public long countRatings() {
        return ratingRepository.count();
    }
}
