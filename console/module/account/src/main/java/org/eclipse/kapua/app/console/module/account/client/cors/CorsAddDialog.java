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

import org.eclipse.kapua.app.console.module.api.client.messages.ConsoleMessages;
import org.eclipse.kapua.app.console.module.api.client.messages.ValidationMessages;
import org.eclipse.kapua.app.console.module.api.client.ui.dialog.entity.EntityAddEditDialog;
import org.eclipse.kapua.app.console.module.api.client.ui.panel.FormPanel;
import org.eclipse.kapua.app.console.module.api.client.ui.widget.KapuaTextField;
import org.eclipse.kapua.app.console.module.api.client.util.ConsoleInfo;
import org.eclipse.kapua.app.console.module.api.client.util.DialogUtils;
import org.eclipse.kapua.app.console.module.api.client.util.FailureHandler;
import org.eclipse.kapua.app.console.module.api.shared.model.session.GwtSession;
import org.eclipse.kapua.app.console.module.endpoint.client.EndpointModel;
import org.eclipse.kapua.app.console.module.endpoint.client.messages.ConsoleEndpointMessages;
import org.eclipse.kapua.app.console.module.endpoint.shared.model.GwtEndpoint;
import org.eclipse.kapua.app.console.module.endpoint.shared.model.GwtEndpointCreator;
import org.eclipse.kapua.app.console.module.endpoint.shared.service.GwtEndpointService;
import org.eclipse.kapua.app.console.module.endpoint.shared.service.GwtEndpointServiceAsync;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class CorsAddDialog extends EntityAddEditDialog {

    private static final GwtEndpointServiceAsync GWT_ENDPOINT_SERVICE = GWT.create(GwtEndpointService.class);
    protected static final ConsoleEndpointMessages MSGS = GWT.create(ConsoleEndpointMessages.class);
    protected static final ConsoleMessages CMSGS = GWT.create(ConsoleMessages.class);
    protected static final ValidationMessages VAL_MSGS = GWT.create(ValidationMessages.class);

    protected KapuaTextField<String> corsOriginField;

    public CorsAddDialog(GwtSession currentSession) {
        super(currentSession);
        DialogUtils.resizeDialog(this, 400, 150);
    }

    @Override
    public void createBody() {
        submitButton.disable();
        FormPanel endpointFormPanel = new FormPanel(FORM_LABEL_WIDTH);

        corsOriginField = new KapuaTextField<String>();
        corsOriginField.setAllowBlank(false);
        corsOriginField.setFieldLabel("* " + MSGS.corsDialogAddFieldOrigin());
        corsOriginField.setToolTip(MSGS.corsDialogAddFieldOriginTooltip());
        corsOriginField.setMaxLength(64);
        endpointFormPanel.add(corsOriginField);

        bodyPanel.add(endpointFormPanel);
    }

    @Override
    public void submit() {
        GwtEndpointCreator gwtEndpointCreator = new GwtEndpointCreator();
        gwtEndpointCreator.setScopeId(currentSession.getSelectedAccountId());

        GWT_ENDPOINT_SERVICE.parseEndpointModel(gwtEndpointCreator, corsOriginField.getValue(), new AsyncCallback<EndpointModel>() {

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
            public void onSuccess(EndpointModel gwtEndpointCreator) {
                GWT_ENDPOINT_SERVICE.create(xsrfToken, (GwtEndpointCreator) gwtEndpointCreator, new AsyncCallback<GwtEndpoint>() {

                    @Override
                    public void onSuccess(GwtEndpoint gwtEndpoint) {
                        exitStatus = true;
                        exitMessage = MSGS.corsDialogAddConfirmation();
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
                            corsOriginField.markInvalid(VAL_MSGS.DUPLICATE_NAME());
                        }
                    }
                });

            }
        });
    }

    @Override
    public String getHeaderMessage() {
        return MSGS.corsDialogAddHeader();
    }

    @Override
    public String getInfoMessage() {
        return MSGS.corsDialogAddInfo();
    }

}
