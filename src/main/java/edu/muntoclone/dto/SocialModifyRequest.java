package edu.muntoclone.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class SocialModifyRequest {

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
    private Integer limitHeadcount;
    private Integer entryFee;
    private String entryFeeInfo;

}
