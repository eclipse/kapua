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

import com.google.gwt.user.client.rpc.IsSerializable;
import org.eclipse.kapua.app.console.module.api.shared.model.GwtUpdatableEntityModel;

import java.util.List;

public class GwtJob extends GwtUpdatableEntityModel implements IsSerializable {

    public String getDescription() {
        return get("description");
    }

    public String getJobName() {
        return get("jobName");
    }

    public void setJobName(String jobName) {
        set("jobName", jobName);
    }

    public void setDescription(String description) {
        set("description", description);
    }

    public List<GwtJobStep> getJobSteps() {
        return get("jobSteps");
    }

    public void setJobSteps(List<GwtJobStep> jobSteps) {
        set("jobSteps", jobSteps);
    }

    public String getJobXmlDefinition() {
        return get("jobXmlDefinition");
    }

    public void setJobXmlDefinition(String xmlJobDefinition) {
        set("jobXmlDefinition", xmlJobDefinition);
    }

}
