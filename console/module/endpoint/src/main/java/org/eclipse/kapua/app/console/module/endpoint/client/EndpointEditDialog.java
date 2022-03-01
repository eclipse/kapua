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

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;

import org.eclipse.kapua.app.console.module.api.client.messages.ValidationMessages;
import org.eclipse.kapua.app.console.module.api.client.util.FailureHandler;
import org.eclipse.kapua.app.console.module.api.shared.model.session.GwtSession;
import org.eclipse.kapua.app.console.module.endpoint.client.messages.ConsoleEndpointMessages;
import org.eclipse.kapua.app.console.module.endpoint.shared.model.GwtEndpoint;
import org.eclipse.kapua.app.console.module.endpoint.shared.service.GwtEndpointService;
import org.eclipse.kapua.app.console.module.endpoint.shared.service.GwtEndpointServiceAsync;

public class EndpointEditDialog extends EndpointAddDialog {

    private static final GwtEndpointServiceAsync GWT_ENDPOINT_SERVICE = GWT.create(GwtEndpointService.class);
    private static final ConsoleEndpointMessages MSGS = GWT.create(ConsoleEndpointMessages.class);
    private static final ValidationMessages VAL_MSGS = GWT.create(ValidationMessages.class);
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

        GWT_ENDPOINT_SERVICE.update(xsrfToken, selectedEndpoint, new AsyncCallback<GwtEndpoint>() {

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
        endpointSecureCheckbox.setValue(gwtEndpoint.getSecure());
    }

}
