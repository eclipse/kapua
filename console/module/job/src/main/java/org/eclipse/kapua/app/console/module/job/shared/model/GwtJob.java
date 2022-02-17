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

import com.google.gwt.user.client.rpc.IsSerializable;
import org.eclipse.kapua.app.console.module.api.shared.model.GwtUpdatableEntityModel;

import java.util.List;

public class GwtJob extends GwtUpdatableEntityModel implements IsSerializable {

    private static final String DESCRIPTION = "description";

    public String getDescription() {
        return get(DESCRIPTION);
    }

    public String getUnescapedDescription() {
        return (String) getUnescaped(DESCRIPTION);
    }

    public void setDescription(String description) {
        set(DESCRIPTION, description);
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
}
