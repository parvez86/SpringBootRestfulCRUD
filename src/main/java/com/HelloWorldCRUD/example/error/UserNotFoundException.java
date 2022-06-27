package com.HelloWorldCRUD.example.error;


public class UserNotFoundException extends NullPointerException{
    public UserNotFoundException(String msg){
        super(msg);
    }
}
