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
package org.eclipse.kapua.app.console.module.account.client.cors;

import org.eclipse.kapua.app.console.module.api.client.util.ConsoleInfo;
import org.eclipse.kapua.app.console.module.api.client.util.FailureHandler;
import org.eclipse.kapua.app.console.module.api.shared.model.session.GwtSession;
import org.eclipse.kapua.app.console.module.endpoint.client.EndpointModel;
import org.eclipse.kapua.app.console.module.endpoint.shared.model.GwtEndpoint;
import org.eclipse.kapua.app.console.module.endpoint.shared.service.GwtEndpointService;
import org.eclipse.kapua.app.console.module.endpoint.shared.service.GwtEndpointServiceAsync;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class CorsEditDialog extends CorsAddDialog {

    private static final GwtEndpointServiceAsync GWT_ENDPOINT_SERVICE = GWT.create(GwtEndpointService.class);
    private final GwtEndpoint selectedEndpoint;

    public CorsEditDialog(GwtSession currentSession, GwtEndpoint selectedEndpoint) {
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
        GWT_ENDPOINT_SERVICE.parseEndpointModel(selectedEndpoint, corsOriginField.getValue(), new AsyncCallback<EndpointModel>() {

            @Override
            public void onFailure(Throwable throwable) {
                exitStatus = false;
                status.hide();
                formPanel.getButtonBar().enable();
                unmask();
                submitButton.enable();
                cancelButton.enable();
                ConsoleInfo.display(CMSGS.error(), MSGS.corsDialogUnableToParse());
                corsOriginField.markInvalid(MSGS.corsDialogUnableToParse());
            }

            @Override
            public void onSuccess(EndpointModel gwtEndpoint) {

                GWT_ENDPOINT_SERVICE.update(xsrfToken, (GwtEndpoint) gwtEndpoint, new AsyncCallback<GwtEndpoint>() {

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
                            corsOriginField.markInvalid(VAL_MSGS.DUPLICATE_NAME());
                        }

                    }

                    @Override
                    public void onSuccess(GwtEndpoint arg0) {
                        exitStatus = true;
                        exitMessage = MSGS.corsDialogEditConfirmation();
                        hide();
                    }
                });
            }
        });
    }

    @Override
    public String getHeaderMessage() {
        return MSGS.corsDialogEditHeader(selectedEndpoint.toString());
    }

    @Override
    public String getInfoMessage() {
        return MSGS.corsDialogEditInfo();
    }

    private void populateEditDialog(GwtEndpoint gwtEndpoint) {
        corsOriginField.setValue(gwtEndpoint.toString());
    }

}
