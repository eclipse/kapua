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
 *     Red Hat Inc
 *******************************************************************************/
package org.eclipse.kapua.service.job.steps.model;

import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.job.engine.commons.operation.AbstractTargetProcessor;
import org.eclipse.kapua.job.engine.commons.wrappers.JobTargetWrapper;
import org.eclipse.kapua.locator.KapuaLocator;
import org.eclipse.kapua.service.device.registry.Device;
import org.eclipse.kapua.service.device.registry.DeviceRegistryService;
import org.eclipse.kapua.service.job.operation.TargetProcessor;
import org.eclipse.kapua.service.job.targets.JobTarget;

import javax.batch.runtime.context.JobContext;
import javax.batch.runtime.context.StepContext;
import javax.inject.Inject;

public class TestJobStepProcessor extends AbstractTargetProcessor implements TargetProcessor {
    private static final KapuaLocator LOCATOR = KapuaLocator.getInstance();
    private static final DeviceRegistryService DEVICE_REGISTRY_SERVICE = LOCATOR.getService(DeviceRegistryService.class);
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

        boolean fail = Boolean.parseBoolean(System.getProperty("testJobDefinitionProcessorFail", "false"));

        if (fail) {
            throw KapuaException.internalError("This processing has been set to fail");
        }
    }

    @Override
    protected String getTargetDisplayName(JobTarget jobTarget) throws KapuaException {
        Device device = DEVICE_REGISTRY_SERVICE.find(jobTarget.getScopeId(), jobTarget.getJobTargetId());
        if (device == null) {
            return "N/A";
        }
        return device.getClientId();
    }
}
