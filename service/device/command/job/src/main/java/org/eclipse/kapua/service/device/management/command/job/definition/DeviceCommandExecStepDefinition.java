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
package org.eclipse.kapua.service.device.management.command.job.definition;

import java.util.Arrays;
import java.util.List;

import org.eclipse.kapua.commons.model.AbstractKapuaNamedEntity;
import org.eclipse.kapua.locator.KapuaLocator;
import org.eclipse.kapua.service.device.management.command.DeviceCommandInput;
import org.eclipse.kapua.service.device.management.command.job.DeviceCommandTargetProcessor;
import org.eclipse.kapua.service.job.step.definition.JobStepDefinition;
import org.eclipse.kapua.service.job.step.definition.JobStepDefinitionFactory;
import org.eclipse.kapua.service.job.step.definition.JobStepProperty;
import org.eclipse.kapua.service.job.step.definition.JobStepType;

public class DeviceCommandExecStepDefinition extends AbstractKapuaNamedEntity implements JobStepDefinition {

    private static final long serialVersionUID = -4994045121586264564L;

    private final KapuaLocator locator = KapuaLocator.getInstance();
    private final JobStepDefinitionFactory jobStepDefinitionFactory = locator.getFactory(JobStepDefinitionFactory.class);

    @Override
    public String getName() {
        return "Device Command Management Execution";
    }

    @Override
    public String getDescription() {
        return null;
    }

    @Override
    public void setDescription(String description) {
    }

    @Override
    public JobStepType getStepType() {
        return JobStepType.TARGET;
    }

    @Override
    public void setStepType(JobStepType jobStepType) {
    }

    @Override
    public void setReaderName(String readerName) {
    }

    @Override
    public String getProcessorName() {
        return DeviceCommandTargetProcessor.class.getName();
    }

    @Override
    public void setProcessorName(String processorName) {
    }

    @Override
    public void setWriterName(String writesName) {
    }

    @Override
    public List<JobStepProperty> getStepProperties() {

        JobStepProperty propertyCommandInput = jobStepDefinitionFactory.newStepProperty(
                DeviceCommandExecPropertyKeys.COMMAND_INPUT,
                DeviceCommandInput.class.getName(),
                null);

        JobStepProperty propertyTimeout = jobStepDefinitionFactory.newStepProperty(
                DeviceCommandExecPropertyKeys.TIMEOUT,
                Long.class.getName(),
                "30000");

        return Arrays.asList(propertyCommandInput, propertyTimeout);
    }

    @Override
    public void setStepProperties(List<JobStepProperty> jobStepProperties) {
    }

}
