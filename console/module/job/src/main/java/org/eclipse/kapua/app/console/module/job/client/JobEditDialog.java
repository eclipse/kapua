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
package org.eclipse.kapua.app.console.module.job.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import org.eclipse.kapua.app.console.module.api.client.GwtKapuaErrorCode;
import org.eclipse.kapua.app.console.module.api.client.GwtKapuaException;
import org.eclipse.kapua.app.console.module.api.client.util.FailureHandler;
import org.eclipse.kapua.app.console.module.api.client.util.KapuaSafeHtmlUtils;
import org.eclipse.kapua.app.console.module.api.shared.model.session.GwtSession;
import org.eclipse.kapua.app.console.module.job.shared.model.GwtJob;
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
        submitButton.disable();
    }

    @Override
    public void submit() {
        selectedJob.setJobName(name.getValue());
        selectedJob.setDescription(KapuaSafeHtmlUtils.htmlUnescape(description.getValue()));

        gwtJobService.update(xsrfToken, selectedJob, new AsyncCallback<GwtJob>() {

            @Override
            public void onSuccess(GwtJob gwtJob) {
                exitStatus = true;
                exitMessage = JOB_MSGS.dialogEditConfirmation();
                hide();
            }

            @Override
            public void onFailure(Throwable cause) {
                exitStatus = false;
                status.hide();
                formPanel.getButtonBar().enable();
                unmask();
                submitButton.enable();
                cancelButton.enable();
                if (!isPermissionErrorMessage(cause)) {
                    if (cause instanceof GwtKapuaException) {
                        GwtKapuaException gwtCause = (GwtKapuaException) cause;
                        if (gwtCause.getCode().equals(GwtKapuaErrorCode.DUPLICATE_NAME)) {
                            name.markInvalid(gwtCause.getMessage());
                        }
                        FailureHandler.handleFormException(formPanel, cause);
                    }
                }
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
        description.setValue(gwtJob.getUnescapedDescription());
    }
}
