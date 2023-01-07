package com.onthe7.pupsroad.common.exception;

public class InvalidEncryptionParameterException extends RuntimeException {

    public InvalidEncryptionParameterException() { super(); }

    public InvalidEncryptionParameterException(Object msg) { super(String.valueOf(msg)); }

}
