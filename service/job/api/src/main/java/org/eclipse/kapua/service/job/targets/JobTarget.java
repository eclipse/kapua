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

import org.eclipse.kapua.model.KapuaUpdatableEntity;
import org.eclipse.kapua.model.id.KapuaId;

/**
 * {@link JobTarget} entity.
 * 
 * @since 1.0
 *
 */
@XmlRootElement(name = "jobTarget")
@XmlAccessorType(XmlAccessType.PROPERTY)
@XmlType(factoryClass = JobTargetXmlRegistry.class, factoryMethod = "newJobTarget")
public interface JobTarget extends KapuaUpdatableEntity {

    public static final String TYPE = "jobTarget";

    public default String getType() {
        return TYPE;
    }

    public KapuaId getJobId();

    public void setJobId(KapuaId jobId);

    public KapuaId getJobTargetId();

    public void setJobTargetId(KapuaId jobTargetId);

    public JobTargetStatus getStatus();

    public void setStatus(JobTargetStatus status);

    public int getStepIndex();

    public void setStepIndex(int stepIndex);

    public Exception getException();

    public void setException(Exception e);

}
