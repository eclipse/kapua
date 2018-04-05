/*******************************************************************************
 * Copyright (c) 2017, 2018 Eurotech and/or its affiliates and others
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
import org.eclipse.kapua.app.console.module.api.shared.model.session.GwtSession;
import org.eclipse.kapua.app.console.module.device.shared.model.GwtDevice;
import org.eclipse.kapua.app.console.module.device.shared.model.GwtDeviceQuery;
import org.eclipse.kapua.app.console.module.device.shared.service.GwtDeviceService;
import org.eclipse.kapua.app.console.module.device.shared.service.GwtDeviceServiceAsync;
import org.eclipse.kapua.app.console.module.job.client.messages.ConsoleJobMessages;
import org.eclipse.kapua.app.console.module.job.shared.model.GwtJob;
import org.eclipse.kapua.app.console.module.job.shared.model.GwtJobTarget;
import org.eclipse.kapua.app.console.module.job.shared.model.GwtJobTargetCreator;
import org.eclipse.kapua.app.console.module.job.shared.service.GwtJobTargetService;
import org.eclipse.kapua.app.console.module.job.shared.service.GwtJobTargetServiceAsync;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class JobTargetAddDialog extends EntityAddEditDialog {

    private static final GwtJobTargetServiceAsync GWT_JOB_TARGET_SERVICE = GWT.create(GwtJobTargetService.class);
    private static final GwtDeviceServiceAsync GWT_DEVICE_SERVICE = GWT.create(GwtDeviceService.class);
    private static final ConsoleJobMessages JOB_MSGS = GWT.create(ConsoleJobMessages.class);

    private final GwtJob gwtSelectedJob;

    private final RadioGroup targetRadioGroup = new RadioGroup();
    private JobTargetAddGrid targetGrid;

    private enum RadioGroupStatus {
        ALL, SELECTED
    }

    public JobTargetAddDialog(GwtSession currentSession, GwtJob gwtSelectedJob) {
        super(currentSession);

        this.gwtSelectedJob = gwtSelectedJob;

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

        targetGrid = new JobTargetAddGrid(currentSession, gwtSelectedJob);
        targetGrid.disable();
        targetGrid.setHeight(255);

        bodyPanel.add(targetRadioGroup);
        bodyPanel.add(targetGrid);
    }

    @Override
    public void submit() {
        if (targetRadioGroup.getValue().getValueAttribute().equals(RadioGroupStatus.SELECTED.name())) {
            doSubmitWithExistenceCheck(targetGrid.getSelectionModel().getSelectedItems());
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
                    doSubmitWithExistenceCheck(result);
                }
            });
        }
    }

    /**
     * Before doing real submit of targets, check if target are already added.
     *
     * @param targets those that are being added
     */
    private void doSubmitWithExistenceCheck(final List<GwtDevice> targets) {

        GWT_JOB_TARGET_SERVICE.findByJobId(currentSession.getSelectedAccountId(), gwtSelectedJob.getId(), true, new AsyncCallback<List<GwtJobTarget>>() {

            @Override
            public void onFailure(Throwable caught) {

                exitStatus = false;
                exitMessage = JOB_MSGS.dialogGetTargetError(caught.getLocalizedMessage());
                hide();
            }

            @Override
            public void onSuccess(List<GwtJobTarget> result) {
                doSubmit(targets, result);
            }
        });

    }

    /**
     * Check if targets being added are new targets, empty targets, union of targets and
     * only add them if needed otherwise report info message.
     *
     * @param targets  those that are being added
     * @param existing targets targets that are already on job
     */
    private void doSubmit(List<GwtDevice> targets, List<GwtJobTarget> existing) {
        List<GwtJobTargetCreator> creatorList = new ArrayList<GwtJobTargetCreator>();

        if (!isTargetsToAdd(targets, existing)) {
            exitStatus = false;
            exitMessage = JOB_MSGS.dialogAddTargetEmpty();
            hide();

            return;
        }

        for (GwtDevice target : targets) {
            GwtJobTargetCreator creator = new GwtJobTargetCreator();
            creator.setScopeId(currentSession.getSelectedAccountId());
            creator.setJobId(gwtSelectedJob.getId());
            creator.setJobTargetId(target.getId());
            creatorList.add(creator);
        }
        GWT_JOB_TARGET_SERVICE.create(xsrfToken, currentSession.getSelectedAccountId(), gwtSelectedJob.getId(), creatorList, new AsyncCallback<List<GwtJobTarget>>() {

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

    /**
     * Check if devices need to be added to existing targets.
     *
     * @param targets  devices to be added
     * @param existing existing devices
     * @return true if devices need to be added, false if not
     */
    private boolean isTargetsToAdd(List<GwtDevice> targets, List<GwtJobTarget> existing) {

        boolean addTargets = false;

        Set<String> targetsSet = new HashSet<String>();
        for (GwtDevice device : targets) {
            targetsSet.add(device.getId());
        }
        Set<String> existsSet = new HashSet<String>();
        for (GwtJobTarget exist : existing) {
            existsSet.add(exist.getJobTargetId());
        }
        if (targetsSet.isEmpty()) {
            addTargets = false;
        } else if (targetsSet.size() > existsSet.size()) {
            addTargets = true;
        } else if (existsSet.containsAll(targetsSet)) {
            addTargets = false;
        } else {
            addTargets = true;
        }

        return addTargets;
    }

}