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
package org.eclipse.kapua.app.console.module.job.client.schedule;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.rpc.AsyncCallback;

import org.eclipse.kapua.app.console.module.api.client.ui.dialog.KapuaDialog;
import org.eclipse.kapua.app.console.module.api.client.ui.widget.EntityCRUDToolbar;
import org.eclipse.kapua.app.console.module.api.shared.model.session.GwtSession;
import org.eclipse.kapua.app.console.module.job.shared.model.GwtJob;
import org.eclipse.kapua.app.console.module.job.shared.model.permission.JobSessionPermission;
import org.eclipse.kapua.app.console.module.job.shared.model.permission.SchedulerSessionPermission;
import org.eclipse.kapua.app.console.module.job.shared.model.scheduler.GwtTrigger;
import org.eclipse.kapua.app.console.module.job.shared.service.GwtJobService;
import org.eclipse.kapua.app.console.module.job.shared.service.GwtJobServiceAsync;

public class JobTabSchedulesToolbar extends EntityCRUDToolbar<GwtTrigger> {

    private String jobId;
    private static final GwtJobServiceAsync JOB_SERVICE = GWT.create(GwtJobService.class);

    public JobTabSchedulesToolbar(GwtSession currentSession) {
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
        super.onRender(target, index);
        checkButtons();
    }

    @Override
    protected KapuaDialog getAddDialog() {
        return new JobScheduleAddDialog(currentSession, jobId);
    }

    @Override
    protected KapuaDialog getDeleteDialog() {
        GwtTrigger selectedJobStep = gridSelectionModel.getSelectedItem();
        JobScheduleDeleteDialog dialog = null;
        if (selectedJobStep != null) {
            dialog = new JobScheduleDeleteDialog(selectedJobStep);
        }
        return dialog;
    }

    @Override
    protected void updateButtonEnablement() {
        super.updateButtonEnablement();
        addEntityButton.setEnabled(currentSession.hasPermission(JobSessionPermission.write()));
        addEntityButton.setEnabled(currentSession.hasPermission(SchedulerSessionPermission.write()));
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
                        addEntityButton.setEnabled(result.getJobXmlDefinition() == null && currentSession.hasPermission(SchedulerSessionPermission.write()));
                    }
                }
            });
        } else {
            if (addEntityButton != null) {
                addEntityButton.setEnabled(false);
            }
        }
    }
}
