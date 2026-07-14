package com.photoreview.repository;

import com.photoreview.model.Photo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface PhotoRepository extends JpaRepository<Photo, Long> {
    
    // Paginated list of photos in a specific category
    Page<Photo> findByCategoryId(Long categoryId, Pageable pageable);
    
    // Search photos by title, photographer name, or category name with pagination
    Page<Photo> findByTitleContainingIgnoreCaseOrUploaderDisplayNameContainingIgnoreCaseOrCategoryNameContainingIgnoreCase(
            String title, String uploaderDisplayName, String categoryName, Pageable pageable);
            
    // List photos uploaded by a specific photographer, sorted by date
    List<Photo> findByUploaderIdOrderByUploadedAtDesc(Long uploaderId);
}
