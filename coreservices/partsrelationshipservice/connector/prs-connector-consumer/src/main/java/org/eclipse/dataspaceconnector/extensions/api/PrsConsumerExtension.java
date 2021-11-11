package org.eclipse.dataspaceconnector.extensions.api;

import org.eclipse.dataspaceconnector.extensions.job.InMemoryJobStore;
import org.eclipse.dataspaceconnector.extensions.job.JobOrchestrator;
import org.eclipse.dataspaceconnector.extensions.job.JobStore;
import org.eclipse.dataspaceconnector.extensions.transferprocess.TransferProcessFileHandler;
import org.eclipse.dataspaceconnector.spi.protocol.web.WebService;
import org.eclipse.dataspaceconnector.spi.system.ServiceExtension;
import org.eclipse.dataspaceconnector.spi.system.ServiceExtensionContext;
import org.eclipse.dataspaceconnector.spi.transfer.TransferProcessManager;
import org.eclipse.dataspaceconnector.spi.transfer.TransferProcessObservable;
import org.eclipse.dataspaceconnector.spi.transfer.store.TransferProcessStore;
import org.eclipse.dataspaceconnector.spi.types.domain.transfer.StatusCheckerRegistry;

import java.util.Set;

public class PrsConsumerExtension implements ServiceExtension {

    @Override
    public Set<String> requires() {
        return Set.of(
                "edc:webservice",
                "dataspaceconnector:transferprocessstore"
        );
    }

    @Override
    public void initialize(ServiceExtensionContext context) {
        initializeJobStore(context);
        initializeFileStatusChecker(context);
        initializeJobOrchestrator(context);
        initializeWebService(context);
    }

    private void initializeJobStore(ServiceExtensionContext context) {
        var jobStore = new InMemoryJobStore();
        context.registerService(JobStore.class, jobStore);
    }

    private void initializeFileStatusChecker(ServiceExtensionContext context) {
        var monitor = context.getMonitor();
        var transferProcessFileHandler = new TransferProcessFileHandler(monitor);
        var statusCheckerReg = context.getService(StatusCheckerRegistry.class);
        statusCheckerReg.register("File", transferProcessFileHandler);
    }

    private void initializeJobOrchestrator(ServiceExtensionContext context) {
        var monitor = context.getMonitor();
        var processManager = context.getService(TransferProcessManager.class);
        var jobStore = context.getService(JobStore.class);
        var transferProcessFileHandler = new TransferProcessFileHandler(monitor);
        var jobOrchestrator = new JobOrchestrator(processManager, jobStore, transferProcessFileHandler);
        TransferProcessObservable transferProcessObservable = context.getService(TransferProcessObservable.class);
        transferProcessObservable.registerListener(jobOrchestrator);
        context.registerService(JobOrchestrator.class, jobOrchestrator);
    }

    private void initializeWebService(ServiceExtensionContext context) {
        var webService = context.getService(WebService.class);
        var processStore = context.getService(TransferProcessStore.class);
        var jobOrchestrator = context.getService(JobOrchestrator.class);
        webService.registerController(new ConsumerApiController(context.getMonitor(), processStore, jobOrchestrator));
    }

}
