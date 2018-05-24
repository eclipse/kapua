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
import org.eclipse.kapua.app.console.module.api.shared.model.GwtUpdatableEntityModel;

import java.util.List;

public class GwtJob extends GwtUpdatableEntityModel implements IsSerializable {

    public String getDescription() {
        return get("description");
    }

    public String getUnescapedDescription() {
        return (String) getUnescaped("description");
    }

    public void setDescription(String description) {
        set("description", description);
    }

    public String getJobName() {
        return get("jobName");
    }

    public void setJobName(String jobName) {
        set("jobName", jobName);
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

    public String getUserName() {
        return get("userName");
    }

    public void setUserName(String userName) {
        set("userName", userName);
    }

}
