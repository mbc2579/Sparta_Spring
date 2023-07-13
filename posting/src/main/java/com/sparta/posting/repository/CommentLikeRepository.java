package com.sparta.posting.repository;

import com.sparta.posting.entity.*;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CommentLikeRepository extends JpaRepository<CommentLike, Long> {
    Optional<CommentLike> findByUserAndPostingAndComment(User user, Posting posting, Comment comment);

    List<CommentLike> findByCommentId(Long id);
}
