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
import org.eclipse.kapua.app.console.shared.model.GwtGroup;
import org.eclipse.kapua.app.console.shared.model.GwtSession;
import org.eclipse.kapua.app.console.shared.service.GwtGroupService;
import org.eclipse.kapua.app.console.shared.service.GwtGroupServiceAsync;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class GroupEditDialog extends GroupAddDialog {

    private final static GwtGroupServiceAsync gwtGroupService = GWT.create(GwtGroupService.class);
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
        gwtGroupService.update(selectedGroup, new AsyncCallback<GwtGroup>() {

            @Override
            public void onFailure(Throwable arg0) {
                m_exitStatus = false;
                m_exitMessage = MSGS.dialogEditError(arg0.getLocalizedMessage());
                hide();
            }

            @Override
            public void onSuccess(GwtGroup arg0) {
                m_exitStatus = true;
                m_exitMessage = MSGS.dialogEditConfirmation();
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
