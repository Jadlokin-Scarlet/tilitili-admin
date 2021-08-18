package com.tilitili.admin.interceptor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.AbstractRequestLoggingFilter;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@Component
public class RequestLoggingFilter extends AbstractRequestLoggingFilter {

    public RequestLoggingFilter() {
        super();
        setIncludeQueryString(true);
        setIncludePayload(true);
        // truncate payloads
        setMaxPayloadLength(1000);
        setIncludeHeaders(false);
        setAfterMessagePrefix("Request received: ");
    }

    @Override
    protected void beforeRequest(HttpServletRequest httpServletRequest, String s) {
        log.info(s);
    }

    @Override
    protected void afterRequest(HttpServletRequest httpServletRequest, String s) {
//        Log.info(s);
    }
}
