package com.onthe7.pupsroad.common.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class ResultVo {

    private Boolean result;

    public static ResultVo success() {
        return new ResultVo(Boolean.TRUE);
    }
}
