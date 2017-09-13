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
package org.eclipse.kapua.service.job.commons.step.definition;

import java.util.List;

import org.eclipse.kapua.commons.model.AbstractKapuaUpdatableEntity;
import org.eclipse.kapua.service.job.commons.operation.DefaultTargetReader;
import org.eclipse.kapua.service.job.commons.operation.DefaultTargetWriter;
import org.eclipse.kapua.service.job.step.definition.JobStepDefinition;
import org.eclipse.kapua.service.job.step.definition.JobStepProperty;
import org.eclipse.kapua.service.job.step.definition.JobStepType;

public abstract class AbstractTargetJobStepDefinition extends AbstractKapuaUpdatableEntity implements JobStepDefinition {

    private static final long serialVersionUID = 474195961081702478L;

    public void setName(String name) {
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
    public String getReaderName() {
        return DefaultTargetReader.class.getName();
    }

    @Override
    public void setReaderName(String readerName) {
    }

    @Override
    public void setProcessorName(String processorName) {
    }

    @Override
    public String getWriterName() {
        return DefaultTargetWriter.class.getName();
    }

    @Override
    public void setWriterName(String writesName) {
    }

    @Override
    public void setStepProperties(List<JobStepProperty> jobStepProperties) {
    }

}
