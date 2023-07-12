package com.sparta.posting.service;

import com.sparta.posting.entity.*;
import com.sparta.posting.repository.CommentLikeRepository;
import com.sparta.posting.repository.CommentRepository;
import com.sparta.posting.repository.LikeRepository;
import com.sparta.posting.repository.PostingRepository;
import com.sparta.posting.security.UserDetailsImpl;
import com.sun.jdi.request.DuplicateRequestException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LikeService {
    private final LikeRepository likeRepository;
    private final PostingRepository postingRepository;
    private final CommentRepository commentRepository;
    private final CommentLikeRepository commentLikeRepository;

    public ResponseEntity<String> post_like(Long postId, UserDetailsImpl userDetails) {
        Posting posting = findPosting(postId);
        User user = userDetails.getUser();

            if (user.getUsername().equals(posting.getUser().getUsername())) {
                return ResponseEntity.badRequest().body("실패");
            } else {
                if (likeRepository.findByUserAndPosting(user, posting).isPresent()) {
                    throw new DuplicateRequestException("이미 눌려있습니다.");
                } else {
                    Like like = new Like(user, posting);
                    likeRepository.save(like);
                    posting.setLikeCount(posting.getLikeCount() + 1);
                    postingRepository.save(posting);
                }
                return ResponseEntity.ok().body("성공");
            }
        }

    public void post_cancel(Long postId, UserDetailsImpl userDetails) {
        Posting posting = findPosting(postId);
        User user = userDetails.getUser();
        if (likeRepository.findByUserAndPosting(user, posting).isPresent()) {
            Like like = likeRepository.findByUserAndPosting(user, posting).orElseThrow(() -> new IllegalArgumentException("좋아요가 눌려있지 않습니다."));
            likeRepository.delete(like);
            posting.setLikeCount(posting.getLikeCount() - 1);
            postingRepository.save(posting);
        } else {
            throw new IllegalArgumentException("좋아요가 눌려 있지 않습니다.");
        }
    }

    public ResponseEntity<String> comment_like(Long postId, Long commentId, UserDetailsImpl userDetails) {
        Posting posting = findPosting(postId);
        Comment comment = findComment(commentId);
        User user = userDetails.getUser();

        if (user.getUsername().equals(comment.getUser().getUsername())) {
            return ResponseEntity.badRequest().body("실패");
        } else {
            if (commentLikeRepository.findByUserAndPostingAndComment(user, posting, comment).isPresent()) {
                throw new DuplicateRequestException("이미 눌려있습니다.");
            } else {
                CommentLike commentlike = new CommentLike(user, comment, posting);
                commentLikeRepository.save(commentlike);

                comment.setLikeCount(comment.getLikeCount() + 1);
                commentRepository.save(comment);
            }
            return ResponseEntity.ok().body("성공");
        }
    }

    public void comment_cancel(Long postId, Long commentId, UserDetailsImpl userDetails) {

        Posting posting = findPosting(postId);
        Comment comment = findComment(commentId);
        User user = userDetails.getUser();

        if (commentLikeRepository.findByUserAndPostingAndComment(user, posting, comment).isPresent()) {
            CommentLike commentlike = commentLikeRepository.findByUserAndPostingAndComment(user, posting, comment).orElseThrow(() -> new IllegalArgumentException("이 댓글에 좋아요가 눌러져 있지 않습니다."));
            commentLikeRepository.delete(commentlike);

            comment.setLikeCount(comment.getLikeCount() - 1);
            commentRepository.save(comment);
        } else {
            throw new IllegalArgumentException("좋아요가 눌려 있지 않습니다.");
        }
    }

    private Posting findPosting(Long id) {
        return postingRepository.findById(id).orElseThrow(() ->
                new IllegalArgumentException("선택한 게시물은 존재하지 않습니다.")
        );
    }

    private Comment findComment(Long id) {
        return commentRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 댓글입니다."));
    }

}
