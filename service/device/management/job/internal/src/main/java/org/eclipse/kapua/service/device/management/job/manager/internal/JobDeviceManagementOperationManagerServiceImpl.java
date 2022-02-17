/*******************************************************************************
 * Copyright (c) 2019, 2022 Eurotech and/or its affiliates and others
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.service.device.management.job.manager.internal;

import org.eclipse.kapua.KapuaEntityNotFoundException;
import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.job.engine.JobEngineFactory;
import org.eclipse.kapua.job.engine.JobEngineService;
import org.eclipse.kapua.job.engine.JobStartOptions;
import org.eclipse.kapua.locator.KapuaLocator;
import org.eclipse.kapua.locator.KapuaProvider;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.device.management.job.JobDeviceManagementOperation;
import org.eclipse.kapua.service.device.management.job.JobDeviceManagementOperationAttributes;
import org.eclipse.kapua.service.device.management.job.JobDeviceManagementOperationFactory;
import org.eclipse.kapua.service.device.management.job.JobDeviceManagementOperationListResult;
import org.eclipse.kapua.service.device.management.job.JobDeviceManagementOperationQuery;
import org.eclipse.kapua.service.device.management.job.JobDeviceManagementOperationService;
import org.eclipse.kapua.service.device.management.job.manager.JobDeviceManagementOperationManagerService;
import org.eclipse.kapua.service.device.management.message.notification.NotifyStatus;
import org.eclipse.kapua.service.device.management.registry.operation.DeviceManagementOperation;
import org.eclipse.kapua.service.device.management.registry.operation.DeviceManagementOperationFactory;
import org.eclipse.kapua.service.device.management.registry.operation.DeviceManagementOperationProperty;
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
 * {@link JobDeviceManagementOperationManagerService} implementation.
 *
 * @since 1.1.0
 */
@KapuaProvider
public class JobDeviceManagementOperationManagerServiceImpl implements JobDeviceManagementOperationManagerService {

    private static final Logger LOG = LoggerFactory.getLogger(JobDeviceManagementOperationManagerService.class);

    private static final KapuaLocator LOCATOR = KapuaLocator.getInstance();

    private static final DeviceManagementOperationRegistryService DEVICE_MANAGEMENT_OPERATION_REGISTRY_SERVICE = LOCATOR.getService(DeviceManagementOperationRegistryService.class);
    private static final DeviceManagementOperationFactory DEVICE_MANAGEMENT_OPERATION_FACTORY = LOCATOR.getFactory(DeviceManagementOperationFactory.class);

    private static final JobDeviceManagementOperationService JOB_DEVICE_MANAGEMENT_OPERATION_SERVICE = LOCATOR.getService(JobDeviceManagementOperationService.class);
    private static final JobDeviceManagementOperationFactory JOB_DEVICE_MANAGEMENT_OPERATION_FACTORY = LOCATOR.getFactory(JobDeviceManagementOperationFactory.class);

    private static final JobEngineService JOB_ENGINE_SERVICE = LOCATOR.getService(JobEngineService.class);
    private static final JobEngineFactory JOB_ENGINE_FACTORY = LOCATOR.getFactory(JobEngineFactory.class);

    private static final JobTargetService JOB_TARGET_SERVICE = LOCATOR.getService(JobTargetService.class);
    private static final JobTargetFactory JOB_TARGET_FACTORY = LOCATOR.getFactory(JobTargetFactory.class);

    @Override
    public void processJobTargetOnNotification(KapuaId scopeId, KapuaId operationId, Date updateOn, String resource, NotifyStatus status) throws KapuaException {
        if (NotifyStatus.RUNNING.equals(status)) {
            return;
        }

        DeviceManagementOperation deviceManagementOperation = getDeviceManagementOperation(scopeId, operationId);

        //
        // UGLY 'DEPLOY-V2'-related part
        if (checkLastNotification(deviceManagementOperation, status, resource)) {
            return;
        }

        //
        // Update the job target
        JobDeviceManagementOperation jobDeviceManagementOperation;
        try {
            jobDeviceManagementOperation = getJobDeviceManagementOperation(scopeId, operationId);
        } catch (KapuaEntityNotFoundException kenfe) {
            LOG.warn("The operationId {} does not match any Job. Likely this is run interactively using a DeviceManagementService ", operationId);
            return;
        }

        JobTargetQuery jobTargetQuery = JOB_TARGET_FACTORY.newQuery(scopeId);
        jobTargetQuery.setPredicate(
                jobTargetQuery.andPredicate(
                        jobTargetQuery.attributePredicate(JobTargetAttributes.JOB_ID, jobDeviceManagementOperation.getJobId()),
                        jobTargetQuery.attributePredicate(JobTargetAttributes.JOB_TARGET_ID, deviceManagementOperation.getDeviceId())
                )
        );

        short attempts = 0;
        short limit = 3;
        boolean failed;
        JobTarget jobTarget = null;
        do {
            try {
                JobTargetListResult jobTargets = JOB_TARGET_SERVICE.query(jobTargetQuery);
                jobTarget = jobTargets.getFirstItem();

                if (jobTarget == null) {
                    LOG.warn("JobTarget with targetId {} for Job {} not found! This is something strange that happened and needs some checking! Reference JobDeviceManagementOperation: {}", deviceManagementOperation.getDeviceId(), jobDeviceManagementOperation.getJobId(), jobDeviceManagementOperation.getId());
                    return;
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
                failed = false;
            } catch (Exception e) {
                failed = true;
                attempts++;

                if (jobTarget == null) {
                    throw e;
                }

                if (attempts >= limit) {
                    LOG.warn("Update JobTarget {} with status {}...  FAILED! ({}/{}) Throwing error: {}...", jobTarget.getId(), jobTarget.getStatus(), attempts, limit, e.getMessage());
                    throw e;
                } else {
                    LOG.warn("Update JobTarget {} with status {}...  FAILED! ({}/{}) Retrying...", jobTarget.getId(), jobTarget.getStatus(), attempts, limit);
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
     * This fixes the double {@link NotifyStatus#COMPLETED} {@link ManagementOperationNotification} set from Kura
     * when performing a Device package download with the 'install' flag is set to {@code true}.
     * <p>
     * If this is not the last {@link ManagementOperationNotification} the processing must stop.
     *
     * @param deviceManagementOperation The current {@link DeviceManagementOperation} which the {@link ManagementOperationNotification} refers to.
     * @param status                    The {@link ManagementOperationNotification} {@link NotifyStatus}.
     * @param resource                  The {@link ManagementOperationNotification} resource.
     * @return {@code true} if this is the last {@link ManagementOperationNotification} for the {@link DeviceManagementOperation}, {@code false} otherwise.
     * @since 1.1.0
     */
    private boolean checkLastNotification(DeviceManagementOperation deviceManagementOperation, NotifyStatus status, String resource) {
        boolean isLastNotification = true;
        if (!NotifyStatus.FAILED.equals(status)) {
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
            return true;
        }
        return false;
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
    private JobDeviceManagementOperation getJobDeviceManagementOperation(KapuaId scopeId, KapuaId operationId) throws KapuaException {

        DeviceManagementOperation deviceManagementOperation = getDeviceManagementOperation(scopeId, operationId);

        JobDeviceManagementOperationQuery query = JOB_DEVICE_MANAGEMENT_OPERATION_FACTORY.newQuery(scopeId);
        query.setPredicate(query.attributePredicate(JobDeviceManagementOperationAttributes.DEVICE_MANAGEMENT_OPERATION_ID, deviceManagementOperation.getId()));

        JobDeviceManagementOperationListResult operations = JOB_DEVICE_MANAGEMENT_OPERATION_SERVICE.query(query);
        JobDeviceManagementOperation jobDeviceManagementOperation = operations.getFirstItem();

        if (jobDeviceManagementOperation == null) {
            throw new KapuaEntityNotFoundException(JobDeviceManagementOperation.TYPE, operationId);
        }

        return jobDeviceManagementOperation;
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
    private DeviceManagementOperation getDeviceManagementOperation(KapuaId scopeId, KapuaId operationId) throws KapuaException {
        DeviceManagementOperation deviceManagementOperation = DEVICE_MANAGEMENT_OPERATION_REGISTRY_SERVICE.findByOperationId(scopeId, operationId);

        if (deviceManagementOperation == null) {
            throw new KapuaEntityNotFoundException(DeviceManagementOperation.TYPE, operationId);
        }

        return deviceManagementOperation;
    }
}
