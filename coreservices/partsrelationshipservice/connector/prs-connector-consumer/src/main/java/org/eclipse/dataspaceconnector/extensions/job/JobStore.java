package org.eclipse.dataspaceconnector.extensions.job;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public interface JobStore {
    Job find(String id);

    void create(Job job);

    @NotNull List<Job> nextForState(JobState state, int max);

    void delete(String processId);
}
