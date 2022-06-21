package edu.muntoclone.exception;

public class SocialHeadcountLimitException extends RuntimeException {

    public SocialHeadcountLimitException() {
        super();
    }

    public SocialHeadcountLimitException(String message) {
        super(message);
    }
}
