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
package org.eclipse.kapua.app.console.module.job.client.steps;

import org.eclipse.kapua.app.console.module.api.client.ui.view.descriptor.AbstractEntityTabDescriptor;
import org.eclipse.kapua.app.console.module.api.shared.model.session.GwtSession;
import org.eclipse.kapua.app.console.module.job.client.JobView;
import org.eclipse.kapua.app.console.module.job.shared.model.GwtJob;
import org.eclipse.kapua.app.console.module.job.shared.model.permission.JobSessionPermission;

public class JobTabStepsDescriptor extends AbstractEntityTabDescriptor<GwtJob, JobTabSteps, JobView> {

    @Override
    public JobTabSteps getTabViewInstance(JobView view, GwtSession currentSession) {
        return new JobTabSteps(currentSession);
    }

    @Override
    public String getViewId() {
        return "job.targets";
    }

    @Override
    public Integer getOrder() {
        return 300;
    }

    @Override
    public Boolean isEnabled(GwtSession currentSession) {
        return currentSession.hasPermission(JobSessionPermission.read());
    }
}
