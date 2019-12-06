/*******************************************************************************
 * Copyright (c) 2017, 2019 Eurotech and/or its affiliates and others
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

import org.eclipse.kapua.model.KapuaNamedEntity;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.model.id.KapuaIdAdapter;
import org.eclipse.kapua.service.job.step.definition.JobStepProperty;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.util.List;

/**
 * {@link JobStep} {@link org.eclipse.kapua.model.KapuaEntity} definition.
 *
 * @since 1.0.0
 */
@XmlRootElement(name = "jobStep")
@XmlAccessorType(XmlAccessType.PROPERTY)
@XmlType(factoryClass = JobStepXmlRegistry.class, factoryMethod = "newJobStep")
public interface JobStep extends KapuaNamedEntity {

    String TYPE = "jobStep";

    @Override
    default String getType() {
        return TYPE;
    }

    @XmlJavaTypeAdapter(KapuaIdAdapter.class)
    KapuaId getJobId();

    void setJobId(KapuaId jobId);

    int getStepIndex();

    void setStepIndex(int stepIndex);

    @XmlJavaTypeAdapter(KapuaIdAdapter.class)
    KapuaId getJobStepDefinitionId();

    void setJobStepDefinitionId(KapuaId jobDefinitionId);

    <P extends JobStepProperty> List<P> getStepProperties();

    void setStepProperties(List<JobStepProperty> jobStepProperties);

}
