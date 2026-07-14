package com.photoreview.service;

import com.photoreview.model.Comment;
import com.photoreview.model.Photo;
import com.photoreview.model.User;
import com.photoreview.repository.CommentRepository;
import com.photoreview.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@Transactional
public class CommentService {

    private final CommentRepository commentRepository;

    public CommentService(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    public Comment addComment(User user, Photo photo, String content) {
        if (content == null || content.trim().isEmpty()) {
            throw new IllegalArgumentException("Comment content cannot be empty.");
        }
        Comment comment = new Comment(user, photo, content.trim());
        return commentRepository.save(comment);
    }

    public Comment updateComment(Long commentId, String content, User user) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new ResourceNotFoundException("Comment not found."));

        // Only the original commenter or admin can edit
        if (!comment.getUser().getId().equals(user.getId()) && 
            user.getRoles().stream().noneMatch(r -> r.getName().equals("ROLE_ADMIN"))) {
            throw new IllegalArgumentException("You are not authorized to edit this comment.");
        }

        comment.setContent(content.trim());
        return commentRepository.save(comment);
    }

    public void deleteComment(Long commentId, User user) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new ResourceNotFoundException("Comment not found."));

        // Commenter, photo owner, or administrator are allowed to delete a comment
        if (!comment.getUser().getId().equals(user.getId()) && 
            !comment.getPhoto().getUploader().getId().equals(user.getId()) && 
            user.getRoles().stream().noneMatch(r -> r.getName().equals("ROLE_ADMIN"))) {
            throw new IllegalArgumentException("You are not authorized to delete this comment.");
        }

        commentRepository.delete(comment);
    }

    public List<Comment> findCommentsByPhoto(Long photoId) {
        return commentRepository.findByPhotoIdOrderByCreatedAtDesc(photoId);
    }

    public List<Comment> findCommentsReceivedByPhotographer(Long uploaderId) {
        return commentRepository.findByPhotoUploaderIdOrderByCreatedAtDesc(uploaderId);
    }

    public long countComments() {
        return commentRepository.count();
    }

    public List<Comment> findAll() {
        return commentRepository.findAll();
    }
}
