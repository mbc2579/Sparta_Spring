package com.sparta.posting.controller;

import com.sparta.posting.security.UserDetailsImpl;
import com.sparta.posting.service.LikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class LikeController {
    private final LikeService likeService;

    @PostMapping("/post/{id}/like")
    public ResponseEntity<String> like(@PathVariable(name = "id") Long post_id, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return likeService.like(post_id, userDetails);
    }

    @DeleteMapping("/post/{id}/like")
    public void cancel(@PathVariable(name = "id") Long postId, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        likeService.cancel(postId, userDetails);
    }
}
