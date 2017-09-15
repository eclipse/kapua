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

import com.extjs.gxt.ui.client.Style.Orientation;
import com.extjs.gxt.ui.client.event.BaseEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.widget.form.Radio;
import com.extjs.gxt.ui.client.widget.form.RadioGroup;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import org.eclipse.kapua.app.console.module.api.client.ui.dialog.entity.EntityAddEditDialog;
import org.eclipse.kapua.app.console.module.api.client.util.DialogUtils;
import org.eclipse.kapua.app.console.module.api.shared.model.GwtSession;
import org.eclipse.kapua.app.console.module.device.shared.model.GwtDevice;
import org.eclipse.kapua.app.console.module.device.shared.model.GwtDeviceQuery;
import org.eclipse.kapua.app.console.module.device.shared.service.GwtDeviceService;
import org.eclipse.kapua.app.console.module.device.shared.service.GwtDeviceServiceAsync;
import org.eclipse.kapua.app.console.module.job.client.messages.ConsoleJobMessages;
import org.eclipse.kapua.app.console.module.job.shared.model.job.GwtJobTarget;
import org.eclipse.kapua.app.console.module.job.shared.model.job.GwtJobTargetCreator;
import org.eclipse.kapua.app.console.module.job.shared.service.GwtJobTargetService;
import org.eclipse.kapua.app.console.module.job.shared.service.GwtJobTargetServiceAsync;

import java.util.ArrayList;
import java.util.List;

public class JobTargetAddDialog extends EntityAddEditDialog {

    private final String jobId;

    private static final GwtJobTargetServiceAsync GWT_JOB_TARGET_SERVICE = GWT.create(GwtJobTargetService.class);
    private static final GwtDeviceServiceAsync GWT_DEVICE_SERVICE = GWT.create(GwtDeviceService.class);
    private static final ConsoleJobMessages JOB_MSGS = GWT.create(ConsoleJobMessages.class);

    private final RadioGroup targetRadioGroup = new RadioGroup();
    private JobTargetAddGrid targetGrid;

    private enum RadioGroupStatus {
        ALL, SELECTED
    }

    public JobTargetAddDialog(GwtSession currentSession, String jobId) {
        super(currentSession);
        this.jobId = jobId;

        DialogUtils.resizeDialog(this, 600, 400);
    }

    @Override
    public void createBody() {

        targetRadioGroup.setOrientation(Orientation.VERTICAL);
        targetRadioGroup.setSelectionRequired(true);
        targetRadioGroup.setStyleAttribute("margin-left", "5px");
        targetRadioGroup.addListener(Events.Change, new Listener<BaseEvent>() {

            @Override
            public void handleEvent(BaseEvent baseEvent) {
                Radio radio = ((RadioGroup) baseEvent.getSource()).getValue();
                if (radio.getValueAttribute().equals(RadioGroupStatus.SELECTED.name())) {
                    targetGrid.enable();
                } else if (radio.getValueAttribute().equals(RadioGroupStatus.ALL.name())) {
                    targetGrid.disable();
                }
            }
        });

        Radio allRadio = new Radio();
        allRadio.setValueAttribute(RadioGroupStatus.ALL.name());
        allRadio.setBoxLabel(JOB_MSGS.allTargets());
        allRadio.setValue(true);
        Radio selectedRadio = new Radio();
        selectedRadio.setValueAttribute(RadioGroupStatus.SELECTED.name());
        selectedRadio.setBoxLabel(JOB_MSGS.selectedTargets());

        targetRadioGroup.add(allRadio);
        targetRadioGroup.add(selectedRadio);

        targetGrid = new JobTargetAddGrid(currentSession, jobId);
        targetGrid.disable();
        targetGrid.setHeight(255);

        bodyPanel.add(targetRadioGroup);
        bodyPanel.add(targetGrid);
    }

    @Override
    public void submit() {
        if (targetRadioGroup.getValue().getValueAttribute().equals(RadioGroupStatus.SELECTED.name())) {
            doSubmit(targetGrid.getSelectionModel().getSelectedItems());
        } else {
            GWT_DEVICE_SERVICE.query(new GwtDeviceQuery(currentSession.getSelectedAccountId()), new AsyncCallback<List<GwtDevice>>() {

                @Override
                public void onFailure(Throwable caught) {
                    unmask();

                    submitButton.enable();
                    cancelButton.enable();
                    status.hide();

                    exitStatus = false;
                    exitMessage = JOB_MSGS.dialogGetTargetError(caught.getLocalizedMessage());

                    hide();
                }

                @Override
                public void onSuccess(List<GwtDevice> result) {
                    doSubmit(result);
                }
            });
        }
    }

    private void doSubmit(List<GwtDevice> targets) {
        List<GwtJobTargetCreator> creatorList = new ArrayList<GwtJobTargetCreator>();
        for (GwtDevice target : targets) {
            GwtJobTargetCreator creator = new GwtJobTargetCreator();
            creator.setScopeId(currentSession.getSelectedAccountId());
            creator.setJobId(jobId);
            creator.setJobTargetId(target.getId());
            creatorList.add(creator);
        }
        GWT_JOB_TARGET_SERVICE.create(xsrfToken, creatorList, new AsyncCallback<List<GwtJobTarget>>() {

            @Override
            public void onSuccess(List<GwtJobTarget> arg0) {
                exitStatus = true;
                exitMessage = JOB_MSGS.dialogAddTargetConfirmation();
                hide();
            }

            @Override
            public void onFailure(Throwable cause) {
                unmask();

                submitButton.enable();
                cancelButton.enable();
                status.hide();

                exitStatus = false;
                exitMessage = JOB_MSGS.dialogAddTargetError(cause.getLocalizedMessage());

                hide();
            }
        });
    }

    @Override
    public String getHeaderMessage() {
        return JOB_MSGS.dialogAddTargetHeader();
    }

    @Override
    public String getInfoMessage() {
        return JOB_MSGS.dialogAddTargetInfo();
    }
}
