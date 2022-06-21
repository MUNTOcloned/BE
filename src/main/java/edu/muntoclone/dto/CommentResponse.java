package edu.muntoclone.dto;

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
    private String writer;
    private LocalDateTime createdAt;
    private int likeCount;

    @Builder
    public CommentResponse(String content, String writer, LocalDateTime createdAt, int likeCount) {
        this.content = content;
        this.writer = writer;
        this.createdAt = createdAt;
        this.likeCount = likeCount;
    }
}