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
package org.eclipse.kapua.service.job.step;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import org.eclipse.kapua.model.KapuaNamedEntityCreator;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.job.step.definition.JobStepProperty;

/**
 * {@link JobStepCreator} encapsulates all the information needed to create a new JobStep in the system.<br>
 * The data provided will be used to seed the new JobStep.
 * 
 * @since 1.0.0
 *
 */
@XmlRootElement(name = "jobCreator")
@XmlAccessorType(XmlAccessType.PROPERTY)
@XmlType(factoryClass = JobStepXmlRegistry.class, factoryMethod = "newJobCreator")
public interface JobStepCreator extends KapuaNamedEntityCreator<JobStep> {

    public String getDescription();

    public void setDescription(String description);

    public KapuaId getJobId();

    public void setJobId(KapuaId jobId);

    public int getStepIndex();

    public void setStepIndex(int stepIndex);

    public KapuaId getJobStepDefinitionId();

    public void setJobStepDefinitionId(KapuaId jobStepDefinitionId);

    public <P extends JobStepProperty> List<P> getJobStepProperties();

    public void setJobStepProperties(List<JobStepProperty> jobStepProperties);
}
