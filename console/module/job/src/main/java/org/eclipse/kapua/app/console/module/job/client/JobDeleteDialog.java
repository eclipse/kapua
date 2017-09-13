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
package org.eclipse.kapua.app.console.module.job.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import org.eclipse.kapua.app.console.module.api.client.ui.dialog.entity.EntityDeleteDialog;
import org.eclipse.kapua.app.console.module.api.client.util.DialogUtils;
import org.eclipse.kapua.app.console.module.job.client.messages.ConsoleJobMessages;
import org.eclipse.kapua.app.console.module.job.shared.model.job.GwtJob;
import org.eclipse.kapua.app.console.module.job.shared.service.GwtJobService;
import org.eclipse.kapua.app.console.module.job.shared.service.GwtJobServiceAsync;

public class JobDeleteDialog extends EntityDeleteDialog {

    private final GwtJob selectedJob;

    private static final ConsoleJobMessages JOB_MSGS = GWT.create(ConsoleJobMessages.class);
    private static final GwtJobServiceAsync GWT_JOB_SERVICE = GWT.create(GwtJobService.class);

    public JobDeleteDialog(GwtJob selectedJob) {
        this.selectedJob = selectedJob;
        DialogUtils.resizeDialog(this, 300, 135);
    }

    @Override
    public void submit() {
        GWT_JOB_SERVICE.delete(xsrfToken, selectedJob.getScopeId(), selectedJob.getId(), new AsyncCallback<Void>() {

            @Override
            public void onSuccess(Void arg0) {
                exitStatus = true;
                exitMessage = JOB_MSGS.dialogDeleteConfirmation();
                hide();
            }

            @Override
            public void onFailure(Throwable cause) {
                exitStatus = false;
                exitMessage = JOB_MSGS.dialogDeleteError(cause.getLocalizedMessage());
                hide();
            }
        });
    }

    @Override
    public String getHeaderMessage() {
        return JOB_MSGS.dialogDeleteHeader(selectedJob.getJobName());
    }

    @Override
    public String getInfoMessage() {
        return JOB_MSGS.dialogDeleteInfo();
    }
}
