package com.sparta.posting.service;

import com.sparta.posting.dto.ApiResponseDto;
import com.sparta.posting.dto.CommentRequestDto;
import com.sparta.posting.dto.CommentResponseDto;
import com.sparta.posting.entity.*;
import com.sparta.posting.repository.CommentLikeRepository;
import com.sparta.posting.repository.CommentRepository;
import com.sparta.posting.repository.PostingRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final PostingRepository postingRepository;
    private final CommentLikeRepository commentLikeRepository;

    public CommentResponseDto createComment(Long post_id, CommentRequestDto requestDto, User user) {
        // 해당 게시글이 있는지
        Optional<Posting> posting = postingRepository.findById(post_id);

        // 해당 게시글이 없을 경우
        if(!posting.isPresent()) {
            throw new IllegalArgumentException("게시글이 존재하지 않습니다.");
        }

        Comment comment = new Comment(requestDto,posting.get(),user);

        commentRepository.save(comment);
        return new CommentResponseDto(comment);
    }

    @Transactional
    public CommentResponseDto updateComment(Long id, CommentRequestDto requestDto, User user) {

        Optional<Comment> checkComment = commentRepository.findById(id);

        if(!checkComment.isPresent()){
            throw new IllegalArgumentException("해당 댓글이 없습니다.");
        }
        Comment comment = checkComment.get();
        Long commentUserId = comment.getUser().getId();

        if(!commentUserId.equals(user.getId()) && !user.getRole().equals(UserRoleEnum.ADMIN)){
            throw new IllegalArgumentException("작성자만 수정할 수 있습니다.");
        }


        comment.update(requestDto);
        log.info("댓글 수정 완료");
        return new CommentResponseDto(comment);
    }

    public ResponseEntity<ApiResponseDto> deleteComment(Long id, User user) {

        Optional<Comment> checkComment = commentRepository.findById(id);

        if(!checkComment.isPresent()){
            throw new IllegalArgumentException("해당 댓글이 없습니다.");
        }

        Comment comment = checkComment.get();
        Long commentUserId = comment.getUser().getId();
        if(!commentUserId.equals(user.getId()) && !user.getRole().equals(UserRoleEnum.ADMIN)){
            throw new IllegalArgumentException("작성자만 삭제할 수 있습니다.");
        }

        deleteLike(comment.getId());
        commentRepository.delete(comment);
        log.info("댓글 삭제 완료");
        return ResponseEntity.status(200).body(new ApiResponseDto(HttpStatus.OK.value(), "댓글 삭제 성공"));
    }

    private void deleteLike(Long id) {
        List<CommentLike> likeList = commentLikeRepository.findByCommentId(id);
        commentLikeRepository.deleteAll(likeList);
    }
}
