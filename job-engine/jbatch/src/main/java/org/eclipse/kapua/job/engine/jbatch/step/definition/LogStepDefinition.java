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
package org.eclipse.kapua.job.engine.jbatch.step.definition;

import java.util.Arrays;
import java.util.List;

import org.eclipse.kapua.job.engine.jbatch.step.LogProcessor;
import org.eclipse.kapua.locator.KapuaLocator;
import org.eclipse.kapua.service.job.commons.step.definition.AbstractGenericJobStepDefinition;
import org.eclipse.kapua.service.job.step.definition.JobStepDefinition;
import org.eclipse.kapua.service.job.step.definition.JobStepDefinitionFactory;
import org.eclipse.kapua.service.job.step.definition.JobStepProperty;

public class LogStepDefinition extends AbstractGenericJobStepDefinition implements JobStepDefinition {

    private static final long serialVersionUID = -3405575285674126806L;

    private final KapuaLocator locator = KapuaLocator.getInstance();
    private final JobStepDefinitionFactory jobStepDefinitionFactory = locator.getFactory(JobStepDefinitionFactory.class);

    @Override
    public String getName() {
        return "Log step";
    }

    @Override
    public String getDescription() {
        return "Just some random log";
    }

    @Override
    public String getProcessorName() {
        return LogProcessor.class.getName();
    }

    @Override
    public List<JobStepProperty> getStepProperties() {

        JobStepProperty propertyLogString = jobStepDefinitionFactory.newStepProperty(
                LogPropertyKeys.LOG_STRING,
                String.class.getName(),
                "Default log");

        return Arrays.asList(propertyLogString);
    }
}