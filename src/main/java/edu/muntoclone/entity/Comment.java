package edu.muntoclone.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Setter
@Getter
@NoArgsConstructor(access = AccessLevel.PUBLIC)
// AccessLever.PROTECTED :
public class Comment extends Timestamped{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "COMMENT_ID")
    private Long id;

    @Column(nullable = false)
    private String content;

    @Column
    private int likeCount;

    @ManyToOne
    @JoinColumn(name = "SOCIAL_ID")
    private Social social;

    @ManyToOne
    @JoinColumn(name = "MEMBER_ID")
    private Member writer;

    @Builder
    public Comment(String content, int likeCount, Social social, Member writer) {
        this.content = content;
        this.likeCount = likeCount;
        this.social = social;
        this.writer = writer;
    }

    // Setter를 지양해야 하는 이유?
}
