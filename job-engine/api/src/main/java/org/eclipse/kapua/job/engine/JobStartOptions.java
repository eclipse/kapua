/*******************************************************************************
 * Copyright (c) 2018, 2022 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.job.engine;

import org.eclipse.kapua.KapuaSerializable;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.model.id.KapuaIdAdapter;
import org.eclipse.kapua.service.job.targets.JobTarget;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.util.Set;

/**
 * {@link JobStartOptions} definition.
 *
 * @since 1.0.0
 */
@XmlRootElement(name = "jobStartOptions")
@XmlAccessorType(XmlAccessType.PROPERTY)
@XmlType(factoryClass = JobEngineXmlRegistry.class, factoryMethod = "newJobStartOptions")
public interface JobStartOptions extends KapuaSerializable {

    /**
     * Gets the sub{@link java.util.List} of {@link org.eclipse.kapua.service.job.targets.JobTarget} {@link KapuaId}s.
     *
     * @return The sub{@link java.util.List} of {@link org.eclipse.kapua.service.job.targets.JobTarget} {@link KapuaId}s.
     * @since 1.0.0
     */
    @XmlElementWrapper(name = "targetIdSublist")
    @XmlElement(name = "targetId")
    @XmlJavaTypeAdapter(KapuaIdAdapter.class)
    Set<KapuaId> getTargetIdSublist();

    /**
     * Sets the sub{@link java.util.List} of {@link org.eclipse.kapua.service.job.targets.JobTarget} {@link KapuaId}s.
     *
     * @param targetIdSublist The sub{@link java.util.List} of {@link org.eclipse.kapua.service.job.targets.JobTarget} {@link KapuaId}s.
     * @since 1.0.0
     */
    void setTargetIdSublist(Set<KapuaId> targetIdSublist);

    /**
     * Removes a {@link org.eclipse.kapua.service.job.targets.JobTarget} {@link KapuaId} from sub{@link java.util.List} of {@link org.eclipse.kapua.service.job.targets.JobTarget} {@link KapuaId}s.
     *
     * @param targetId The {@link org.eclipse.kapua.service.job.targets.JobTarget} {@link KapuaId} to remove.
     * @since 1.0.0
     */
    @XmlTransient
    void removeTargetIdToSublist(KapuaId targetId);

    /**
     * Adds a {@link org.eclipse.kapua.service.job.targets.JobTarget} {@link KapuaId} from sub{@link java.util.List} of {@link org.eclipse.kapua.service.job.targets.JobTarget} {@link KapuaId}s.
     *
     * @param targetId The {@link org.eclipse.kapua.service.job.targets.JobTarget} {@link KapuaId} to add.
     * @since 1.0.0
     */
    @XmlTransient
    void addTargetIdToSublist(KapuaId targetId);

    /**
     * Gets whether or not the {@link JobTarget#getStepIndex()} needs to be reset to the given {@link #getFromStepIndex()}.
     *
     * @return {@code true} if the {@link JobTarget#getStepIndex()} needs to be reset to the given {@link #getFromStepIndex()}, {@code false} otherwise.
     * @since 1.1.0
     */
    boolean getResetStepIndex();

    /**
     * Sets whether or not the {@link JobTarget#getStepIndex()} needs to be reset to the given {@link #getFromStepIndex()}.
     *
     * @param resetStepIndex {@code true} if the {@link JobTarget#getStepIndex()} needs to be reset to the given {@link #getFromStepIndex()}, {@code false} otherwise.
     * @since 1.1.0
     */
    void setResetStepIndex(boolean resetStepIndex);

    /**
     * Gets the starting {@link org.eclipse.kapua.service.job.step.JobStep} index.
     *
     * @return The starting {@link org.eclipse.kapua.service.job.step.JobStep} index.
     * @since 1.0.0
     */
    Integer getFromStepIndex();

    /**
     * Sets the starting {@link org.eclipse.kapua.service.job.step.JobStep} index.
     *
     * @param fromStepIndex The starting {@link org.eclipse.kapua.service.job.step.JobStep} index.
     * @since 1.0.0
     */
    void setFromStepIndex(Integer fromStepIndex);

    /**
     * Gets whether or not enqueue the {@link org.eclipse.kapua.service.job.execution.JobExecution}.
     *
     * @return {@code true} if the {@link org.eclipse.kapua.service.job.execution.JobExecution} needs to be enqueued, {@code false} otherwise.
     * @since 1.1.0
     */
    boolean getEnqueue();

    /**
     * Sets whether or not enqueue the {@link org.eclipse.kapua.service.job.execution.JobExecution}.
     *
     * @param enqueue {@code true} if the {@link org.eclipse.kapua.service.job.execution.JobExecution} needs to be enqueued, {@code false} otherwise.
     * @since 1.1.0
     */
    void setEnqueue(boolean enqueue);
}
