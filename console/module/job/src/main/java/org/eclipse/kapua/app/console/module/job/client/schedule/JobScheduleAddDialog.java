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

import com.extjs.gxt.ui.client.widget.form.DateField;
import com.extjs.gxt.ui.client.widget.form.MultiField;
import com.extjs.gxt.ui.client.widget.form.TimeField;
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
        retryInterval = new KapuaNumberField();
        cronExpression = new KapuaTextField<String>();

        DialogUtils.resizeDialog(this, 400, 250);
    }

    @Override
    public void createBody() {
        FormPanel mainPanel = new FormPanel(150);
        MultiField<?> multiFieldStartsOn=new MultiField<Object>();
        MultiField<?> multiFieldEndsOn=new MultiField<Object>();
        multiFieldStartsOn.setFieldLabel("* " + JOB_MSGS.dialogAddScheduleStartsOnLabel());
        multiFieldStartsOn.setSpacing(20);
        multiFieldEndsOn.setFieldLabel("* " + JOB_MSGS.dialogAddScheduleEndsOnLabel());
        multiFieldEndsOn.setSpacing(20);

        triggerName.setAllowBlank(false);
        triggerName.setMaxLength(255);
        triggerName.setFieldLabel("* " + JOB_MSGS.dialogAddScheduleScheduleNameLabel());
        mainPanel.add(triggerName);

        startsOn.setFormatValue(true);
        startsOn.setAllowBlank(false);
        startsOn.setWidth(90);
        startsOn.setEmptyText(JOB_MSGS.dialogAddScheduleDatePlaceholder());
        startsOn.getPropertyEditor().setFormat(DateTimeFormat.getFormat("dd/MM/yyyy"));
        multiFieldStartsOn.add(startsOn);

        startsOnTime.setAllowBlank(false);
        startsOnTime.setFormat(DateTimeFormat.getFormat("HH:mm"));
        startsOnTime.setEditable(false);
        startsOnTime.setWidth(85);
        startsOnTime.setEmptyText(JOB_MSGS.dialogAddScheduleTimePlaceholder());
        multiFieldStartsOn.add(startsOnTime);
        mainPanel.add(multiFieldStartsOn);

        endsOn.setFormatValue(true);
        endsOn.setWidth(90);
        endsOn.setEmptyText(JOB_MSGS.dialogAddScheduleDatePlaceholder());
        endsOn.getPropertyEditor().setFormat(DateTimeFormat.getFormat("dd/MM/yyyy"));
        multiFieldEndsOn.add(endsOn);

        endsOnTime.setFormat(DateTimeFormat.getFormat("HH:mm"));
        endsOnTime.setEditable(false);
        endsOnTime.setWidth(85);
        endsOnTime.setEmptyText(JOB_MSGS.dialogAddScheduleTimePlaceholder());
        multiFieldEndsOn.add(endsOnTime);
        mainPanel.add(multiFieldEndsOn);

        retryInterval.setFieldLabel("* " + JOB_MSGS.dialogAddScheduleRetryIntervalLabel());
        retryInterval.setAllowDecimals(false);
        retryInterval.setAllowNegative(false);
        retryInterval.setMaxLength(9);
        mainPanel.add(retryInterval);

        cronExpression.setFieldLabel("* " + JOB_MSGS.dialogAddScheduleCronScheduleLabel());
        cronExpression.setMaxLength(255);
        mainPanel.add(cronExpression);

        bodyPanel.add(mainPanel);
    }

    @Override
    protected void preSubmit() {
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

        if (cronExpression.getValue() == null && retryInterval.getValue() == null) {
            cronExpression.markInvalid(VAL_MSGS.retryIntervalOrCronRequired());
            retryInterval.markInvalid(VAL_MSGS.retryIntervalOrCronRequired());
            return;
        } else if (retryInterval.getValue() != null) {
            super.preSubmit();
        } else if (cronExpression.getValue() != null) {
            TRIGGER_SERVICE.validateCronExpression(cronExpression.getValue(), new AsyncCallback<Boolean>() {

                @Override
                public void onFailure(Throwable caught) {
                    ConsoleInfo.display(MSGS.popupError(), JOB_MSGS.unableToValidateCronExpression(caught.getMessage()));
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
        } else {
            cronExpression.markInvalid(VAL_MSGS.cronExpressionRequired());
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
