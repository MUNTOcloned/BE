package edu.muntoclone.entity;

import edu.muntoclone.type.MeetingType;
import edu.muntoclone.type.RecruitmentType;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Social {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "SOCIAL_ID")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "MEMBER_ID")
    private Member owner;

    @ManyToOne
    @JoinColumn(name = "CATEGORY_ID")
    private Category category;

    private String title;

    private String content;

    private String imageUrl;

    private LocalDate startDate;

    private LocalTime startTime;

    @Enumerated(EnumType.STRING)
    private MeetingType meetingType;

    @Enumerated(EnumType.STRING)
    private RecruitmentType recruitmentType;

    private String address;

    private Integer limitHeadcount;

    private Integer entryFee;

    private String entryFeeInfo;

    private String question;

}