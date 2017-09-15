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

import org.eclipse.kapua.app.console.module.api.shared.model.GwtEntityCreator;

public class GwtJobTargetCreator extends GwtEntityCreator {

    private String jobId;
    private String jobTargetId;

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
