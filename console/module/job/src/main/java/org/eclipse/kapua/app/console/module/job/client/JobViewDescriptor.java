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
package org.eclipse.kapua.app.console.module.job.client;

import org.eclipse.kapua.app.console.module.api.client.resources.icons.IconSet;
import org.eclipse.kapua.app.console.module.api.client.ui.view.EntityView;
import org.eclipse.kapua.app.console.module.api.client.ui.view.descriptor.AbstractEntityViewDescriptor;
import org.eclipse.kapua.app.console.module.api.shared.model.session.GwtSession;
import org.eclipse.kapua.app.console.module.job.shared.model.permission.JobSessionPermission;

public class JobViewDescriptor extends AbstractEntityViewDescriptor {

    @Override
    public String getName() {
        return JobView.getName();
    }

    @Override
    public IconSet getIcon() {
        return IconSet.SORT_AMOUNT_ASC;
    }

    @Override
    public EntityView getViewInstance(GwtSession currentSession) {
        return new JobView(currentSession);
    }

    @Override
    public String getViewId() {
        return "job";
    }

    @Override
    public Integer getOrder() {
        return 400;
    }

    @Override
    public Boolean isEnabled(GwtSession currentSession) {
        return currentSession.hasPermission(JobSessionPermission.read());
    }
}
