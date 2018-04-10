/*******************************************************************************
 * Copyright (c) 2018 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.job.engine.jbatch;

import org.eclipse.kapua.job.engine.JobStartOptions;
import org.eclipse.kapua.model.id.KapuaId;

import java.util.ArrayList;
import java.util.List;

public class JobStartOptionsImpl implements JobStartOptions {

    private List<KapuaId> targetIdSublist;

    private Integer fromStepIndex;

    public JobStartOptionsImpl() {
    }

    @Override
    public List<KapuaId> getTargetIdSublist() {
        if (targetIdSublist == null) {
            targetIdSublist = new ArrayList<>();
        }

        return targetIdSublist;
    }

    @Override
    public void setTargetIdSublist(List<KapuaId> targetIdSublist) {
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
    public Integer getFromStepIndex() {
        return fromStepIndex;
    }

    @Override
    public void setFromStepIndex(Integer fromStepIndex) {
        this.fromStepIndex = fromStepIndex;
    }
}
