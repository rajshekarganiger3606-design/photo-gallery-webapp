package com.photoreview.service;

import com.photoreview.model.Like;
import com.photoreview.model.Photo;
import com.photoreview.model.User;
import com.photoreview.repository.LikeRepository;
import com.photoreview.repository.PhotoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Optional;

@Service
@Transactional
public class LikeService {

    private final LikeRepository likeRepository;
    private final PhotoRepository photoRepository;

    public LikeService(LikeRepository likeRepository, PhotoRepository photoRepository) {
        this.likeRepository = likeRepository;
        this.photoRepository = photoRepository;
    }

    public boolean toggleLike(User user, Photo photo) {
        Optional<Like> existingLike = likeRepository.findByUserIdAndPhotoId(user.getId(), photo.getId());
        
        if (existingLike.isPresent()) {
            // Unlike operation
            likeRepository.delete(existingLike.get());
            photo.setTotalLikes(Math.max(0, photo.getTotalLikes() - 1));
            photoRepository.save(photo);
            return false;
        } else {
            // Like operation
            Like newLike = new Like(user, photo);
            likeRepository.save(newLike);
            photo.setTotalLikes(photo.getTotalLikes() + 1);
            photoRepository.save(photo);
            return true;
        }
    }

    public boolean hasLiked(Long userId, Long photoId) {
        return likeRepository.existsByUserIdAndPhotoId(userId, photoId);
    }
}
