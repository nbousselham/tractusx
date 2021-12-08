package org.eclipse.dataspaceconnector.transfer.core.transfer;

import com.github.javafaker.Faker;
import org.eclipse.dataspaceconnector.spi.monitor.Monitor;
import org.eclipse.dataspaceconnector.spi.transfer.store.TransferProcessStore;
import org.eclipse.dataspaceconnector.spi.types.domain.transfer.TransferProcess;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static java.time.Instant.now;
import static org.assertj.core.api.Assertions.assertThat;
import static org.eclipse.dataspaceconnector.spi.types.domain.transfer.TransferProcessStates.ERROR;
import static org.eclipse.dataspaceconnector.spi.types.domain.transfer.TransferProcessStates.IN_PROGRESS;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CancelLongRunningProcessesTest {

    public static final long STATE_TIMEOUT_MS = 20000;
    public static final int BATCH_SIZE = 5;

    Faker faker = new Faker();

    @Mock
    Monitor monitor;
    @Mock
    TransferProcessStore transferProcessStore;
    @Captor
    ArgumentCaptor<TransferProcess> transferProcessCaptor;

    CancelLongRunningProcesses sut;

    @BeforeEach
    public void setup() {
        sut = CancelLongRunningProcesses.builder()
                .monitor(monitor)
                .transferProcessStore(transferProcessStore)
                .batchSize(BATCH_SIZE)
                .stateTimeoutInMs(STATE_TIMEOUT_MS)
                .build();
    }

    @Test
    public void run() {
        // Arrange
        var p1 = TransferProcess.Builder.newInstance()
                .id(faker.lorem().characters())
                .stateTimestamp(now().toEpochMilli())
                .build();
        var p2 = TransferProcess.Builder.newInstance()
                .id(faker.lorem().characters())
                .stateTimestamp(now().minusSeconds(STATE_TIMEOUT_MS + 1).toEpochMilli())
                .build();

        when(transferProcessStore.nextForState(IN_PROGRESS.code(), BATCH_SIZE)).thenReturn(List.of(p1, p2));
        doNothing().when(transferProcessStore).update(transferProcessCaptor.capture());

        // Act
        sut.run();

        // Assert
        verify(transferProcessStore, times(1)).update(p2);
        var transferProcess = transferProcessCaptor.getValue();
        assertThat(transferProcess).usingRecursiveComparison().ignoringFields("stateCount", "stateTimestamp").isEqualTo(
                TransferProcess.Builder.newInstance()
                    .id(p2.getId())
                    .state(ERROR.code())
                    .errorDetail("Timeout")
                    .build()
        );
    }
}