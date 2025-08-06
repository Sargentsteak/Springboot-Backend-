package com.Bookstoreproject.exceptions;

public class BookCreationFailed extends RuntimeException{
    public  BookCreationFailed(String message){
        super(message);
    }
}
