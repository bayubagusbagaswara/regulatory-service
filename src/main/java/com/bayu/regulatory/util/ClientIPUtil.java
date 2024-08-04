package com.bayu.regulatory.util;

import jakarta.servlet.http.HttpServletRequest;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

@UtilityClass
@Slf4j
public class ClientIPUtil {

    public String getClientIP(HttpServletRequest servletRequest) {
        String clientIP = servletRequest.getHeader("X-Forwarded-For");
        if (clientIP == null || clientIP.isEmpty()) {
            clientIP = servletRequest.getHeader("X-Real-IP");
        }
        if (clientIP == null || clientIP.isEmpty()) {
            clientIP = servletRequest.getRemoteAddr();
        }
        log.info("Client IP Address: {}", clientIP);
        return clientIP;
    }

}
