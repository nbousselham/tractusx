//
// Copyright (c) 2021 Copyright Holder (Catena-X Consortium)
//
// See the AUTHORS file(s) distributed with this work for additional
// information regarding authorship.
//
// See the LICENSE file(s) distributed with this work for
// additional information regarding license terms.
//
package net.catenax.prs.connector.consumer.monitor;

import org.eclipse.dataspaceconnector.spi.monitor.Monitor;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Supplier;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Logging monitor using JUL
 */
public class LoggerMonitor implements Monitor {

    private static final Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

    @Override
    public void severe(Supplier<String> supplier, Throwable... errors) {
        logForEach(supplier, Level.SEVERE, errors);
    }

    @Override
    public void severe(String message, Throwable... errors) {
        logForEach(() -> message, Level.SEVERE, errors);
    }

    @Override
    public void severe(Map<String, Object> data) {
        data.forEach((key, value) -> LOGGER.log(Level.SEVERE, key, value));
    }

    @Override
    public void warning(Supplier<String> supplier, Throwable... errors) {
        logForEach(supplier, Level.WARNING, errors);
    }

    @Override
    public void warning(String message, Throwable... errors) {
        logForEach(() -> message, Level.WARNING, errors);
    }

    @Override
    public void info(Supplier<String> supplier, Throwable... errors) {
        logForEach(supplier, Level.INFO, errors);
    }

    @Override
    public void info(String message, Throwable... errors) {
        logForEach(() -> message, Level.INFO, errors);
    }

    @Override
    public void debug(Supplier<String> supplier, Throwable... errors) {
        logForEach(supplier, Level.FINE, errors);
    }

    @Override
    public void debug(String message, Throwable... errors) {
        logForEach(() -> message, Level.FINE, errors);
    }

    private void logForEach(Supplier<String> message, Level level, Throwable... errors) {
        Arrays.stream(errors).forEach(error -> LOGGER.log(level, message.get(), error));
    }
}
