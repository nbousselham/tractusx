package org.eclipse.dataspaceconnector.transfer.core.transfer;

import com.github.javafaker.Faker;
import org.eclipse.dataspaceconnector.junit.launcher.EdcExtension;
import org.eclipse.dataspaceconnector.spi.transfer.TransferProcessListener;
import org.eclipse.dataspaceconnector.spi.transfer.TransferProcessObservable;
import org.eclipse.dataspaceconnector.spi.transfer.store.TransferProcessStore;
import org.eclipse.dataspaceconnector.spi.types.domain.metadata.DataEntry;
import org.eclipse.dataspaceconnector.spi.types.domain.transfer.DataRequest;
import org.eclipse.dataspaceconnector.spi.types.domain.transfer.StatusChecker;
import org.eclipse.dataspaceconnector.spi.types.domain.transfer.StatusCheckerRegistry;
import org.eclipse.dataspaceconnector.spi.types.domain.transfer.TransferProcess;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.Properties;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static java.util.Collections.emptyList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.easymock.EasyMock.*;
import static org.eclipse.dataspaceconnector.spi.types.domain.transfer.TransferProcess.Type.CONSUMER;
import static org.eclipse.dataspaceconnector.spi.types.domain.transfer.TransferProcessStates.ERROR;
import static org.eclipse.dataspaceconnector.spi.types.domain.transfer.TransferProcessStates.IN_PROGRESS;

@ExtendWith(EdcExtension.class)
public class TransferProcessWatchdogIntegrationTest {

    Faker faker = new Faker();

    @BeforeAll
    public static void setProperties() {
        Properties props = System.getProperties();
        props.setProperty("edc.watchdog.timeout", "1");
    }

    @Test
    public void cancelLongRunningProcess(TransferProcessStore transferProcessStore, TransferProcessObservable transferProcessObservable, StatusCheckerRegistry statusCheckerRegistry) throws InterruptedException {
        // Arrange
        DataRequest request = DataRequest.Builder.newInstance().protocol("ids-rest")
                .dataEntry(DataEntry.Builder.newInstance().id(faker.lorem().characters()).build())
                .connectorId(faker.lorem().characters())
                .connectorAddress(faker.internet().url())
                .destinationType(faker.lorem().word())
                .managedResources(false)
                .build();

        StatusChecker statusChecker = mock(StatusChecker.class);
        expect(statusChecker.isComplete(anyObject(TransferProcess.class), eq(emptyList()))).andReturn(false);
        statusCheckerRegistry.register(request.getDestinationType(), statusChecker);

        var process = TransferProcess.Builder.newInstance()
                .id(faker.lorem().characters())
                .dataRequest(request)
                .type(CONSUMER)
                .state(IN_PROGRESS.code())
                .build();

        CountDownLatch latch = new CountDownLatch(1);
        transferProcessObservable.registerListener(new TestListener(latch));

        // Act
        transferProcessStore.update(process);
        latch.await(1, TimeUnit.MINUTES);

        // Assert
        assertThat(transferProcessStore.find(process.getId())).usingRecursiveComparison()
                .ignoringFields("stateTimestamp", "stateCount")
                .isEqualTo(TransferProcess.Builder.newInstance()
                        .id(process.getId())
                        .dataRequest(request)
                        .type(CONSUMER)
                        .state(ERROR.code())
                        .errorDetail("Cancelled")
                        .build());
    }

    static class TestListener implements TransferProcessListener {

        private final CountDownLatch latch;

        public TestListener(CountDownLatch latch) {
            this.latch = latch;
        }

        @Override
        public void error(TransferProcess process) {
            latch.countDown();
        }
    }

}
