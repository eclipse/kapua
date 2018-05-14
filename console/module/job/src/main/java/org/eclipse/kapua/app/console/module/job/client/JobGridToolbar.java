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

import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Element;
import org.eclipse.kapua.app.console.module.api.client.resources.icons.IconSet;
import org.eclipse.kapua.app.console.module.api.client.resources.icons.KapuaIcon;
import org.eclipse.kapua.app.console.module.api.client.ui.button.Button;
import org.eclipse.kapua.app.console.module.api.client.ui.dialog.KapuaDialog;
import org.eclipse.kapua.app.console.module.api.client.ui.widget.EntityCRUDToolbar;
import org.eclipse.kapua.app.console.module.api.shared.model.session.GwtSession;
import org.eclipse.kapua.app.console.module.job.client.messages.ConsoleJobMessages;
import org.eclipse.kapua.app.console.module.job.shared.model.GwtJob;
import org.eclipse.kapua.app.console.module.job.shared.model.permission.JobSessionPermission;

public class JobGridToolbar extends EntityCRUDToolbar<GwtJob> {

    private static final ConsoleJobMessages JOB_MSGS = GWT.create(ConsoleJobMessages.class);

    private Button startJobButton;
    private Button stopJobButton;

    public JobGridToolbar(GwtSession currentSession) {
        super(currentSession);
    }

    public Button getStartJobButton() {
        return startJobButton;
    }

    public Button getStopJobButton() {
        return stopJobButton;
    }

    @Override
    protected void onRender(Element target, int index) {

        startJobButton = new Button(JOB_MSGS.jobStartButton(), new KapuaIcon(IconSet.PLAY), new SelectionListener<ButtonEvent>() {

            @Override
            public void componentSelected(ButtonEvent buttonEvent) {
                JobStartDialog dialog = new JobStartDialog(gridSelectionModel.getSelectedItem());
                dialog.show();
            }
        });
        startJobButton.disable();
        addExtraButton(startJobButton);

        stopJobButton = new Button(JOB_MSGS.jobStopButton(), new KapuaIcon(IconSet.STOP), new SelectionListener<ButtonEvent>() {

            @Override
            public void componentSelected(ButtonEvent buttonEvent) {
                JobStopDialog dialog = new JobStopDialog(gridSelectionModel.getSelectedItem());
                dialog.show();
            }
        });
        stopJobButton.disable();
        addExtraButton(stopJobButton);

        super.onRender(target, index);

        getEditEntityButton().disable();
        getDeleteEntityButton().disable();
        getAddEntityButton().setEnabled(currentSession.hasPermission(JobSessionPermission.write()));
    }

    @Override
    protected KapuaDialog getAddDialog() {
        return new JobAddDialog(currentSession);
    }

    @Override
    protected KapuaDialog getEditDialog() {
        GwtJob selectedJob = gridSelectionModel.getSelectedItem();
        JobEditDialog dialog = null;
        if (selectedJob != null) {
            dialog = new JobEditDialog(currentSession, selectedJob);
        }
        return dialog;
    }

    @Override
    protected KapuaDialog getDeleteDialog() {
        JobDeleteDialog dialog = null;
        if (selectedEntity != null) {
            dialog = new JobDeleteDialog(selectedEntity);
        }
        return dialog;
    }
}
