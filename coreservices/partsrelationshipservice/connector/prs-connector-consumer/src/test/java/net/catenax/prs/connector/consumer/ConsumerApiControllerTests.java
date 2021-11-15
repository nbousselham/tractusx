package net.catenax.prs.connector.consumer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.javafaker.Faker;
import jakarta.ws.rs.core.Response;
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

import java.io.FileReader;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ConsumerApiControllerTests {

    @Spy
    Monitor monitor = new ConsoleMonitor();

    @Mock
    ConsumerService service;

    @InjectMocks
    ConsumerApiController controller;

    String processId = UUID.randomUUID().toString();

    Faker faker = new Faker();

    TransferProcessStates status = faker.options().option(TransferProcessStates.class);

    FileRequest fileRequest = FileRequest.builder()
            .connectorAddress(faker.internet().url())
            .destinationPath(faker.file().fileName())
            .build();

    TransferInitiateResponse transferResponse = TransferInitiateResponse.Builder.newInstance()
            .id(faker.lorem().characters())
            .status(faker.options().option(ResponseStatus.class)).build();

    @Captor
    ArgumentCaptor<DataRequest> dataRequestCaptor;

    @Test
    public void checkHealth_Returns() {
        assertThat(controller.checkHealth()).isEqualTo("I'm alive!");
    }


    @Test
    public void initiateTransfer_WhenFailure_ReturnsError() throws Exception {
        // Act
        var response = controller.initiateTransfer(fileRequest);
        // Assert
        assertThat(response.getStatus()).isEqualTo(500);
        assertThat(response.getEntity()).isNull();
    }

    @Test
    public void initiateTransfer_WhenSuccess_ReturnsTransferId() throws Exception {
        // Arrange
        when(service.initiateTransfer(fileRequest)).thenReturn(Optional.of(transferResponse));
        // Act
        var response = controller.initiateTransfer(fileRequest);
        // Assert
        assertThat(response.getStatus()).isEqualTo(200);
        assertThat(response.getEntity()).isEqualTo(transferResponse.getId());
    }

    @Test
    public void getStatus_WhenNotFound_ReturnsNotFound() {
        //Act
        var response = controller.getStatus(processId);
        //Assert
        assertThat(response.getStatus()).isEqualTo(Response.Status.NOT_FOUND.getStatusCode());
    }

    @Test
    public void getStatus_WhenSuccess_ReturnsStatus() {
        //Arrange
        when(service.getStatus(processId)).thenReturn(Optional.of(status));
        //Act
        var response = controller.getStatus(processId);
        //Assert
        assertThat(response.getEntity()).isEqualTo(status.name());
        assertThat(response.getStatus()).isEqualTo(Response.Status.OK.getStatusCode());
    }
}
