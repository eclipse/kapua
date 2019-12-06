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
package org.eclipse.kapua.service.job.targets;

import org.eclipse.kapua.model.KapuaUpdatableEntity;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.model.id.KapuaIdAdapter;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

/**
 * {@link JobTarget} definition.
 *
 * @since 1.0.0
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

    /**
     * Gets the {@link org.eclipse.kapua.service.job.Job} {@link KapuaId}.
     *
     * @return The {@link org.eclipse.kapua.service.job.Job} {@link KapuaId}.
     * @since 1.0.0
     */
    @XmlJavaTypeAdapter(KapuaIdAdapter.class)
    KapuaId getJobId();

    /**
     * Sets the {@link org.eclipse.kapua.service.job.Job} {@link KapuaId}.
     *
     * @param jobId The {@link org.eclipse.kapua.service.job.Job} {@link KapuaId}.
     * @since 1.0.0
     */
    void setJobId(KapuaId jobId);

    /**
     * Gets the {@link org.eclipse.kapua.model.KapuaEntity} {@link KapuaId}.
     *
     * @return The {@link org.eclipse.kapua.model.KapuaEntity} {@link KapuaId}.
     * @since 1.0.0
     */
    @XmlJavaTypeAdapter(KapuaIdAdapter.class)
    KapuaId getJobTargetId();

    /**
     * Sets the {@link org.eclipse.kapua.model.KapuaEntity} {@link KapuaId}.
     *
     * @param jobTargetId The {@link org.eclipse.kapua.model.KapuaEntity} {@link KapuaId}.
     * @since 1.0.0
     */
    void setJobTargetId(KapuaId jobTargetId);

    /**
     * Gets the {@link JobTargetStatus}.
     *
     * @return The {@link JobTargetStatus}.
     * @since 1.0.0
     */
    JobTargetStatus getStatus();

    /**
     * Sets the {@link JobTargetStatus}.
     *
     * @param status The {@link JobTargetStatus}.
     * @since 1.0.0
     */
    void setStatus(JobTargetStatus status);

    /**
     * Gets the step index.
     *
     * @return The step index.
     * @since 1.0.0
     */
    int getStepIndex();

    /**
     * Sets The step index.
     *
     * @param stepIndex The step index.
     * @since 1.0.0
     */
    void setStepIndex(int stepIndex);

    /**
     * Gets the descriptive message linked to the current {@link #getStatus()}.
     *
     * @return The descriptive message linked to the current {@link #getStatus()}.
     * @since 1.1.0
     */
    String getStatusMessage();

    /**
     * Sets the descriptive message linked to the current {@link #getStatus()}.
     *
     * @param statusMessage The descriptive message linked to the current {@link #getStatus()}.
     * @since 1.1.0
     */
    void setStatusMessage(String statusMessage);

    /**
     * Gets the {@link Exception} occurred on the last processing.
     *
     * @return The {@link Exception} occurred on the last processing.
     * @since 1.0.0
     * @deprecated since 1.1.0 - No longer used.
     */
    @Deprecated
    @XmlTransient
    Exception getException();

    /**
     * Sets the {@link Exception} occurred on the last processing.
     *
     * @param e The {@link Exception} occurred on the last processing.
     * @since 1.0.0
     * @deprecated since 1.1.0 - No longer used.
     */
    @Deprecated
    @XmlTransient
    void setException(Exception e);

}
