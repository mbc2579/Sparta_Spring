package com.sparta.posting.dto;

import lombok.Getter;

@Getter
public class PostingRequestDto {
    private String title;
    private String username;
    private String contents;
    private String password;
}
