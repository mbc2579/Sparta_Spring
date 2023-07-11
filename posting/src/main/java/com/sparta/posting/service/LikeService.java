package com.sparta.posting.service;

import com.sparta.posting.entity.Like;
import com.sparta.posting.entity.Posting;
import com.sparta.posting.entity.User;
import com.sparta.posting.repository.LikeRepository;
import com.sparta.posting.repository.PostingRepository;
import com.sparta.posting.repository.UserRepository;
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
    private final UserRepository userRepository;


    public ResponseEntity<String> like(Long postId, UserDetailsImpl userDetails) {
        Posting posting = findPosting(postId);
        User user = userDetails.getUser();

            if (user.getUsername().equals(posting.getUser().getUsername())) {
                return ResponseEntity.badRequest().body("Error");
            } else {
                if (likeRepository.findByUserAndPosting(user, posting).isPresent()) {
                    throw new DuplicateRequestException("좋아요가 이미 눌러져 있습니다.");
                } else {
                    Like like = new Like(user, posting);
                    likeRepository.save(like);
                    posting.setLikeCount(posting.getLikeCount() + 1);
                    postingRepository.save(posting);
                }
                return ResponseEntity.ok().body("Success");
            }
        }

    public void cancel(Long postId, UserDetailsImpl userDetails) {
        Posting posting = findPosting(postId);
        User user = userDetails.getUser();
        if (likeRepository.findByUserAndPosting(user, posting).isPresent()) {
            Like like = likeRepository.findByUserAndPosting(user, posting).orElseThrow(() -> new IllegalArgumentException("이 게시글에 좋아요가 눌러져 있지 않습니다."));
            likeRepository.delete(like);
            posting.setLikeCount(posting.getLikeCount() - 1);
            postingRepository.save(posting);
        } else {
            throw new IllegalArgumentException("이 게시글에 좋아요가 눌러져 있지 않습니다.");
        }
    }

    private Posting findPosting(Long id) {
        return postingRepository.findById(id).orElseThrow(() ->
                new IllegalArgumentException("선택한 게시물은 존재하지 않습니다.")
        );
    }

    private User findUser(String username) {
        return userRepository.findByUsername(username).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));
    }
}
