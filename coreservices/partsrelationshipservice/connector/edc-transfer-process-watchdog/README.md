# edc-transfer-process-watchdog

This extension starts a watchdog thread that cleans up long-running transfer processes. Processes in state `IN_PROGRESS` are tracked and moved to state `ERROR` after a timeout expires. This prevents the consumer connector to wait for the results of providers indefinitely, when the provider fails to provide a result.

## Configuration

The watchdog thread can be configured with the following properties:

* `edc.watchdog.period`: period in seconds (decimal values allowed) for polling for running transfer processes with default of 1s.
* `edc.watchdog.timeout`: timeout in seconds (decimal values allowed) after which processes are cancelled with default of 60s.