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
package org.eclipse.kapua.app.console.module.job.client.steps;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.rpc.AsyncCallback;
import org.eclipse.kapua.app.console.module.api.client.ui.dialog.KapuaDialog;
import org.eclipse.kapua.app.console.module.api.client.ui.widget.EntityCRUDToolbar;
import org.eclipse.kapua.app.console.module.api.shared.model.session.GwtSession;
import org.eclipse.kapua.app.console.module.job.shared.model.GwtJob;
import org.eclipse.kapua.app.console.module.job.shared.model.GwtJobStep;
import org.eclipse.kapua.app.console.module.job.shared.model.permission.JobSessionPermission;
import org.eclipse.kapua.app.console.module.job.shared.service.GwtJobService;
import org.eclipse.kapua.app.console.module.job.shared.service.GwtJobServiceAsync;

public class JobTabStepsToolbar extends EntityCRUDToolbar<GwtJobStep> {

    private String jobId;
    private static final GwtJobServiceAsync JOB_SERVICE = GWT.create(GwtJobService.class);

    public JobTabStepsToolbar(GwtSession currentSession) {
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
    protected KapuaDialog getAddDialog() {
        return new JobStepAddDialog(currentSession, jobId);
    }

    @Override
    protected KapuaDialog getEditDialog() {
        GwtJobStep selectedJobStep = gridSelectionModel.getSelectedItem();
        JobStepEditDialog dialog = null;
        if (selectedJobStep != null) {
            dialog = new JobStepEditDialog(currentSession, selectedJobStep);
        }
        return dialog;
    }

    @Override
    protected KapuaDialog getDeleteDialog() {
        GwtJobStep selectedJobStep = gridSelectionModel.getSelectedItem();
        JobStepDeleteDialog dialog = null;
        if (selectedJobStep != null) {
            dialog = new JobStepDeleteDialog(selectedJobStep);
        }
        return dialog;
    }

    @Override
    protected void onRender(Element target, int index) {
        super.onRender(target, index);
        checkButtons();
    }

    private void checkButtons() {
        if (jobId != null) {
            JOB_SERVICE.find(currentSession.getSelectedAccountId(), jobId, new AsyncCallback<GwtJob>() {

                @Override
                public void onFailure(Throwable caught) {

                }

                @Override
                public void onSuccess(GwtJob result) {
                    if (addEntityButton != null) {
                        addEntityButton.setEnabled(result.getJobXmlDefinition() == null && currentSession.hasPermission(JobSessionPermission.write()));
                    }
                    if (editEntityButton != null) {
                        editEntityButton.setEnabled(gridSelectionModel != null && gridSelectionModel.getSelectedItem() != null && result.getJobXmlDefinition() == null);
                    }
                    if (deleteEntityButton != null) {
                        deleteEntityButton.setEnabled(gridSelectionModel != null && gridSelectionModel.getSelectedItem() != null && result.getJobXmlDefinition() == null);
                    }
                }
            });
        } else {
            if (addEntityButton != null) {
                addEntityButton.setEnabled(currentSession.hasPermission(JobSessionPermission.write()));
            }
            if (editEntityButton != null) {
                editEntityButton.setEnabled(false);
            }
            if (deleteEntityButton != null) {
                deleteEntityButton.setEnabled(false);
            }
        }
    }
}
