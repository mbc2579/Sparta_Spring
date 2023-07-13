package com.sparta.posting.service;

import com.sparta.posting.dto.LoginRequestDto;
import com.sparta.posting.dto.SignupRequestDto;
import com.sparta.posting.entity.User;
import com.sparta.posting.entity.UserRoleEnum;
import com.sparta.posting.jwt.JwtUtil;
import com.sparta.posting.repository.UserRepository;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.regex.Pattern;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;


    public String signup(SignupRequestDto requestDto) {
        String username = requestDto.getUsername();
        String password = passwordEncoder.encode(requestDto.getPassword());
        UserRoleEnum role = requestDto.getRole();

        if (Pattern.matches("^[a-zA-Z0-9`~!@#$%^&*()-_=+]{8,15}$", requestDto.getPassword())) {
            // 회원 중복 확인
            Optional<User> checkUsername = userRepository.findByUsername(username);
            if (checkUsername.isPresent()) {
                throw new IllegalArgumentException("중복된 username 입니다.");
            } else {
                User user = new User(username, password, role);
                userRepository.save(user);

                return "";
            }

        }
        throw new IllegalArgumentException("username과 password 형식에 맞춰 작성해주세요");

//        UserRoleEnum user = UserRoleEnum.USER;

    }

//    public void login(LoginRequestDto requestDto, HttpServletResponse response) {
//        // 클라이언트로 부터 전달 받은 id 와 password 를 가져옵니다.
//        String inputName = requestDto.getUsername();
//        String password = requestDto.getPassword();
//
//        // 사용자를 확인하고, 비밀번호를 확인합니다.
//        Optional<User> checkUser = userRepository.findByUsername(inputName);
//
//        // DB에 없는 사용자인 경우 혹은 비밀번호가 일치하지 않을 경우
//        if (!checkUser.isPresent() || !passwordEncoder.matches(password, checkUser.get().getPassword())) {
//            // 서버 측에 로그를 찍는 역할을 합니다.
//            log.info(requestDto.getUsername());
//            log.info(requestDto.getPassword());
//            log.error("로그인 정보가 일치하지 않습니다.");
//            throw new IllegalArgumentException("로그인 정보가 일치하지 않습니다.");
//        }
//
//        // JWT 생성 및 쿠키에 저장 후 Response 객체에 추가..
//        String token = jwtUtil.createToken(requestDto.getUsername(), UserRoleEnum.USER);
//        log.info("token : " + token);
//        response.addHeader(JwtUtil.AUTHORIZATION_HEADER, token);
//
//        // 서버 측에 로그를 찍는 역할을 합니다.
//        log.info(inputName + "님이 로그인에 성공하였습니다");
//    }
}
