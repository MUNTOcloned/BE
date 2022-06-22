package edu.muntoclone.exception.handle;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler
    public ErrorResponse handleHttpMessageNotReadableException(
            HttpMessageNotReadableException e, HttpServletRequest request) {

        log.error("handleHttpMessageNotReadableException", e);
        String path = request.getRequestURI();
        return ErrorResponse.of(path, ErrorCode.MISSING_REQUEST_BODY);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler
    public ErrorResponse handleHttpRequestMethodNotSupportedException(
            HttpRequestMethodNotSupportedException e, HttpServletRequest request) {

        log.error("handleHttpRequestMethodNotSupportedException", e);
        String path = request.getRequestURI();
        return ErrorResponse.of(path, ErrorCode.MISSING_REQUEST_BODY);
    }

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler
    public ErrorResponse handleAccessDeniedException(
            AccessDeniedException e, HttpServletRequest request) {

        log.error("handleAccessDeniedException", e);
        String path = request.getRequestURI();
        return ErrorResponse.of(path, ErrorCode.ACCESS_DENIED);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler
    public ErrorResponse handleIllegalArgumentException(
            IllegalArgumentException e, HttpServletRequest request) {

        log.error("handleIllegalArgumentException", e);
        String path = request.getRequestURI();

        return ErrorResponse.of(path, ErrorCode.INVALID_INPUT_VALUE);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler
    public ErrorResponse handleMethodArgumentNotValidException(
            MethodArgumentNotValidException e, HttpServletRequest request) {

        log.error("handleMethodArgumentNotValidException", e);
        String path = request.getRequestURI();
        return ErrorResponse.of(path, ErrorCode.INVALID_INPUT_VALUE, e.getBindingResult());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler
    public ErrorResponse handleHttpMediaTypeNotSupportedException(
            HttpMediaTypeNotSupportedException e, HttpServletRequest request) {

        log.error("handleHttpMediaTypeNotSupportedException", e);
        log.info("ContentType = {}", e.getContentType());
        String path = request.getRequestURI();
        return ErrorResponse.of(path, ErrorCode.MEDIA_TYPE_NOT_SUPPORTED);
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler
    public String handleException(Exception e, HttpServletRequest request) {
        log.error("handleException", e);
        return request.getRequestURI() + " -> " + e.getMessage();
        //return ErrorResponse.of(path, ErrorCode.INTERNAL_SERVER_ERROR);
    }

}
