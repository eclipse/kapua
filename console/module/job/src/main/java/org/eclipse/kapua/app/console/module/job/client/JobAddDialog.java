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
package org.eclipse.kapua.app.console.module.job.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import org.eclipse.kapua.app.console.module.api.client.GwtKapuaErrorCode;
import org.eclipse.kapua.app.console.module.api.client.GwtKapuaException;
import org.eclipse.kapua.app.console.module.api.client.messages.ConsoleMessages;
import org.eclipse.kapua.app.console.module.api.client.ui.dialog.entity.EntityAddEditDialog;
import org.eclipse.kapua.app.console.module.api.client.ui.panel.FormPanel;
import org.eclipse.kapua.app.console.module.api.client.ui.widget.KapuaTextField;
import org.eclipse.kapua.app.console.module.api.client.util.ConsoleInfo;
import org.eclipse.kapua.app.console.module.api.client.util.DialogUtils;
import org.eclipse.kapua.app.console.module.api.client.util.FailureHandler;
import org.eclipse.kapua.app.console.module.api.client.util.validator.TextFieldValidator;
import org.eclipse.kapua.app.console.module.api.client.util.validator.TextFieldValidator.FieldType;
import org.eclipse.kapua.app.console.module.api.shared.model.session.GwtSession;
import org.eclipse.kapua.app.console.module.job.client.messages.ConsoleJobMessages;
import org.eclipse.kapua.app.console.module.job.shared.model.GwtJob;
import org.eclipse.kapua.app.console.module.job.shared.model.GwtJobCreator;
import org.eclipse.kapua.app.console.module.job.shared.service.GwtJobService;
import org.eclipse.kapua.app.console.module.job.shared.service.GwtJobServiceAsync;

public class JobAddDialog extends EntityAddEditDialog {

    protected KapuaTextField<String> name;
    protected KapuaTextField<String> description;

    private static final GwtJobServiceAsync GWT_JOB_SERVICE = GWT.create(GwtJobService.class);

    protected static final ConsoleJobMessages JOB_MSGS = GWT.create(ConsoleJobMessages.class);
    protected static final ConsoleMessages CONSOLE_MSGS = GWT.create(ConsoleMessages.class);

    public JobAddDialog(GwtSession currentSession) {
        super(currentSession);
        DialogUtils.resizeDialog(this, 400, 200);
    }

    @Override
    public void createBody() {
        submitButton.disable();
        FormPanel jobFormPanel = new FormPanel(FORM_LABEL_WIDTH);

        name = new KapuaTextField<String>();
        name.setAllowBlank(false);
        name.setMinLength(3);
        name.setMaxLength(255);
        name.setName("name");
        name.setFieldLabel("* " + JOB_MSGS.dialogAddFieldName());
        name.setValidator(new TextFieldValidator(name, FieldType.NAME_SPACE));
        name.setToolTip(JOB_MSGS.dialogAddFieldNameTooltip());
        jobFormPanel.add(name);

        description = new KapuaTextField<String>();
        description.setAllowBlank(true);
        description.setMaxLength(255);
        description.setName("description");
        description.setFieldLabel(JOB_MSGS.dialogAddFieldDescription());
        description.setToolTip(JOB_MSGS.dialogAddFieldDescriptionTooltip());
        jobFormPanel.add(description);

        bodyPanel.add(jobFormPanel);
    }

    public void validateJob() {
        if (name.getValue() == null) {
            ConsoleInfo.display("Error", CONSOLE_MSGS.allFieldsRequired());
        }
    }

    @Override
    protected void preSubmit() {
        validateJob();
        super.preSubmit();
    }

    @Override
    public void submit() {
        GwtJobCreator gwtJobCreator = new GwtJobCreator();

        gwtJobCreator.setScopeId(currentSession.getSelectedAccountId());

        gwtJobCreator.setName(name.getValue());
        gwtJobCreator.setDescription(description.getValue());

        GWT_JOB_SERVICE.create(xsrfToken, gwtJobCreator, new AsyncCallback<GwtJob>() {

            @Override
            public void onSuccess(GwtJob gwtJob) {
                exitStatus = true;
                exitMessage = JOB_MSGS.dialogAddConfirmation();
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
                    }
                    FailureHandler.handleFormException(formPanel, cause);
                }
            }
        });
    }

    @Override
    public String getHeaderMessage() {
        return JOB_MSGS.dialogAddHeader();
    }

    @Override
    public String getInfoMessage() {
        return JOB_MSGS.dialogAddInfo();
    }
}
