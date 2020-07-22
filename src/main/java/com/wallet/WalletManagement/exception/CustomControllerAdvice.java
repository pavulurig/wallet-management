package com.wallet.WalletManagement.exception;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Date;

@ControllerAdvice
public class CustomControllerAdvice extends ResponseEntityExceptionHandler {


    @ExceptionHandler(value = {InvalidRequestBodyException.class})
    public ResponseEntity<Object> handleInvalidRequestBodyException(InvalidRequestBodyException e, WebRequest webRequest) {
        ErrorMessage errorMessage = new ErrorMessage();
        errorMessage.setDate(new Date());
        errorMessage.setMessage(e.getMessage());
        errorMessage.setResponseCode(HttpStatus.BAD_REQUEST.toString());
        return new ResponseEntity<>(errorMessage, new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = {Exception.class})
    public ResponseEntity<Object> handleExceptions(Exception e, WebRequest webRequest) {
        ErrorMessage errorMessage = new ErrorMessage();
        errorMessage.setDate(new Date());
        errorMessage.setMessage(e.getMessage());
        errorMessage.setResponseCode(HttpStatus.INTERNAL_SERVER_ERROR.toString());
        return new ResponseEntity<>(errorMessage, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(value = {UnProcessoableException.class})
    public ResponseEntity<Object> handleUnProcessableException(Exception e, WebRequest webRequest) {
        ErrorMessage errorMessage = new ErrorMessage();
        errorMessage.setDate(new Date());
        errorMessage.setMessage(e.getMessage());
        errorMessage.setResponseCode(HttpStatus.UNPROCESSABLE_ENTITY.toString());
        return new ResponseEntity<>(errorMessage, new HttpHeaders(), HttpStatus.UNPROCESSABLE_ENTITY);
    }
}
