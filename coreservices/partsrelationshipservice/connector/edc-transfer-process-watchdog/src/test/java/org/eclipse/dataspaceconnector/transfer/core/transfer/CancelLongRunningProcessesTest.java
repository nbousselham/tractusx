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

import java.time.Duration;
import java.util.List;

import static java.time.Instant.now;
import static java.time.temporal.ChronoUnit.SECONDS;
import static org.assertj.core.api.Assertions.assertThat;
import static org.easymock.EasyMock.*;
import static org.eclipse.dataspaceconnector.spi.types.domain.transfer.TransferProcessStates.ERROR;
import static org.eclipse.dataspaceconnector.spi.types.domain.transfer.TransferProcessStates.IN_PROGRESS;

@ExtendWith(EasyMockExtension.class)
class CancelLongRunningProcessesTest {

    public static final Duration STATE_TIMEOUT_MS = Duration.of(20, SECONDS);
    public static final int BATCH_SIZE = 5;

    Faker faker = new Faker();

    @Mock
    Monitor monitor;
    @Mock
    TransferProcessStore transferProcessStore;

    Capture<TransferProcess> transferProcessCaptor = Capture.newInstance();

    CancelLongRunningProcesses sut;

    @BeforeEach
    public void setup() {
        sut = CancelLongRunningProcesses.builder()
                .monitor(monitor)
                .transferProcessStore(transferProcessStore)
                .batchSize(BATCH_SIZE)
                .stateTimeout(STATE_TIMEOUT_MS)
                .build();
    }

    @Test
    public void run_shouldCancelProcessInTimeout() {
        // Arrange
        var activeProcessNotInTimeout = TransferProcess.Builder.newInstance()
                .id(faker.lorem().characters())
                .stateTimestamp(now().toEpochMilli())
                .build();
        var activeProcessInTimeout = TransferProcess.Builder.newInstance()
                .id(faker.lorem().characters())
                .stateTimestamp(now().minus(STATE_TIMEOUT_MS).toEpochMilli())
                .build();

        expect(transferProcessStore.nextForState(IN_PROGRESS.code(), BATCH_SIZE)).andReturn(List.of(activeProcessNotInTimeout, activeProcessInTimeout));
        transferProcessStore.update(capture(transferProcessCaptor));
        replay(transferProcessStore);

        // Act
        sut.run();

        // Assert
        var transferProcess = transferProcessCaptor.getValue();
        assertThat(transferProcess)
                .usingRecursiveComparison()
                .ignoringFields("stateCount", "stateTimestamp")
                .isEqualTo(
                    TransferProcess.Builder.newInstance()
                        .id(activeProcessInTimeout.getId())
                        .state(ERROR.code())
                        .errorDetail("Timed out waiting for process to complete after > 20s")
                        .build()
                );
    }
}