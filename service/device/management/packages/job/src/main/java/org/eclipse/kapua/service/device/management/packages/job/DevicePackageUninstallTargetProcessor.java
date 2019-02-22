/*******************************************************************************
 * Copyright (c) 2017, 2019 Eurotech and/or its affiliates and others
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

import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.commons.model.id.IdGenerator;
import org.eclipse.kapua.commons.model.id.KapuaEid;
import org.eclipse.kapua.commons.security.KapuaSecurityUtils;
import org.eclipse.kapua.job.engine.commons.operation.AbstractTargetProcessor;
import org.eclipse.kapua.job.engine.commons.wrappers.JobTargetWrapper;
import org.eclipse.kapua.locator.KapuaLocator;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.device.management.job.JobDeviceManagementOperationCreator;
import org.eclipse.kapua.service.device.management.job.JobDeviceManagementOperationFactory;
import org.eclipse.kapua.service.device.management.job.JobDeviceManagementOperationService;
import org.eclipse.kapua.service.device.management.packages.DevicePackageFactory;
import org.eclipse.kapua.service.device.management.packages.DevicePackageManagementService;
import org.eclipse.kapua.service.device.management.packages.job.definition.DevicePackageUninstallPropertyKeys;
import org.eclipse.kapua.service.device.management.packages.model.uninstall.DevicePackageUninstallOptions;
import org.eclipse.kapua.service.device.management.packages.model.uninstall.DevicePackageUninstallRequest;
import org.eclipse.kapua.service.job.operation.TargetOperation;
import org.eclipse.kapua.service.job.targets.JobTarget;

import javax.batch.runtime.context.JobContext;
import javax.batch.runtime.context.StepContext;
import javax.inject.Inject;

/**
 * {@link TargetOperation} for {@link DevicePackageManagementService#uninstallExec(KapuaId, KapuaId, DevicePackageUninstallRequest, Long)}.
 *
 * @since 1.0.0
 */
public class DevicePackageUninstallTargetProcessor extends AbstractTargetProcessor implements TargetOperation {

    private static final KapuaLocator LOCATOR = KapuaLocator.getInstance();

    private static final DevicePackageManagementService PACKAGES_MANAGEMENT_SERVICE = LOCATOR.getService(DevicePackageManagementService.class);
    private static final DevicePackageFactory DEVICE_PACKAGE_FACTORY = LOCATOR.getFactory(DevicePackageFactory.class);

    private static final JobDeviceManagementOperationService JOB_DEVICE_MANAGEMENT_OPERATION_SERVICE = LOCATOR.getService(JobDeviceManagementOperationService.class);
    private static final JobDeviceManagementOperationFactory JOB_DEVICE_MANAGEMENT_OPERATION_FACTORY = LOCATOR.getFactory(JobDeviceManagementOperationFactory.class);

    @Inject
    JobContext jobContext;

    @Inject
    StepContext stepContext;

    @Override
    protected void initProcessing(JobTargetWrapper wrappedJobTarget) {
        setContext(jobContext, stepContext);
    }

    @Override
    public void processTarget(JobTarget jobTarget) throws KapuaException {

        KapuaId scopeId = jobTarget.getScopeId();
        KapuaId jobId = jobTarget.getJobId();
        KapuaId operationId = new KapuaEid(IdGenerator.generate());

        //
        // Save the jobId-deviceManementOperationId pair to track resuming
        JobDeviceManagementOperationCreator jobDeviceManagementOperationCreator = JOB_DEVICE_MANAGEMENT_OPERATION_FACTORY.newCreator(scopeId);
        jobDeviceManagementOperationCreator.setJobId(jobId);
        jobDeviceManagementOperationCreator.setDeviceManagementOperationId(operationId);

        KapuaSecurityUtils.doPrivileged(() -> JOB_DEVICE_MANAGEMENT_OPERATION_SERVICE.create(jobDeviceManagementOperationCreator));

        //
        // Extract parameters from context
        DevicePackageUninstallRequest packageUninstallRequest = stepContextWrapper.getStepProperty(DevicePackageUninstallPropertyKeys.PACKAGE_UNINSTALL_REQUEST, DevicePackageUninstallRequest.class);
        Long timeout = stepContextWrapper.getStepProperty(DevicePackageUninstallPropertyKeys.TIMEOUT, Long.class);

        //
        // Send the request
        DevicePackageUninstallOptions packageUninstallOptions = DEVICE_PACKAGE_FACTORY.newDevicePackageUninstallOptions();
        packageUninstallOptions.setTimeout(timeout);
        packageUninstallOptions.setForcedOperationId(operationId);

        KapuaSecurityUtils.doPrivileged(() -> PACKAGES_MANAGEMENT_SERVICE.uninstallExec(scopeId, jobTarget.getJobTargetId(), packageUninstallRequest, packageUninstallOptions));
    }
}
