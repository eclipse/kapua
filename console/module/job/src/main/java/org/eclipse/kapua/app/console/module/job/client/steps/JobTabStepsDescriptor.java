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
package org.eclipse.kapua.app.console.module.job.client.steps;

import org.eclipse.kapua.app.console.module.api.client.ui.view.descriptor.AbstractEntityTabDescriptor;
import org.eclipse.kapua.app.console.module.api.shared.model.GwtSession;
import org.eclipse.kapua.app.console.module.job.client.JobView;
import org.eclipse.kapua.app.console.module.job.shared.model.job.GwtJob;

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
        return 200;
    }

    @Override
    public Boolean isEnabled(GwtSession currentSession) {
        return true;
    }
}
