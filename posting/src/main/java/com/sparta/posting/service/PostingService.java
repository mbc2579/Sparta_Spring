package com.sparta.posting.service;

import com.sparta.posting.dto.PostingRequestDto;
import com.sparta.posting.dto.PostingResponseDto;
import com.sparta.posting.entity.Posting;
import com.sparta.posting.entity.User;
import com.sparta.posting.repository.PostingRepository;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@Service
public class PostingService {
    private final PostingRepository postingRepository;

    public PostingService(PostingRepository postingRepository) {
        this.postingRepository = postingRepository;
    }

    public PostingResponseDto createPosting(PostingRequestDto requestDto, User user) {
        // RequestDto -> Entity
        Posting posting = new Posting(requestDto);

        posting.setUser(user);

        // DB 저장
        Posting savePosting = postingRepository.save(posting);

        // Entity -> ResponseDto
        PostingResponseDto postingResponseDto = new PostingResponseDto(savePosting);

        return postingResponseDto;
    }

    public List<PostingResponseDto> getPostings() {
        // DB 조회
        return postingRepository.findAllByOrderByModifiedAtDesc().stream().map(PostingResponseDto::new).toList();
    }

    public PostingResponseDto lookupPosting(Long id) {
        // 해당 게시물이 DB에 존재하는지 확인
        Posting posting = findPosting(id);
        return new PostingResponseDto(posting);
    }

    @Transactional
    public PostingResponseDto updatePosting(Long id, PostingRequestDto requestDto, User user) {

        // 1. 해당 게시글이 있는지 확인
        Posting post = this.findPosting(id);

        // 2. 해당 게시글의 작성자라면 수정하도록 함.
        String postUsername = post.getUser().getUsername(); // 게시글의 작성자 이름
        String loginUsername = user.getUsername(); // 로그인된 사용자 이름

        if(postUsername.equals(loginUsername)){
            post.update(requestDto);
        }
        return new PostingResponseDto(post);
    }

    public void deletePosting(HttpServletResponse res, Long id, User user) throws IOException {
        // 1. 해당 게시글이 있는지 확인
        Posting posting = this.findPosting(id);

        // 2. 해당 게시글의 작성자라면 수정하도록 함.
        String postUsername = posting.getUser().getUsername(); // 게시글의 작성자 이름
        String loginUsername = user.getUsername(); // 로그인된 사용자 이름

        if(postUsername.equals(loginUsername)){
            postingRepository.delete(posting);
            this.responseResult(res,200,"게시글 삭제 성공");
        }else {
            this.responseResult(res,401,"게시글 삭제 실패");
        }
    }

    private void responseResult(HttpServletResponse response, int statusCode, String message) throws IOException {
        String jsonResponse = "{\"status\": " + statusCode + ", \"message\": \"" + message + "\"}";

        // Content-Type 및 문자 인코딩 설정
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        // PrintWriter 를 사용하여 응답 데이터 전송
        PrintWriter writer = response.getWriter();
        writer.write(jsonResponse);
        writer.flush();
    }

    private Posting findPosting(Long id) {
        return postingRepository.findById(id).orElseThrow(() ->
                new IllegalArgumentException("선택한 게시물은 존재하지 않습니다.")
        );
    }
}