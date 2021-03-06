package edu.muntoclone.dto;

import edu.muntoclone.entity.Social;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SocialDetailsResponse {

    private Long id;
    private Long categoryId;
    private String socialImageUrl;
    private String title;
    private String content;
    private String meetingType;
    private String recruitmentType;
    private String address;
    private LocalDate startDate;
    private LocalTime startTime;
    private Integer limitHeadcount;
    private Integer entryFee;
    private String entryFeeInfo;
    private String question;
    private Integer socialLikeCount;

    private boolean isParticipate;

    @Builder
    public SocialDetailsResponse(Long id, Long categoryId, String socialImageUrl, String title, String content,
                                 String meetingType, String recruitmentType, String address, String question,
                                 LocalDate startDate, LocalTime startTime, Integer limitHeadcount,
                                 Integer entryFee, String entryFeeInfo, Integer socialLikeCount, boolean isParticipate) {

        this.categoryId = categoryId;
        this.id = id;
        this.socialImageUrl = socialImageUrl;
        this.title = title;
        this.content = content;
        this.meetingType = meetingType;
        this.recruitmentType = recruitmentType;
        this.address = address;
        this.startDate = startDate;
        this.startTime = startTime;
        this.limitHeadcount = limitHeadcount;
        this.question = question;
        this.entryFee = entryFee;
        this.entryFeeInfo = entryFeeInfo;
        this.socialLikeCount = socialLikeCount;
        this.isParticipate = isParticipate;
    }

    public static SocialDetailsResponse of(Social social, boolean isParticipate) {
        return SocialDetailsResponse.builder()
                .id(social.getId())
                .categoryId(social.getCategory().getId())
                .question(social.getQuestion())
                .socialImageUrl(social.getImageUrl())
                .title(social.getTitle())
                .content(social.getContent())
                .meetingType(social.getMeetingType().getValue())
                .recruitmentType(social.getRecruitmentType().getValue())
                .address(social.getAddress())
                .startDate(social.getStartDate())
                .startTime(social.getStartTime())
                .limitHeadcount(social.getLimitHeadcount())
                .entryFee(social.getEntryFee())
                .entryFeeInfo(social.getEntryFeeInfo())
                .socialLikeCount(0)
                .isParticipate(isParticipate)
                .build();
    }
}
