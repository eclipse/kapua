/*******************************************************************************
 * Copyright (c) 2019 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.job.engine.proxy.dto;

import java.util.Set;

import org.eclipse.kapua.job.engine.JobStartOptions;
import org.eclipse.kapua.model.id.KapuaId;

/**
 * Simple POJO to contain {@link JobStartOptions} info without implementation specific (e.g. JPA) code
 */
public class JobStartOptionsDto implements JobStartOptions {

    private Set<KapuaId> targetIdSublist;
    private Integer fromStepIndex;
    private boolean resetStepIndex;
    private boolean enqueue;

    @Override
    public Set<KapuaId> getTargetIdSublist() {
        return targetIdSublist;
    }

    @Override
    public void setTargetIdSublist(Set<KapuaId> targetIdSublist) {
        this.targetIdSublist = targetIdSublist;
    }

    @Override
    public void removeTargetIdToSublist(KapuaId targetId) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void addTargetIdToSublist(KapuaId targetId) {
        throw new UnsupportedOperationException();
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
