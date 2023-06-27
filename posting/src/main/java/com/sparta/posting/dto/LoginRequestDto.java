package com.sparta.posting.dto;

import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class LoginRequestDto {
    private String username;

    private String password;
}
