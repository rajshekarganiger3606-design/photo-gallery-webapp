package com.photoreview.repository;

import com.photoreview.model.Like;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface LikeRepository extends JpaRepository<Like, Long> {
    Optional<Like> findByUserIdAndPhotoId(Long userId, Long photoId);
    boolean existsByUserIdAndPhotoId(Long userId, Long photoId);
    long countByPhotoId(Long photoId);
}
