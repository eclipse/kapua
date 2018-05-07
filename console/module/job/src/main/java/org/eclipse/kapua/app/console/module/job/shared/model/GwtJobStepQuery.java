/*******************************************************************************
 * Copyright (c) 2017, 2018 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.app.console.module.job.shared.model;

import com.google.gwt.user.client.rpc.IsSerializable;
import org.eclipse.kapua.app.console.module.api.shared.model.query.GwtQuery;

public class GwtJobStepQuery extends GwtQuery {

    public enum GwtSortOrder implements IsSerializable {
        ASCENDING, DESCENDING
    }

    public enum GwtSortAttribute implements IsSerializable {
        STEP_INDEX, JOB_STEP_NAME;
    }

    private String jobId;
    private GwtSortOrder sortOrder;
    private GwtSortAttribute sortAttribute;

    public String getJobId() {
        return jobId;
    }

    public void setJobId(String jobId) {
        this.jobId = jobId;
    }

    public GwtSortOrder getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(GwtSortOrder sortOrder) {
        this.sortOrder = sortOrder;
    }

    public GwtSortAttribute getSortAttribute() {
        return sortAttribute;
    }

    public void setSortAttribute(GwtSortAttribute sortAttribute) {
        this.sortAttribute = sortAttribute;
    }

}
