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

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import org.eclipse.kapua.app.console.module.api.client.ui.dialog.entity.EntityDeleteDialog;
import org.eclipse.kapua.app.console.module.api.client.util.DialogUtils;
import org.eclipse.kapua.app.console.module.job.client.messages.ConsoleJobMessages;
import org.eclipse.kapua.app.console.module.job.shared.model.job.GwtTrigger;
import org.eclipse.kapua.app.console.module.job.shared.service.GwtTriggerService;
import org.eclipse.kapua.app.console.module.job.shared.service.GwtTriggerServiceAsync;

public class JobScheduleDeleteDialog extends EntityDeleteDialog {

    private final GwtTrigger selectedTrigger;
    private static final GwtTriggerServiceAsync TRIGGER_SERVICE = GWT.create(GwtTriggerService.class);
    private static final ConsoleJobMessages JOB_MSGS = GWT.create(ConsoleJobMessages.class);

    public JobScheduleDeleteDialog(GwtTrigger selectedTrigger) {
        this.selectedTrigger = selectedTrigger;
        DialogUtils.resizeDialog(this, 300, 135);
    }

    @Override
    public void submit() {
        TRIGGER_SERVICE.delete(xsrfToken, selectedTrigger.getScopeId(), selectedTrigger.getId(), new AsyncCallback<Void>() {

            @Override
            public void onSuccess(Void arg0) {
                exitStatus = true;
                exitMessage = JOB_MSGS.dialogDeleteScheduleConfirmation();
                hide();
            }

            @Override
            public void onFailure(Throwable cause) {
                exitStatus = false;
                exitMessage = JOB_MSGS.dialogDeleteScheduleError(cause.getLocalizedMessage());
                hide();
            }
        });
    }

    @Override
    public String getHeaderMessage() {
        return JOB_MSGS.dialogDeleteScheduleHeader(selectedTrigger.getTriggerName());
    }

    @Override
    public String getInfoMessage() {
        return JOB_MSGS.dialogDeleteScheduleInfo();
    }

}
