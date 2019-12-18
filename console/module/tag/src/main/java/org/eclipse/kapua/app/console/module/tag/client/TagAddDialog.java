/*******************************************************************************
 * Copyright (c) 2018, 2019 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.app.console.module.tag.client;

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
import org.eclipse.kapua.app.console.module.tag.client.messages.ConsoleTagMessages;
import org.eclipse.kapua.app.console.module.tag.shared.model.GwtTag;
import org.eclipse.kapua.app.console.module.tag.shared.model.GwtTagCreator;
import org.eclipse.kapua.app.console.module.tag.shared.service.GwtTagService;
import org.eclipse.kapua.app.console.module.tag.shared.service.GwtTagServiceAsync;

public class TagAddDialog extends EntityAddEditDialog {

    private static final ConsoleTagMessages MSGS = GWT.create(ConsoleTagMessages.class);
    private static final ConsoleMessages CONSOLE_MSGS = GWT.create(ConsoleMessages.class);

    private static final GwtTagServiceAsync GWT_TAG_SERVICE = GWT.create(GwtTagService.class);

    protected KapuaTextField<String> tagNameField;
    protected KapuaTextField<String> tagDescriptionField;

    public TagAddDialog(GwtSession currentSession) {
        super(currentSession);
        DialogUtils.resizeDialog(this, 400, 200);
    }

    @Override
    public void createBody() {
        submitButton.disable();
        FormPanel tagFormPanel = new FormPanel(FORM_LABEL_WIDTH);
        tagNameField = new KapuaTextField<String>();
        tagNameField.setAllowBlank(false);
        tagNameField.setFieldLabel("* " + MSGS.dialogAddFieldName());
        tagNameField.setValidator(new TextFieldValidator(tagNameField, FieldType.NAME_SPACE));
        tagNameField.setMinLength(3);
        tagNameField.setMaxLength(255);
        tagFormPanel.add(tagNameField);

        tagDescriptionField = new KapuaTextField<String>();
        tagDescriptionField.setAllowBlank(true);
        tagDescriptionField.setFieldLabel(MSGS.dialogAddFieldTagDescription());
        tagDescriptionField.setToolTip(MSGS.dialogAddFieldTagDescriptionTooltip());
        tagDescriptionField.setName("description");
        tagDescriptionField.setMaxLength(255);
        tagFormPanel.add(tagDescriptionField);
        bodyPanel.add(tagFormPanel);
    }

    public void validateTags() {
        if (tagNameField.getValue() == null) {
            ConsoleInfo.display("Error", CONSOLE_MSGS.allFieldsRequired());
        }
    }

    @Override
    protected void preSubmit() {
        validateTags();
        super.preSubmit();
    }

    @Override
    public void submit() {
        GwtTagCreator gwtTagCreator = new GwtTagCreator();
        gwtTagCreator.setScopeId(currentSession.getSelectedAccountId());
        gwtTagCreator.setName(tagNameField.getValue());
        gwtTagCreator.setDescription(tagDescriptionField.getValue());
        GWT_TAG_SERVICE.create(gwtTagCreator, new AsyncCallback<GwtTag>() {

            @Override
            public void onSuccess(GwtTag gwtTag) {
                exitStatus = true;
                exitMessage = MSGS.dialogAddConfirmation();
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
                            tagNameField.markInvalid(gwtCause.getMessage());
                        }
                        FailureHandler.handleFormException(formPanel, cause);
                    }
                }
            }
        });

    }

    @Override
    public String getHeaderMessage() {
        return MSGS.dialogAddHeader();
    }

    @Override
    public String getInfoMessage() {
        return MSGS.dialogAddInfo();
    }

}
