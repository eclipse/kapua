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
package org.eclipse.kapua.service.device.management.configuration.job;

import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.commons.security.KapuaSecurityUtils;
import org.eclipse.kapua.job.engine.commons.operation.AbstractDeviceTargetProcessor;
import org.eclipse.kapua.job.engine.commons.wrappers.JobTargetWrapper;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.device.management.configuration.DeviceConfiguration;
import org.eclipse.kapua.service.device.management.configuration.DeviceConfigurationManagementService;
import org.eclipse.kapua.service.device.management.configuration.job.definition.DeviceConfigurationPutPropertyKeys;
import org.eclipse.kapua.service.job.operation.TargetProcessor;
import org.eclipse.kapua.service.job.targets.JobTarget;

import javax.batch.runtime.context.JobContext;
import javax.batch.runtime.context.StepContext;
import javax.inject.Inject;

/**
 * {@link TargetProcessor} for {@link DeviceConfigurationManagementService#put(KapuaId, KapuaId, DeviceConfiguration, Long)}.
 *
 * @since 1.0.0
 */
public class DeviceConfigurationPutTargetProcessor extends AbstractDeviceTargetProcessor implements TargetProcessor {
    private static final DeviceConfigurationManagementService CONFIGURATION_MANAGEMENT_SERVICE = LOCATOR.getService(DeviceConfigurationManagementService.class);

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

        DeviceConfiguration configuration = stepContextWrapper.getStepProperty(DeviceConfigurationPutPropertyKeys.CONFIGURATION, DeviceConfiguration.class);
        Long timeout = stepContextWrapper.getStepProperty(DeviceConfigurationPutPropertyKeys.TIMEOUT, Long.class);

        KapuaSecurityUtils.doPrivileged(() -> CONFIGURATION_MANAGEMENT_SERVICE.put(jobTarget.getScopeId(), jobTarget.getJobTargetId(), configuration, timeout));
    }
}
