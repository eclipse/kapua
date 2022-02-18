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
package org.eclipse.kapua.service.job.step;

import org.eclipse.kapua.model.KapuaNamedEntityCreator;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.model.id.KapuaIdAdapter;
import org.eclipse.kapua.service.job.step.definition.JobStepProperty;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.util.List;

/**
 * {@link JobStepCreator} {@link org.eclipse.kapua.model.KapuaEntityCreator} definition
 *
 * @since 1.0.0
 */
@XmlRootElement(name = "jobStepCreator")
@XmlAccessorType(XmlAccessType.PROPERTY)
@XmlType(factoryClass = JobStepXmlRegistry.class, factoryMethod = "newJobStepCreator")
public interface JobStepCreator extends KapuaNamedEntityCreator<JobStep> {

    @XmlJavaTypeAdapter(KapuaIdAdapter.class)
    KapuaId getJobId();

    void setJobId(KapuaId jobId);

    Integer getStepIndex();

    void setStepIndex(Integer stepIndex);

    @XmlJavaTypeAdapter(KapuaIdAdapter.class)
    KapuaId getJobStepDefinitionId();

    void setJobStepDefinitionId(KapuaId jobStepDefinitionId);

    @XmlElementWrapper(name = "jobStepProperties")
    @XmlElement(name = "jobStepProperty")
    <P extends JobStepProperty> List<P> getStepProperties();

    void setJobStepProperties(List<JobStepProperty> jobStepProperties);
}
