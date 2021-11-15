//
// Copyright (c) 2021 Copyright Holder (Catena-X Consortium)
//
// See the AUTHORS file(s) distributed with this work for additional
// information regarding authorship.
//
// See the LICENSE file(s) distributed with this work for
// additional information regarding license terms.
//
package org.eclipse.dataspaceconnector.extensions.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.catenax.prs.client.ApiException;
import net.catenax.prs.client.api.PartsRelationshipServiceApi;
import net.catenax.prs.client.model.PartRelationshipsWithInfos;
import net.catenax.prs.requests.PartsTreeByObjectIdRequest;
import org.eclipse.dataspaceconnector.spi.monitor.Monitor;
import org.eclipse.dataspaceconnector.spi.transfer.flow.DataFlowController;
import org.eclipse.dataspaceconnector.spi.transfer.flow.DataFlowInitiateResponse;
import org.eclipse.dataspaceconnector.spi.transfer.response.ResponseStatus;
import org.eclipse.dataspaceconnector.spi.types.domain.transfer.DataRequest;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Handles a data flow to transfer a file.
 */
// Removed BeanMembersShouldSerialize rule because Monitor is final,
// so adding transient will not have any impact.
@SuppressWarnings({"PMD.CommentRequired", "PMD.GuardLogStatement", "PMD.BeanMembersShouldSerialize"})
public class FileTransferFlowController implements DataFlowController {

    private final Monitor monitor;

    private final PartsRelationshipServiceApi prsClient;

    private final ObjectMapper mapper;

    /**
     * @param monitor Logger
     */
    public FileTransferFlowController(final Monitor monitor, final PartsRelationshipServiceApi prsClient) {
        this.monitor = monitor;
        this.prsClient = prsClient;
        this.mapper = new ObjectMapper();
    }

    @Override
    public boolean canHandle(final DataRequest dataRequest) {
        return "file".equalsIgnoreCase(dataRequest.getDataDestination().getType());
    }

    @Override
    public @NotNull DataFlowInitiateResponse initiateFlow(final DataRequest dataRequest) {
        // verify partsTreeRequest
        final String serializedRequest = dataRequest.getDataDestination().getProperty("request");
        PartsTreeByObjectIdRequest request;
        monitor.info("Received request " + serializedRequest);
        try {
            request = mapper.readValue(serializedRequest, PartsTreeByObjectIdRequest.class);
            monitor.info("request with " + request.getObjectIDManufacturer());
        } catch (JsonProcessingException e) {
            final String message = "Error deserializing PartsTreeByObjectIdRequest" + e.getMessage();
            monitor.severe(message);
            return new DataFlowInitiateResponse(ResponseStatus.FATAL_ERROR, message);
        }

        byte[] partRelationshipsWithInfos = null;
        try {
            var response = prsClient.getPartsTreeByOneIdAndObjectId(request.getOneIDManufacturer(), request.getObjectIDManufacturer(),
                    request.getView(), request.getAspect(), request.getDepth());
            partRelationshipsWithInfos = (mapper.writeValueAsBytes(response));
        } catch (ApiException | JsonProcessingException e) {
            final String message = "Error when getting partRelationshipsWithInfos" + e.getMessage();
            monitor.severe(message);
            return new DataFlowInitiateResponse(ResponseStatus.FATAL_ERROR, message);
        }

        var destinationPath = Path.of(dataRequest.getDataDestination().getProperty("path"));
        try {
            Files.createFile(destinationPath);
        } catch (IOException e) {
            final String message = "Error creating file at" + destinationPath + e.getMessage();
            monitor.severe(message);
            return new DataFlowInitiateResponse(ResponseStatus.FATAL_ERROR, message);
        }

        try {
            Files.write(destinationPath, partRelationshipsWithInfos);
        } catch (IOException e) {
            final String message = "Error writing in file at" + destinationPath + e.getMessage();
            monitor.severe(message);
            return new DataFlowInitiateResponse(ResponseStatus.FATAL_ERROR, message);
        }

        return DataFlowInitiateResponse.OK;
    }

}
