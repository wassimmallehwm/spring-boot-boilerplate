package com.boilerplate.configuration.exceptions.handlers;

import com.boilerplate.configuration.exceptions.DataNotFoundException;
import com.boilerplate.configuration.exceptions.DuplicatedException;
import com.boilerplate.configuration.exceptions.InvalidPassword;
import org.apache.tomcat.util.http.fileupload.FileUploadException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.webjars.NotFoundException;

import java.io.IOException;
import java.util.Objects;

@ControllerAdvice
public class RestExceptionHandler {
    private final Logger logger = LoggerFactory.getLogger(RestExceptionHandler.class);
    @ExceptionHandler(value = {NotFoundException.class, DataNotFoundException.class})
    public ResponseEntity<RestErrorResponse> handleNotFoundException(Exception e){
        logger.error(e.getMessage());
        return new ResponseEntity<>(
                new RestErrorResponse(RestErrorCode.NOT_FOUND, e.getMessage()),
                HttpStatus.NOT_FOUND
        );
    }
    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException e) {
        logger.error(e.getMessage());
        /*List<String> errors = ex.getBindingResult().getFieldErrors()
                .stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.toList());*/
        boolean required = e.getBindingResult().getFieldErrors().stream()
                .anyMatch(fieldError -> Objects.equals(fieldError.getCode(), "NotBlank"));

        if(required){
            return new ResponseEntity<>(
                    new RestErrorResponse(RestErrorCode.FIELDS_REQUIRED, "errors.required"),
                    HttpStatus.BAD_REQUEST
            );
        }
        return new ResponseEntity<>(
                new RestErrorResponse(RestErrorCode.FIELDS_INVALID, "errors.invalid"),
                HttpStatus.BAD_REQUEST
        );
    }
    @ExceptionHandler(IOException.class)
    public ResponseEntity<?> handleIOException(IOException e){
        logger.error(e.getMessage());
        e.printStackTrace();
        return new ResponseEntity<>(
                new RestErrorResponse(RestErrorCode.SERVER_ERROR, "errors.server-error"),
                HttpStatus.INTERNAL_SERVER_ERROR
        );
    }
    @ExceptionHandler(DuplicatedException.class)
    public ResponseEntity<?> handleDuplicatedException(DuplicatedException e){
        logger.error(e.getMessage());
        e.printStackTrace();
        return new ResponseEntity<>(
                new RestErrorResponse(RestErrorCode.DUPLICATED, e.getMessage()),
                HttpStatus.BAD_REQUEST
        );
    }
    @ExceptionHandler(FileUploadException.class)
    public ResponseEntity<?> handleFileUploadException(FileUploadException e){
        logger.error(e.getMessage());
        e.printStackTrace();
        return new ResponseEntity<>(
                new RestErrorResponse(RestErrorCode.FILE_UPLOAD, e.getMessage()),
                HttpStatus.BAD_REQUEST
        );
    }
    @ExceptionHandler(InvalidPassword.class)
    public ResponseEntity<?> handleInvalidPassword(InvalidPassword e){
        logger.error(e.getMessage());
        e.printStackTrace();
        return new ResponseEntity<>(
                new RestErrorResponse(RestErrorCode.INVALID_PASSWORD, e.getMessage()),
                HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler(RuntimeException.class)
    public void handleRuntimeException(RuntimeException e){
        logger.error(e.getMessage());
        e.printStackTrace();
    }
    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleException(Exception e){
        logger.error(e.getMessage());
        e.printStackTrace();
        return new ResponseEntity<>(
                new RestErrorResponse(RestErrorCode.SERVER_ERROR, "errors.server-error"),
                HttpStatus.INTERNAL_SERVER_ERROR
        );
    }
}
