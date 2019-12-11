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
package org.eclipse.kapua.app.console.module.job.client.execution;

import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Element;
import org.eclipse.kapua.app.console.module.api.client.resources.icons.IconSet;
import org.eclipse.kapua.app.console.module.api.client.resources.icons.KapuaIcon;
import org.eclipse.kapua.app.console.module.api.client.ui.button.Button;
import org.eclipse.kapua.app.console.module.api.client.ui.widget.EntityCRUDToolbar;
import org.eclipse.kapua.app.console.module.api.shared.model.session.GwtSession;
import org.eclipse.kapua.app.console.module.job.client.messages.ConsoleJobMessages;
import org.eclipse.kapua.app.console.module.job.shared.model.GwtJobExecution;

public class JobTabExecutionsToolbar extends EntityCRUDToolbar<GwtJobExecution> {

    private static final ConsoleJobMessages JOB_MSGS = GWT.create(ConsoleJobMessages.class);

    private String jobId;

    private Button stopJobButton;
    private Button logExecutionButton;

    public JobTabExecutionsToolbar(GwtSession currentSession) {
        super(currentSession, true);
    }

    public String getJobId() {
        return jobId;
    }

    public void setJobId(String jobId) {
        this.jobId = jobId;
        checkButtons();
    }

    @Override
    protected void onRender(Element target, int index) {

        stopJobButton = new Button(JOB_MSGS.jobStopButton(), new KapuaIcon(IconSet.STOP), new SelectionListener<ButtonEvent>() {

            @Override
            public void componentSelected(ButtonEvent buttonEvent) {
                JobExecutionStopDialog dialog = new JobExecutionStopDialog(gridSelectionModel.getSelectedItem());
                dialog.show();
            }
        });
        stopJobButton.disable();
        addExtraButton(stopJobButton);

        logExecutionButton = new JobExecutionLogButton(new SelectionListener<ButtonEvent>() {
            @Override
            public void componentSelected(ButtonEvent buttonEvent) {
                JobExecutionLogDialog logDialog = new JobExecutionLogDialog(gridSelectionModel.getSelectedItem());
                logDialog.show();
            }
        });
        addExtraButton(logExecutionButton);

        super.onRender(target, index);

        checkButtons();
    }

    @Override
    protected void updateButtonEnablement() {
        super.updateButtonEnablement();

        checkButtons();
    }

    private void checkButtons() {
        if (refreshEntityButton != null) {
            refreshEntityButton.setEnabled(gridSelectionModel != null && gridSelectionModel.getSelectedItem() != null);
        }

        if (stopJobButton != null) {
            stopJobButton.setEnabled(gridSelectionModel != null && gridSelectionModel.getSelectedItem() != null && gridSelectionModel.getSelectedItem().getEndedOn() == null);
        }

        if (logExecutionButton != null) {
            logExecutionButton.setEnabled(gridSelectionModel != null && gridSelectionModel.getSelectedItem() != null);
        }
    }

}
