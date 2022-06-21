package edu.muntoclone.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CommentRegisterRequset {
    private String content;

    @Builder
    public CommentRegisterRequset(String content) {
        this.content = content;
    }
}
