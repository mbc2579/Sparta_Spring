package com.sparta.posting.controller;

import com.sparta.posting.dto.PostingRequestDto;
import com.sparta.posting.dto.PostingResponseDto;
import com.sparta.posting.security.UserDetailsImpl;
import com.sparta.posting.service.PostingService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api")
public class PostingController {
    private final PostingService postingService;

    public PostingController(PostingService postingService) {
        this.postingService = postingService;
    }

    @PostMapping("/post")
    public PostingResponseDto createPosting(@RequestBody PostingRequestDto requestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return postingService.createPosting(requestDto, userDetails.getUser());
    }

    @GetMapping("/posts")
    public List<PostingResponseDto> getPostings() {
        return postingService.getPostings();
    }

    @GetMapping("/post/{id}")
    public PostingResponseDto lookupPosting(@PathVariable Long id) {
        return postingService.lookupPosting(id);
    }


    @PutMapping("/post/{id}")
    public PostingResponseDto updatePosting(@PathVariable Long id, @RequestBody PostingRequestDto requestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return postingService.updatePosting(id, requestDto, userDetails);
    }

    @DeleteMapping("/post/{id}")
    public void deletePosting(@PathVariable Long id,HttpServletResponse res, @AuthenticationPrincipal UserDetailsImpl userDetails) throws IOException {
        postingService.deletePosting(res, id, userDetails.getUser());
    }
}