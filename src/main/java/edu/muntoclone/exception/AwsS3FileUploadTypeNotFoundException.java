package edu.muntoclone.exception;

public class AwsS3FileUploadTypeNotFoundException extends RuntimeException {

    public AwsS3FileUploadTypeNotFoundException() {
        super();
    }

    public AwsS3FileUploadTypeNotFoundException(String message) {
        super(message);
    }
}
