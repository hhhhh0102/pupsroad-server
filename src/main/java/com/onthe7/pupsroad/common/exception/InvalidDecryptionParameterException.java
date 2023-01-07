package com.onthe7.pupsroad.common.exception;

public class InvalidDecryptionParameterException extends RuntimeException {

    public InvalidDecryptionParameterException() { super(); }

    public InvalidDecryptionParameterException(Object msg) { super(String.valueOf(msg)); }

}
