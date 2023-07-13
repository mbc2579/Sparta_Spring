package com.sparta.posting.dto;

import com.sparta.posting.entity.UserRoleEnum;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SignupRequestDto {

    private String username;

    private String password;

    private UserRoleEnum role;
}


