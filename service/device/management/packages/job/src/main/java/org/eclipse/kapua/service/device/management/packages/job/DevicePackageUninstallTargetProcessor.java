/*******************************************************************************
 * Copyright (c) 2017, 2022 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.service.device.management.packages.job;

import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.commons.security.KapuaSecurityUtils;
import org.eclipse.kapua.job.engine.commons.wrappers.JobTargetWrapper;
import org.eclipse.kapua.locator.KapuaLocator;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.device.management.packages.DevicePackageFactory;
import org.eclipse.kapua.service.device.management.packages.DevicePackageManagementService;
import org.eclipse.kapua.service.device.management.packages.job.definition.DevicePackageUninstallPropertyKeys;
import org.eclipse.kapua.service.device.management.packages.model.uninstall.DevicePackageUninstallOptions;
import org.eclipse.kapua.service.device.management.packages.model.uninstall.DevicePackageUninstallRequest;
import org.eclipse.kapua.service.job.operation.TargetProcessor;
import org.eclipse.kapua.service.job.targets.JobTarget;
import org.eclipse.kapua.service.job.targets.JobTargetStatus;

import javax.batch.runtime.context.JobContext;
import javax.batch.runtime.context.StepContext;
import javax.inject.Inject;

/**
 * {@link TargetProcessor} for {@link DevicePackageManagementService#uninstallExec(KapuaId, KapuaId, DevicePackageUninstallRequest, Long)}.
 *
 * @since 1.0.0
 */
public class DevicePackageUninstallTargetProcessor extends AbstractDevicePackageTargetProcessor implements TargetProcessor {
    private static final KapuaLocator LOCATOR = KapuaLocator.getInstance();
    private static final DevicePackageManagementService PACKAGES_MANAGEMENT_SERVICE = LOCATOR.getService(DevicePackageManagementService.class);
    private static final DevicePackageFactory DEVICE_PACKAGE_FACTORY = LOCATOR.getFactory(DevicePackageFactory.class);

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

        //
        // Extract parameters from context
        DevicePackageUninstallRequest packageUninstallRequest = stepContextWrapper.getStepProperty(DevicePackageUninstallPropertyKeys.PACKAGE_UNINSTALL_REQUEST, DevicePackageUninstallRequest.class);
        Long timeout = stepContextWrapper.getStepProperty(DevicePackageUninstallPropertyKeys.TIMEOUT, Long.class);

        //
        // Send the request
        DevicePackageUninstallOptions packageUninstallOptions = DEVICE_PACKAGE_FACTORY.newPackageUninstallOptions();
        packageUninstallOptions.setTimeout(timeout);

        KapuaId operationId = KapuaSecurityUtils.doPrivileged(() -> PACKAGES_MANAGEMENT_SERVICE.uninstallExec(scopeId, jobTarget.getJobTargetId(), packageUninstallRequest, packageUninstallOptions));

        //
        // Save the jobId-deviceManagementOperationId pair to track resuming
        createJobDeviceManagementOperation(scopeId, jobId, jobTarget, operationId);
    }

    @Override
    protected JobTargetStatus getCompletedStatus(JobTarget jobTarget) {

        if (JobTargetStatus.PROCESS_AWAITING.equals(jobTarget.getStatus())) {
            return JobTargetStatus.AWAITING_COMPLETION;
        }

        return super.getCompletedStatus(jobTarget);
    }
}
