/*******************************************************************************
 * Copyright (c) 2019 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.service.device.management.job.manager;

import org.eclipse.kapua.KapuaEntityNotFoundException;
import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.commons.model.query.predicate.AndPredicateImpl;
import org.eclipse.kapua.commons.model.query.predicate.AttributePredicateImpl;
import org.eclipse.kapua.job.engine.JobEngineFactory;
import org.eclipse.kapua.job.engine.JobEngineService;
import org.eclipse.kapua.job.engine.JobStartOptions;
import org.eclipse.kapua.locator.KapuaLocator;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.KapuaService;
import org.eclipse.kapua.service.device.management.job.JobDeviceManagementOperation;
import org.eclipse.kapua.service.device.management.job.JobDeviceManagementOperationAttributes;
import org.eclipse.kapua.service.device.management.job.JobDeviceManagementOperationFactory;
import org.eclipse.kapua.service.device.management.job.JobDeviceManagementOperationListResult;
import org.eclipse.kapua.service.device.management.job.JobDeviceManagementOperationQuery;
import org.eclipse.kapua.service.device.management.job.JobDeviceManagementOperationService;
import org.eclipse.kapua.service.device.management.message.notification.OperationStatus;
import org.eclipse.kapua.service.device.management.registry.operation.DeviceManagementOperation;
import org.eclipse.kapua.service.device.management.registry.operation.DeviceManagementOperationAttributes;
import org.eclipse.kapua.service.device.management.registry.operation.DeviceManagementOperationFactory;
import org.eclipse.kapua.service.device.management.registry.operation.DeviceManagementOperationListResult;
import org.eclipse.kapua.service.device.management.registry.operation.DeviceManagementOperationProperty;
import org.eclipse.kapua.service.device.management.registry.operation.DeviceManagementOperationQuery;
import org.eclipse.kapua.service.device.management.registry.operation.DeviceManagementOperationRegistryService;
import org.eclipse.kapua.service.device.management.registry.operation.notification.ManagementOperationNotification;
import org.eclipse.kapua.service.job.targets.JobTarget;
import org.eclipse.kapua.service.job.targets.JobTargetAttributes;
import org.eclipse.kapua.service.job.targets.JobTargetFactory;
import org.eclipse.kapua.service.job.targets.JobTargetListResult;
import org.eclipse.kapua.service.job.targets.JobTargetQuery;
import org.eclipse.kapua.service.job.targets.JobTargetService;
import org.eclipse.kapua.service.job.targets.JobTargetStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;

/**
 * Default logic to process a {@link org.eclipse.kapua.service.device.management.registry.operation.notification.ManagementOperationNotification}
 * from a Device after a DEPLOY-V2 has started.
 *
 * @since 1.1.0
 */
public interface JobDeviceManagementOperationManagerService extends KapuaService {

    Logger LOG = LoggerFactory.getLogger(JobDeviceManagementOperationManagerService.class);

    KapuaLocator LOCATOR = KapuaLocator.getInstance();

    DeviceManagementOperationRegistryService DEVICE_MANAGEMENT_OPERATION_REGISTRY_SERVICE = LOCATOR.getService(DeviceManagementOperationRegistryService.class);
    DeviceManagementOperationFactory DEVICE_MANAGEMENT_OPERATION_FACTORY = LOCATOR.getFactory(DeviceManagementOperationFactory.class);

    JobDeviceManagementOperationService JOB_DEVICE_MANAGEMENT_OPERATION_SERVICE = LOCATOR.getService(JobDeviceManagementOperationService.class);
    JobDeviceManagementOperationFactory JOB_DEVICE_MANAGEMENT_OPERATION_FACTORY = LOCATOR.getFactory(JobDeviceManagementOperationFactory.class);

    JobEngineService JOB_ENGINE_SERVICE = LOCATOR.getService(JobEngineService.class);
    JobEngineFactory JOB_ENGINE_FACTORY = LOCATOR.getFactory(JobEngineFactory.class);

    JobTargetService JOB_TARGET_SERVICE = LOCATOR.getService(JobTargetService.class);
    JobTargetFactory JOB_TARGET_FACTORY = LOCATOR.getFactory(JobTargetFactory.class);

    /**
     * When a {@link ManagementOperationNotification} with {@link OperationStatus#COMPLETED} or {@link OperationStatus#FAILED} tries to look if this {@link DeviceManagementOperation}
     * is associated with a {@link org.eclipse.kapua.service.job.Job}.
     * <p>
     * If the associaton is found, the related {@link JobTarget#getStatus()} is updated with the result and eventually starts the {@link org.eclipse.kapua.service.job.Job} to continue
     * the processing of the {@link JobTarget}.
     *
     * @param scopeId     The scope {@link KapuaId} of the {@link ManagementOperationNotification}.
     * @param operationId The {@link ManagementOperationNotification#getOperationId()}.
     * @param updateOn    The {@link Date} that the {@link ManagementOperationNotification} arrived.
     * @param resource    The {@link ManagementOperationNotification#getResource()}.
     * @param status      The {@link ManagementOperationNotification#getStatus()}
     * @throws KapuaException If something goes bad.
     * @since 1.1.0
     */
    default void processJobTargetOnNotification(KapuaId scopeId, KapuaId operationId, Date updateOn, String resource, OperationStatus status) throws KapuaException {

        if (OperationStatus.RUNNING.equals(status)) {
            return;
        }

        DeviceManagementOperation deviceManagementOperation = getDeviceManagementOperation(scopeId, operationId);

        //
        // UGLY 'DEPLOY-V2'-related part
        //
        boolean isLastNotification = true;
        if (!OperationStatus.FAILED.equals(status)) {
            for (DeviceManagementOperationProperty ip : deviceManagementOperation.getInputProperties()) {
                if (ip.getName().equals("kapua.package.download.install")) {
                    if (resource.equals("download")) {
                        isLastNotification = !Boolean.parseBoolean(ip.getPropertyValue());
                    }
                    break;
                }
            }
        }

        if (!isLastNotification) {
            return;
        }

        //
        // Update the job target
        JobDeviceManagementOperation jobDeviceManagementOperation = getJobDeviceManagementOperation(scopeId, operationId);

        short attempts = 0;
        short limit = 3;
        boolean failed = false;
        JobTarget jobTarget = null;
        do {
            try {
                JobTargetQuery jobTargetQuery = JOB_TARGET_FACTORY.newQuery(scopeId);
                jobTargetQuery.setPredicate(
                        new AndPredicateImpl(
                                new AttributePredicateImpl<>(JobTargetAttributes.JOB_ID, jobDeviceManagementOperation.getJobId()),
                                new AttributePredicateImpl<>(JobTargetAttributes.JOB_TARGET_ID, deviceManagementOperation.getDeviceId())
                        )
                );

                JobTargetListResult jobTargets = JOB_TARGET_SERVICE.query(jobTargetQuery);

                jobTarget = jobTargets.getFirstItem();

                if (jobTarget == null) {
                    throw new IllegalStateException();
                }

                switch (status) {
                    case COMPLETED:
                        jobTarget.setStatus(JobTargetStatus.NOTIFIED_COMPLETION);
                        break;
                    case FAILED:
                        jobTarget.setStatus(JobTargetStatus.PROCESS_FAILED);
                        break;
                    case STALE:
                    default:
                        break;
                }

                JOB_TARGET_SERVICE.update(jobTarget);
            } catch (Exception e) {
                failed = true;
                attempts++;

                if (attempts >= limit || jobTarget == null) {
                    throw e;
                } else {
                    LOG.warn("Update JobTarget {} with status {}...  FAILED! Retrying...", jobTarget.getId(), jobTarget.getStatus());
                }
            }
        } while (failed);

        //
        // If PROCESS_FAILED no need to continue the JobTarget processing
        if (JobTargetStatus.PROCESS_FAILED.equals(jobTarget.getStatus())) {
            return;
        }

        //
        // Start the job
        JobStartOptions jobStartOptions = JOB_ENGINE_FACTORY.newJobStartOptions();
        jobStartOptions.addTargetIdToSublist(jobTarget.getId());
        jobStartOptions.setFromStepIndex(jobTarget.getStepIndex());
        jobStartOptions.setEnqueue(true);

        JOB_ENGINE_SERVICE.startJob(scopeId, jobDeviceManagementOperation.getJobId(), jobStartOptions);
    }

    /**
     * Gets the {@link JobDeviceManagementOperation} associated with the given {@link DeviceManagementOperation#getOperationId()}.
     *
     * @param scopeId     The scope {@link KapuaId} of the {@link JobDeviceManagementOperation}.
     * @param operationId The {@link DeviceManagementOperation#getOperationId()} to match.
     * @return The matched {@link JobDeviceManagementOperation}
     * @throws KapuaEntityNotFoundException if there is no {@link JobDeviceManagementOperation} with the given {@code operationId}.
     * @throws KapuaException               If something goes bad.
     * @since 1.1.0
     */
    default JobDeviceManagementOperation getJobDeviceManagementOperation(KapuaId scopeId, KapuaId operationId) throws KapuaException {
        JobDeviceManagementOperationQuery query = JOB_DEVICE_MANAGEMENT_OPERATION_FACTORY.newQuery(scopeId);
        query.setPredicate(new AttributePredicateImpl<>(JobDeviceManagementOperationAttributes.DEVICE_MANAGEMENT_OPERATION_ID, operationId));

        JobDeviceManagementOperationListResult operations = JOB_DEVICE_MANAGEMENT_OPERATION_SERVICE.query(query);
        JobDeviceManagementOperation deviceManagementOperation = operations.getFirstItem();

        if (deviceManagementOperation == null) {
            throw new KapuaEntityNotFoundException(JobDeviceManagementOperation.TYPE, operationId);
        }

        return deviceManagementOperation;
    }


    /**
     * Gets the {@link DeviceManagementOperation} that matches the given {@code operationId}.
     *
     * @param scopeId     The scope {@link KapuaId} of the {@link DeviceManagementOperation}.
     * @param operationId The {@link DeviceManagementOperation#getOperationId()} to match.
     * @return The matched {@link DeviceManagementOperation}.
     * @throws KapuaEntityNotFoundException if there is no {@link DeviceManagementOperation} with the given {@code operationId}.
     * @throws KapuaException               If something goes bad.
     * @since 1.1.0
     */
    default DeviceManagementOperation getDeviceManagementOperation(KapuaId scopeId, KapuaId operationId) throws KapuaException {
        DeviceManagementOperationQuery query = DEVICE_MANAGEMENT_OPERATION_FACTORY.newQuery(scopeId);
        query.setPredicate(new AttributePredicateImpl<>(DeviceManagementOperationAttributes.OPERATION_ID, operationId));

        DeviceManagementOperationListResult operations = DEVICE_MANAGEMENT_OPERATION_REGISTRY_SERVICE.query(query);
        DeviceManagementOperation deviceManagementOperation = operations.getFirstItem();

        if (deviceManagementOperation == null) {
            throw new KapuaEntityNotFoundException(DeviceManagementOperation.TYPE, operationId);
        }

        return deviceManagementOperation;
    }
}
