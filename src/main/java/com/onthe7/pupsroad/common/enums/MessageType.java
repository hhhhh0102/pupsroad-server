package com.onthe7.pupsroad.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum MessageType {

    SMS("SMS"),
    LMS("LMS"),
    MMS("MMS"),
    ALIMTALK("ALIMTALK");

    private String value;

}
