package com.sparta.posting.service;

import com.sparta.posting.dto.PostingRequestDto;
import com.sparta.posting.dto.PostingResponseDto;
import com.sparta.posting.entity.Like;
import com.sparta.posting.entity.Posting;
import com.sparta.posting.entity.User;
import com.sparta.posting.entity.UserRoleEnum;
import com.sparta.posting.repository.LikeRepository;
import com.sparta.posting.repository.PostingRepository;
import com.sparta.posting.security.UserDetailsImpl;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@Slf4j
@Service
public class PostingService {
    private final PostingRepository postingRepository;
    private final LikeRepository likeRepository;

    public PostingService(LikeRepository likeRepository, PostingRepository postingRepository, LikeRepository likeRepository1) {
        this.postingRepository = postingRepository;
        this.likeRepository = likeRepository1;
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
    public PostingResponseDto updatePosting(Long id, PostingRequestDto requestDto, UserDetailsImpl userDetails) {

        // 1. 해당 게시글이 있는지 확인
        Posting post = this.findPosting(id);

        // 2. 해당 게시글의 작성자라면 수정하도록 함.
//        String postUsername = post.getUser().getUsername(); // 게시글의 작성자 이름
        User loginUser  = userDetails.getUser(); // 로그인된 사용자 이름

        if(loginUser.getRole().equals(UserRoleEnum.ADMIN) || post.getUser().equals(loginUser)){
            post.update(requestDto);
            return new PostingResponseDto(post);
        }
//        this.responseResult(res,400,"작성자만 수정할 수 있습니다.");
        throw new IllegalArgumentException("작성자만 수정할 수 있습니다.");
    }

    public void deletePosting(HttpServletResponse res, Long id, User user) throws IOException{
        // 1. 해당 게시글이 있는지 확인
        Posting posting = this.findPosting(id);

        String postUsername = posting.getUser().getUsername(); // 게시글의 작성자 이름
        String loginUsername = user.getUsername(); // 로그인된 사용자 이름

        if(postUsername.equals(loginUsername) || user.getRole().equals(UserRoleEnum.ADMIN)){
            deleteLike(posting.getId());
            postingRepository.delete(posting);
            this.responseResult(res,200,"게시글 삭제 성공");
        }else {
            throw new IllegalArgumentException("작성자만 삭제할 수 있습니다.");
        }
    }

    private void deleteLike(Long id) {
        List<Like> likeList = likeRepository.findByPostingId(id);
        likeRepository.deleteAll(likeList);
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