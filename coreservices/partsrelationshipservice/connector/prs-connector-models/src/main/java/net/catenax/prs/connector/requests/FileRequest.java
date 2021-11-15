//
// Copyright (c) 2021 Copyright Holder (Catena-X Consortium)
//
// See the AUTHORS file(s) distributed with this work for additional
// information regarding authorship.
//
// See the LICENSE file(s) distributed with this work for
// additional information regarding license terms.
//
package net.catenax.prs.connector.requests;

import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

/**
 * JSON payload for file transfer request.
 */
@Value
@Jacksonized
@Builder(toBuilder = true)
public class FileRequest {
    /**
     * Provider connector address to send the message to.
     */
    String connectorAddress;

    /**
     * Destination path where the file should be copied.
     */
    String destinationPath;

    /**
     * Parts Tree Request.
     */
    PartsTreeByObjectIdRequest partsTreeRequest;
}
