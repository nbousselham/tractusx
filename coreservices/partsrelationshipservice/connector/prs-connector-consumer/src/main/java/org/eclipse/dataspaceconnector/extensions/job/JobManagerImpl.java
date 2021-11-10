package org.eclipse.dataspaceconnector.extensions.job;

import org.eclipse.dataspaceconnector.spi.monitor.Monitor;
import org.eclipse.dataspaceconnector.spi.transfer.store.TransferProcessStore;
import org.eclipse.dataspaceconnector.spi.types.domain.transfer.TransferProcess;
import org.eclipse.dataspaceconnector.spi.types.domain.transfer.TransferProcessStates;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.atomic.AtomicBoolean;

import static java.util.concurrent.TimeUnit.SECONDS;
import static java.util.stream.Collectors.toList;

public class JobManagerImpl implements JobManager {
    private final AtomicBoolean active = new AtomicBoolean();

    private final int batchSize = 5;
    private JobStore jobStore;
    private JobOrchestrator jobOrchestrator;
    private TransferProcessStore processStore;
    private Monitor monitor;
    private ScheduledExecutorService executor;

    private JobManagerImpl() {
    }

    @Override
    public void start(JobStore jobStore, TransferProcessStore processStore) {
        this.jobStore = jobStore;
        this.processStore = processStore;
        active.set(true);
        executor = Executors.newSingleThreadScheduledExecutor();
        executor.scheduleWithFixedDelay(this::run, 0, 5, SECONDS);
    }

    @Override
    public void stop() {
        active.set(false);
        if (executor != null) {
            executor.shutdownNow();
        }
    }

    private void run() {
        Collection<Job> jobsInProgress = jobStore.nextForState(JobState.IN_PROGRESS, batchSize);

        for (Job job : jobsInProgress) {
            List<TransferProcess> completedProcesses = job.getTransferProcessIds().stream()
                    .map(id -> processStore.find(id))
                    .filter(p -> p.getState() == TransferProcessStates.COMPLETED.code())
                    .collect(toList());

            // TODO: start transfers handling in parallel
            for (TransferProcess completedProcess : completedProcesses) {
                jobOrchestrator.handleTransferProcessCompleted(job, completedProcess);
            }

            // aggregate when all processes are completed
        }
        
    }

    public static class Builder {
        private final JobManagerImpl manager;

        private Builder() {
            manager = new JobManagerImpl();
        }

        public static JobManagerImpl.Builder newInstance() {
            return new JobManagerImpl.Builder();
        }

        public JobManagerImpl.Builder monitor(Monitor monitor) {
            manager.monitor = monitor;
            return this;
        }

        public JobManagerImpl.Builder jobOrchestrator(JobOrchestrator jobOrchestrator) {
            manager.jobOrchestrator = jobOrchestrator;
            return this;
        }

        public JobManager build() {
            Objects.requireNonNull(manager.monitor, "monitor");
            Objects.requireNonNull(manager.jobOrchestrator, "jobOrchestrator");
            return manager;
        }
    }
}
