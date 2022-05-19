package com.HelloWorldCRUD.example.advice;

import com.HelloWorldCRUD.example.util.ApiResponse;
import com.HelloWorldCRUD.example.util.ApiResponseMessage;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.persistence.EntityNotFoundException;

@RestControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {
    @Autowired
    private ApiResponseMessage responseMessage;
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request){
        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setApiResponse(responseMessage.validation_error(ex.getObjectName()), null, com.HelloWorldCRUD.example.entity.HttpStatus.ERROR);
        apiResponse.setFieldError(ex.getBindingResult().getFieldError().getField()+": "+ex.getBindingResult().getFieldError().getDefaultMessage());
        return ResponseEntity.status(org.springframework.http.HttpStatus.BAD_REQUEST).body(apiResponse);
    }
    @ExceptionHandler({EntityNotFoundException.class, ConstraintViolationException.class})
    private ResponseEntity<ApiResponse> handleEntityNotFound(RuntimeException ex){
        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setApiResponse(responseMessage.entity_validation_error(""), null, com.HelloWorldCRUD.example.entity.HttpStatus.ERROR);
        apiResponse.setFieldError(ex.getMessage());
        return ResponseEntity.status(org.springframework.http.HttpStatus.BAD_REQUEST).body(apiResponse);
    }
}
