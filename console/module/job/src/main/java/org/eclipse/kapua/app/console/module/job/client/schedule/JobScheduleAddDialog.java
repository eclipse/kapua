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
package org.eclipse.kapua.app.console.module.job.client.schedule;

import com.extjs.gxt.ui.client.event.BaseEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.widget.HorizontalPanel;
import com.extjs.gxt.ui.client.widget.Label;
import com.extjs.gxt.ui.client.widget.form.DateField;
import com.extjs.gxt.ui.client.widget.form.TimeField;
import com.extjs.gxt.ui.client.widget.form.ComboBox.TriggerAction;
import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.rpc.AsyncCallback;
import org.eclipse.kapua.app.console.module.api.client.GwtKapuaErrorCode;
import org.eclipse.kapua.app.console.module.api.client.GwtKapuaException;
import org.eclipse.kapua.app.console.module.api.client.messages.ValidationMessages;
import org.eclipse.kapua.app.console.module.api.client.ui.dialog.entity.EntityAddEditDialog;
import org.eclipse.kapua.app.console.module.api.client.ui.panel.FormPanel;
import org.eclipse.kapua.app.console.module.api.client.ui.widget.KapuaNumberField;
import org.eclipse.kapua.app.console.module.api.client.ui.widget.KapuaTextField;
import org.eclipse.kapua.app.console.module.api.client.util.ConsoleInfo;
import org.eclipse.kapua.app.console.module.api.client.util.DialogUtils;
import org.eclipse.kapua.app.console.module.api.client.util.FailureHandler;
import org.eclipse.kapua.app.console.module.api.shared.model.session.GwtSession;
import org.eclipse.kapua.app.console.module.job.client.messages.ConsoleJobMessages;
import org.eclipse.kapua.app.console.module.job.shared.model.scheduler.GwtTrigger;
import org.eclipse.kapua.app.console.module.job.shared.model.scheduler.GwtTriggerCreator;
import org.eclipse.kapua.app.console.module.job.shared.model.scheduler.GwtTriggerProperty;
import org.eclipse.kapua.app.console.module.job.shared.service.GwtTriggerService;
import org.eclipse.kapua.app.console.module.job.shared.service.GwtTriggerServiceAsync;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class JobScheduleAddDialog extends EntityAddEditDialog {

    protected static final ConsoleJobMessages JOB_MSGS = GWT.create(ConsoleJobMessages.class);
    private static final ValidationMessages VAL_MSGS = GWT.create(ValidationMessages.class);
    private static final GwtTriggerServiceAsync TRIGGER_SERVICE = GWT.create(GwtTriggerService.class);
    private static final String KAPUA_ID_CLASS_NAME = "org.eclipse.kapua.model.id.KapuaId";

    private final String jobId;
    protected final KapuaTextField<String> triggerName;
    protected final DateField startsOn;
    protected final TimeField startsOnTime;
    protected final DateField endsOn;
    protected final TimeField endsOnTime;
    protected final KapuaNumberField retryInterval;
    protected final KapuaTextField<String> cronExpression;
    private Label startsOnLabel;
    private Label endsOnLabel;

    public JobScheduleAddDialog(GwtSession currentSession, String jobId) {
        super(currentSession);
        this.jobId = jobId;

        triggerName = new KapuaTextField<String>();
        startsOn = new DateField();
        startsOn.getPropertyEditor().setFormat(DateTimeFormat.getFormat("dd/MM/yyyy"));
        startsOnTime = new TimeField();
        startsOnTime.setEditable(false);
        endsOn = new DateField();
        endsOn.getPropertyEditor().setFormat(DateTimeFormat.getFormat("dd/MM/yyyy"));
        endsOnTime = new TimeField();
        endsOnTime.setEditable(false);
        startsOnLabel = new Label();
        endsOnLabel = new Label();
        retryInterval = new KapuaNumberField();
        cronExpression = new KapuaTextField<String>();

        DialogUtils.resizeDialog(this, 400, 250);
    }

    @Override
    public void createBody() {
        submitButton.disable();
        FormPanel mainPanel = new FormPanel(150);
        HorizontalPanel startsOnPanel = new HorizontalPanel();
        HorizontalPanel endsOnPanel = new HorizontalPanel();
        endsOnPanel.setStyleAttribute("padding", "4px 0px 4px 0px");

        Listener<BaseEvent> listener = new Listener<BaseEvent>() {

            @Override
            public void handleEvent(BaseEvent be) {
                formPanel.fireEvent(Events.OnClick);
            }
        };

        triggerName.setAllowBlank(false);
        triggerName.setMaxLength(255);
        triggerName.setFieldLabel("* " + JOB_MSGS.dialogAddScheduleScheduleNameLabel());
        triggerName.setToolTip(JOB_MSGS.dialogAddScheduleNameTooltip());
        mainPanel.add(triggerName);

        startsOn.setFormatValue(true);
        startsOn.setAllowBlank(false);
        startsOn.setWidth(95);
        startsOn.setEmptyText(JOB_MSGS.dialogAddScheduleDatePlaceholder());
        startsOn.getPropertyEditor().setFormat(DateTimeFormat.getFormat("dd/MM/yyyy"));
        startsOn.setToolTip(JOB_MSGS.dialogAddScheduleStartsOnTooltip());
        startsOn.getDatePicker().addListener(Events.Select, listener);
        startsOnLabel.setText("* " + JOB_MSGS.dialogAddScheduleStartsOnLabel());
        startsOnLabel.setWidth(FORM_LABEL_WIDTH);
        startsOnLabel.setStyleAttribute("padding", "0px 101px 0px 0px");
        startsOnPanel.add(startsOnLabel);
        startsOnPanel.add(startsOn);

        startsOnTime.setAllowBlank(false);
        startsOnTime.setFormat(DateTimeFormat.getFormat("HH:mm"));
        startsOnTime.setEditable(false);
        startsOnTime.setWidth(100);
        startsOnTime.setStyleAttribute("padding", "0px 0px 0px 17px");
        startsOnTime.setEmptyText(JOB_MSGS.dialogAddScheduleTimePlaceholder());
        startsOnTime.setToolTip(JOB_MSGS.dialogAddScheduleStartsOnTimeTooltip());
        startsOnTime.setTriggerAction(TriggerAction.ALL);
        startsOnTime.addListener(Events.Select, listener);
        startsOnPanel.add(startsOnTime);
        mainPanel.add(startsOnPanel);

        endsOn.setFormatValue(true);
        endsOn.setWidth(95);
        endsOn.setEmptyText(JOB_MSGS.dialogAddScheduleDatePlaceholder());
        endsOn.getPropertyEditor().setFormat(DateTimeFormat.getFormat("dd/MM/yyyy"));
        endsOn.setToolTip(JOB_MSGS.dialogAddScheduleEndsOnTooltip());
        endsOnLabel.setText("* " + JOB_MSGS.dialogAddScheduleEndsOnLabel());
        endsOnLabel.setWidth(FORM_LABEL_WIDTH);
        endsOnLabel.setStyleAttribute("padding", "0px 106px 0px 0px");
        endsOn.getDatePicker().addListener(Events.Select, listener);
        endsOnPanel.add(endsOnLabel);
        endsOnPanel.add(endsOn);

        endsOnTime.setFormat(DateTimeFormat.getFormat("HH:mm"));
        endsOnTime.setEditable(false);
        endsOnTime.setWidth(100);
        endsOnTime.setEmptyText(JOB_MSGS.dialogAddScheduleTimePlaceholder());
        endsOnTime.setToolTip(JOB_MSGS.dialogAddScheduleEndsOnTimeTooltip());
        endsOnTime.setStyleAttribute("padding", "0px 0px 0px 17px");
        endsOnTime.setTriggerAction(TriggerAction.ALL);
        endsOnTime.addListener(Events.Select, listener);
        endsOnPanel.add(endsOnTime);
        mainPanel.add(endsOnPanel);

        retryInterval.setFieldLabel("* " + JOB_MSGS.dialogAddScheduleRetryIntervalLabel());
        retryInterval.setAllowDecimals(false);
        retryInterval.setMinValue(1);
        retryInterval.setMaxLength(9);
        retryInterval.setToolTip(JOB_MSGS.dialogAddScheduleRetryIntervalTooltip());
        mainPanel.add(retryInterval);

        cronExpression.setFieldLabel("* " + JOB_MSGS.dialogAddScheduleCronScheduleLabel());
        cronExpression.setMaxLength(255);
        cronExpression.setToolTip(JOB_MSGS.dialogAddScheduleCronScheduleTooltip());
        mainPanel.add(cronExpression);

        bodyPanel.add(mainPanel);
    }

    @Override
    protected void preSubmit() {
        cronExpression.clearInvalid();

        if (triggerName.getValue() == null) {
            triggerName.markInvalid(VAL_MSGS.nameRequiredMsg());
            return;
        }

        if (endsOn.getValue() == null && endsOnTime.getValue() != null) {
            endsOn.markInvalid(VAL_MSGS.endTimeWithoutEndDate());
            return;
        }
        if (startsOn.getValue() == null && startsOnTime.getValue() != null) {
            startsOn.markInvalid(VAL_MSGS.startTimeWithoutStartDate());
            return;
        }
        if (startsOn.getValue() != null && startsOnTime.getValue() == null) {
            startsOnTime.markInvalid(VAL_MSGS.startDateWithoutStartTime());
            return;
        }
        if (startsOn.getValue() != null && endsOn.getValue() != null) {
            if (startsOn.getValue().after(endsOn.getValue())) {
                startsOn.markInvalid(VAL_MSGS.startsOnDateLaterThanEndsOn());
                return;
            }
        }
        if (startsOn.getValue() != null && endsOn.getValue() != null && startsOnTime != null && endsOnTime != null) {
            if (startsOn.getValue().equals(endsOn.getValue()) && startsOnTime.getValue().getDate().after(endsOnTime.getValue().getDate())) {
                startsOnTime.markInvalid(VAL_MSGS.startsOnTimeLaterThanEndsOn());
                return;
            }
        }

        if (endsOn.getValue() != null) {
            if (endsOnTime.getValue() == null) {
                endsOnTime.markInvalid(VAL_MSGS.endDateWithoutEndTime());
                return;
            } else {
                if (endsOn.getValue().before(startsOn.getValue())) {
                    endsOn.markInvalid(VAL_MSGS.endsOnDateEarlierThanStartsOn());
                    return;
                } else if (endsOn.getValue().equals(startsOn.getValue()) && endsOnTime.getValue().getDate().before(startsOnTime.getValue().getDate())) {
                    endsOnTime.markInvalid(VAL_MSGS.endsOnTimeEarlierThanStartsOn());
                    return;
                }
            }
        }

        if (cronExpression.getValue() == null && cronExpression.isValid() && retryInterval.getValue() == null && retryInterval.isValid()) {
            cronExpression.markInvalid(VAL_MSGS.retryIntervalOrCronRequired());
            retryInterval.markInvalid(VAL_MSGS.retryIntervalOrCronRequired());
            return;
        } else if (retryInterval.getValue() != null) {
            super.preSubmit();
        } else if (cronExpression.getValue() != null) {
            TRIGGER_SERVICE.validateCronExpression(cronExpression.getValue(), new AsyncCallback<Boolean>() {

                @Override
                public void onFailure(Throwable caught) {
                    ConsoleInfo.display(MSGS.popupError(), JOB_MSGS.unableToValidateCronExpression());
                    cronExpression.markInvalid(VAL_MSGS.invalidCronExpression());
                }

                @Override
                public void onSuccess(Boolean result) {
                    if (result) {
                        JobScheduleAddDialog.super.preSubmit();
                    } else {
                        cronExpression.markInvalid(VAL_MSGS.invalidCronExpression());
                    }
                }
            });
        }
    }

    @Override
    public void submit() {
        GwtTriggerCreator gwtTriggerCreator = new GwtTriggerCreator();

        gwtTriggerCreator.setScopeId(currentSession.getSelectedAccountId());

        gwtTriggerCreator.setTriggerName(triggerName.getValue());
        if (startsOn.getValue() != null) {
            Date startsOnDate = startsOn.getValue();
            startsOnDate.setTime(startsOnDate.getTime() + (3600 * 1000 * startsOnTime.getValue().getHour()) + 60 * 1000 * startsOnTime.getValue().getMinutes());
            gwtTriggerCreator.setStartsOn(startsOnDate);
            if (endsOn.getValue() != null && endsOnTime.getValue() != null) {
                // According to validation, endsOn and endsOnTime should be both either null or having a value
                Date endsOnDate = endsOn.getValue();
                endsOnDate.setTime(endsOnDate.getTime() + (3600 * 1000 * endsOnTime.getValue().getHour()) + 60 * 1000 * endsOnTime.getValue().getMinutes());
                gwtTriggerCreator.setEndsOn(endsOnDate);
            }
        }
        gwtTriggerCreator.setRetryInterval(retryInterval.getValue() != null ? retryInterval.getValue().longValue() : null);
        gwtTriggerCreator.setCronScheduling(cronExpression.getValue());
        List<GwtTriggerProperty> gwtTriggerPropertyList = new ArrayList<GwtTriggerProperty>();
        gwtTriggerPropertyList.add(new GwtTriggerProperty("scopeId", KAPUA_ID_CLASS_NAME, currentSession.getSelectedAccountId()));
        gwtTriggerPropertyList.add(new GwtTriggerProperty("jobId", KAPUA_ID_CLASS_NAME, jobId));
        gwtTriggerCreator.setTriggerProperties(gwtTriggerPropertyList);

        TRIGGER_SERVICE.create(xsrfToken, gwtTriggerCreator, new AsyncCallback<GwtTrigger>() {

            @Override
            public void onSuccess(GwtTrigger arg0) {
                exitStatus = true;
                exitMessage = JOB_MSGS.dialogAddScheduleConfirmation();
                hide();
            }

            @Override
            public void onFailure(Throwable cause) {
                exitStatus = false;
                FailureHandler.handleFormException(formPanel, cause);
                status.hide();
                formPanel.getButtonBar().enable();
                unmask();
                submitButton.enable();
                cancelButton.enable();
                if (cause instanceof GwtKapuaException) {
                    GwtKapuaException gwtCause = (GwtKapuaException) cause;
                    if (gwtCause.getCode().equals(GwtKapuaErrorCode.DUPLICATE_NAME)) {
                        triggerName.markInvalid(gwtCause.getMessage());
                    }
                }
            }
        });
    }

    @Override
    public String getHeaderMessage() {
        return JOB_MSGS.dialogAddScheduleHeader();
    }

    @Override
    public String getInfoMessage() {
        return JOB_MSGS.dialogAddScheduleInfo();
    }
}
