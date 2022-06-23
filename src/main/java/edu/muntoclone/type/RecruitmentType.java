package edu.muntoclone.type;

import lombok.Getter;

@Getter
public enum RecruitmentType {

    APPROVED("approved"),
    EARLY_BIRD("early bird");

    private final String value;

    RecruitmentType(String value) {
        this.value = value;
    }
}