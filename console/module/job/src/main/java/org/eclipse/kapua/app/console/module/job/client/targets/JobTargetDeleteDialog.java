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
package org.eclipse.kapua.app.console.module.job.client.targets;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import org.eclipse.kapua.app.console.module.api.client.ui.dialog.entity.EntityDeleteDialog;
import org.eclipse.kapua.app.console.module.api.client.util.DialogUtils;
import org.eclipse.kapua.app.console.module.job.client.messages.ConsoleJobMessages;
import org.eclipse.kapua.app.console.module.job.shared.model.job.GwtJobTarget;
import org.eclipse.kapua.app.console.module.job.shared.service.GwtJobTargetService;
import org.eclipse.kapua.app.console.module.job.shared.service.GwtJobTargetServiceAsync;

public class JobTargetDeleteDialog extends EntityDeleteDialog {

    private static final ConsoleJobMessages JOB_MSGS = GWT.create(ConsoleJobMessages.class);
    private static final GwtJobTargetServiceAsync GWT_JOB_TARGET_SERVICE = GWT.create(GwtJobTargetService.class);
    private GwtJobTarget gwtJobTarget;

    public JobTargetDeleteDialog(GwtJobTarget gwtJobTarget) {
        this.gwtJobTarget = gwtJobTarget;

        DialogUtils.resizeDialog(this, 300, 135);
    }

    @Override
    public String getHeaderMessage() {
        return JOB_MSGS.dialogDeleteTargetHeader(gwtJobTarget.getJobTargetId());
    }

    @Override
    public String getInfoMessage() {
        return JOB_MSGS.dialogDeleteTargetInfo();
    }

    @Override
    public void submit() {
        GWT_JOB_TARGET_SERVICE.delete(xsrfToken, gwtJobTarget.getScopeId(), gwtJobTarget.getId(), new AsyncCallback<Void>() {

            @Override
            public void onSuccess(Void arg0) {
                exitStatus = true;
                exitMessage = JOB_MSGS.dialogDeleteTargetConfirmation();
                hide();
            }

            @Override
            public void onFailure(Throwable cause) {
                exitStatus = false;
                exitMessage = JOB_MSGS.dialogDeleteTargetError(cause.getLocalizedMessage());
                hide();
            }
        });

    }
}
