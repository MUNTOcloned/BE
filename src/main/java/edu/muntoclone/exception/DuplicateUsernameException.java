package edu.muntoclone.exception;

public class DuplicateUsernameException extends RuntimeException {

    public DuplicateUsernameException() {
        super();
    }

    public DuplicateUsernameException(String message) {
        super(message);
    }
}
