package com.photoreview.dto;

import jakarta.validation.constraints.NotBlank;

public class CommentDto {

    @NotBlank(message = "Comment content cannot be empty")
    private String content;

    public CommentDto() {
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
