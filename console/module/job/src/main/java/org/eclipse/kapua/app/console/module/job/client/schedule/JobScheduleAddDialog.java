/*******************************************************************************
 * Copyright (c) 2017, 2019 Eurotech and/or its affiliates and others
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

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.eclipse.kapua.app.console.module.api.client.GwtKapuaErrorCode;
import org.eclipse.kapua.app.console.module.api.client.GwtKapuaException;
import org.eclipse.kapua.app.console.module.api.client.messages.ValidationMessages;
import org.eclipse.kapua.app.console.module.api.client.ui.dialog.entity.EntityAddEditDialog;
import org.eclipse.kapua.app.console.module.api.client.ui.panel.FormPanel;
import org.eclipse.kapua.app.console.module.api.client.ui.widget.KapuaDateField;
import org.eclipse.kapua.app.console.module.api.client.ui.widget.KapuaNumberField;
import org.eclipse.kapua.app.console.module.api.client.ui.widget.KapuaTextField;
import org.eclipse.kapua.app.console.module.api.client.util.ConsoleInfo;
import org.eclipse.kapua.app.console.module.api.client.util.DialogUtils;
import org.eclipse.kapua.app.console.module.api.client.util.FailureHandler;
import org.eclipse.kapua.app.console.module.api.client.util.validator.AfterDateValidator;
import org.eclipse.kapua.app.console.module.api.client.util.validator.BeforeDateValidator;
import org.eclipse.kapua.app.console.module.api.shared.model.session.GwtSession;
import org.eclipse.kapua.app.console.module.job.client.messages.ConsoleJobMessages;
import org.eclipse.kapua.app.console.module.job.shared.model.scheduler.GwtTrigger;
import org.eclipse.kapua.app.console.module.job.shared.model.scheduler.GwtTriggerCreator;
import org.eclipse.kapua.app.console.module.job.shared.model.scheduler.GwtTriggerProperty;
import org.eclipse.kapua.app.console.module.job.shared.service.GwtTriggerService;
import org.eclipse.kapua.app.console.module.job.shared.service.GwtTriggerServiceAsync;
import com.extjs.gxt.ui.client.event.BaseEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.widget.HorizontalPanel;
import com.extjs.gxt.ui.client.widget.Label;
import com.extjs.gxt.ui.client.widget.form.ComboBox.TriggerAction;
import com.extjs.gxt.ui.client.widget.form.TimeField;
import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class JobScheduleAddDialog extends EntityAddEditDialog {

    protected static final ConsoleJobMessages JOB_MSGS = GWT.create(ConsoleJobMessages.class);
    private static final ValidationMessages VAL_MSGS = GWT.create(ValidationMessages.class);
    private static final GwtTriggerServiceAsync TRIGGER_SERVICE = GWT.create(GwtTriggerService.class);
    private static final String KAPUA_ID_CLASS_NAME = "org.eclipse.kapua.model.id.KapuaId";

    private final String jobId;
    protected final KapuaTextField<String> triggerName;
    protected final KapuaDateField startsOn;
    protected final TimeField startsOnTime;
    protected final KapuaDateField endsOn;
    protected final TimeField endsOnTime;
    protected final KapuaNumberField retryInterval;
    protected final KapuaTextField<String> cronExpression;
    private Label startsOnLabel;
    private Label endsOnLabel;

    public JobScheduleAddDialog(GwtSession currentSession, String jobId) {
        super(currentSession);
        this.jobId = jobId;

        triggerName = new KapuaTextField<String>();
        startsOn = new KapuaDateField(this);
        startsOn.setMaxLength(10);
        startsOn.getPropertyEditor().setFormat(DateTimeFormat.getFormat("dd/MM/yyyy"));
        startsOnTime = new TimeField();
        startsOnTime.setEditable(false);
        endsOn = new KapuaDateField(this);
        endsOn.setMaxLength(10);
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
        FormPanel mainPanel = new FormPanel(140);
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
        startsOn.setWidth(90);
        startsOn.setEmptyText(JOB_MSGS.dialogAddScheduleDatePlaceholder());
        startsOn.getPropertyEditor().setFormat(DateTimeFormat.getFormat("dd/MM/yyyy"));
        startsOn.setToolTip(JOB_MSGS.dialogAddScheduleStartsOnTooltip());
        startsOn.setValidator(new AfterDateValidator(endsOn));

        startsOn.getDatePicker().addListener(Events.Select, listener);
        startsOnLabel.setText("* " + JOB_MSGS.dialogAddScheduleStartsOnLabel());
        startsOnLabel.setWidth(FORM_LABEL_WIDTH);
        startsOnLabel.setStyleAttribute("padding", "0px 91px 0px 0px");
        startsOnPanel.add(startsOnLabel);
        startsOnPanel.add(startsOn);

        startsOnTime.setFormat(DateTimeFormat.getFormat("HH:mm"));
        startsOnTime.setAllowBlank(false);
        startsOnTime.setEditable(false);
        startsOnTime.setWidth(90);
        startsOnTime.setStyleAttribute("position", "relative");
        startsOnTime.setStyleAttribute("left", "25px");
        startsOnTime.setEmptyText(JOB_MSGS.dialogAddScheduleTimePlaceholder());
        startsOnTime.setToolTip(JOB_MSGS.dialogAddScheduleStartsOnTimeTooltip());
        startsOnTime.setTriggerAction(TriggerAction.ALL);
        startsOnTime.addListener(Events.Select, listener);
        startsOnPanel.add(startsOnTime);
        mainPanel.add(startsOnPanel);

        endsOn.setFormatValue(true);
        endsOn.setWidth(90);
        endsOn.setEmptyText(JOB_MSGS.dialogAddScheduleDatePlaceholder());
        endsOn.getPropertyEditor().setFormat(DateTimeFormat.getFormat("dd/MM/yyyy"));
        endsOn.setToolTip(JOB_MSGS.dialogAddScheduleEndsOnTooltip());
        endsOnLabel.setText(JOB_MSGS.dialogAddScheduleEndsOnLabel());
        endsOnLabel.setStyleAttribute("margin-left", "10px");
        endsOn.setValidator(new BeforeDateValidator(startsOn));

        endsOnLabel.setWidth(FORM_LABEL_WIDTH);
        endsOnLabel.setStyleAttribute("padding", "0px 96px 0px 0px");
        endsOn.getDatePicker().addListener(Events.Select, listener);
        endsOnPanel.add(endsOnLabel);
        endsOnPanel.add(endsOn);

        endsOnTime.setFormat(DateTimeFormat.getFormat("HH:mm"));
        endsOnTime.setEditable(false);
        endsOnTime.setWidth(90);
        endsOnTime.setStyleAttribute("position", "relative");
        endsOnTime.setStyleAttribute("left", "25px");
        endsOnTime.setEmptyText(JOB_MSGS.dialogAddScheduleTimePlaceholder());
        endsOnTime.setToolTip(JOB_MSGS.dialogAddScheduleEndsOnTimeTooltip());
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
        }

        if (endsOn.getValue() == null && endsOnTime.getValue() != null) {
            endsOn.markInvalid(VAL_MSGS.endTimeWithoutEndDate());
        }

        if (startsOn.getValue() == null && startsOnTime.getValue() != null) {
            startsOn.markInvalid(VAL_MSGS.startTimeWithoutStartDate());
        }
        if (startsOn.getValue() != null && startsOnTime.getValue() == null) {
            startsOnTime.markInvalid(VAL_MSGS.startDateWithoutStartTime());
        }
        if (startsOn.getValue() != null && endsOn.getValue() != null) {
            if (startsOn.getValue().after(endsOn.getValue())) {
                startsOn.markInvalid(VAL_MSGS.startsOnDateLaterThanEndsOn());
            }
        }
        if (startsOn.getValue() != null && endsOn.getValue() != null && startsOnTime.getValue() != null && endsOnTime.getValue() != null) {
            if (startsOn.getValue().equals(endsOn.getValue()) && startsOnTime.getValue().getDate().after(endsOnTime.getValue().getDate())) {
                startsOnTime.markInvalid(VAL_MSGS.startsOnTimeLaterThanEndsOn());
            }
        }

        if (startsOn.getValue() != null) {
            if (startsOnTime.getValue() == null) {
                startsOnTime.markInvalid(VAL_MSGS.startDateWithoutStartTime());
            }
        } else {
            startsOn.markInvalid(VAL_MSGS.emptyStartDate());
            if (startsOnTime.getValue() == null) {
                startsOnTime.markInvalid(VAL_MSGS.emptyStartTime());
            }
        }
        if (endsOn.getValue() != null) {
            if (endsOnTime.getValue() == null) {
                endsOnTime.setAllowBlank(false);
                endsOnTime.markInvalid(VAL_MSGS.endDateWithoutEndTime());
            } else {
                if (startsOn.getValue() != null && endsOn.getValue().before(startsOn.getValue())) {
                    endsOn.markInvalid(VAL_MSGS.endsOnDateEarlierThanStartsOn());
                } else if (startsOn.getValue() != null && startsOnTime.getValue() != null && endsOn.getValue().equals(startsOn.getValue()) && endsOnTime.getValue().getDate().before(startsOnTime.getValue().getDate())) {
                    endsOnTime.markInvalid(VAL_MSGS.endsOnTimeEarlierThanStartsOn());
                } else {
                    endsOn.clearInvalid();
                    endsOnTime.clearInvalid();
                    startsOn.clearInvalid();
                }
            }
        } else {
            endsOnTime.setAllowBlank(true);
        }

        endsOn.addListener(Events.OnBlur, new Listener<BaseEvent>() {

            @Override
            public void handleEvent(BaseEvent be) {
                if (endsOn.getValue() == null) {
                    endsOnTime.clearInvalid();
                }
            }
        });

        if (endsOnTime.getValue() != null) {
            if (endsOn.getValue() == null) {
                endsOn.setAllowBlank(false);
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
        if (!triggerName.isValid() || !startsOn.isValid() || !startsOnTime.isValid() || !endsOn.isValid() || !endsOnTime.isValid() || !retryInterval.isValid() || !cronExpression.isValid() ) {
            formPanel.isValid(false);
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
                    if (gwtCause.getCode().equals(GwtKapuaErrorCode.SCHEDULE_DUPLICATE_NAME)) {
                        triggerName.markInvalid(gwtCause.getMessage());
                    } else if (gwtCause.getCode().equals(GwtKapuaErrorCode.RETRY_AND_CRON_BOTH_SELECTED)) {
                        cronExpression.markInvalid(gwtCause.getMessage());
                        retryInterval.markInvalid(gwtCause.getMessage());
                    } else if (gwtCause.getCode().equals(GwtKapuaErrorCode.SAME_START_AND_DATE)) {
                        startsOn.markInvalid(gwtCause.getMessage());
                        startsOnTime.markInvalid(gwtCause.getMessage());
                        endsOn.markInvalid(gwtCause.getMessage());
                        endsOnTime.markInvalid(gwtCause.getMessage());
                    } else if (gwtCause.getCode().equals(GwtKapuaErrorCode.TRIGGER_NEVER_FIRE)) {
                        startsOn.markInvalid(gwtCause.getMessage());
                        startsOnTime.markInvalid(gwtCause.getMessage());
                        endsOn.markInvalid(gwtCause.getMessage());
                        endsOnTime.markInvalid(gwtCause.getMessage());
                        cronExpression.markInvalid(gwtCause.getMessage());
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
