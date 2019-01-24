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

import org.eclipse.kapua.model.KapuaUpdatableEntity;
import org.eclipse.kapua.model.id.KapuaId;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * {@link JobTarget} entity.
 *
 * @since 1.0
 */
@XmlRootElement(name = "jobTarget")
@XmlAccessorType(XmlAccessType.PROPERTY)
@XmlType(factoryClass = JobTargetXmlRegistry.class, factoryMethod = "newJobTarget")
public interface JobTarget extends KapuaUpdatableEntity {

    String TYPE = "jobTarget";

    @Override
    default String getType() {
        return TYPE;
    }

    KapuaId getJobId();

    void setJobId(KapuaId jobId);

    KapuaId getJobTargetId();

    void setJobTargetId(KapuaId jobTargetId);

    JobTargetStatus getStatus();

    void setStatus(JobTargetStatus status);

    int getStepIndex();

    void setStepIndex(int stepIndex);

    String getStatusMessage();

    void setStatusMessage(String statusMessage);

    /**
     * @deprecated No longer used.
     */
    @Deprecated
    Exception getException();

    /**
     * @deprecated No longer used.
     */
    @Deprecated
    void setException(Exception e);

}
