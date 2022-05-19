package com.HelloWorldCRUD.example.advice;


import com.HelloWorldCRUD.example.error.UserNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class UserAdviceController {
    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<String> handleUserNotFoundException(ClassNotFoundException exception){
        return new ResponseEntity<>("Data Not Found!", HttpStatus.BAD_REQUEST);
    }
}
