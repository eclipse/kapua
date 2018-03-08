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
package org.eclipse.kapua.app.console.module.job.client.schedule;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Element;
import org.eclipse.kapua.app.console.module.api.client.resources.icons.IconSet;
import org.eclipse.kapua.app.console.module.api.client.resources.icons.KapuaIcon;
import org.eclipse.kapua.app.console.module.api.client.ui.tab.KapuaTabItem;
import org.eclipse.kapua.app.console.module.api.shared.model.session.GwtSession;
import org.eclipse.kapua.app.console.module.job.client.messages.ConsoleJobMessages;
import org.eclipse.kapua.app.console.module.job.shared.model.GwtJob;

public class JobTabSchedules extends KapuaTabItem<GwtJob> {

    private static final ConsoleJobMessages JOB_MSGS = GWT.create(ConsoleJobMessages.class);

    private final JobTabSchedulesGrid schedulesGrid;

    public JobTabSchedules(GwtSession currentSession) {
        super(currentSession, JOB_MSGS.gridJobTabSchedulesLabel(), new KapuaIcon(IconSet.CLOCK_O));
        schedulesGrid = new JobTabSchedulesGrid(null, currentSession);
        schedulesGrid.setRefreshOnRender(false);
        setEnabled(false);
    }

    @Override
    protected void onRender(Element parent, int index) {
        super.onRender(parent, index);
        setBorders(false);
        add(schedulesGrid);
    }

    @Override
    protected void doRefresh() {
        schedulesGrid.refresh();
        schedulesGrid.getToolbar().getAddEntityButton().setEnabled(selectedEntity != null);
        schedulesGrid.getToolbar().getRefreshEntityButton().setEnabled(selectedEntity != null);
    }

    @Override
    public void setEntity(GwtJob gwtJob) {
        super.setEntity(gwtJob);
        if (gwtJob != null) {
            setEnabled(true);
            schedulesGrid.setJobId(gwtJob.getId());
            schedulesGrid.getToolbar().setJobId(gwtJob.getId());
        } else {
            setEnabled(false);
            schedulesGrid.setJobId(null);
            schedulesGrid.getToolbar().setJobId(null);
        }
    }
}
