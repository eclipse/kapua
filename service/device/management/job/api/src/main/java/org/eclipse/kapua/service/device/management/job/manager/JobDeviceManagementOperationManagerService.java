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
import org.eclipse.kapua.service.job.targets.JobTarget;
import org.eclipse.kapua.service.job.targets.JobTargetAttributes;
import org.eclipse.kapua.service.job.targets.JobTargetFactory;
import org.eclipse.kapua.service.job.targets.JobTargetListResult;
import org.eclipse.kapua.service.job.targets.JobTargetQuery;
import org.eclipse.kapua.service.job.targets.JobTargetService;
import org.eclipse.kapua.service.job.targets.JobTargetStatus;

import java.util.Date;

public interface JobDeviceManagementOperationManagerService extends KapuaService {

    KapuaLocator LOCATOR = KapuaLocator.getInstance();

    DeviceManagementOperationRegistryService DEVICE_MANAGEMENT_OPERATION_REGISTRY_SERVICE = LOCATOR.getService(DeviceManagementOperationRegistryService.class);
    DeviceManagementOperationFactory DEVICE_MANAGEMENT_OPERATION_FACTORY = LOCATOR.getFactory(DeviceManagementOperationFactory.class);

    JobDeviceManagementOperationService JOB_DEVICE_MANAGEMENT_OPERATION_SERVICE = LOCATOR.getService(JobDeviceManagementOperationService.class);
    JobDeviceManagementOperationFactory JOB_DEVICE_MANAGEMENT_OPERATION_FACTORY = LOCATOR.getFactory(JobDeviceManagementOperationFactory.class);

    JobEngineService JOB_ENGINE_SERVICE = LOCATOR.getService(JobEngineService.class);
    JobEngineFactory JOB_ENGINE_FACTORY = LOCATOR.getFactory(JobEngineFactory.class);

    JobTargetService JOB_TARGET_SERVICE = LOCATOR.getService(JobTargetService.class);
    JobTargetFactory JOB_TARGET_FACTORY = LOCATOR.getFactory(JobTargetFactory.class);

    default void processJobTargetOnNotification(KapuaId scopeId, KapuaId operationId, Date updateOn, String resource, OperationStatus status) throws KapuaException {

        if (OperationStatus.RUNNING.equals(status)) {
            return;
        }

        DeviceManagementOperation deviceManagementOperation = getDeviceManagementOperation(scopeId, operationId);

        //
        // UGLY 'DEPLOY-V2'-related part
        //
        boolean isLastNotification = true;
        for (DeviceManagementOperationProperty ip : deviceManagementOperation.getInputProperties()) {
            if (ip.getName().equals("kapua.package.download.install")) {
                if (resource.equals("download")) {
                    isLastNotification = !Boolean.parseBoolean(ip.getPropertyValue());
                }
                break;
            }
        }

        if (!isLastNotification) {
            return;
        }

        //
        // Update the job target
        JobDeviceManagementOperation jobDeviceManagementOperation = getJobDeviceManagementOperation(scopeId, operationId);

        JobTargetQuery jobTargetQuery = JOB_TARGET_FACTORY.newQuery(scopeId);
        jobTargetQuery.setPredicate(
                new AndPredicateImpl(
                        new AttributePredicateImpl<>(JobTargetAttributes.JOB_ID, jobDeviceManagementOperation.getJobId()),
                        new AttributePredicateImpl<>(JobTargetAttributes.JOB_TARGET_ID, deviceManagementOperation.getDeviceId())
                )
        );

        JobTargetListResult jobTargets = JOB_TARGET_SERVICE.query(jobTargetQuery);

        JobTarget jobTarget = jobTargets.getFirstItem();

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

        if (JobTargetStatus.PROCESS_FAILED.equals(jobTarget.getStatus())) {
            return;
        }

        //
        // Look for the job device management operation entity

        JobStartOptions jobStartOptions = JOB_ENGINE_FACTORY.newJobStartOptions();
        jobStartOptions.addTargetIdToSublist(jobTarget.getId());
        jobStartOptions.setFromStepIndex(jobTarget.getStepIndex());
        jobStartOptions.setEnqueue(true);

        //
        // Start the job
        JOB_ENGINE_SERVICE.startJob(scopeId, jobDeviceManagementOperation.getJobId(), jobStartOptions);
    }

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
