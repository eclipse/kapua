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
import org.eclipse.kapua.app.console.module.api.client.util.FailureHandler;
import org.eclipse.kapua.app.console.module.api.shared.model.session.GwtSession;
import org.eclipse.kapua.app.console.module.endpoint.client.messages.ConsoleEndpointMessages;
import org.eclipse.kapua.app.console.module.endpoint.shared.model.GwtEndpoint;
import org.eclipse.kapua.app.console.module.endpoint.shared.service.GwtEndpointService;
import org.eclipse.kapua.app.console.module.endpoint.shared.service.GwtEndpointServiceAsync;

public class EndpointEditDialog extends EndpointAddDialog {

    private static final GwtEndpointServiceAsync GWT_ENDPOINT_SERVICE = GWT.create(GwtEndpointService.class);
    private static final ConsoleEndpointMessages MSGS = GWT.create(ConsoleEndpointMessages.class);
    private GwtEndpoint selectedEndpoint;

    public EndpointEditDialog(GwtSession currentSession, GwtEndpoint selectedEndpoint) {
        super(currentSession);
        this.selectedEndpoint = selectedEndpoint;
    }

    @Override
    public void createBody() {

        super.createBody();
        populateEditDialog(selectedEndpoint);
    }

    @Override
    public void submit() {
        selectedEndpoint.setSchema(endpointSchemaField.getValue());
        selectedEndpoint.setDns(endpointDnsField.getValue());
        selectedEndpoint.setPort(endpointPortField.getValue());
        selectedEndpoint.setSecure(endpointSecureCheckboxGroup.getValue() != null);

        GWT_ENDPOINT_SERVICE.update(selectedEndpoint, new AsyncCallback<GwtEndpoint>() {

            @Override
            public void onFailure(Throwable cause) {
                exitStatus = false;
                FailureHandler.handleFormException(formPanel, cause);
                status.hide();
                formPanel.getButtonBar().enable();
                unmask();
                submitButton.enable();
                cancelButton.enable();
            }

            @Override
            public void onSuccess(GwtEndpoint arg0) {
                exitStatus = true;
                exitMessage = MSGS.dialogEditConfirmation();
                hide();
            }
        });
    }

    @Override
    public String getHeaderMessage() {
        return MSGS.dialogEditHeader(selectedEndpoint.toString());
    }

    @Override
    public String getInfoMessage() {
        return MSGS.dialogEditInfo();
    }

    private void populateEditDialog(GwtEndpoint gwtEndpoint) {
        endpointSchemaField.setValue(gwtEndpoint.getSchema());
        endpointDnsField.setValue(gwtEndpoint.getDns());
        endpointPortField.setValue(gwtEndpoint.getPort());
        endpointSercureCheckbox.setValue(gwtEndpoint.getSecure());
    }

}
