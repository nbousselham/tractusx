package org.eclipse.dataspaceconnector.transfer.core.transfer;

import com.github.javafaker.Faker;
import org.easymock.Capture;
import org.easymock.EasyMockExtension;
import org.easymock.Mock;
import org.eclipse.dataspaceconnector.spi.monitor.Monitor;
import org.eclipse.dataspaceconnector.spi.transfer.store.TransferProcessStore;
import org.eclipse.dataspaceconnector.spi.types.domain.transfer.TransferProcess;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static java.time.Instant.now;
import static org.assertj.core.api.Assertions.assertThat;
import static org.easymock.EasyMock.*;
import static org.eclipse.dataspaceconnector.spi.types.domain.transfer.TransferProcessStates.ERROR;
import static org.eclipse.dataspaceconnector.spi.types.domain.transfer.TransferProcessStates.IN_PROGRESS;

@ExtendWith(EasyMockExtension.class)
class CancelLongRunningProcessesTest {

    private static final Duration STATE_TIMEOUT_MS = Duration.of(20, ChronoUnit.SECONDS);
    private static final int BATCH_SIZE = 5;

    Faker faker = new Faker();

    @Mock
    Monitor monitor;
    @Mock
    TransferProcessStore transferProcessStore;

    Capture<TransferProcess> transferProcessCaptor = Capture.newInstance();

    Clock clock = Clock.fixed(Instant.now(), ZoneOffset.UTC);

    CancelLongRunningProcesses sut;

    @BeforeEach
    public void setup() {
        sut = CancelLongRunningProcesses.builder()
                .monitor(monitor)
                .transferProcessStore(transferProcessStore)
                .batchSize(BATCH_SIZE)
                .stateTimeout(STATE_TIMEOUT_MS)
                .clock(clock)
                .build();
    }

    @Test
    public void run_shouldCancelProcessInTimeout() {
        // Arrange
        var timeoutDate = Date.from(now(clock).minus(STATE_TIMEOUT_MS));
        var activeProcessNotInTimeout = TransferProcess.Builder.newInstance()
                .id(faker.lorem().characters())
                .stateTimestamp(faker.date().future(1, TimeUnit.SECONDS, timeoutDate).getTime())
                .build();
        var activeProcessInTimeout = TransferProcess.Builder.newInstance()
                .id(faker.lorem().characters())
                .stateTimestamp(faker.date().past(1, TimeUnit.SECONDS, timeoutDate).getTime())
                .build();

        expect(transferProcessStore.nextForState(IN_PROGRESS.code(), BATCH_SIZE)).andReturn(List.of(activeProcessNotInTimeout, activeProcessInTimeout));
        transferProcessStore.update(capture(transferProcessCaptor));
        expectLastCall();
        replay(transferProcessStore);

        // Act
        sut.run();

        // Assert
        verify(transferProcessStore);
        assertThat(transferProcessCaptor.getValue())
                .usingRecursiveComparison()
                .ignoringFields("stateCount", "stateTimestamp")
                .isEqualTo(
                    TransferProcess.Builder.newInstance()
                        .id(activeProcessInTimeout.getId())
                        .state(ERROR.code())
                        .errorDetail("Timed out waiting for process to complete after > " + STATE_TIMEOUT_MS.toMillis() + "ms")
                        .build()
                );
    }
}