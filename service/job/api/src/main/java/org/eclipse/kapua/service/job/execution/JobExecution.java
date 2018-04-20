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
package org.eclipse.kapua.service.job.execution;

import java.util.Date;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import org.eclipse.kapua.model.KapuaUpdatableEntity;
import org.eclipse.kapua.model.id.KapuaId;

/**
 * {@link JobExecution} entity.
 * 
 * @since 1.0
 *
 */
@XmlRootElement(name = "jobExecution")
@XmlAccessorType(XmlAccessType.PROPERTY)
@XmlType(factoryClass = JobExecutionXmlRegistry.class, factoryMethod = "newJobExecution")
public interface JobExecution extends KapuaUpdatableEntity {

    public static final String TYPE = "jobExecution";

    public default String getType() {
        return TYPE;
    }

    public KapuaId getJobId();

    public void setJobId(KapuaId jobId);

    public Date getStartedOn();

    public void setStartedOn(Date startedOn);

    public Date getEndedOn();

    public void setEndedOn(Date endedOn);

}
