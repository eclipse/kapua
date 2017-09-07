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
package org.eclipse.kapua.app.console.client.job.targets;

import com.google.gwt.user.client.Element;
import org.eclipse.kapua.app.console.client.ui.dialog.KapuaDialog;
import org.eclipse.kapua.app.console.client.ui.widget.EntityCRUDToolbar;
import org.eclipse.kapua.app.console.shared.model.GwtJobTarget;
import org.eclipse.kapua.app.console.shared.model.GwtSession;

public class JobTabTargetsToolbar extends EntityCRUDToolbar<GwtJobTarget> {

    private String jobId;

    public JobTabTargetsToolbar(GwtSession currentSession) {
        super(currentSession);
    }

    public void setJobId(String jobId) {
        this.jobId = jobId;
    }

    public String getJobId() {
        return jobId;
    }

    @Override
    protected KapuaDialog getAddDialog() {
        return new JobTargetAddDialog(currentSession, jobId);
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
        super.onRender(target, index);
        refreshEntityButton.setEnabled(gridSelectionModel != null && gridSelectionModel.getSelectedItem() != null);
    }
}
