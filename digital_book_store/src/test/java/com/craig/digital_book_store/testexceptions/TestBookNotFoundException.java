package com.craig.digital_book_store.testexceptions;

public class TestBookNotFoundException extends RuntimeException{

    public TestBookNotFoundException(String message){
        super(message);
    }
}