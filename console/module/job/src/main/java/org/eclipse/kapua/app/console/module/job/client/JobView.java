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
package org.eclipse.kapua.app.console.module.job.client;

import com.google.gwt.core.client.GWT;
import org.eclipse.kapua.app.console.module.api.client.ui.grid.EntityGrid;
import org.eclipse.kapua.app.console.module.api.client.ui.panel.EntityFilterPanel;
import org.eclipse.kapua.app.console.module.api.client.ui.view.AbstractEntityView;
import org.eclipse.kapua.app.console.module.api.shared.model.session.GwtSession;
import org.eclipse.kapua.app.console.module.job.client.messages.ConsoleJobMessages;
import org.eclipse.kapua.app.console.module.job.shared.model.GwtJob;

public class JobView extends AbstractEntityView<GwtJob> {

    private JobGrid jobGrid;

    public JobView(GwtSession currentSession) {
        super(currentSession);
    }

    private static final ConsoleJobMessages MSGS = GWT.create(ConsoleJobMessages.class);

    public static String getName() {
        return MSGS.mainMenuJobs();
    }

    @Override
    public EntityGrid<GwtJob> getEntityGrid(AbstractEntityView<GwtJob> entityView, GwtSession currentSession) {
        if (jobGrid == null ) {
            jobGrid = new JobGrid(entityView, currentSession);
        }

        return jobGrid;
    }

    @Override
    public EntityFilterPanel<GwtJob> getEntityFilterPanel(AbstractEntityView<GwtJob> entityView, GwtSession currentSession) {
        return new JobFilterPanel(this, currentSession);
    }

}
