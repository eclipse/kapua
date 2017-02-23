/*******************************************************************************
 * Copyright (c) 2011, 2017 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.app.console.client.group;

import org.eclipse.kapua.app.console.client.messages.ConsoleGroupMessages;
import org.eclipse.kapua.app.console.client.ui.dialog.entity.EntityDeleteDialog;
import org.eclipse.kapua.app.console.client.util.DialogUtils;
import org.eclipse.kapua.app.console.client.util.FailureHandler;
import org.eclipse.kapua.app.console.shared.model.GwtGroup;
import org.eclipse.kapua.app.console.shared.service.GwtGroupService;
import org.eclipse.kapua.app.console.shared.service.GwtGroupServiceAsync;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class GroupDeleteDialog extends EntityDeleteDialog {

    private static final GwtGroupServiceAsync gwtGroupService = GWT.create(GwtGroupService.class);
    private GwtGroup gwtGroup;
    private final static ConsoleGroupMessages MSGS = GWT.create(ConsoleGroupMessages.class);


    public GroupDeleteDialog(GwtGroup gwtGroup) {
        this.gwtGroup = gwtGroup;
        DialogUtils.resizeDialog(this, 300, 135);
    }

    @Override
    public void submit() {
        gwtGroupService.delete(gwtGroup.getScopeId(), gwtGroup.getId(), new AsyncCallback<Void>() {

            @Override
            public void onFailure(Throwable arg0) {
                FailureHandler.handle(arg0);
                m_exitStatus = false;
                m_exitMessage = MSGS.dialogDeleteError(arg0.getLocalizedMessage());
                hide();

            }

            @Override
            public void onSuccess(Void arg0) {
                m_exitStatus = true;
                m_exitMessage = MSGS.dialogDeleteConfirmation();
                hide();
            }
        });

    }

    @Override
    public String getHeaderMessage() {
        return MSGS.dialogDeleteHeader(gwtGroup.getGroupName());
    }

    @Override
    public String getInfoMessage() {
        return MSGS.dialogDeleteInfo();
    }

}
