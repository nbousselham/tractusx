package net.catenax.prs.connector.consumer.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.javafaker.Faker;
import net.catenax.prs.connector.requests.FileRequest;
import net.catenax.prs.connector.requests.PartsTreeByObjectIdRequest;
import org.eclipse.dataspaceconnector.monitor.ConsoleMonitor;
import org.eclipse.dataspaceconnector.spi.monitor.Monitor;
import org.eclipse.dataspaceconnector.spi.transfer.TransferInitiateResponse;
import org.eclipse.dataspaceconnector.spi.transfer.TransferProcessManager;
import org.eclipse.dataspaceconnector.spi.transfer.response.ResponseStatus;
import org.eclipse.dataspaceconnector.spi.transfer.store.TransferProcessStore;
import org.eclipse.dataspaceconnector.spi.types.domain.metadata.DataEntry;
import org.eclipse.dataspaceconnector.spi.types.domain.transfer.DataAddress;
import org.eclipse.dataspaceconnector.spi.types.domain.transfer.DataRequest;
import org.eclipse.dataspaceconnector.spi.types.domain.transfer.TransferProcess;
import org.eclipse.dataspaceconnector.spi.types.domain.transfer.TransferProcessStates;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ConsumerServiceTests {

    @Spy
    Monitor monitor = new ConsoleMonitor();

    @Mock
    TransferProcessStore processStore;

    @Mock
    TransferProcessManager transferProcessManager;

    @InjectMocks
    ConsumerService service;

    String processId = UUID.randomUUID().toString();

    Faker faker = new Faker();

    @Captor
    ArgumentCaptor<DataRequest> dataRequestCaptor;

    @Test
    public void getStatus_WhenProcessNotInStore_ReturnsEmpty() {
        // Act
        var response = service.getStatus(processId);
        // Assert
        assertThat(response.isEmpty());
    }

    @Test
    public void getStatus_WhenProcessInStore_ReturnsState() {
        // Arrange
        TransferProcess transferProcess = mock(TransferProcess.class);
        when(transferProcess.getState()).thenReturn(TransferProcessStates.PROVISIONING.code());
        when(processStore.find(processId)).thenReturn(transferProcess);
        // Act
        var response = service.getStatus(processId);
        // Assert
        assertThat(response).contains(TransferProcessStates.PROVISIONING);
    }

    @Test
    public void initiateTransfer_WhenFileRequestValid_ReturnsProcessId() throws JsonProcessingException {
        // Arrange
        FileRequest fileRequest = FileRequest.builder()
                .connectorAddress(faker.internet().url())
                .destinationPath(faker.file().fileName())
                .partsTreeRequest(PartsTreeByObjectIdRequest.builder()
                        .oneIDManufacturer(faker.company().name())
                        .objectIDManufacturer(faker.lorem().characters(10, 20))
                        .view("AS_BUILT")
                        .depth(faker.number().numberBetween(1, 5))
                        .build())
                .build();

        var dataRequest = DataRequest.Builder.newInstance()
                .id(UUID.randomUUID().toString())
                .connectorAddress(fileRequest.getConnectorAddress())
                .protocol("ids-rest")
                .connectorId("consumer")
                .dataEntry(DataEntry.Builder.newInstance()
                        .id("prs-request")
                        .policyId("use-eu")
                        .build())
                .dataDestination(DataAddress.Builder.newInstance()
                        .type("File")
                        .property("path", fileRequest.getDestinationPath())
                        .build())
                .managedResources(false)
                .build();

        when(transferProcessManager.initiateConsumerRequest(any(DataRequest.class)))
                .thenReturn(TransferInitiateResponse.Builder.newInstance().id(UUID.randomUUID().toString()).status(ResponseStatus.OK).build());
        ObjectMapper mapper = new ObjectMapper();

        // Act
        var response = service.initiateTransfer(fileRequest);
        // Assert
        assertThat(response).isPresent();
        // Verify that initiateConsumerRequest got called with correct DataRequest input.
        verify(transferProcessManager).initiateConsumerRequest(dataRequestCaptor.capture());
        assertThat(dataRequestCaptor.getValue()).usingRecursiveComparison()
                .ignoringFields("id")
                .ignoringFieldsMatchingRegexes("dataDestination.properties") // Ignore properties as it contains json.
                .isEqualTo(dataRequest);
        assertThat(dataRequestCaptor.getValue().getId()).isNotBlank();
        var serializedRequest = dataRequestCaptor.getValue().getDataDestination().getProperties().get("request");
        assertThat(mapper.readValue(serializedRequest, PartsTreeByObjectIdRequest.class)).isEqualTo(fileRequest.getPartsTreeRequest());
        var destinationFile = dataRequestCaptor.getValue().getDataDestination().getProperties().get("path");
        assertThat(destinationFile).isEqualTo(fileRequest.getDestinationPath());
    }
}
