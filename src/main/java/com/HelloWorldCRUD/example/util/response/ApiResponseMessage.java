package com.HelloWorldCRUD.example.util.response;

import org.springframework.stereotype.Component;

@Component
public class ApiResponseMessage {

    private final String HTTP_200_FOUND = "successfully found";
    private final String HTTP_200_CREATED = "successfully created";
    private final String HTTP_200_UPDATED = "successfully updated";
    private final String HTTP_200_DELETED = "successfully deleted";
    private final String HTTP_200_DEACTIVATION = "successfully deactivated";

    private final String HTTP_400_NOT_FOUND = "not found";
    private final String HTTP_400_NOT_CREATED = "not created";
    private final String HTTP_400_NOT_UPDATED = "not updated";
    private final String HTTP_400_NOT_DELETED = "not deleted";
    private final String HTTP_400_DEACTIVATION= "not deactivated";

    private final String HTTP_400_PATH_VARIABLE_ERROR = "path variable error";
    private final String HTTP_400_FIELD_ERROR = "field error";
    private final String HTTP_400_METHOD_ERROR = "method error";
    private final String HTTP_400_VALIDATION_ERROR = "validation error";
    private final String HTTP_400_ENTITY_VALIDATION_ERROR = "entity validation error";
    private final String HTTP_400_MESSAGE_UNREADABLE_ERROR = "invalid request body";
    private final String HTTP_400_TYPE_MISMATCH_ERROR= "parameter mismatch error";
    private final String HTTP_400_MISSING_SERVLET_PARAMETERS= "parameter missing error";


    public String successfully_found(String class_name){
        return class_name+" "+HTTP_200_FOUND+".";
    }

    public String successfully_created(String class_name){
        return class_name+" "+HTTP_200_CREATED+".";
    }

    public String successfully_updated(String class_name){
        return class_name+" "+HTTP_200_UPDATED+".";
    }

    public String successfully_deleted(String class_name){
        return class_name+" "+HTTP_200_DELETED+".";
    }


    public String not_found(String class_name){
        return class_name+" "+HTTP_400_NOT_FOUND+".";
    }

    public String not_created(String class_name){
        return class_name+" "+HTTP_400_NOT_CREATED+".";
    }

    public String not_updated(String class_name){
        return class_name+" "+HTTP_400_NOT_UPDATED+".";
    }

    public String not_deleted(String class_name){
        return class_name+" "+HTTP_400_NOT_DELETED+".";
    }

    public String path_variable_error(String path){
        return path+" "+HTTP_400_PATH_VARIABLE_ERROR+" occurred.";
    }

    public String field_error(String field_name){
        return field_name+" "+HTTP_400_FIELD_ERROR+" occurred.";
    }

    public String method_error(String method_name){
        return method_name+" "+HTTP_400_METHOD_ERROR+" occurred.";
    }
    public String validation_error(String class_name){
        return class_name+" "+HTTP_400_VALIDATION_ERROR+" occurred.";
    }
    public String entity_validation_error(String class_name){ return class_name+" "+HTTP_400_ENTITY_VALIDATION_ERROR+" occurred.";}
    public String parameters_type_mismatch_error(String class_name){ return class_name+" "+HTTP_400_TYPE_MISMATCH_ERROR+" occurred.";}
    public String missing_servlet_parameters_error(String class_name){ return class_name+" "+HTTP_400_MISSING_SERVLET_PARAMETERS+" occurred.";}
    public String deactivated(String class_name){
        return class_name+" "+HTTP_200_DEACTIVATION+".";
    }
    public String not_deactivated(String class_name){
        return class_name+" "+HTTP_400_DEACTIVATION+".";
    }
    public String message_unreadable_error(String class_name){
        return class_name+" "+HTTP_400_MESSAGE_UNREADABLE_ERROR+".";
    }
}
