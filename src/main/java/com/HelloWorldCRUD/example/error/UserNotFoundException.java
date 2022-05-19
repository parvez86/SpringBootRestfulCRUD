package com.HelloWorldCRUD.example.error;

import lombok.Data;
import org.springframework.stereotype.Component;


public class UserNotFoundException extends NullPointerException{
    public UserNotFoundException(String msg){
        super(msg);
    }

}
