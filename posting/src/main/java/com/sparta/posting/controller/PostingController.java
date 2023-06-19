package com.sparta.posting.controller;

import com.sparta.posting.dto.PostingRequestDto;
import com.sparta.posting.dto.PostingResponseDto;
import com.sparta.posting.service.PostingService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class PostingController {
    private final PostingService postingService;

    public PostingController(PostingService postingService) {
        this.postingService = postingService;
    }

    @PostMapping("/post")
    public PostingResponseDto createPosting(@RequestBody PostingRequestDto requestDto) {
        return postingService.createPosting(requestDto);
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
    public PostingResponseDto updatePosting(@PathVariable Long id, @RequestBody PostingRequestDto requestDto) {
        return postingService.updatePosting(id, requestDto);
    }

    @DeleteMapping("/post/{id}")
    public Boolean deletePosting(@PathVariable Long id, @RequestBody PostingRequestDto requestDto) {
        return postingService.deletePosting(id, requestDto);
    }
}
