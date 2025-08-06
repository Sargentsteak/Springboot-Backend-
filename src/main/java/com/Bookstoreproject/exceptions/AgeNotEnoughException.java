package com.Bookstoreproject.exceptions;

public class AgeNotEnoughException extends RuntimeException{
    public AgeNotEnoughException(String message){
        super(message);
    }
}
