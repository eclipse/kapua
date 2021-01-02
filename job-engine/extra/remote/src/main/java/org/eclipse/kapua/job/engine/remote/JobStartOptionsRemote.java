/*******************************************************************************
 * Copyright (c) 2019, 2021 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.job.engine.remote;

import org.eclipse.kapua.job.engine.JobStartOptions;
import org.eclipse.kapua.model.id.KapuaId;

import java.util.HashSet;
import java.util.Set;

/**
 * {@link JobStartOptions} implementation.
 *
 * @since 1.1.0
 */
public class JobStartOptionsRemote implements JobStartOptions {

    private Set<KapuaId> targetIdSublist;
    private boolean resetStepIndex;
    private Integer fromStepIndex;
    private boolean enqueue;

    public JobStartOptionsRemote() {
    }

    public JobStartOptionsRemote(JobStartOptions jobStartOptions) {
        this();

        setTargetIdSublist(jobStartOptions.getTargetIdSublist());
        setFromStepIndex(jobStartOptions.getFromStepIndex());
        setEnqueue(jobStartOptions.getEnqueue());
    }

    @Override
    public Set<KapuaId> getTargetIdSublist() {
        if (targetIdSublist == null) {
            targetIdSublist = new HashSet<>();
        }

        return targetIdSublist;
    }

    @Override
    public void setTargetIdSublist(Set<KapuaId> targetIdSublist) {
        this.targetIdSublist = targetIdSublist;
    }

    @Override
    public void addTargetIdToSublist(KapuaId targetId) {
        getTargetIdSublist().add(targetId);
    }

    @Override
    public void removeTargetIdToSublist(KapuaId targetId) {
        getTargetIdSublist().remove(targetId);
    }

    @Override
    public boolean getResetStepIndex() {
        return resetStepIndex;
    }

    @Override
    public void setResetStepIndex(boolean resetStepIndex) {
        this.resetStepIndex = resetStepIndex;
    }

    @Override
    public Integer getFromStepIndex() {
        return fromStepIndex;
    }

    @Override
    public void setFromStepIndex(Integer fromStepIndex) {
        this.fromStepIndex = fromStepIndex;
    }

    @Override
    public boolean getEnqueue() {
        return enqueue;
    }

    @Override
    public void setEnqueue(boolean enqueue) {
        this.enqueue = enqueue;
    }
}
