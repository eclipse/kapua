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
 *******************************************************************************/
package org.eclipse.kapua.app.console.client.user.dialog;

import org.eclipse.kapua.app.console.client.messages.ConsoleUserMessages;
import org.eclipse.kapua.app.console.client.ui.dialog.entity.EntityDeleteDialog;
import org.eclipse.kapua.app.console.client.util.DialogUtils;
import org.eclipse.kapua.app.console.shared.model.user.GwtUser;
import org.eclipse.kapua.app.console.shared.service.GwtUserService;
import org.eclipse.kapua.app.console.shared.service.GwtUserServiceAsync;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class UserDeleteDialog extends EntityDeleteDialog {

    private static final ConsoleUserMessages MSGS = GWT.create(ConsoleUserMessages.class);

    private static final GwtUserServiceAsync GWT_USER_SERVICE = GWT.create(GwtUserService.class);

    private GwtUser gwtUser;

    public UserDeleteDialog(GwtUser gwtUser) {
        this.gwtUser = gwtUser;

        DialogUtils.resizeDialog(this, 300, 135);
    }

    @Override
    public String getHeaderMessage() {
        return MSGS.dialogDeleteHeader(gwtUser.getUsername());
    }

    @Override
    public String getInfoMessage() {
        return MSGS.dialogDeleteInfo();
    }

    @Override
    public void submit() {
        GWT_USER_SERVICE.delete(xsrfToken, gwtUser.getScopeId(), gwtUser.getId(), new AsyncCallback<Void>() {

            @Override
            public void onSuccess(Void arg0) {
                exitStatus = true;
                exitMessage = MSGS.dialogDeleteConfirmation();
                hide();
            }

            @Override
            public void onFailure(Throwable cause) {
                exitStatus = false;
                exitMessage = MSGS.dialogDeleteError(cause.getLocalizedMessage());
                hide();
            }
        });

    }

}
