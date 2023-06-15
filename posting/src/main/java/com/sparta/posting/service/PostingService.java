package com.sparta.posting.service;

import com.sparta.posting.dto.PostingRequestDto;
import com.sparta.posting.dto.PostingResponseDto;
import com.sparta.posting.entity.Posting;
import com.sparta.posting.repository.PostingRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class PostingService {
    private final PostingRepository postingRepository;

    public PostingService(PostingRepository postingRepository) {
        this.postingRepository = postingRepository;
    }

    public PostingResponseDto createPosting(PostingRequestDto requestDto) {
        // RequestDto -> Entity
        Posting posting = new Posting(requestDto);

        // DB 저장
        Posting savePosting = postingRepository.save(posting);

        // Entity -> ResponseDto
        PostingResponseDto postingResponseDto = new PostingResponseDto(savePosting);

        return postingResponseDto;
    }

    public List<PostingResponseDto> getPostings() {
        // DB 조회
        return postingRepository.findAllByOrderByModifiedAtDesc().stream().map(PostingResponseDto::new).toList();
    }

    public PostingResponseDto lookupPosting(Long id) {
        // 해당 게시물이 DB에 존재하는지 확인
        Posting posting = findPosting(id);

        return new PostingResponseDto(posting);
    }

    @Transactional
    public PostingResponseDto updatePosting(Long id, PostingRequestDto requestDto) {
        // 해당 게시물이 DB에 존재하는지 확인
        Posting posting = findPosting(id);

        if(posting.getPassword().equals(requestDto.getPassword())) {
            // 게시물 내용 수정
            posting.update(requestDto);
        } else {
            // 비밀번호가 같지 않을경우 수정이 되지 않음
        }
        return new PostingResponseDto(posting);
    }

    public Boolean deletePosting(Long id, PostingRequestDto requestDto) {
        // 해당 게시물이 DB에 존재하는지 확인
        Posting posting = findPosting(id);

        if(posting.getPassword().equals(requestDto.getPassword())) {
            // 게시물 삭제
            postingRepository.delete(posting);
            return true;
        } else {
            return false;
        }
    }

    private Posting findPosting(Long id) {
        return postingRepository.findById(id).orElseThrow(() ->
                new IllegalArgumentException("선택한 게시물은 존재하지 않습니다.")
        );
    }
}
