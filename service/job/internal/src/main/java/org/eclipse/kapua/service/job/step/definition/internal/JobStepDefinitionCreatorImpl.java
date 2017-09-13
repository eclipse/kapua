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
package org.eclipse.kapua.service.job.step.definition.internal;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.kapua.commons.model.AbstractKapuaNamedEntityCreator;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.job.step.definition.JobStepDefinition;
import org.eclipse.kapua.service.job.step.definition.JobStepDefinitionCreator;
import org.eclipse.kapua.service.job.step.definition.JobStepProperty;
import org.eclipse.kapua.service.job.step.definition.JobStepType;

/**
 * {@link JobStepDefinitionCreator} encapsulates all the information needed to create a new JobStepDefinition in the system.<br>
 * The data provided will be used to seed the new JobStepDefinition.
 * 
 * @since 1.0.0
 *
 */
public class JobStepDefinitionCreatorImpl extends AbstractKapuaNamedEntityCreator<JobStepDefinition> implements JobStepDefinitionCreator {

    private static final long serialVersionUID = 4602067255120049746L;

    private String description;
    private JobStepType jobStepType;
    private String readerName;
    private String processorName;
    private String writerName;
    private List<JobStepProperty> jobStepProperties;

    protected JobStepDefinitionCreatorImpl(KapuaId scopeId) {
        super(scopeId);
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public JobStepType getStepType() {
        return jobStepType;
    }

    @Override
    public void setStepType(JobStepType jobStepType) {
        this.jobStepType = jobStepType;
    }

    @Override
    public String getReaderName() {
        return readerName;
    }

    @Override
    public void setReaderName(String readerName) {
        this.readerName = readerName;
    }

    @Override
    public String getProcessorName() {
        return processorName;
    }

    @Override
    public void setProcessorName(String processorName) {
        this.processorName = processorName;
    }

    @Override
    public String getWriterName() {
        return writerName;
    }

    @Override
    public void setWriterName(String writerName) {
        this.writerName = writerName;
    }

    @Override
    public List<JobStepProperty> getStepProperties() {
        if (jobStepProperties == null) {
            jobStepProperties = new ArrayList<>();
        }

        return jobStepProperties;
    }

    @Override
    public void setStepProperties(List<JobStepProperty> jobStepProperties) {
        this.jobStepProperties = jobStepProperties;
    }

}
