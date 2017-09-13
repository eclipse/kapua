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
package org.eclipse.kapua.service.job.targets;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import org.eclipse.kapua.model.KapuaUpdatableEntityCreator;
import org.eclipse.kapua.model.id.KapuaId;

/**
 * {@link JobTargetCreator} encapsulates all the information needed to create a new JobTarget in the system.<br>
 * The data provided will be used to seed the new JobTarget.
 * 
 * @since 1.0.0
 *
 */
@XmlRootElement(name = "jobCreator")
@XmlAccessorType(XmlAccessType.PROPERTY)
@XmlType(factoryClass = JobTargetXmlRegistry.class, factoryMethod = "newJobCreator")
public interface JobTargetCreator extends KapuaUpdatableEntityCreator<JobTarget> {

    public KapuaId getJobId();

    public void setJobId(KapuaId jobId);

    public KapuaId getJobTargetId();

    public void setJobTargetId(KapuaId jobTargetId);
}
