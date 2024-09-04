package com.aungPyae22.demoFileHandling.exceptions;

public class PException extends RuntimeException{
    public PException(String message){
        super(message);
    }

    public PException(String message, Throwable cause) {
        super(message, cause);
    }

    public PException(Throwable cause) {
        super(cause);
    }
}
