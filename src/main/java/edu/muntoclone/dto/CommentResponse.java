package edu.muntoclone.dto;

import edu.muntoclone.entity.Member;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class CommentResponse {
    private String content;
    private Member writer;
    private LocalDateTime createdAt;
    private int likeCount;

    public CommentResponse(String content, Member writer, LocalDateTime createdAt, int likeCount) {
        this.content = content;
        this.writer = writer;
        this.createdAt = createdAt;
        this.likeCount = likeCount;
    }
}