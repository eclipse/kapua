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
package org.eclipse.kapua.app.console.module.job.client.schedule;

import com.google.gwt.user.client.Element;
import org.eclipse.kapua.app.console.module.api.client.ui.dialog.KapuaDialog;
import org.eclipse.kapua.app.console.module.api.client.ui.widget.EntityCRUDToolbar;
import org.eclipse.kapua.app.console.module.api.shared.model.GwtSession;
import org.eclipse.kapua.app.console.module.job.shared.model.job.GwtTrigger;

public class JobTabSchedulesToolbar extends EntityCRUDToolbar<GwtTrigger> {

    private String jobId;

    public JobTabSchedulesToolbar(GwtSession currentSession) {
        super(currentSession);
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

    private void checkButtons() {
        if (addEntityButton != null) {
            addEntityButton.setEnabled(jobId != null);
        }
        if (editEntityButton != null) {
            editEntityButton.setEnabled(gridSelectionModel != null && gridSelectionModel.getSelectedItem() != null);
        }
        if (deleteEntityButton != null) {
            deleteEntityButton.setEnabled(gridSelectionModel != null && gridSelectionModel.getSelectedItem() != null);
        }
    }
}
