package edu.muntoclone.type;

import lombok.Getter;

@Getter
public enum MeetingType {

    ONLINE("online"),
    OFFLINE("offline");

    private final String value;

    MeetingType(String value) {
        this.value = value;
    }
}
