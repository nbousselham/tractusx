# edc-transfer-process-watchdog

This extension starts a watchdog thread that cleans up long-running transfer processes. Processes in state `IN_PROGRESS` are tracked and moved to state `ERROR` after a timeout expires. This prevents the consumer connector to poll for the results of providers indefinitely, when the provider fails to provide one.

## Configuration

The watchdog thread can be configured with the following properties:

* `edc.watchdog.period`: period in seconds (decimal values allowed) for polling for running transfer processes with default of 1s.
* `edc.watchdog.timeout`: timeout in seconds (decimal values allowed) after which processes are cancelled with default of 60s.

## Known issues

Watchdog process status updates to ERROR might cause a race condition with the updates performed from the main loop in `TransferProcessManagerImpl`. At the moment EDC offers no capabilities of preventing this in any way. Issue [#330](https://github.com/eclipse-dataspaceconnector/DataSpaceConnector/issues/330) focuses on reworking `TransferProcessManagerImpl` to prevent such situations.

The consequences of this race condition are mild though. If a process happens to finish at the same time as its timeout the resulting state of the process (COMPLETED vs ERROR) will be determined by the thread that manages to update the process the last.