/*******************************************************************************
 * Copyright (c) 2011, 2016 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *
 *******************************************************************************/
package org.eclipse.kapua.app.console.client.user.tabs.permission;

import org.eclipse.kapua.app.console.client.messages.ConsoleUserMessages;
import org.eclipse.kapua.app.console.client.ui.dialog.entity.EntityDeleteDialog;
import org.eclipse.kapua.app.console.client.util.DialogUtils;
import org.eclipse.kapua.app.console.shared.model.authorization.GwtAccessPermission;
import org.eclipse.kapua.app.console.shared.service.GwtAccessPermissionService;
import org.eclipse.kapua.app.console.shared.service.GwtAccessPermissionServiceAsync;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class PermissionDeleteDialog extends EntityDeleteDialog {

    private static final ConsoleUserMessages MSGS = GWT.create(ConsoleUserMessages.class);

    private static final GwtAccessPermissionServiceAsync gwtAccessPermissionService = GWT.create(GwtAccessPermissionService.class);

    private GwtAccessPermission gwtAccessPermission;

    public PermissionDeleteDialog(GwtAccessPermission gwtAccessPermission) {
        this.gwtAccessPermission = gwtAccessPermission;

        DialogUtils.resizeDialog(this, 300, 135);
    }

    @Override
    public String getHeaderMessage() {
        return MSGS.dialogDeletePermissionHeader(gwtAccessPermission.toString());
    }

    @Override
    public String getInfoMessage() {
        return MSGS.dialogDeletePermissionInfo();
    }

    @Override
    public void submit() {
        gwtAccessPermissionService.delete(xsrfToken, gwtAccessPermission.getScopeId(), gwtAccessPermission.getId(), new AsyncCallback<Void>() {

            @Override
            public void onSuccess(Void arg0) {
                m_exitStatus = true;
                m_exitMessage = MSGS.dialogDeletePermissionConfirmation();
                hide();
            }

            @Override
            public void onFailure(Throwable cause) {
                m_exitStatus = false;
                m_exitMessage = MSGS.dialogDeletePermissionError(cause.getLocalizedMessage());
                hide();
            }
        });

    }

}
