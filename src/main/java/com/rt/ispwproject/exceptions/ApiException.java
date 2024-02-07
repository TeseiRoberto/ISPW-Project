package com.rt.ispwproject.exceptions;

public class ApiException extends Exception {

    public ApiException(String apiName, String message)
    {
        super(apiName + " error: " + message);
    }
}
