package com.sparta.posting.dto;

import com.sparta.posting.entity.Posting;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class PostingResponseDto {
    private Long id;
    private String title;
    private String contents;
    private Integer likeCount;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;

    public PostingResponseDto(Posting posting) {
        this.id = posting.getId();
        this.title = posting.getTitle();
        this.contents = posting.getContents();
        this.likeCount = posting.getLikeCount();
        this.createdAt = posting.getCreatedAt();
        this.modifiedAt = posting.getModifiedAt();
    }
}