/*******************************************************************************
 * Copyright (c) 2017, 2019 Eurotech and/or its affiliates and others
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

import org.eclipse.kapua.app.console.module.api.client.resources.icons.IconSet;
import org.eclipse.kapua.app.console.module.api.client.resources.icons.KapuaIcon;
import org.eclipse.kapua.app.console.module.api.client.ui.button.Button;
import org.eclipse.kapua.app.console.module.api.client.ui.dialog.KapuaDialog;
import org.eclipse.kapua.app.console.module.api.client.ui.widget.EntityCRUDToolbar;
import org.eclipse.kapua.app.console.module.api.shared.model.session.GwtSession;
import org.eclipse.kapua.app.console.module.job.client.messages.ConsoleJobMessages;
import org.eclipse.kapua.app.console.module.job.shared.model.GwtJob;
import org.eclipse.kapua.app.console.module.job.shared.model.GwtJobTarget;
import org.eclipse.kapua.app.console.module.job.shared.model.permission.JobSessionPermission;
import org.eclipse.kapua.app.console.module.job.shared.service.GwtJobService;
import org.eclipse.kapua.app.console.module.job.shared.service.GwtJobServiceAsync;

import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.google.gwt.core.client.GWT;
import com.google.gwt.http.client.URL;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class JobTabTargetsToolbar extends EntityCRUDToolbar<GwtJobTarget> {

    private static final ConsoleJobMessages JOB_MSGS = GWT.create(ConsoleJobMessages.class);

    private static final GwtJobServiceAsync JOB_SERVICE = GWT.create(GwtJobService.class);

    private GwtJob gwtSelectedJob;

    private Button jobStartTargetButton;
    private Button exportButton;

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

        exportButton = new Button(JOB_MSGS.exportToCSV(), new KapuaIcon(IconSet.FILE_TEXT_O),
                new SelectionListener<ButtonEvent>() {

                    @Override
                    public void componentSelected(ButtonEvent be) {
                        export();
                    }
                });
        exportButton.disable();
        addExtraButton(exportButton);
        super.onRender(target, index);

        checkButtons();
    }

    private void export() {
        StringBuilder sbUrl = new StringBuilder("exporter_job_target?format=")
                .append("csv")
                .append("&scopeId=")
                .append(URL.encodeQueryString(currentSession.getSelectedAccountId()));

            sbUrl.append("&jobId=")
                    .append(gwtSelectedJob.getId());

        Window.open(sbUrl.toString(), "_blank", "location=no");
    }

    @Override
    protected void updateButtonEnablement() {
        super.updateButtonEnablement();

        jobStartTargetButton.setEnabled(selectedEntity != null && currentSession.hasPermission(JobSessionPermission.execute()));
        addEntityButton.setEnabled(selectedEntity != null && currentSession.hasPermission(JobSessionPermission.write()));
        deleteEntityButton
                .setEnabled(selectedEntity != null && currentSession.hasPermission(JobSessionPermission.delete())
                        && currentSession.hasPermission(JobSessionPermission.write()));
        deleteEntityButton.setText(JOB_MSGS.tabTargetsDeleteButton());
        exportButton.setEnabled(gwtSelectedJob != null);
    }

    private void checkButtons() {
        if (gwtSelectedJob != null) {
            if (exportButton != null) {
                exportButton.setEnabled(true);
            }
            JOB_SERVICE.find(currentSession.getSelectedAccountId(), gwtSelectedJob.getId(), new AsyncCallback<GwtJob>() {

                @Override
                public void onFailure(Throwable caught) {

                }

                @Override
                public void onSuccess(GwtJob result) {
                    if (addEntityButton != null) {
                        addEntityButton.setEnabled(result.getJobXmlDefinition() == null && currentSession.hasPermission(JobSessionPermission.write()));
                    }

                    if (deleteEntityButton != null) {
                        deleteEntityButton.setEnabled(
                                gridSelectionModel != null && gridSelectionModel.getSelectedItem() != null
                                        && result.getJobXmlDefinition() == null
                                        && currentSession.hasPermission(JobSessionPermission.delete())
                                        && currentSession.hasPermission(JobSessionPermission.write()));
                    }

                    if (jobStartTargetButton != null) {
                        jobStartTargetButton.setEnabled(gridSelectionModel != null && gridSelectionModel.getSelectedItem() != null && currentSession.hasPermission(JobSessionPermission.execute()));
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
            if (exportButton != null) {
                exportButton.setEnabled(false);
            }
        }
    }

}
