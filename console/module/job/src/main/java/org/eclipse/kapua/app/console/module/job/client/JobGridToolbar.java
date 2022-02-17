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

import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Element;
import org.eclipse.kapua.app.console.module.api.client.resources.icons.IconSet;
import org.eclipse.kapua.app.console.module.api.client.resources.icons.KapuaIcon;
import org.eclipse.kapua.app.console.module.api.client.ui.button.KapuaButton;
import org.eclipse.kapua.app.console.module.api.client.ui.dialog.KapuaDialog;
import org.eclipse.kapua.app.console.module.api.client.ui.widget.EntityCRUDToolbar;
import org.eclipse.kapua.app.console.module.api.shared.model.session.GwtSession;
import org.eclipse.kapua.app.console.module.job.client.messages.ConsoleJobMessages;
import org.eclipse.kapua.app.console.module.job.shared.model.GwtJob;
import org.eclipse.kapua.app.console.module.job.shared.model.permission.JobSessionPermission;

public class JobGridToolbar extends EntityCRUDToolbar<GwtJob> {

    private static final ConsoleJobMessages JOB_MSGS = GWT.create(ConsoleJobMessages.class);

    private KapuaButton startJobButton;
    private KapuaButton stopJobButton;
    private KapuaButton restartJobButton;
    private KapuaButton deleteForcedJobButton;

    public JobGridToolbar(GwtSession currentSession) {
        super(currentSession);
    }

    public KapuaButton getStartJobButton() {
        return startJobButton;
    }

    public KapuaButton getStopJobButton() {
        return stopJobButton;
    }

    public KapuaButton getRestartJobButton() {
        return restartJobButton;
    }

    public KapuaButton getDeleteForcedJobButton() {
        return deleteForcedJobButton;
    }

    @Override
    protected void onRender(Element target, int index) {

        startJobButton = new KapuaButton(JOB_MSGS.jobStartButton(), new KapuaIcon(IconSet.PLAY), new SelectionListener<ButtonEvent>() {

            @Override
            public void componentSelected(ButtonEvent buttonEvent) {
                JobStartDialog dialog = new JobStartDialog(gridSelectionModel.getSelectedItem());
                dialog.addListener(Events.Hide, getHideDialogListener());
                dialog.show();
            }
        });
        startJobButton.disable();
        addExtraButton(startJobButton);

        stopJobButton = new KapuaButton(JOB_MSGS.jobStopButton(), new KapuaIcon(IconSet.STOP), new SelectionListener<ButtonEvent>() {

            @Override
            public void componentSelected(ButtonEvent buttonEvent) {
                JobStopDialog dialog = new JobStopDialog(gridSelectionModel.getSelectedItem());
                dialog.addListener(Events.Hide, getHideDialogListener());
                dialog.show();
            }
        });
        stopJobButton.disable();
        addExtraButton(stopJobButton);

        restartJobButton = new KapuaButton(JOB_MSGS.jobRestartButton(), new KapuaIcon(IconSet.REPEAT), new SelectionListener<ButtonEvent>() {

            @Override
            public void componentSelected(ButtonEvent buttonEvent) {
                JobRestartDialog dialog = new JobRestartDialog(gridSelectionModel.getSelectedItem());
                dialog.addListener(Events.Hide, getHideDialogListener());
                dialog.show();
            }
        });
        restartJobButton.disable();
        addExtraButton(restartJobButton);

        deleteForcedJobButton = new KapuaButton(JOB_MSGS.jobDeleteForcedButton(), new KapuaIcon(IconSet.EXCLAMATION_TRIANGLE), new SelectionListener<ButtonEvent>() {

            @Override
            public void componentSelected(ButtonEvent buttonEvent) {
                JobDeleteForcedDialog dialog = new JobDeleteForcedDialog(gridSelectionModel.getSelectedItem());
                dialog.addListener(Events.Hide, getHideDialogListener());
                dialog.show();
            }
        });
        deleteForcedJobButton.disable();
        if (currentSession.hasPermission(JobSessionPermission.deleteAll())) {
            addExtraButton(deleteForcedJobButton);
        }

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
