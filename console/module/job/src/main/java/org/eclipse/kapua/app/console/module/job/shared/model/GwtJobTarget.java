/*******************************************************************************
 * Copyright (c) 2017, 2022 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.app.console.module.job.shared.model;

import org.eclipse.kapua.app.console.module.api.shared.model.GwtUpdatableEntityModel;

public class GwtJobTarget extends GwtUpdatableEntityModel {

    public enum GwtJobTargetStatus {
        PROCESS_OK, PROCESS_FAILED, PROCESS_AWAITING
    }

    @Override
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

    public String getStatusMessage() {
        return get("statusMessage");
    }

    public void setStatusMessage(String statusMessage) {
        set("statusMessage", statusMessage);
    }

    //
    // Additional fields
    //
    public String getClientId() {
        return get("clientId");
    }

    public void setClientId(String clientId) {
        set("clientId", clientId);
    }

    public String getDisplayName() {
        return get("displayName");
    }

    public void setDisplayName(String displayName) {
        set("displayName", displayName);
    }
}
