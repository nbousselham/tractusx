package org.eclipse.dataspaceconnector.extensions.job;

import org.eclipse.dataspaceconnector.spi.EdcException;
import org.eclipse.dataspaceconnector.spi.types.domain.transfer.TransferProcess;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.function.Supplier;

public class InMemoryJobStore implements JobStore {

    private static final int TIMEOUT = 1000;
    private final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
    private final Map<String, Job> jobsById = new HashMap<>();

    @Override
    public Job find(String id) {
        return readLock(() -> jobsById.get(id));
    }

    @Override
    public void create(Job job) {
        writeLock(() -> {
            job.transitionInitial();
            jobsById.put(job.getId(), job);
            return null;
        });
    }

    private <T> T readLock(Supplier<T> work) {
        try {
            if (!lock.readLock().tryLock(TIMEOUT, TimeUnit.MILLISECONDS)) {
                throw new EdcException("Timeout acquiring read lock");
            }
            try {
                return work.get();
            } finally {
                lock.readLock().unlock();
            }
        } catch (InterruptedException e) {
            Thread.interrupted();
            throw new EdcException(e);
        }
    }

    private <T> T writeLock(Supplier<T> work) {
        try {
            if (!lock.writeLock().tryLock(TIMEOUT, TimeUnit.MILLISECONDS)) {
                throw new EdcException("Timeout acquiring write lock");
            }
            try {
                return work.get();
            } finally {
                lock.writeLock().unlock();
            }
        } catch (InterruptedException e) {
            Thread.interrupted();
            throw new EdcException(e);
        }
    }
}
