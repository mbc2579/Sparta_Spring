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
    public ResponseEntity<String> post_like(@PathVariable Long id, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return likeService.post_like(id, userDetails);
    }

    @DeleteMapping("/post/{id}/like")
    public void post_cancel(@PathVariable Long id, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        likeService.post_cancel(id, userDetails);
    }

    @PostMapping("/post/{id}/comment/like")
    public ResponseEntity<String> comment_like(@PathVariable(name = "id") Long post_id, @PathVariable(name = "id") Long comment_id, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return likeService.comment_like(post_id, comment_id, userDetails);
    }

    @DeleteMapping("/post/{id}/comment/like")
    public void comment_cancel(@PathVariable(name = "id") Long postId, @PathVariable(name = "id") Long commentId, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        likeService.comment_cancel(postId, commentId, userDetails);
    }
}
