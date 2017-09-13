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
package org.eclipse.kapua.service.job;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import org.eclipse.kapua.model.KapuaNamedEntity;
import org.eclipse.kapua.service.job.step.JobStep;

/**
 * {@link Job} entity.
 * 
 * @since 1.0.0
 *
 */
@XmlRootElement(name = "job")
@XmlAccessorType(XmlAccessType.PROPERTY)
@XmlType(factoryClass = JobXmlRegistry.class, factoryMethod = "newJob")
public interface Job extends KapuaNamedEntity {

    public static final String TYPE = "job";

    public default String getType() {
        return TYPE;
    }

    public String getDescription();

    public void setDescription(String description);

    public List<JobStep> getJobSteps();

    public void setJobSteps(List<JobStep> jobSteps);

    public String getJobXmlDefinition();

    public void setJobXmlDefinition(String jobXmlDefinition);

}
