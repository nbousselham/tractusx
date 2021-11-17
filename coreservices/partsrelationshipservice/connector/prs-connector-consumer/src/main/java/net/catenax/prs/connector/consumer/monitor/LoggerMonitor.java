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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Supplier;

/**
 * Logging monitor using java.util.logging
 */
public class LoggerMonitor implements Monitor {

    private static final Logger LOGGER = LoggerFactory.getLogger(LoggerMonitor.class);

    @Override
    public void severe(Supplier<String> supplier, Throwable... errors) {
        severe(supplier.get(), errors);
    }

    @Override
    public void severe(String message, Throwable... errors) {
        if (errors.length != 0) {
            Arrays.stream(errors).forEach(error -> LOGGER.error(message, error));
        } else {
            LOGGER.error(message);
        }
    }

    @Override
    public void severe(Map<String, Object> data) {
        data.forEach(LOGGER::error);
    }

    @Override
    public void warning(Supplier<String> supplier, Throwable... errors) {
        warning(supplier.get(), errors);
    }

    @Override
    public void warning(String message, Throwable... errors) {
        if (errors.length != 0) {
            Arrays.stream(errors).forEach(error -> LOGGER.warn(message, error));
        } else {
            LOGGER.warn(message);
        }
    }

    @Override
    public void info(Supplier<String> supplier, Throwable... errors) {
        info(supplier.get(), errors);
    }

    @Override
    public void info(String message, Throwable... errors) {
        if (errors.length != 0) {
            Arrays.stream(errors).forEach(error -> LOGGER.info(message, error));
        } else {
            LOGGER.info(message);
        }
    }

    @Override
    public void debug(Supplier<String> supplier, Throwable... errors) {
        debug(supplier.get(), errors);
    }

    @Override
    public void debug(String message, Throwable... errors) {
        if (errors.length != 0) {
            Arrays.stream(errors).forEach(error -> LOGGER.debug(message, error));
        } else {
            LOGGER.debug(message);
        }
    }
}
