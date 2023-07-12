package com.sparta.posting.dto;

import com.sparta.posting.entity.Comment;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class CommentResponseDto {
    private Long id;
    private String content;
    private Integer likeCount;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
    private String userName;

    public CommentResponseDto(Comment comment) {
        this.id = comment.getId();
        this.content = comment.getContent();
        this.likeCount = comment.getLikeCount();
        this.createdAt = comment.getCreatedAt();
        this.modifiedAt = comment.getModifiedAt();
        this.userName = comment.getUser().getUsername();
    }
}
