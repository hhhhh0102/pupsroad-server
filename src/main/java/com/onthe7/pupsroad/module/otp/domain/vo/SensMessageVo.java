package com.onthe7.pupsroad.module.otp.domain.vo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class SensMessageVo {

    private String statusCode;
    private String statusName;
    private String requestId;
    private String requestTime;

}
