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
package org.eclipse.kapua.service.job.step.definition;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import org.eclipse.kapua.model.KapuaNamedEntityCreator;

/**
 * {@link JobStepDefinitionCreator} encapsulates all the information needed to create a new JobStepDefinition in the system.<br>
 * The data provided will be used to seed the new JobStepDefinition.
 * 
 * @since 1.0.0
 *
 */
@XmlRootElement(name = "jobCreator")
@XmlAccessorType(XmlAccessType.PROPERTY)
@XmlType(factoryClass = JobStepDefinitionXmlRegistry.class, factoryMethod = "newJobCreator")
public interface JobStepDefinitionCreator extends KapuaNamedEntityCreator<JobStepDefinition> {

    public String getDescription();

    public void setDescription(String description);

    public JobStepType getStepType();

    public void setStepType(JobStepType jobStepType);

    public String getReaderName();

    public void setReaderName(String readerName);

    public String getProcessorName();

    public void setProcessorName(String processorName);

    public String getWriterName();

    public void setWriterName(String writesName);

    public List<JobStepProperty> getStepProperties();

    public void setStepProperties(List<JobStepProperty> jobStepProperties);
}
