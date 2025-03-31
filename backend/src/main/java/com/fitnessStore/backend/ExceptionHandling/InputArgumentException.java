package com.fitnessStore.backend.ExceptionHandling;

public class InputArgumentException extends RuntimeException{
    public InputArgumentException(String message){
        super(message);
    }
}
