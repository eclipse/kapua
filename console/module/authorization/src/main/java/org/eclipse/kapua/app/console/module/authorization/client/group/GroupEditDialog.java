/*******************************************************************************
 * Copyright (c) 2017 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.app.console.module.authorization.client.group;

import org.eclipse.kapua.app.console.module.authorization.client.messages.ConsoleGroupMessages;
import org.eclipse.kapua.app.console.module.authorization.shared.model.GwtGroup;
import org.eclipse.kapua.app.console.module.api.shared.model.GwtSession;
import org.eclipse.kapua.app.console.module.authorization.shared.service.GwtGroupService;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import org.eclipse.kapua.app.console.module.authorization.shared.service.GwtGroupServiceAsync;

public class GroupEditDialog extends GroupAddDialog {

    private final static GwtGroupServiceAsync GWT_GROUP_SERVICE = GWT.create(GwtGroupService.class);
    private final static ConsoleGroupMessages MSGS = GWT.create(ConsoleGroupMessages.class);
    private GwtGroup selectedGroup;

    public GroupEditDialog(GwtSession currentSession, GwtGroup selectedGroup) {
        super(currentSession);
        this.selectedGroup = selectedGroup;
    }

    @Override
    public void createBody() {

        super.createBody();
        populateEditDialog(selectedGroup);
    }

    @Override
    public void submit() {
        selectedGroup.setGroupName(groupNameField.getValue());
        GWT_GROUP_SERVICE.update(selectedGroup, new AsyncCallback<GwtGroup>() {

            @Override
            public void onFailure(Throwable arg0) {
                exitStatus = false;
                exitMessage = MSGS.dialogEditError(arg0.getLocalizedMessage());
                hide();
            }

            @Override
            public void onSuccess(GwtGroup arg0) {
                exitStatus = true;
                exitMessage = MSGS.dialogEditConfirmation();
                hide();
            }
        });
    }

    @Override
    public String getHeaderMessage() {
        return MSGS.dialogEditHeader(selectedGroup.getGroupName());
    }

    @Override
    public String getInfoMessage() {
        return MSGS.dialogEditInfo();
    }

    private void populateEditDialog(GwtGroup gwtGroup) {
        groupNameField.setValue(gwtGroup.getGroupName());

    }

}
