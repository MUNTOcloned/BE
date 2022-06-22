package edu.muntoclone.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CommentRegisterRequset {
    private String content;
    private int likeCount;

    @Builder
    public CommentRegisterRequset(String content) {
        this.content = content;
        this.likeCount = likeCount;
    }
}
