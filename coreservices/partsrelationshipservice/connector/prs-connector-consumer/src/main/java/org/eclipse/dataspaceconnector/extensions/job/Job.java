package org.eclipse.dataspaceconnector.extensions.job;

import java.time.Instant;
import java.util.*;

import static java.lang.String.format;

public class Job {

    private String id;
    private String filename;
    private String destinationPath;
    private JobState state;
    private long stateTimestamp;
    private Collection<String> transferProcessIds;
    private Collection<String> completedTransferProcessIds;

    private Job() {
    }

    public String getId() {
        return id;
    }

    public JobState getState() {
        return state;
    }

    public Collection<String> getTransferProcessIds() {
        return transferProcessIds;
    }

    public Collection<String> getCompletedTransferProcessIds() {
        return completedTransferProcessIds;
    }

    public long getStateTimestamp() {
        return stateTimestamp;
    }

    public String getFilename() {
        return filename;
    }

    public String getDestinationPath() {
        return destinationPath;
    }

    public void transitionInitial() {
        transition(JobState.INITIAL, JobState.UNSAVED);
    }

    public void transitionInProgress() {
        transition(JobState.IN_PROGRESS, JobState.INITIAL, JobState.IN_PROGRESS);
    }

    public void transitionTransfersFinished() {
        transition(JobState.TRANSFERS_FINISHED, JobState.IN_PROGRESS);
    }

    public void transitionComplete() {
        transition(JobState.COMPLETED, JobState.TRANSFERS_FINISHED);
    }

    public void addTransferProcess(String transferProcessId) {
        transferProcessIds.add(transferProcessId);
    }

    public void transferProcessCompleted(String id) {
        transferProcessIds.remove(id);
        completedTransferProcessIds.add(id);
    }

    public void updateStateTimestamp() {
        stateTimestamp = Instant.now().toEpochMilli();
    }

    private void transition(JobState end, JobState... starts) {
        if (Arrays.stream(starts).noneMatch(s -> s == state)) {
            throw new IllegalStateException(format("Cannot transition from state %s to %s", state, end));
        }
        state = end;
        updateStateTimestamp();
    }

    public static class Builder {
        private final Job job;

        private Builder() {
            job = new Job();
        }

        public static Job.Builder newInstance() {
            return new Job.Builder();
        }

        public Job.Builder id(String id) {
            job.id = id;
            return this;
        }

        public Job.Builder state(JobState state) {
            job.state = state;
            return this;
        }

        public Job.Builder filename(String filename) {
            job.filename = filename;
            return this;
        }

        public Job.Builder destinationPath(String destinationPath) {
            job.destinationPath = destinationPath;
            return this;
        }

        public Job.Builder stateTimestamp(long value) {
            job.stateTimestamp = value;
            return this;
        }

        public Job build() {
            Objects.requireNonNull(job.id, "id");
            if (job.state == JobState.UNSAVED && job.stateTimestamp == 0) {
                job.stateTimestamp = Instant.now().toEpochMilli();
            }
            job.transferProcessIds = new HashSet<>();
            job.completedTransferProcessIds = new ArrayList<>();
            return job;
        }
    }
}
