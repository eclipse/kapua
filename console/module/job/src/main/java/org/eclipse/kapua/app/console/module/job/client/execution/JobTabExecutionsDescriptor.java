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
package org.eclipse.kapua.app.console.module.job.client.execution;

import org.eclipse.kapua.app.console.module.api.client.ui.view.descriptor.AbstractEntityTabDescriptor;
import org.eclipse.kapua.app.console.module.api.shared.model.session.GwtSession;
import org.eclipse.kapua.app.console.module.job.client.JobView;
import org.eclipse.kapua.app.console.module.job.shared.model.GwtJob;
import org.eclipse.kapua.app.console.module.job.shared.model.permission.JobSessionPermission;

public class JobTabExecutionsDescriptor extends AbstractEntityTabDescriptor<GwtJob, JobTabExecutions, JobView> {

    @Override
    public JobTabExecutions getTabViewInstance(JobView view, GwtSession currentSession) {
        return new JobTabExecutions(currentSession);
    }

    @Override
    public String getViewId() {
        return "job.executions";
    }

    @Override
    public Integer getOrder() {
        return 500;
    }

    @Override
    public Boolean isEnabled(GwtSession currentSession) {
        return currentSession.hasPermission(JobSessionPermission.read());
    }
}
