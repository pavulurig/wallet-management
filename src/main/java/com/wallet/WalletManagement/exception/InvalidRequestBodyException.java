package com.wallet.WalletManagement.exception;

public class InvalidRequestBodyException extends Exception {

    public InvalidRequestBodyException(String message) {
        super(message);
    }

    public InvalidRequestBodyException(String message,Throwable throwable) {
        super(message,throwable);
    }

}
