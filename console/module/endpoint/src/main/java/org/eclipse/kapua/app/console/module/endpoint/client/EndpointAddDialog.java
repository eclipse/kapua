/*******************************************************************************
 * Copyright (c) 2018, 2022 Eurotech and/or its affiliates and others
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.app.console.module.endpoint.client;

import org.eclipse.kapua.app.console.module.api.client.messages.ConsoleMessages;
import org.eclipse.kapua.app.console.module.api.client.messages.ValidationMessages;
import org.eclipse.kapua.app.console.module.api.client.ui.dialog.entity.EntityAddEditDialog;
import org.eclipse.kapua.app.console.module.api.client.ui.panel.FormPanel;
import org.eclipse.kapua.app.console.module.api.client.ui.validator.RegexFieldValidator;
import org.eclipse.kapua.app.console.module.api.client.ui.widget.KapuaNumberField;
import org.eclipse.kapua.app.console.module.api.client.ui.widget.KapuaTextField;
import org.eclipse.kapua.app.console.module.api.client.util.ConsoleInfo;
import org.eclipse.kapua.app.console.module.api.client.util.DialogUtils;
import org.eclipse.kapua.app.console.module.api.client.util.FailureHandler;
import org.eclipse.kapua.app.console.module.api.shared.model.session.GwtSession;
import org.eclipse.kapua.app.console.module.endpoint.client.messages.ConsoleEndpointMessages;
import org.eclipse.kapua.app.console.module.endpoint.shared.model.GwtEndpoint;
import org.eclipse.kapua.app.console.module.endpoint.shared.model.GwtEndpointCreator;
import org.eclipse.kapua.app.console.module.endpoint.shared.model.validation.GwtEndpointValidationRegex;
import org.eclipse.kapua.app.console.module.endpoint.shared.service.GwtEndpointService;
import org.eclipse.kapua.app.console.module.endpoint.shared.service.GwtEndpointServiceAsync;
import org.eclipse.kapua.service.endpoint.EndpointInfo;

import com.extjs.gxt.ui.client.widget.form.CheckBox;
import com.extjs.gxt.ui.client.widget.form.CheckBoxGroup;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class EndpointAddDialog extends EntityAddEditDialog {

    private static final GwtEndpointServiceAsync GWT_ENDPOINT_SERVICE = GWT.create(GwtEndpointService.class);
    private static final ConsoleEndpointMessages MSGS = GWT.create(ConsoleEndpointMessages.class);
    private static final ConsoleMessages CMSGS = GWT.create(ConsoleMessages.class);
    private static final ValidationMessages VAL_MSGS = GWT.create(ValidationMessages.class);

    protected KapuaTextField<String> endpointSchemaField;
    protected KapuaTextField<String> endpointDnsField;
    protected KapuaNumberField endpointPortField;

    protected CheckBoxGroup endpointSecureCheckboxGroup;
    protected CheckBox endpointSecureCheckbox;

    public EndpointAddDialog(GwtSession currentSession) {
        super(currentSession);
        DialogUtils.resizeDialog(this, 400, 220);
    }

    @Override
    public void createBody() {
        submitButton.disable();
        FormPanel endpointFormPanel = new FormPanel(FORM_LABEL_WIDTH);

        endpointSchemaField = new KapuaTextField<String>();
        endpointSchemaField.setAllowBlank(false);
        endpointSchemaField.setFieldLabel("* " + MSGS.dialogAddFieldSchema());
        endpointSchemaField.setToolTip(MSGS.dialogAddFieldSchemaTooltip());
        endpointSchemaField.setValidator(new RegexFieldValidator(GwtEndpointValidationRegex.URI_SCHEME, MSGS.dialogAddFieldSchemaValidation()));
        endpointSchemaField.setMaxLength(64);
        endpointFormPanel.add(endpointSchemaField);

        endpointDnsField = new KapuaTextField<String>();
        endpointDnsField.setAllowBlank(false);
        endpointDnsField.setFieldLabel("* " + MSGS.dialogAddFieldDns());
        endpointDnsField.setToolTip(MSGS.dialogAddFieldDnsTooltip());
        endpointDnsField.setValidator(new RegexFieldValidator(GwtEndpointValidationRegex.URI_DNS, MSGS.dialogAddFieldDnsValidation()));
        endpointDnsField.setMaxLength(1024);
        endpointFormPanel.add(endpointDnsField);

        endpointPortField = new KapuaNumberField();
        endpointPortField.setAllowBlank(false);
        endpointPortField.setFieldLabel("* " + MSGS.dialogAddFieldPort());
        endpointPortField.setToolTip(MSGS.dialogAddFieldPortTooltip());
        endpointPortField.setValidator(new RegexFieldValidator(GwtEndpointValidationRegex.URI_PORT, MSGS.dialogAddFieldPortValidation()));
        endpointPortField.setAllowNegative(false);
        endpointPortField.setAllowDecimals(false);
        endpointPortField.setMaxLength(5);
        endpointPortField.setMinValue(1);
        endpointPortField.setMaxValue(65535);
        endpointPortField.setPropertyEditorType(Integer.class);
        endpointFormPanel.add(endpointPortField);

        endpointSecureCheckbox = new CheckBox();
        endpointSecureCheckbox.setBoxLabel("");
        endpointSecureCheckbox.setToolTip(MSGS.dialogAddFieldSecureTooltip());

        endpointSecureCheckboxGroup = new CheckBoxGroup();
        endpointSecureCheckboxGroup.setFieldLabel(MSGS.dialogAddFieldSecure());
        endpointSecureCheckboxGroup.add(endpointSecureCheckbox);
        endpointFormPanel.add(endpointSecureCheckboxGroup);

        bodyPanel.add(endpointFormPanel);
    }

    public void validateEndPoint() {
        if (endpointSchemaField.getValue() == null || endpointDnsField.getValue() == null) {
            ConsoleInfo.display(CMSGS.error(), CMSGS.allFieldsRequired());
        } else if (endpointPortField.getValue() == null) {
            ConsoleInfo.display(CMSGS.error(), MSGS.portFieldEmptyOrInvalid());
        } else if (!endpointSchemaField.isValid()) {
            ConsoleInfo.display(CMSGS.error(), endpointSchemaField.getErrorMessage());
        } else if (!endpointDnsField.isValid()) {
            ConsoleInfo.display(CMSGS.error(), endpointDnsField.getErrorMessage());
        } else if (!endpointPortField.isValid()) {
            ConsoleInfo.display(CMSGS.error(), endpointPortField.getErrorMessage());
        }
    }

    @Override
    protected void preSubmit() {
        validateEndPoint();
        super.preSubmit();
    }

    @Override
    public void submit() {
        GwtEndpointCreator gwtEndpointCreator = new GwtEndpointCreator();
        gwtEndpointCreator.setScopeId(currentSession.getSelectedAccountId());
        gwtEndpointCreator.setSchema(endpointSchemaField.getValue());
        gwtEndpointCreator.setDns(endpointDnsField.getValue());
        gwtEndpointCreator.setPort(endpointPortField.getValue());
        gwtEndpointCreator.setSecure(endpointSecureCheckboxGroup.getValue() != null);
        gwtEndpointCreator.setEndpointType(EndpointInfo.ENDPOINT_TYPE_RESOURCE);

        GWT_ENDPOINT_SERVICE.create(xsrfToken, gwtEndpointCreator, new AsyncCallback<GwtEndpoint>() {

            @Override
            public void onSuccess(GwtEndpoint gwtEndpoint) {
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
                    FailureHandler.handleFormException(formPanel, cause);
                    endpointDnsField.markInvalid(VAL_MSGS.DUPLICATE_NAME());
                    endpointSchemaField.markInvalid(VAL_MSGS.DUPLICATE_NAME());
                    endpointPortField.markInvalid(VAL_MSGS.DUPLICATE_NAME());
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
