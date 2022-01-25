/*
Copyright (c) 2021-2022 T-Systems International GmbH (Catena-X Consortium)
See the AUTHORS file(s) distributed with this work for additional
information regarding authorship.

See the LICENSE file(s) distributed with this work for
additional information regarding license terms.
*/

package net.catenax.semantics.exceptions;

public class AdapterException extends RuntimeException {
    public int getStatus() {
        return status;
    }

    private int status = 500;

    public AdapterException(String message, Throwable inner) {
        super(message, inner);
    }

    public AdapterException(String message) {
        super(message);
    }

    public AdapterException(String message, Throwable inner, int status) {
        super(message, inner);
        this.status = status;
    }

    public AdapterException(String message, int status) {
        super(message);
        this.status = status;
    }

}