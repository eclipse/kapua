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
package org.eclipse.kapua.app.console.client.job;

import org.eclipse.kapua.app.console.client.job.execution.JobTabExecutions;
import org.eclipse.kapua.app.console.client.job.schedule.JobTabSchedules;
import org.eclipse.kapua.app.console.client.job.steps.JobTabSteps;
import org.eclipse.kapua.app.console.client.job.targets.JobTabTargets;
import org.eclipse.kapua.app.console.client.ui.grid.EntityGrid;
import org.eclipse.kapua.app.console.client.ui.panel.EntityFilterPanel;
import org.eclipse.kapua.app.console.client.ui.tab.KapuaTabItem;
import org.eclipse.kapua.app.console.client.ui.view.EntityView;
import org.eclipse.kapua.app.console.shared.model.GwtSession;
import org.eclipse.kapua.app.console.shared.model.job.GwtJob;

import java.util.ArrayList;
import java.util.List;

public class JobView extends EntityView<GwtJob> {

    private JobTabTargets tabTargets;
    private JobTabSteps tabSteps;
    private JobTabSchedules tabSchedules;
    private JobTabExecutions tabExecutions;

    public JobView(GwtSession currentSession) {
        super(currentSession);
    }

    @Override
    public List<KapuaTabItem<GwtJob>> getTabs(EntityView<GwtJob> entityView, GwtSession currentSession) {
        List<KapuaTabItem<GwtJob>> tabs = new ArrayList<KapuaTabItem<GwtJob>>();
        if (tabSteps == null) {
            tabSteps = new JobTabSteps(currentSession);
            tabs.add(tabSteps);
        }
        if (tabTargets == null) {
            tabTargets = new JobTabTargets(currentSession);
            tabs.add(tabTargets);
        }
        if (tabSchedules == null) {
            tabSchedules = new JobTabSchedules(currentSession);
            tabs.add(tabSchedules);
        }
        if (tabExecutions == null) {
            tabExecutions = new JobTabExecutions(currentSession);
            tabs.add(tabExecutions);
        }
        return tabs;
    }

    @Override
    public EntityGrid<GwtJob> getEntityGrid(EntityView<GwtJob> entityView, GwtSession currentSession) {
        return new JobGrid(entityView, currentSession);
    }

    @Override
    public EntityFilterPanel<GwtJob> getEntityFilterPanel(EntityView<GwtJob> entityView, GwtSession currentSession2) {
        return null;
    }

}
