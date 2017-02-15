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
package org.eclipse.kapua.app.console.client.user.tabs.role;

import org.eclipse.kapua.app.console.client.messages.ConsoleUserMessages;
import org.eclipse.kapua.app.console.client.ui.dialog.entity.EntityDeleteDialog;
import org.eclipse.kapua.app.console.client.util.DialogUtils;
import org.eclipse.kapua.app.console.shared.model.authorization.GwtAccessRole;
import org.eclipse.kapua.app.console.shared.service.GwtAccessRoleService;
import org.eclipse.kapua.app.console.shared.service.GwtAccessRoleServiceAsync;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class AccessRoleDeleteDialog extends EntityDeleteDialog {

    private static final ConsoleUserMessages MSGS = GWT.create(ConsoleUserMessages.class);

    private static final GwtAccessRoleServiceAsync gwtAccessRoleService = GWT.create(GwtAccessRoleService.class);

    private GwtAccessRole gwtAccessRole;

    public AccessRoleDeleteDialog(GwtAccessRole gwtAccessRole) {
        this.gwtAccessRole = gwtAccessRole;

        DialogUtils.resizeDialog(this, 300, 135);
    }

    @Override
    public String getHeaderMessage() {
        return MSGS.dialogDeleteRoleHeader(gwtAccessRole.getRoleName());
    }

    @Override
    public String getInfoMessage() {
        return MSGS.dialogDeleteRoleInfo();
    }

    @Override
    public void submit() {
        gwtAccessRoleService.delete(xsrfToken, gwtAccessRole.getScopeId(), gwtAccessRole.getId(), new AsyncCallback<Void>() {

            @Override
            public void onSuccess(Void arg0) {
                m_exitStatus = true;
                m_exitMessage = MSGS.dialogDeleteRoleConfirmation();
                hide();
            }

            @Override
            public void onFailure(Throwable cause) {
                m_exitStatus = false;
                m_exitMessage = MSGS.dialogDeleteRoleError(cause.getLocalizedMessage());
                hide();
            }
        });

    }

}
