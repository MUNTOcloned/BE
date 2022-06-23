package edu.muntoclone.exception.handle;

import edu.muntoclone.controller.AuthenticationApiController;
import edu.muntoclone.exception.DuplicateUsernameException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@RestControllerAdvice(assignableTypes = {AuthenticationApiController.class})
public class AuthenticationExceptionHandler {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler
    public ErrorResponse handleDuplicateUsernameException(
            DuplicateUsernameException e, HttpServletRequest request) {

        log.error("handleDuplicateUsernameException", e);
        String path = request.getRequestURI();
        return ErrorResponse.of(path, ErrorCode.DUPLICATE_USERNAME);
    }

}