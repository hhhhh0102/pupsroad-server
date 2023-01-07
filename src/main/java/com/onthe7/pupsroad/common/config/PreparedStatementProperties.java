package com.onthe7.pupsroad.common.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Getter
@Component
public class PreparedStatementProperties {

    @Value("${PS_GET_WALK_ID_BY_LOCATION_INFO}")
    private String getWalkIdByLocationInfo;
}
