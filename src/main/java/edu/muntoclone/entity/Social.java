package edu.muntoclone.entity;

import edu.muntoclone.type.MeetingType;
import edu.muntoclone.type.RecruitmentType;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.util.StringUtils;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Social extends Timestamped {

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

    public void modify(Category category, String title, String content,
                       String imageUrl, LocalDate startDate, LocalTime startTime,
                       MeetingType meetingType, RecruitmentType recruitmentType,
                       Integer limitHeadcount, Integer entryFee, String entryFeeInfo) {
        this.category = category;
        this.title = title;
        this.content = content;
        this.imageUrl = imageUrl;
        this.startDate = startDate;
        this.startTime = startTime;
        this.meetingType = meetingType;
        this.recruitmentType = recruitmentType;
        this.limitHeadcount = limitHeadcount;
        this.entryFee = entryFee;
        this.entryFeeInfo = entryFeeInfo;
    }

    public void modify(Category category, String title, String content,
                       String imageUrl, LocalDate startDate, LocalTime startTime,
                       Integer limitHeadcount) {
        this.category = category;
        this.title = title;
        this.content = content;
        this.imageUrl = imageUrl;
        this.startDate = startDate;
        this.startTime = startTime;
        this.limitHeadcount = limitHeadcount;
    }

    @Builder
    public Social(Member owner, Category category, String title,
                  String content, String imageUrl, LocalDate startDate,
                  LocalTime startTime, MeetingType meetingType, RecruitmentType recruitmentType,
                  String address, Integer limitHeadcount, Integer entryFee,
                  String entryFeeInfo, String question
    ) {
        this.owner = owner;
        this.category = category;
        this.title = title;
        this.content = content;
        this.imageUrl = imageUrl;
        this.startDate = startDate;
        this.startTime = startTime;
        this.meetingType = meetingType;
        this.recruitmentType = recruitmentType;
        this.address = address;
        this.limitHeadcount = limitHeadcount;
        this.entryFee = entryFee;
        this.entryFeeInfo = entryFeeInfo;
        this.question = question;
    }
}