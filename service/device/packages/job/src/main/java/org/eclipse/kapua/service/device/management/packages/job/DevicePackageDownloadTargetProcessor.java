/*******************************************************************************
 * Copyright (c) 2017 Eurotech and/or its affiliates and others
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
import org.eclipse.kapua.commons.security.KapuaSecurityUtils;
import org.eclipse.kapua.locator.KapuaLocator;
import org.eclipse.kapua.service.device.management.packages.DevicePackageManagementService;
import org.eclipse.kapua.service.device.management.packages.job.definition.DevicePackageDownloadPropertyKeys;
import org.eclipse.kapua.service.device.management.packages.model.download.DevicePackageDownloadRequest;
import org.eclipse.kapua.service.job.commons.operation.AbstractTargetProcessor;
import org.eclipse.kapua.service.job.operation.TargetOperation;
import org.eclipse.kapua.service.job.targets.JobTarget;

import javax.batch.runtime.context.JobContext;
import javax.batch.runtime.context.StepContext;
import javax.inject.Inject;

public class DevicePackageDownloadTargetProcessor extends AbstractTargetProcessor implements TargetOperation {

    private static final KapuaLocator LOCATOR = KapuaLocator.getInstance();
    private static final DevicePackageManagementService PACKAGES_MANAGEMENT_SERVICE = LOCATOR.getService(DevicePackageManagementService.class);

    @Inject
    JobContext jobContext;

    @Inject
    StepContext stepContext;

    @Override
    public void processTarget(JobTarget jobTarget) throws KapuaException {
        setContext(jobContext, stepContext);

        DevicePackageDownloadRequest packageDownloadRequest = kapuaStepContext.getStepProperty(DevicePackageDownloadPropertyKeys.PACKAGE_DOWNLOAD_REQUEST, DevicePackageDownloadRequest.class);
        Long timeout = kapuaStepContext.getStepProperty(DevicePackageDownloadPropertyKeys.TIMEOUT, Long.class);

        KapuaSecurityUtils.doPrivileged(() -> PACKAGES_MANAGEMENT_SERVICE.downloadExec(jobTarget.getScopeId(), jobTarget.getJobTargetId(), packageDownloadRequest, timeout));
    }
}
