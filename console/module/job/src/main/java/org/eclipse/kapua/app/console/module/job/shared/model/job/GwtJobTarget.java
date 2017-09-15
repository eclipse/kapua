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
package org.eclipse.kapua.app.console.module.job.shared.model.job;

import org.eclipse.kapua.app.console.module.api.shared.model.GwtUpdatableEntityModel;

public class GwtJobTarget extends GwtUpdatableEntityModel {

    public enum GwtJobTargetStatus {
        PROCESS_OK, PROCESS_FAILED, PROCESS_AWAITING
    }

    @Override
    @SuppressWarnings({ "unchecked" })
    public <X> X get(String property) {
        if ("statusEnum".equals(property)) {
            return (X) GwtJobTargetStatus.valueOf(getStatus());
        } else {
            return super.get(property);
        }
    }

    public String getJobId() {
        return get("jobId");
    }

    public void setJobId(String jobId) {
        set("jobId", jobId);
    }

    public String getJobTargetId() {
        return get("jobTargetId");
    }

    public void setJobTargetId(String jobTargetId) {
        set("jobTargetId", jobTargetId);
    }

    public String getStatus() {
        return get("status");
    }

    public String getStatusEnum() {
        return get("statusEnum");
    }

    public void setStatus(String status) {
        set("status", status);
    }

    public int getStepIndex() {
        return get("stepIndex");
    }

    public void setStepIndex(int stepIndex) {
        set("stepIndex", stepIndex);
    }

    public String getErrorMessage() {
        return get("errorMessage");
    }

    public void setErrorMessage(String errorMessage) {
        set("errorMessage", errorMessage);
    }

}
