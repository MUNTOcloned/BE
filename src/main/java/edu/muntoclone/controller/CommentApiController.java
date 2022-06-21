package edu.muntoclone.controller;

import edu.muntoclone.dto.CommentRegisterRequset;
import edu.muntoclone.dto.CommentResponse;
import edu.muntoclone.entity.Comment;
import edu.muntoclone.repository.CommentRepository;
import edu.muntoclone.security.PrincipalDetails;
import edu.muntoclone.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

//@Slf4j : 추상화(인터페이스) 역할을 하는 라이브러리
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
class CommentApiController {

    private final CommentService commentService;

    @PostMapping("/socials/{id}/comments")
    public void commentRegister(@PathVariable Long id,
                                CommentRegisterRequset commentRegisterRequest,
                                @AuthenticationPrincipal PrincipalDetails principalDetails) {
        commentService.registerComment(id, commentRegisterRequest, principalDetails);
    }

//    @GetMapping("/socials/{id}/comments")
//    public CommentResponse findGetComment(@PathVariable Long id) {
//        // 소셜 아이디를 보내면 해당하는 아이디의 소셜이 가지고 있는 댓글 정보 가져오기
//        // 소셜 정보 : 소셜 ID
//        // 댓글 정보 : 작성자 ID, 댓글 내용, 작성시간, 좋아요 개수
//        final Comment comment = commentService.findBySocialId(id);
//        return CommentRepository.of(comment);
//    }

    @DeleteMapping("/comments/{id}")
    public void commentDelete(@PathVariable Long id, @AuthenticationPrincipal PrincipalDetails principalDetails) {
        // 댓글 아이디로 댓글 삭제
        // 로그인 유저의 ID와 작성자 ID가 일치하면 삭제
        final Long commentWriterId = commentService.findById(id).getWriter().getId();
        final Long loginMemberId = principalDetails.getMember().getId();

        if (commentWriterId.equals(loginMemberId)) throw new IllegalArgumentException("댓글 작성자가 아닙니다.");

        commentService.deleteById(id);
    }
}