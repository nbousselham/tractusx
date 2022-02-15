package net.catenax.semantics.framework;

public class StatusRuntimeException extends RuntimeException  {

    public int getStatus() {
        return status;
    }

    private int status = 500;

    public StatusRuntimeException(String message, Throwable inner) {
        super(message, inner);
    }

    public StatusRuntimeException(String message) {
        super(message);
    }

    public StatusRuntimeException(String message, Throwable inner, int status) {
        super(message, inner);
        this.status = status;
    }

    public StatusRuntimeException(String message, int status) {
        super(message);
        this.status = status;
    }

}
