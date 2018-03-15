/*******************************************************************************
 * Copyright (c) 2018 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.app.console.module.endpoint.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import org.eclipse.kapua.app.console.module.api.client.ui.dialog.entity.EntityAddEditDialog;
import org.eclipse.kapua.app.console.module.api.client.ui.panel.FormPanel;
import org.eclipse.kapua.app.console.module.api.client.ui.widget.KapuaNumberField;
import org.eclipse.kapua.app.console.module.api.client.ui.widget.KapuaTextField;
import org.eclipse.kapua.app.console.module.api.client.util.DialogUtils;
import org.eclipse.kapua.app.console.module.api.client.util.FailureHandler;
import org.eclipse.kapua.app.console.module.api.shared.model.session.GwtSession;
import org.eclipse.kapua.app.console.module.endpoint.client.messages.ConsoleEndpointMessages;
import org.eclipse.kapua.app.console.module.endpoint.shared.model.GwtEndpoint;
import org.eclipse.kapua.app.console.module.endpoint.shared.model.GwtEndpointCreator;
import org.eclipse.kapua.app.console.module.endpoint.shared.service.GwtEndpointService;
import org.eclipse.kapua.app.console.module.endpoint.shared.service.GwtEndpointServiceAsync;

public class EndpointAddDialog extends EntityAddEditDialog {

    private final static GwtEndpointServiceAsync GWT_ENDPOINT_SERVICE = GWT.create(GwtEndpointService.class);
    private final static ConsoleEndpointMessages MSGS = GWT.create(ConsoleEndpointMessages.class);

    protected KapuaTextField<String> endpointSchemaField;
    protected KapuaTextField<String> endpointDnsField;
    protected KapuaNumberField endpointPortField;

    public EndpointAddDialog(GwtSession currentSession) {
        super(currentSession);
        DialogUtils.resizeDialog(this, 400, 200);
    }

    @Override
    public void createBody() {
        FormPanel endpointFormPanel = new FormPanel(FORM_LABEL_WIDTH);

        endpointSchemaField = new KapuaTextField<String>();
        endpointSchemaField.setAllowBlank(false);
        endpointSchemaField.setFieldLabel("* " + MSGS.dialogAddFieldDns());
        endpointSchemaField.setToolTip("* " + MSGS.dialogAddFieldDnsTooltip());
        endpointSchemaField.setMaxLength(64);
        endpointFormPanel.add(endpointSchemaField);

        endpointDnsField = new KapuaTextField<String>();
        endpointDnsField.setAllowBlank(false);
        endpointDnsField.setFieldLabel("* " + MSGS.dialogAddFieldDns());
        endpointDnsField.setToolTip("* " + MSGS.dialogAddFieldDnsTooltip());
        endpointDnsField.setMaxLength(255);
        endpointFormPanel.add(endpointDnsField);

        endpointPortField = new KapuaNumberField();
        endpointPortField.setAllowBlank(false);
        endpointPortField.setFieldLabel("* " + MSGS.dialogAddFieldPort());
        endpointPortField.setToolTip("* " + MSGS.dialogAddFieldPortTooltip());
        endpointPortField.setMaxLength(6);
        endpointPortField.setMinValue(0);
        endpointPortField.setMaxValue(65536);
        endpointFormPanel.add(endpointPortField);

        bodyPanel.add(endpointFormPanel);
    }

    @Override
    public void submit() {
        GwtEndpointCreator gwtEndpointCreator = new GwtEndpointCreator();
        gwtEndpointCreator.setScopeId(currentSession.getSelectedAccountId());
        gwtEndpointCreator.setSchema(endpointSchemaField.getValue());
        gwtEndpointCreator.setDns(endpointDnsField.getValue());
        gwtEndpointCreator.setPort(endpointPortField.getValue());

        GWT_ENDPOINT_SERVICE.create(gwtEndpointCreator, new AsyncCallback<GwtEndpoint>() {

            @Override
            public void onSuccess(GwtEndpoint arg0) {
                exitStatus = true;
                exitMessage = MSGS.dialogAddConfirmation();
                hide();
            }

            @Override
            public void onFailure(Throwable cause) {
                FailureHandler.handleFormException(formPanel, cause);
                status.hide();
                formPanel.getButtonBar().enable();
                unmask();
                submitButton.enable();
                cancelButton.enable();
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
