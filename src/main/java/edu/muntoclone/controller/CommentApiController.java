package edu.muntoclone.controller;

import edu.muntoclone.dto.CommentRegisterRequset;
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

    @GetMapping("/socials/{id}/comments")
    public CommentResponse findGetComment(@PathVariable Long id) {
        final Comment comment = commentService.findById(id);
        return CommentRepository.of(comment);
    }

    @DeleteMapping("/comments/{id}")
    public void commentDelete(@PathVariable Long id, @AuthenticationPrincipal PrincipalDetails principalDetails) {
        final Long commentWriterId = commentService.findById(id).getWirter().getId();
        final Long loginMemberId = principalDetails.getMember().getId();

        if (commentWriterId.equals(loginMemberId)) throw new IllegalArgumentException("");
    }
}