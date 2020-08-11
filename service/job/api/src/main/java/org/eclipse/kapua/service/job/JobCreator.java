/*******************************************************************************
 * Copyright (c) 2017, 2020 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.service.job;

import org.eclipse.kapua.model.KapuaNamedEntityCreator;
import org.eclipse.kapua.service.job.step.JobStep;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import java.util.List;

/**
 * {@link JobCreator} {@link org.eclipse.kapua.model.KapuaEntityCreator} definition.
 *
 * @since 1.0.0
 */
@XmlRootElement(name = "jobCreator")
@XmlAccessorType(XmlAccessType.PROPERTY)
@XmlType(factoryClass = JobXmlRegistry.class, factoryMethod = "newJobCreator")
public interface JobCreator extends KapuaNamedEntityCreator<Job> {

    List<JobStep> getJobSteps();

    void setJobSteps(List<JobStep> jobSteps);

    String getJobXmlDefinition();

    void setJobXmlDefinition(String jobXmlDefinition);

}
