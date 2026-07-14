package com.photoreview.service;

import com.photoreview.dto.PhotoUploadDto;
import com.photoreview.model.Category;
import com.photoreview.model.Photo;
import com.photoreview.model.User;
import com.photoreview.repository.PhotoRepository;
import com.photoreview.repository.CategoryRepository;
import com.photoreview.repository.RatingRepository;
import com.photoreview.model.Rating;
import com.photoreview.exception.ResourceNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@Transactional
public class PhotoService {

    private final PhotoRepository photoRepository;
    private final CategoryRepository categoryRepository;
    private final RatingRepository ratingRepository;
    private final FileStorageService fileStorageService;

    public PhotoService(PhotoRepository photoRepository, CategoryRepository categoryRepository, 
                        RatingRepository ratingRepository, FileStorageService fileStorageService) {
        this.photoRepository = photoRepository;
        this.categoryRepository = categoryRepository;
        this.ratingRepository = ratingRepository;
        this.fileStorageService = fileStorageService;
    }

    public Photo uploadPhoto(PhotoUploadDto dto, User uploader) {
        if (dto.getFile() == null || dto.getFile().isEmpty()) {
            throw new IllegalArgumentException("Photo file is required.");
        }
        
        Category category = categoryRepository.findById(dto.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category not found."));

        String fileUrl = fileStorageService.storeFile(dto.getFile());

        Photo photo = new Photo();
        photo.setTitle(dto.getTitle());
        photo.setDescription(dto.getDescription());
        photo.setImageUrl(fileUrl);
        photo.setUploader(uploader);
        photo.setCategory(category);

        return photoRepository.save(photo);
    }

    public Photo updatePhoto(Long photoId, PhotoUploadDto dto, User uploader) {
        Photo photo = photoRepository.findById(photoId)
                .orElseThrow(() -> new ResourceNotFoundException("Photo not found."));

        // Only original uploader or admin has permissions to edit details
        if (!photo.getUploader().getId().equals(uploader.getId()) && 
            uploader.getRoles().stream().noneMatch(r -> r.getName().equals("ROLE_ADMIN"))) {
            throw new IllegalArgumentException("You are not authorized to edit this photo.");
        }

        Category category = categoryRepository.findById(dto.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category not found."));

        photo.setTitle(dto.getTitle());
        photo.setDescription(dto.getDescription());
        photo.setCategory(category);

        // Optionally overwrite file if provided
        if (dto.getFile() != null && !dto.getFile().isEmpty()) {
            fileStorageService.deleteFile(photo.getImageUrl());
            String newFileUrl = fileStorageService.storeFile(dto.getFile());
            photo.setImageUrl(newFileUrl);
        }

        return photoRepository.save(photo);
    }

    public void deletePhoto(Long photoId, User user) {
        Photo photo = photoRepository.findById(photoId)
                .orElseThrow(() -> new ResourceNotFoundException("Photo not found."));

        // Only original uploader or admin has permissions to purge photos
        if (!photo.getUploader().getId().equals(user.getId()) && 
            user.getRoles().stream().noneMatch(r -> r.getName().equals("ROLE_ADMIN"))) {
            throw new IllegalArgumentException("You are not authorized to delete this photo.");
        }

        fileStorageService.deleteFile(photo.getImageUrl());
        photoRepository.delete(photo);
    }

    public Photo findById(Long id) {
        return photoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Photo not found with id: " + id));
    }

    public List<Photo> findPhotosByPhotographer(Long uploaderId) {
        return photoRepository.findByUploaderIdOrderByUploadedAtDesc(uploaderId);
    }

    public Page<Photo> getPhotos(Long categoryId, String searchQuery, String sortBy, int page, int size) {
        Sort sort;
        if ("highest-rated".equalsIgnoreCase(sortBy)) {
            sort = Sort.by(Sort.Direction.DESC, "averageRating").and(Sort.by(Sort.Direction.DESC, "uploadedAt"));
        } else if ("most-popular".equalsIgnoreCase(sortBy)) {
            sort = Sort.by(Sort.Direction.DESC, "totalLikes").and(Sort.by(Sort.Direction.DESC, "uploadedAt"));
        } else {
            sort = Sort.by(Sort.Direction.DESC, "uploadedAt");
        }

        Pageable pageable = PageRequest.of(page, size, sort);

        if (searchQuery != null && !searchQuery.trim().isEmpty()) {
            // General query search bypasses category selections to ensure wide scope matching
            return photoRepository.findByTitleContainingIgnoreCaseOrUploaderDisplayNameContainingIgnoreCaseOrCategoryNameContainingIgnoreCase(
                    searchQuery, searchQuery, searchQuery, pageable);
        } else if (categoryId != null) {
            return photoRepository.findByCategoryId(categoryId, pageable);
        } else {
            return photoRepository.findAll(pageable);
        }
    }

    public void updateAverageRating(Long photoId) {
        Photo photo = photoRepository.findById(photoId)
                .orElseThrow(() -> new ResourceNotFoundException("Photo not found."));

        List<Rating> ratings = ratingRepository.findByPhotoId(photoId);
        if (ratings.isEmpty()) {
            photo.setAverageRating(0.0);
        } else {
            double sum = ratings.stream().mapToDouble(Rating::getScore).sum();
            double avg = sum / ratings.size();
            avg = Math.round(avg * 100.0) / 100.0; // Round to 2 decimal points
            photo.setAverageRating(avg);
        }
        photoRepository.save(photo);
    }

    public long countPhotos() {
        return photoRepository.count();
    }

    public List<Photo> getMostPopularPhotos(int limit) {
        Pageable pageable = PageRequest.of(0, limit, Sort.by(Sort.Direction.DESC, "totalLikes").and(Sort.by(Sort.Direction.DESC, "averageRating")));
        return photoRepository.findAll(pageable).getContent();
    }
}
