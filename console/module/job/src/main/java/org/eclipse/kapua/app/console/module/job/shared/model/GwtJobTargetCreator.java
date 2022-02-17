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

import org.eclipse.kapua.app.console.module.api.shared.model.GwtEntityCreator;

public class GwtJobTargetCreator extends GwtEntityCreator {

    private String jobId;
    private String jobTargetId;
    private String clientId;


    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getJobId() {
        return jobId;
    }

    public void setJobId(String jobId) {
        this.jobId = jobId;
    }

    public String getJobTargetId() {
        return jobTargetId;
    }

    public void setJobTargetId(String jobTargetId) {
        this.jobTargetId = jobTargetId;
    }

}
