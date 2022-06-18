package edu.muntoclone.aws;

import lombok.Getter;

@Getter
public enum AwsS3FileUploadType {

    PROFILE("profiles/"),
    SOCIAL("socials/");
    private final String path;
    AwsS3FileUploadType(String path) {
        this.path = path;
    }
}
