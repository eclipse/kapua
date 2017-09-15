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
package org.eclipse.kapua.service.device.management.bundle.job;

import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.commons.security.KapuaSecurityUtils;
import org.eclipse.kapua.locator.KapuaLocator;
import org.eclipse.kapua.service.device.management.bundle.DeviceBundleManagementService;
import org.eclipse.kapua.service.device.management.command.job.definition.DeviceBundlePropertyKeys;
import org.eclipse.kapua.service.job.commons.operation.AbstractTargetProcessor;
import org.eclipse.kapua.service.job.operation.TargetOperation;
import org.eclipse.kapua.service.job.targets.JobTarget;

import javax.batch.runtime.context.JobContext;
import javax.batch.runtime.context.StepContext;
import javax.inject.Inject;

public class DeviceBundleStopTargetProcessor extends AbstractTargetProcessor implements TargetOperation {

    private static final KapuaLocator LOCATOR = KapuaLocator.getInstance();
    private static final DeviceBundleManagementService BUNDLE_MANAGEMENT_SERVICE = LOCATOR.getService(DeviceBundleManagementService.class);

    @Inject
    JobContext jobContext;

    @Inject
    StepContext stepContext;

    @Override
    public void processTarget(JobTarget jobTarget) throws KapuaException {
        setContext(jobContext, stepContext);

        String bundleId = kapuaStepContext.getStepProperty(DeviceBundlePropertyKeys.BUNDLE_ID, String.class);
        Long timeout = kapuaStepContext.getStepProperty(DeviceBundlePropertyKeys.TIMEOUT, Long.class);

        KapuaSecurityUtils.doPrivileged(() -> BUNDLE_MANAGEMENT_SERVICE.stop(jobTarget.getScopeId(), jobTarget.getJobTargetId(), bundleId, timeout));
    }
}
