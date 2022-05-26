package com.HelloWorldCRUD.example.util;

import com.HelloWorldCRUD.example.entity.HttpStatus;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Data
@Component
@NoArgsConstructor
public class ApiResponse {
    private String message;
    private Object object;
    private HttpStatus status;

    @Setter
    private String fieldError;
    @Setter
    private String methodError;
    @Setter
    private String pathError;
    public void setApiResponse(String sms, Object obj, HttpStatus httpStatus){
        message = sms;
        object = obj;
        status = httpStatus;
    }
}
