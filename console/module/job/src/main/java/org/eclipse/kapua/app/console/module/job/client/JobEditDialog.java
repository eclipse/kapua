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
import org.eclipse.kapua.app.console.module.api.shared.model.GwtSession;
import org.eclipse.kapua.app.console.module.job.shared.model.job.GwtJob;
import org.eclipse.kapua.app.console.module.job.shared.service.GwtJobService;
import org.eclipse.kapua.app.console.module.job.shared.service.GwtJobServiceAsync;

public class JobEditDialog extends JobAddDialog {

    private GwtJob selectedJob;

    private GwtJobServiceAsync gwtJobService = GWT.create(GwtJobService.class);

    public JobEditDialog(GwtSession currentSession, GwtJob selectedJob) {
        super(currentSession);
        this.selectedJob = selectedJob;
    }

    @Override
    public void createBody() {
        super.createBody();
        populateEditDialog(selectedJob);
    }

    @Override
    public void submit() {
        selectedJob.setJobName(name.getValue());
        selectedJob.setDescription(description.getValue());

        gwtJobService.update(xsrfToken, selectedJob, new AsyncCallback<GwtJob>() {

            @Override
            public void onSuccess(GwtJob arg0) {
                exitStatus = true;
                exitMessage = JOB_MSGS.dialogEditConfirmation();
                hide();
            }

            @Override
            public void onFailure(Throwable cause) {
                exitStatus = false;
                exitMessage = JOB_MSGS.dialogEditError(cause.getLocalizedMessage());
                hide();
            }
        });

    }

    @Override
    public String getHeaderMessage() {
        return JOB_MSGS.dialogEditHeader(selectedJob.getJobName());
    }

    @Override
    public String getInfoMessage() {
        return JOB_MSGS.dialogEditInfo();
    }

    private void populateEditDialog(GwtJob gwtJob) {
        name.setValue(gwtJob.getJobName());
        description.setValue(gwtJob.getDescription());
    }
}
