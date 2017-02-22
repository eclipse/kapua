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
import org.eclipse.kapua.app.console.client.ui.dialog.entity.EntityAddEditDialog;
import org.eclipse.kapua.app.console.client.ui.panel.FormPanel;
import org.eclipse.kapua.app.console.client.util.DialogUtils;
import org.eclipse.kapua.app.console.shared.model.GwtGroup;
import org.eclipse.kapua.app.console.shared.model.GwtSession;
import org.eclipse.kapua.app.console.shared.service.GwtGroupService;
import org.eclipse.kapua.app.console.shared.service.GwtGroupServiceAsync;

import com.extjs.gxt.ui.client.widget.form.TextField;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class GroupAddDialog extends EntityAddEditDialog {

    private final static GwtGroupServiceAsync gwtGroupService = GWT.create(GwtGroupService.class);
    private final static ConsoleGroupMessages MSGS = GWT.create(ConsoleGroupMessages.class);

    protected TextField<String> groupNameField;

    public GroupAddDialog(GwtSession currentSession) {
        super(currentSession);
        DialogUtils.resizeDialog(this, 400, 200);
    }

    @Override
    public void createBody() {
        FormPanel groupFormPanel = new FormPanel(FORM_LABEL_WIDTH);
        groupNameField = new TextField<String>();
        groupNameField.setAllowBlank(false);
        groupNameField.setFieldLabel(MSGS.dialogAddFieldName());
        groupNameField.setToolTip(MSGS.dialogAddFieldNameTooltip());
        groupFormPanel.add(groupNameField);
        m_bodyPanel.add(groupFormPanel);
    }

    @Override
    public void submit() {
        GwtGroupCreator gwtGroupCreator = new GwtGroupCreator();
        gwtGroupCreator.setScopeId(currentSession.getSelectedAccount().getId());
        gwtGroupCreator.setName(groupNameField.getValue());
        gwtGroupService.create(gwtGroupCreator, new AsyncCallback<GwtGroup>() {

            @Override
            public void onSuccess(GwtGroup arg0) {
                m_exitStatus = true;
                m_exitMessage = MSGS.dialogAddConfirmation();
                hide();
            }

            @Override
            public void onFailure(Throwable arg0) {
                m_exitStatus = false;
                m_exitMessage = MSGS.dialogAddError(arg0.getLocalizedMessage());
            }
        });

    }

    @Override
    public String getHeaderMessage() {
        return MSGS.dialogAddHeader();
    }

    @Override
    public String getInfoMessage() {
        return MSGS.dialogAddInfo();
    }

}