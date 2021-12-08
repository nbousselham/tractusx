# edc-transfer-process-watchdog

This extension starts a watchdog thread that cleans up long-running transfer processes. Processes in state `IN_PROGRESS` are tracked and moved to state `ERROR` after a timeout expires. This prevents the consumer connector to wait for the results of providers indefinitely, when the provider fails to provide a result.

## Configuration

The watchdog thread can be configured with the following properties:

* `edc.watchdog.delay`: delay in milliseconds for polling for running transfer processes (default 1s).
* `edc.watchdog.timeout`: timeout in milliseconds after which processes are cancelled (default 60s).