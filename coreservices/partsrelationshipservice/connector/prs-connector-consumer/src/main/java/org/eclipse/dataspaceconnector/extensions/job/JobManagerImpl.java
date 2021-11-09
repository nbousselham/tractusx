package org.eclipse.dataspaceconnector.extensions.job;

import org.eclipse.dataspaceconnector.spi.monitor.Monitor;
import org.eclipse.dataspaceconnector.spi.transfer.store.TransferProcessStore;
import org.eclipse.dataspaceconnector.transfer.core.transfer.TransferProcessManagerImpl;

import java.util.Collection;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.atomic.AtomicBoolean;

import static java.util.concurrent.TimeUnit.SECONDS;

public class JobManagerImpl implements JobManager {
    private final AtomicBoolean active = new AtomicBoolean();

    private int batchSize = 5;
    private JobStore jobStore;
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
        executor.scheduleWithFixedDelay(this::run, 10, 5, SECONDS);
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
            Set<String> transferProcessIds = job.getTransferProcessIds();

            monitor.info("JobId=" + job.getId() + "  transferProcesses=" + transferProcessIds.toString());
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

        public JobManager build() {
            Objects.requireNonNull(manager.monitor, "monitor");
            return manager;
        }
    }
}
