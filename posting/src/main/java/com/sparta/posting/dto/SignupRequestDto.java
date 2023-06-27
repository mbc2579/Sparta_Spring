package com.sparta.posting.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SignupRequestDto {


    @Pattern(regexp = "^[a-z0-9]{4,10}$")
    private String username;


//    @Pattern(regexp = "^[A-Za-z0-9]{8,15}$")
    private String password;

}


