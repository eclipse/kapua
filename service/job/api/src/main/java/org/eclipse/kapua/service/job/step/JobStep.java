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

import org.eclipse.kapua.model.KapuaNamedEntity;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.job.step.definition.JobStepProperty;

/**
 * {@link JobStep} entity.
 *
 * @since 1.0
 *
 */
@XmlRootElement(name = "jobStep")
@XmlAccessorType(XmlAccessType.PROPERTY)
@XmlType(factoryClass = JobStepXmlRegistry.class, factoryMethod = "newJob")
public interface JobStep extends KapuaNamedEntity {

    public static final String TYPE = "jobStep";

    public default String getType() {
        return TYPE;
    }

    public String getDescription();

    public void setDescription(String description);

    public KapuaId getJobId();

    public void setJobId(KapuaId jobId);

    public int getStepIndex();

    public void setStepIndex(int stepIndex);

    public KapuaId getJobStepDefinitionId();

    public void setJobStepDefinitionId(KapuaId jobDefinitionId);

    public <P extends JobStepProperty> List<P> getStepProperties();

    public void setStepProperties(List<JobStepProperty> jobStepProperties);

}
