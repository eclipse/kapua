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
import org.eclipse.kapua.app.console.module.api.client.resources.icons.IconSet;
import org.eclipse.kapua.app.console.module.api.client.resources.icons.KapuaIcon;
import org.eclipse.kapua.app.console.module.api.client.ui.dialog.entity.EntityDeleteDialog;
import org.eclipse.kapua.app.console.module.api.client.util.DialogUtils;
import org.eclipse.kapua.app.console.module.job.client.messages.ConsoleJobMessages;
import org.eclipse.kapua.app.console.module.job.shared.model.GwtJob;
import org.eclipse.kapua.app.console.module.job.shared.service.GwtJobService;
import org.eclipse.kapua.app.console.module.job.shared.service.GwtJobServiceAsync;

public class JobDeleteForcedDialog extends EntityDeleteDialog {

    private final GwtJob selectedJob;

    private static final ConsoleJobMessages JOB_MSGS = GWT.create(ConsoleJobMessages.class);
    private static final GwtJobServiceAsync GWT_JOB_SERVICE = GWT.create(GwtJobService.class);

    public JobDeleteForcedDialog(GwtJob selectedJob) {
        this.selectedJob = selectedJob;
        DialogUtils.resizeDialog(this, 300, 135);
        setDisabledFormPanelEvents(true);
    }

    @Override
    public void submit() {
        GWT_JOB_SERVICE.deleteForced(xsrfToken, selectedJob.getScopeId(), selectedJob.getId(), new AsyncCallback<Void>() {

            @Override
            public void onSuccess(Void arg0) {
                exitStatus = true;
                exitMessage = JOB_MSGS.dialogDeleteForcedStoppedMessage();
                hide();
            }

            @Override
            public void onFailure(Throwable cause) {
                exitStatus = false;
                if (!isPermissionErrorMessage(cause)) {
                    exitMessage = JOB_MSGS.dialogDeleteForcedErrorMessage();
                }
                hide();
            }
        });
    }

    @Override
    public String getHeaderMessage() {
        return JOB_MSGS.dialogDeleteForcedDialogHeader(selectedJob.getJobName());
    }

    @Override
    public String getInfoMessage() {
        return JOB_MSGS.dialogDeleteForcedDialogInfo();
    }

    @Override
    public KapuaIcon getInfoIcon() {
        return new KapuaIcon(IconSet.EXCLAMATION_TRIANGLE);
    }
}
