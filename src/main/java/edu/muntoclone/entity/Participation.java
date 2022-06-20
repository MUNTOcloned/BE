package edu.muntoclone.entity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Participation extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PARTICIPATION_ID")
    private Long id;

    @Lob
    private String answer;

    @Column(nullable = false)
    private Integer approvedStatus;

    @ManyToOne
    @JoinColumn(name = "MEMBER_ID")
    private Member member;

    @ManyToOne
    @JoinColumn(name = "SOCIAL_ID")
    private Social social;

    @Builder
    public Participation(String answer, Integer approvedStatus, Member member, Social social) {
        this.answer = answer;
        this.approvedStatus = approvedStatus;
        this.member = member;
        this.social = social;
    }
}