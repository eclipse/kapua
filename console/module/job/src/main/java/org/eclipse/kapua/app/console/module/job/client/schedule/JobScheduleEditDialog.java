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
import org.eclipse.kapua.app.console.module.api.shared.model.GwtSession;
import org.eclipse.kapua.app.console.module.job.shared.model.job.GwtTrigger;
import org.eclipse.kapua.app.console.module.job.shared.service.GwtTriggerService;
import org.eclipse.kapua.app.console.module.job.shared.service.GwtTriggerServiceAsync;

public class JobScheduleEditDialog extends JobScheduleAddDialog {

    private final GwtTrigger selectedGwtTrigger;
    private static final GwtTriggerServiceAsync TRIGGER_SERVICE = GWT.create(GwtTriggerService.class);

    public JobScheduleEditDialog(GwtSession currentSession, GwtTrigger selectedGwtTrigger) {
        super(currentSession, selectedGwtTrigger.getJobId());
        this.selectedGwtTrigger = selectedGwtTrigger;
    }

    @Override
    public void createBody() {
        super.createBody();

        populateEditDialog(selectedGwtTrigger);
    }

    private void populateEditDialog(GwtTrigger selectedGwtTrigger) {
        triggerName.setValue(selectedGwtTrigger.getTriggerName());
        startsOn.setValue(selectedGwtTrigger.getStartsOn());
        endsOn.setValue(selectedGwtTrigger.getEndsOn());
        retryInterval.setValue(selectedGwtTrigger.getRetryInterval());
        cronExpression.setValue(selectedGwtTrigger.getCronScheduling());
    }

    @Override
    public void submit() {
        selectedGwtTrigger.setTriggerName(triggerName.getValue());
        selectedGwtTrigger.setStartsOn(startsOn.getValue());
        selectedGwtTrigger.setEndsOn(endsOn.getValue());
        selectedGwtTrigger.setRetryInterval(retryInterval.getValue().longValue());
        selectedGwtTrigger.setCronScheduling(cronExpression.getValue());

        TRIGGER_SERVICE.update(xsrfToken, selectedGwtTrigger, new AsyncCallback<GwtTrigger>() {

            @Override
            public void onSuccess(GwtTrigger arg0) {
                exitStatus = true;
                exitMessage = JOB_MSGS.dialogEditScheduleConfirmation();
                hide();
            }

            @Override
            public void onFailure(Throwable cause) {
                exitStatus = false;
                exitMessage = JOB_MSGS.dialogEditScheduleError(cause.getLocalizedMessage());
            }
        });

    }

    @Override
    public String getHeaderMessage() {
        return JOB_MSGS.dialogEditScheduleHeader(selectedGwtTrigger.getTriggerName());
    }

    @Override
    public String getInfoMessage() {
        return JOB_MSGS.dialogEditScheduleInfo();
    }

}
