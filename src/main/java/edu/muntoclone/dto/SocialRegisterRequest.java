package edu.muntoclone.dto;

import edu.muntoclone.entity.Category;
import edu.muntoclone.entity.Member;
import edu.muntoclone.entity.Social;
import edu.muntoclone.type.MeetingType;
import edu.muntoclone.type.RecruitmentType;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SocialRegisterRequest {

    private Long categoryId;
    private String title;
    private String content;
    private MultipartFile imageFile;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate startDate;

    @DateTimeFormat(pattern = "HH:mm")
    private LocalTime startTime;
    private String meetingType;
    private String address;
    private String recruitmentType;

    private String question;
    private Integer limitHeadcount;
    private Integer entryFee;
    private String entryFeeInfo;

    @Builder
    public SocialRegisterRequest(Long categoryId, String title, String content,
                                 MultipartFile imageFile, LocalDate startDate, LocalTime startTime,
                                 String meetingType, String address, String recruitmentType, String question,
                                 Integer limitHeadcount, Integer entryFee, String entryFeeInfo
    ) {
        this.categoryId = categoryId;
        this.title = title;
        this.content = content;
        this.imageFile = imageFile;
        this.startDate = startDate;
        this.startTime = startTime;
        this.meetingType = meetingType;
        this.address = address;
        this.recruitmentType = recruitmentType;
        this.question = question;
        this.limitHeadcount = limitHeadcount;
        this.entryFee = entryFee;
        this.entryFeeInfo = entryFeeInfo;
    }

    public Social toEntity(Member owner, Category category, String imageUrl) {
        // TODO: 2022-06-18 ?????? ??? ??? DTO ?????? enum ?????? ?????? ??????????
        final MeetingType mType =
                MeetingType.ONLINE.getValue().equals(this.meetingType)
                        ? MeetingType.ONLINE : MeetingType.OFFLINE;

        final RecruitmentType rType =
                RecruitmentType.APPROVED.getValue().equals(this.recruitmentType)
                        ? RecruitmentType.APPROVED : RecruitmentType.EARLY_BIRD;

        return Social.builder()
                .owner(owner)
                .category(category)
                .title(this.title)
                .content(this.content)
                .imageUrl(imageUrl)
                .startDate(this.getStartDate())
                .startTime(this.getStartTime())
                .meetingType(mType)
                .address(this.address)
                .recruitmentType(rType)
                .question(question)
                .limitHeadcount(this.limitHeadcount)
                .entryFee(this.entryFee)
                .entryFeeInfo(this.entryFeeInfo)
                .build();
    }
}