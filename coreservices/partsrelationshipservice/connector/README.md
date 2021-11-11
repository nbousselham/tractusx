## Consumer-provider orchestration spike

## Spike scenario

The goal of this spike was to clarify how a consumer connector can coordinate multiple calls to provider connectors to retrieve a distributed parts tree. The spike will focus on the following points: 

* A consumer can start a long-running background process to orchestrate multiple provider jobs.
* A consumer can issue requests to multiple providers.
* A consumer can poll the state of a provider job, wait for it to be finished, in order to access the result.
* A consumer can wait for the result of one provider, and use the result data to start further provider jobs.

# Running the spike code

Use the `run-integration-test.sh` script to run the spike. This script starts 3 provider connectors that serve files and a consumer connector that starts a query against the first provider. At the end of the execution you should see the content of all test document files.

# Provider file structure

Several test document files can be found within the `ci` directory. These files are served by the provider connectors and have the following structure:

```json
{
  "value": "<some value>",
  "transferProcesses": [
    {
      "file": "<file to retrieve>",
      "connectorUrl": "<provider connector url>"
    }
  ]
}
```

The `value` field represents some information provided in this file. The presence of `transferProcesses` indicates further partial files need to be retrieved from other connectors.

# Orchestrating logic

The `JobOrchestrator` class takes care of coordinating requests to multiple providers to retrieve all required partial files.

1- First a request is sent to `provider1` to retrieve `test-document-1`.
1- The `JobOrchestrator` registers itself as a `TransferProcessListener` in the `PrsConsumerExtension`, and thus its `complete` method gets called when the first request is finished.
1- The `JobOrchestrator` parses the result and detects that `test-document-1` indicates that further partial files`test-document-2` and `test-document-3` need to be retrieved from `provider2` and `provider3` respectively.
1- Requests are sent to `provider2` and `provider3`.
1- The `complete` callback is triggered again when results are ready and the process of parsing the result and making further calls continues...
1- ...until no further calls are required.

Have a look in [Confluence](https://confluence.catena-x.net/display/ARTI/MTPDC+EDC+Orchestration) for more details on the orchestration logic.