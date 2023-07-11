package com.sparta.posting.service;

import com.sparta.posting.dto.ApiResponseDto;
import com.sparta.posting.dto.CommentRequestDto;
import com.sparta.posting.dto.CommentResponseDto;
import com.sparta.posting.entity.Comment;
import com.sparta.posting.entity.Posting;
import com.sparta.posting.entity.User;
import com.sparta.posting.entity.UserRoleEnum;
import com.sparta.posting.repository.CommentRepository;
import com.sparta.posting.repository.PostingRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final PostingRepository postingRepository;

    public CommentResponseDto createComment(Long post_id, CommentRequestDto requestDto, User user) {
        // 해당 게시글이 있는지
        Optional<Posting> posting = postingRepository.findById(post_id);

        // 해당 게시글이 없을 경우
        if(!posting.isPresent()) {
            log.error("게시글이 존재하지 않습니다.");
            return null;
        }

        // 해당 게시글이 있을 경우
        Comment comment = new Comment(requestDto,posting.get(),user);

        // DB에 댓글 저장.
        commentRepository.save(comment);
        return new CommentResponseDto(comment);
    }

    @Transactional
    public CommentResponseDto updateComment(Long id, CommentRequestDto requestDto, User user) {

        Optional<Comment> checkComment = commentRepository.findById(id);

        if(!checkComment.isPresent()){
            log.error("해당 댓글이 없습니다.");
            return null;
        }
        Comment comment = checkComment.get();
        Long commentUserId = comment.getUser().getId();

        if(!commentUserId.equals(user.getId()) && !user.getRole().equals(UserRoleEnum.ADMIN)){
            log.error("해당 댓글의 작성자가 아닙니다.");
            return null;
        }


        comment.update(requestDto);
        log.info("댓글 수정 완료");
        return new CommentResponseDto(comment);
    }

    public ResponseEntity<ApiResponseDto> deleteComment(Long id, User user) {

        Optional<Comment> checkComment = commentRepository.findById(id);

        if(!checkComment.isPresent()){
            log.error("해당 댓글이 없습니다.");
            return null;
        }

        Comment comment = checkComment.get();
        Long commentUserId = comment.getUser().getId();
        if(!commentUserId.equals(user.getId()) && !user.getRole().equals(UserRoleEnum.ADMIN)){
            log.error("해당 댓글의 작성자가 아닙니다.");
            return null;
        }

        commentRepository.delete(comment);
        log.info("댓글 삭제 완료");
        return ResponseEntity.status(200).body(new ApiResponseDto(HttpStatus.OK.value(), "댓글 삭제 성공"));
    }

    public List<CommentResponseDto> getComments(Long postId) {
        List<Comment> commentList = commentRepository.findAllByPostingIdOrderByCreatedAt(postId);
        List<CommentResponseDto> commentResponseDtoList = new ArrayList<>();

        for (Comment comment : commentList) {
            commentResponseDtoList.add(new CommentResponseDto(comment));
        }
        return commentResponseDtoList;
    }
}
