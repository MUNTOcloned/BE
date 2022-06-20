package edu.muntoclone.service;

import edu.muntoclone.dto.CommentRegisterRequset;
import edu.muntoclone.entity.Comment;
import edu.muntoclone.entity.Member;
import edu.muntoclone.repository.CommentRepository;
import edu.muntoclone.security.PrincipalDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;

    private final SocialService socialService;

    // 댓글 등록
    @Transactional
    public void registerComment(Long sid, CommentRegisterRequset commentRegisterRequest,
                                PrincipalDetails principalDetails) {
        final Member member = principalDetails.getMember();
        final Comment comment = new Comment();

        comment.setSocial(socialService.findById(sid));
        comment.setWriter(member);
        comment.setContent(commentRegisterRequest.getContent());
        comment.setLikeCount(0);

        commentRepository.save(comment);
    }

    // 댓글 조회


    // 댓글 삭제
    @Transactional
    public void deleteById(Long cid){
        commentRepository.deleteById(cid);
    }
}
