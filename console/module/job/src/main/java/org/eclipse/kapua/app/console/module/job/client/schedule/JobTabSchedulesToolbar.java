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
package org.eclipse.kapua.app.console.module.job.client.schedule;

import com.google.gwt.user.client.Element;
import org.eclipse.kapua.app.console.module.api.client.ui.dialog.KapuaDialog;
import org.eclipse.kapua.app.console.module.api.client.ui.widget.EntityCRUDToolbar;
import org.eclipse.kapua.app.console.module.api.shared.model.session.GwtSession;
import org.eclipse.kapua.app.console.module.job.shared.model.permission.SchedulerSessionPermission;
import org.eclipse.kapua.app.console.module.job.shared.model.scheduler.GwtTrigger;

public class JobTabSchedulesToolbar extends EntityCRUDToolbar<GwtTrigger> {

    private String jobId;

    public JobTabSchedulesToolbar(GwtSession currentSession) {
        super(currentSession, true);
    }

    public String getJobId() {
        return jobId;
    }

    public void setJobId(String jobId) {
        this.jobId = jobId;
    }

    @Override
    protected void onRender(Element target, int index) {
        super.onRender(target, index);
        if (jobId != null) {
            addEntityButton.setEnabled(currentSession.hasPermission(SchedulerSessionPermission.write()));
            refreshEntityButton.setEnabled(true);
        } else {
            addEntityButton.setEnabled(false);
            refreshEntityButton.setEnabled(false);
        }
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
        addEntityButton.setEnabled(jobId != null && currentSession.hasPermission(SchedulerSessionPermission.write()));
        deleteEntityButton.setEnabled(
                selectedEntity != null && currentSession.hasPermission(SchedulerSessionPermission.delete()));
    }
}
