package com.sparta.posting.repository;

import com.sparta.posting.entity.Like;
import com.sparta.posting.entity.Posting;
import com.sparta.posting.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface LikeRepository extends JpaRepository<Like, Long> {
    Optional<Like> findByUserAndPosting(User user, Posting posting);

    List<Like> findByPostingId(Long id);
}
