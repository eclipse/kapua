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
import org.eclipse.kapua.app.console.module.api.client.ui.dialog.entity.EntityDeleteDialog;
import org.eclipse.kapua.app.console.module.api.client.util.DialogUtils;
import org.eclipse.kapua.app.console.module.api.client.util.FailureHandler;
import org.eclipse.kapua.app.console.module.endpoint.client.messages.ConsoleEndpointMessages;
import org.eclipse.kapua.app.console.module.endpoint.shared.model.GwtEndpoint;
import org.eclipse.kapua.app.console.module.endpoint.shared.service.GwtEndpointService;
import org.eclipse.kapua.app.console.module.endpoint.shared.service.GwtEndpointServiceAsync;

public class EndpointDeleteDialog extends EntityDeleteDialog {

    private static final ConsoleEndpointMessages MSGS = GWT.create(ConsoleEndpointMessages.class);

    private static final GwtEndpointServiceAsync GWT_ENDPOINT_SERVICE = GWT.create(GwtEndpointService.class);

    private GwtEndpoint gwtEndpoint;

    public EndpointDeleteDialog(GwtEndpoint gwtEndpoint) {
        this.gwtEndpoint = gwtEndpoint;
        DialogUtils.resizeDialog(this, 300, 135);
    }

    @Override
    public void submit() {
        GWT_ENDPOINT_SERVICE.delete(gwtEndpoint.getScopeId(), gwtEndpoint.getId(), new AsyncCallback<Void>() {

            @Override
            public void onFailure(Throwable arg0) {
                FailureHandler.handle(arg0);
                exitStatus = false;
                exitMessage = MSGS.dialogDeleteError(arg0.getLocalizedMessage());
                hide();

            }

            @Override
            public void onSuccess(Void arg0) {
                exitStatus = true;
                exitMessage = MSGS.dialogDeleteConfirmation();
                hide();
            }
        });

    }

    @Override
    public String getHeaderMessage() {
        return MSGS.dialogDeleteHeader(gwtEndpoint.toString());
    }

    @Override
    public String getInfoMessage() {
        return MSGS.dialogDeleteInfo();
    }

}
