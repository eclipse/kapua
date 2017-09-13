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
package org.eclipse.kapua.service.device.management.packages.job.definition;

import org.eclipse.kapua.locator.KapuaLocator;
import org.eclipse.kapua.service.device.management.packages.job.DevicePackageDownloadTargetProcessor;
import org.eclipse.kapua.service.device.management.packages.model.download.DevicePackageDownloadRequest;
import org.eclipse.kapua.service.job.commons.step.definition.AbstractTargetJobStepDefinition;
import org.eclipse.kapua.service.job.step.definition.JobStepDefinition;
import org.eclipse.kapua.service.job.step.definition.JobStepDefinitionFactory;
import org.eclipse.kapua.service.job.step.definition.JobStepProperty;

import java.util.Arrays;
import java.util.List;

public class DevicePackagesDownloadStepDefinition extends AbstractTargetJobStepDefinition implements JobStepDefinition {

    private static final long serialVersionUID = -4994045121586264564L;

    private final KapuaLocator locator = KapuaLocator.getInstance();
    private final JobStepDefinitionFactory jobStepDefinitionFactory = locator.getFactory(JobStepDefinitionFactory.class);

    @Override
    public String getName() {
        return "Device Packages Management Download Execution";
    }

    @Override
    public String getDescription() {
        return "Downloads a package using the Device Packages Management Service";
    }

    @Override
    public String getProcessorName() {
        return DevicePackageDownloadTargetProcessor.class.getName();
    }

    @Override
    public List<JobStepProperty> getStepProperties() {

        JobStepProperty propertyDownloadRequest = jobStepDefinitionFactory.newStepProperty(
                DevicePackageDownloadPropertyKeys.PACKAGE_DOWNLOAD_REQUEST,
                DevicePackageDownloadRequest.class.getName(),
                null);

        JobStepProperty propertyTimeout = jobStepDefinitionFactory.newStepProperty(
                DevicePackageDownloadPropertyKeys.TIMEOUT,
                Long.class.getName(),
                "30000");

        return Arrays.asList(propertyDownloadRequest, propertyTimeout);
    }
}
