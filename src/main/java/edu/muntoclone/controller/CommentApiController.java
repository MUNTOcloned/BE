package edu.muntoclone.controller;

import edu.muntoclone.dto.CommentRegisterRequset;
import edu.muntoclone.dto.CommentResponse;
import edu.muntoclone.security.PrincipalDetails;
import edu.muntoclone.service.CommentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

//@Slf4j : 추상화(인터페이스) 역할을 하는 라이브러리
@Slf4j
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
class CommentApiController {

    private final CommentService commentService;

    // 댓글 등록
    @PostMapping("/socials/{id}/comments")
    public void commentRegister(@PathVariable Long id,
                                @RequestBody CommentRegisterRequset commentRegisterRequest,
                                @AuthenticationPrincipal PrincipalDetails principalDetails) {

        System.out.println(commentRegisterRequest);
        commentService.registerComment(id, commentRegisterRequest, principalDetails);
    }

    // 특정 소셜 댓글 조회
    @GetMapping("/socials/{id}/comments")
    public List<CommentResponse> findGetComment(@PathVariable Long id) {
        // 소셜 아이디를 보내면 해당하는 아이디의 소셜이 가지고 있는 댓글 정보 가져오기
        // 소셜 정보 : 소셜 ID
        // 댓글 정보 : 작성자 ID, 댓글 내용, 작성시간, 좋아요 개수 반환

        // 받아온 정보 리스트에 추가
        List<CommentResponse> commentResponses = commentService.findAllBySocialId(id)
                .stream()
                .map(c -> new CommentResponse(c.getContent(), c.getWriter(), c.getCreatedAt(), c.getLikeCount()))
                        .collect(Collectors.toList());

        return commentResponses;
    }

    // 댓글 삭제
    @DeleteMapping("/comments/{id}")
    public void commentDelete(@PathVariable Long id, @AuthenticationPrincipal PrincipalDetails principalDetails) {
        // 댓글 아이디로 댓글 삭제
        // 로그인 유저의 ID와 작성자 ID가 일치하는지 확인
        final Long commentWriterId = commentService.findById(id).getWriter().getId();
        final Long loginMemberId = principalDetails.getMember().getId();

        // ID가 다르면 에러 발생
        if (commentWriterId.equals(loginMemberId)) throw new IllegalArgumentException("댓글 작성자가 아닙니다.");

        // ID 일치시 삭제
        commentService.deleteById(id);
    }
}