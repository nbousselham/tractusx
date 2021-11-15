package org.eclipse.dataspaceconnector.extensions.job;

import org.eclipse.dataspaceconnector.spi.EdcException;

import java.util.Arrays;

public enum JobState {
    UNSAVED(0),
    INITIAL(100),
    IN_PROGRESS(600),
    TRANSFERS_FINISHED(700),
    COMPLETED(800),
    ERROR(-1);

    private final int code;

    JobState(int code) {
        this.code = code;
    }

    public static JobState from(int code) {
        return Arrays.stream(values()).filter(tps -> tps.code == code).findFirst().orElseThrow(() -> new EdcException("Invalid job state code: " + code));
    }

    public int code() {
        return code;
    }
}
