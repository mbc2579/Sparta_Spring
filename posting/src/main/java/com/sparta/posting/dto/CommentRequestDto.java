package com.sparta.posting.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommentRequestDto {
    @NotBlank(message = "빈칸입니다. 다시 작성해주세요.")
    private String Content;
}
