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
package org.eclipse.kapua.app.console.client.job.schedule;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.rpc.AsyncCallback;
import org.eclipse.kapua.app.console.client.ui.dialog.KapuaDialog;
import org.eclipse.kapua.app.console.client.ui.widget.EntityCRUDToolbar;
import org.eclipse.kapua.app.console.shared.model.GwtSession;
import org.eclipse.kapua.app.console.shared.model.job.GwtTrigger;
import org.eclipse.kapua.app.console.shared.model.job.GwtJob;
import org.eclipse.kapua.app.console.shared.service.GwtJobService;
import org.eclipse.kapua.app.console.shared.service.GwtJobServiceAsync;

public class JobTabSchedulesToolbar extends EntityCRUDToolbar<GwtTrigger> {

    private static final GwtJobServiceAsync JOB_SERVICE = GWT.create(GwtJobService.class);

    private String jobId;

    public JobTabSchedulesToolbar(GwtSession currentSession) {
        super(currentSession);
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
            JOB_SERVICE.find(currentSession.getSelectedAccount().getId(), jobId, new AsyncCallback<GwtJob>() {

                @Override
                public void onFailure(Throwable caught) {

                }

                @Override
                public void onSuccess(GwtJob result) {
                    addEntityButton.setEnabled(jobId != null && result.getJobXmlDefinition() == null);
                }
            });
        } else {
            addEntityButton.setEnabled(false);
        }
        editEntityButton.setEnabled(gridSelectionModel != null && gridSelectionModel.getSelectedItem() != null);
        deleteEntityButton.setEnabled(gridSelectionModel != null && gridSelectionModel.getSelectedItem() != null);
        refreshEntityButton.setEnabled(gridSelectionModel != null && gridSelectionModel.getSelectedItem() != null);
    }

    @Override
    protected KapuaDialog getAddDialog() {
        return new JobScheduleAddDialog(currentSession, jobId);
    }

    @Override
    protected KapuaDialog getEditDialog() {
        GwtTrigger selectedItem = gridSelectionModel.getSelectedItem();
        JobScheduleEditDialog dialog = null;
        if (selectedItem != null) {
            dialog = new JobScheduleEditDialog(currentSession, selectedItem);
        }
        return dialog;
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
}
