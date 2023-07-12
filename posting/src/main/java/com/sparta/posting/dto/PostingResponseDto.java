package com.sparta.posting.dto;

import com.sparta.posting.entity.Comment;
import com.sparta.posting.entity.Posting;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
public class PostingResponseDto {
    private Long id;
    private String title;
    private String contents;
    private Integer likeCount;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
    private List<CommentResponseDto> commentList = new ArrayList<>();

    public PostingResponseDto(Posting posting) {
        this.id = posting.getId();
        this.title = posting.getTitle();
        this.contents = posting.getContents();
        this.likeCount = posting.getLikeCount();
        this.createdAt = posting.getCreatedAt();
        this.modifiedAt = posting.getModifiedAt();

        if (posting.getCommentList().size() > 0) {
            for (Comment comment : posting.getCommentList()) {
                this.commentList.add(new CommentResponseDto(comment));
            }
        }
    }
}