package com.sparta.posting.dto;

import com.sparta.posting.entity.Posting;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class PostingResponseDto {
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
    private Long id;
    private String title;
    private String contents;
    private String username;

    public PostingResponseDto(Posting posting) {
        this.createdAt = posting.getCreatedAt();
        this.modifiedAt = posting.getModifiedAt();
        this.id = posting.getId();
        this.title = posting.getTitle();
        this.contents = posting.getContents();
        this.username = posting.getUsername();
    }
}
