package com.example.login_auth_api.infra.adapters;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.example.login_auth_api.domain.ports.LogPort;

@Component
public class LogAdapter implements LogPort {

    private static final Logger logger = LoggerFactory.getLogger(LogAdapter.class);

    @Override
    public void trace(String msg) {
        logger.trace("\"" + msg.strip() + "\"");
    }

    @Override
    public void debug(String msg) {
        logger.debug("\"" + msg.strip() + "\"");
    }

    @Override
    public void info(String msg) {
        logger.info("\"" + msg.strip() + "\"");
    }

    @Override
    public void warn(String msg) {
        logger.warn("\"" + msg.strip() + "\"");
    }

    @Override
    public void error(String msg) {
        logger.error("\"" + msg.strip() + "\"");
    }
    
}