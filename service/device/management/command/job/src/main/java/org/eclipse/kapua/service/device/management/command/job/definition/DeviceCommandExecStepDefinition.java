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
package org.eclipse.kapua.service.device.management.command.job.definition;

import org.eclipse.kapua.job.engine.commons.step.definition.AbstractTargetJobStepDefinition;
import org.eclipse.kapua.locator.KapuaLocator;
import org.eclipse.kapua.service.device.management.command.DeviceCommandInput;
import org.eclipse.kapua.service.device.management.command.job.DeviceCommandExecTargetProcessor;
import org.eclipse.kapua.service.job.step.definition.JobStepDefinition;
import org.eclipse.kapua.service.job.step.definition.JobStepDefinitionFactory;
import org.eclipse.kapua.service.job.step.definition.JobStepProperty;

import java.util.Arrays;
import java.util.List;

public class DeviceCommandExecStepDefinition extends AbstractTargetJobStepDefinition implements JobStepDefinition {

    private static final long serialVersionUID = -4994045121586264564L;

    private final KapuaLocator locator = KapuaLocator.getInstance();
    private final JobStepDefinitionFactory jobStepDefinitionFactory = locator.getFactory(JobStepDefinitionFactory.class);

    @Override
    public String getName() {
        return "Command Execution";
    }

    @Override
    public String getDescription() {
        return "Execution of a command using the Device Command Management Service";
    }

    @Override
    public String getProcessorName() {
        return DeviceCommandExecTargetProcessor.class.getName();
    }

    @Override
    public List<JobStepProperty> getStepProperties() {

        JobStepProperty propertyCommandInput = jobStepDefinitionFactory.newStepProperty(
                DeviceCommandExecPropertyKeys.COMMAND_INPUT,
                DeviceCommandInput.class.getName(),
                null,
                null);

        JobStepProperty propertyTimeout = jobStepDefinitionFactory.newStepProperty(
                DeviceCommandExecPropertyKeys.TIMEOUT,
                Long.class.getName(),
                "30000",
                null);

        return Arrays.asList(propertyCommandInput, propertyTimeout);
    }
}
