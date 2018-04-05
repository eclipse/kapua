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
package org.eclipse.kapua.app.console.module.job.client.targets;

import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.rpc.AsyncCallback;
import org.eclipse.kapua.app.console.module.api.client.resources.icons.IconSet;
import org.eclipse.kapua.app.console.module.api.client.resources.icons.KapuaIcon;
import org.eclipse.kapua.app.console.module.api.client.ui.button.Button;
import org.eclipse.kapua.app.console.module.api.client.ui.dialog.KapuaDialog;
import org.eclipse.kapua.app.console.module.api.client.ui.widget.EntityCRUDToolbar;
import org.eclipse.kapua.app.console.module.api.shared.model.session.GwtSession;
import org.eclipse.kapua.app.console.module.job.client.messages.ConsoleJobMessages;
import org.eclipse.kapua.app.console.module.job.shared.model.GwtJob;
import org.eclipse.kapua.app.console.module.job.shared.model.GwtJobTarget;
import org.eclipse.kapua.app.console.module.job.shared.service.GwtJobService;
import org.eclipse.kapua.app.console.module.job.shared.service.GwtJobServiceAsync;

public class JobTabTargetsToolbar extends EntityCRUDToolbar<GwtJobTarget> {

    private static final ConsoleJobMessages JOB_MSGS = GWT.create(ConsoleJobMessages.class);

    private static final GwtJobServiceAsync JOB_SERVICE = GWT.create(GwtJobService.class);

    private GwtJob gwtSelectedJob;

    private Button jobStartTargetButton;

    public JobTabTargetsToolbar(GwtSession currentSession) {
        super(currentSession, true);
    }

    public void setJob(GwtJob gwtSelectedJob) {
        this.gwtSelectedJob = gwtSelectedJob;

        checkButtons();
    }

    public GwtJob getJob() {
        return gwtSelectedJob;
    }

    @Override
    protected KapuaDialog getAddDialog() {
        return new JobTargetAddDialog(currentSession, gwtSelectedJob);
    }

    @Override
    protected KapuaDialog getDeleteDialog() {
        GwtJobTarget selectedJobTarget = gridSelectionModel.getSelectedItem();
        JobTargetDeleteDialog dialog = null;
        if (selectedJobTarget != null) {
            dialog = new JobTargetDeleteDialog(selectedJobTarget);
        }
        return dialog;
    }

    @Override
    protected void onRender(Element target, int index) {
        jobStartTargetButton = new Button(JOB_MSGS.jobStartTargetButton(), new KapuaIcon(IconSet.PLAY), new SelectionListener<ButtonEvent>() {

            @Override
            public void componentSelected(ButtonEvent buttonEvent) {
                JobTargetStartTargetDialog dialog = new JobTargetStartTargetDialog(gwtSelectedJob, gridSelectionModel.getSelectedItem());
                dialog.show();
            }
        });
        jobStartTargetButton.disable();
        addExtraButton(jobStartTargetButton);

        super.onRender(target, index);

        checkButtons();
    }

    @Override
    protected void updateButtonEnablement() {
        super.updateButtonEnablement();

        jobStartTargetButton.setEnabled(selectedEntity != null);
    }

    private void checkButtons() {
        if (gwtSelectedJob != null) {
            JOB_SERVICE.find(currentSession.getSelectedAccountId(), gwtSelectedJob.getId(), new AsyncCallback<GwtJob>() {

                @Override
                public void onFailure(Throwable caught) {

                }

                @Override
                public void onSuccess(GwtJob result) {
                    if (addEntityButton != null) {
                        addEntityButton.setEnabled(result.getJobXmlDefinition() == null);
                    }

                    if (deleteEntityButton != null) {
                        deleteEntityButton.setEnabled(gridSelectionModel != null && gridSelectionModel.getSelectedItem() != null && result.getJobXmlDefinition() == null);
                    }

                    if (jobStartTargetButton != null) {
                        jobStartTargetButton.setEnabled(gridSelectionModel != null && gridSelectionModel.getSelectedItem() != null);
                    }
                }
            });
        } else {
            if (addEntityButton != null) {
                addEntityButton.setEnabled(false);
            }
            if (deleteEntityButton != null) {
                deleteEntityButton.setEnabled(false);
            }
            if (jobStartTargetButton != null) {
                jobStartTargetButton.setEnabled(false);
            }
        }
    }
}
