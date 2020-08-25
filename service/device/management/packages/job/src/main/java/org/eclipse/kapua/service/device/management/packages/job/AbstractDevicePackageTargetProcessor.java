/*******************************************************************************
 * Copyright (c) 2019, 2020 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.service.device.management.packages.job;

import org.eclipse.kapua.KapuaEntityNotFoundException;
import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.commons.security.KapuaSecurityUtils;
import org.eclipse.kapua.job.engine.JobEngineFactory;
import org.eclipse.kapua.job.engine.JobEngineService;
import org.eclipse.kapua.job.engine.JobStartOptions;
import org.eclipse.kapua.job.engine.commons.operation.AbstractTargetProcessor;
import org.eclipse.kapua.locator.KapuaLocator;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.device.management.job.JobDeviceManagementOperation;
import org.eclipse.kapua.service.device.management.job.JobDeviceManagementOperationCreator;
import org.eclipse.kapua.service.device.management.job.JobDeviceManagementOperationFactory;
import org.eclipse.kapua.service.device.management.job.JobDeviceManagementOperationService;
import org.eclipse.kapua.service.device.management.message.notification.OperationStatus;
import org.eclipse.kapua.service.device.management.packages.DevicePackageManagementService;
import org.eclipse.kapua.service.device.management.registry.operation.DeviceManagementOperation;
import org.eclipse.kapua.service.device.management.registry.operation.DeviceManagementOperationFactory;
import org.eclipse.kapua.service.device.management.registry.operation.DeviceManagementOperationRegistryService;
import org.eclipse.kapua.service.job.operation.TargetProcessor;
import org.eclipse.kapua.service.job.targets.JobTarget;
import org.eclipse.kapua.service.job.targets.JobTargetStatus;

/**
 * {@link AbstractDevicePackageTargetProcessor} for {@link DevicePackageManagementService} operations.
 *
 * @since 1.1.0
 */
public abstract class AbstractDevicePackageTargetProcessor extends AbstractTargetProcessor implements TargetProcessor {

    private static final KapuaLocator LOCATOR = KapuaLocator.getInstance();

    private static final DeviceManagementOperationRegistryService DEVICE_MANAGEMENT_OPERATION_REGISTRY_SERVICE = LOCATOR.getService(DeviceManagementOperationRegistryService.class);
    private static final DeviceManagementOperationFactory DEVICE_MANAGEMENT_OPERATION_FACTORY = LOCATOR.getFactory(DeviceManagementOperationFactory.class);

    private static final JobDeviceManagementOperationService JOB_DEVICE_MANAGEMENT_OPERATION_SERVICE = LOCATOR.getService(JobDeviceManagementOperationService.class);
    private static final JobDeviceManagementOperationFactory JOB_DEVICE_MANAGEMENT_OPERATION_FACTORY = LOCATOR.getFactory(JobDeviceManagementOperationFactory.class);

    private static final JobEngineService JOB_ENGINE_SERVICE = LOCATOR.getService(JobEngineService.class);
    private static final JobEngineFactory JOB_ENGINE_FACTORY = LOCATOR.getFactory(JobEngineFactory.class);

    protected void createJobDeviceManagementOperation(KapuaId scopeId, KapuaId jobId, JobTarget jobTarget, KapuaId operationId) throws KapuaException {
        //
        // Save the jobId-deviceManagementOperationId pair to track resuming
        JobDeviceManagementOperationCreator jobDeviceManagementOperationCreator = JOB_DEVICE_MANAGEMENT_OPERATION_FACTORY.newCreator(scopeId);
        jobDeviceManagementOperationCreator.setJobId(jobId);
        jobDeviceManagementOperationCreator.setDeviceManagementOperationId(operationId);

        JobDeviceManagementOperation jobDeviceManagementOperation = KapuaSecurityUtils.doPrivileged(() -> JOB_DEVICE_MANAGEMENT_OPERATION_SERVICE.create(jobDeviceManagementOperationCreator));

        //
        // Check if the operation has already COMPLETED/FAILED
        DeviceManagementOperation deviceManagementOperation = KapuaSecurityUtils.doPrivileged(() -> DEVICE_MANAGEMENT_OPERATION_REGISTRY_SERVICE.find(scopeId, operationId));

        if (deviceManagementOperation == null) {
            throw new KapuaEntityNotFoundException(DeviceManagementOperation.TYPE, operationId);
        }

        if (deviceManagementOperation.getEndedOn() != null && deviceManagementOperation.getEndedOn().before(jobDeviceManagementOperation.getCreatedOn())) {
            JobTargetStatus jobTargetStatus;
            if (OperationStatus.COMPLETED.equals(deviceManagementOperation.getStatus())) {
                jobTargetStatus = JobTargetStatus.NOTIFIED_COMPLETION;
            } else if (OperationStatus.FAILED.equals(deviceManagementOperation.getStatus())) {
                jobTargetStatus = JobTargetStatus.PROCESS_FAILED;
            } else {
                return;
            }

            jobTarget.setStatus(jobTargetStatus);

            //
            // If PROCESS_FAILED no need to continue the JobTarget processing
            if (JobTargetStatus.PROCESS_FAILED.equals(jobTarget.getStatus())) {
                return;
            }

            //
            // Enqueue the job
            JobStartOptions jobStartOptions = JOB_ENGINE_FACTORY.newJobStartOptions();
            jobStartOptions.addTargetIdToSublist(jobTarget.getId());
            jobStartOptions.setFromStepIndex(jobTarget.getStepIndex());
            jobStartOptions.setEnqueue(true);

            KapuaSecurityUtils.doPrivileged(() -> JOB_ENGINE_SERVICE.startJob(scopeId, jobDeviceManagementOperation.getJobId(), jobStartOptions));
        }
    }

}
