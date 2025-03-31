package com.fitnessStore.backend.ExceptionHandling;

public class IncorrectToken extends RuntimeException{
    public IncorrectToken(String message){
        super(message);
    }
}
