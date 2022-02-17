/*******************************************************************************
 * Copyright (c) 2017, 2022 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.app.console.module.authorization.client.group;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import org.eclipse.kapua.app.console.module.api.client.ui.dialog.entity.EntityDeleteDialog;
import org.eclipse.kapua.app.console.module.api.client.util.DialogUtils;
import org.eclipse.kapua.app.console.module.authorization.client.messages.ConsoleGroupMessages;
import org.eclipse.kapua.app.console.module.authorization.shared.model.GwtGroup;
import org.eclipse.kapua.app.console.module.authorization.shared.service.GwtGroupService;
import org.eclipse.kapua.app.console.module.authorization.shared.service.GwtGroupServiceAsync;

public class GroupDeleteDialog extends EntityDeleteDialog {

    private static final ConsoleGroupMessages MSGS = GWT.create(ConsoleGroupMessages.class);

    private static final GwtGroupServiceAsync GWT_GROUP_SERVICE = GWT.create(GwtGroupService.class);

    private GwtGroup gwtGroup;

    public GroupDeleteDialog(GwtGroup gwtGroup) {
        this.gwtGroup = gwtGroup;
        DialogUtils.resizeDialog(this, 300, 135);
        setDisabledFormPanelEvents(true);
    }

    @Override
    public void submit() {
        GWT_GROUP_SERVICE.delete(gwtGroup.getScopeId(), gwtGroup.getId(), new AsyncCallback<Void>() {

            @Override
            public void onFailure(Throwable arg0) {
                exitStatus = false;
                if (!isPermissionErrorMessage(arg0)) {
                    exitMessage = MSGS.dialogDeleteError(arg0.getLocalizedMessage());
                }
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
        return MSGS.dialogDeleteHeader(gwtGroup.getGroupName());
    }

    @Override
    public String getInfoMessage() {
        return MSGS.dialogDeleteInfo();
    }

}
