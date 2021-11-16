package org.eclipse.dataspaceconnector.extensions.monitor;

import org.eclipse.dataspaceconnector.spi.monitor.Monitor;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Supplier;
import java.util.logging.Level;
import java.util.logging.Logger;

public class LoggerMonitor implements Monitor {

    private final static Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

    public LoggerMonitor() {
    }

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
        logForEach(()-> message, Level.FINE, errors);
    }

    private void logForEach(Supplier<String> message, Level level, Throwable... errors) {
        Arrays.stream(errors).forEach(error -> LOGGER.log(level, message.get(), error));
    }
}
