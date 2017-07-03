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
 *******************************************************************************/
package org.eclipse.kapua.app.console.client.connection;

import org.eclipse.kapua.app.console.client.messages.ConsoleConnectionMessages;
import org.eclipse.kapua.app.console.client.ui.dialog.entity.EntityAddEditDialog;
import org.eclipse.kapua.app.console.client.ui.panel.FormPanel;
import org.eclipse.kapua.app.console.client.util.Constants;
import org.eclipse.kapua.app.console.client.util.DialogUtils;
import org.eclipse.kapua.app.console.client.util.FailureHandler;
import org.eclipse.kapua.app.console.shared.model.GwtSession;
import org.eclipse.kapua.app.console.shared.model.connection.GwtDeviceConnection;
import org.eclipse.kapua.app.console.shared.model.connection.GwtDeviceConnection.GwtConnectionUserCouplingMode;
import org.eclipse.kapua.app.console.shared.model.connection.GwtDeviceConnectionOption;
import org.eclipse.kapua.app.console.shared.model.user.GwtUser;
import org.eclipse.kapua.app.console.shared.service.GwtDeviceConnectionOptionService;
import org.eclipse.kapua.app.console.shared.service.GwtDeviceConnectionOptionServiceAsync;
import org.eclipse.kapua.app.console.shared.service.GwtUserService;
import org.eclipse.kapua.app.console.shared.service.GwtUserServiceAsync;

import com.extjs.gxt.ui.client.data.ListLoadResult;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.form.CheckBox;
import com.extjs.gxt.ui.client.widget.form.ComboBox;
import com.extjs.gxt.ui.client.widget.form.ComboBox.TriggerAction;
import com.extjs.gxt.ui.client.widget.layout.FormLayout;
import com.extjs.gxt.ui.client.widget.form.FieldSet;
import com.extjs.gxt.ui.client.widget.form.SimpleComboBox;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class ConnectionEditDialog extends EntityAddEditDialog {

    private final static GwtUserServiceAsync GWT_USER_SERVICE = GWT.create(GwtUserService.class);
    private final static GwtDeviceConnectionOptionServiceAsync GWT_CONNECTION_OPTION_SERVICE = GWT.create(GwtDeviceConnectionOptionService.class);
    private final static ConsoleConnectionMessages MSGS = GWT.create(ConsoleConnectionMessages.class);

    private GwtDeviceConnection selectedDeviceConnection;
    // Security Options fields
    private SimpleComboBox<String> couplingModeCombo;
    private ComboBox<GwtUser> reservedUserCombo;
    private CheckBox allowUserChangeCheckbox;
    private TextField<String> lastUserField;

    private static final GwtUser NO_USER;

    static {
        NO_USER = new GwtUser();
        NO_USER.setUsername(MSGS.connectionFormReservedUserNoUser());
        NO_USER.setId(null);
    }

    public ConnectionEditDialog(GwtSession currentSession, GwtDeviceConnection selectedDeviceConnection) {
        super(currentSession);
        DialogUtils.resizeDialog(this, 440, 260);
        this.selectedDeviceConnection = selectedDeviceConnection;
    }

    @Override
    public void createBody() {
        FormPanel groupFormPanel = new FormPanel(FORM_LABEL_WIDTH);
        FieldSet fieldSetSecurityOptions = new FieldSet();
        FormLayout layoutSecurityOptions = new FormLayout();
        layoutSecurityOptions.setLabelWidth(Constants.LABEL_WIDTH_DEVICE_FORM);
        fieldSetSecurityOptions.setLayout(layoutSecurityOptions);
        fieldSetSecurityOptions.setHeading(MSGS.connectionFormFieldsetSecurityOptions());

        lastUserField = new TextField<String>();
        lastUserField.setName("connectionUserLastUserField");
        lastUserField.setFieldLabel(MSGS.connectionFormLastUser());
        lastUserField.setToolTip(MSGS.connectionFormLastUserTooltip());
        lastUserField.setWidth(225);
        lastUserField.setReadOnly(true);
        fieldSetSecurityOptions.add(lastUserField);

        // Connection user coupling mode
        couplingModeCombo = new SimpleComboBox<String>();
        couplingModeCombo.setName("connectionUserCouplingModeCombo");
        couplingModeCombo.setEditable(false);
        couplingModeCombo.setTypeAhead(false);
        couplingModeCombo.setAllowBlank(false);
        couplingModeCombo.setFieldLabel(MSGS.connectionFormUserCouplingMode());
        couplingModeCombo.setToolTip(MSGS.connectionFormUserCouplingModeTooltip());
        couplingModeCombo.setTriggerAction(TriggerAction.ALL);

        couplingModeCombo.add(GwtConnectionUserCouplingMode.INHERITED.getLabel());
        couplingModeCombo.add(GwtConnectionUserCouplingMode.LOOSE.getLabel());
        couplingModeCombo.add(GwtConnectionUserCouplingMode.STRICT.getLabel());

        couplingModeCombo.setSimpleValue(GwtConnectionUserCouplingMode.INHERITED.getLabel());
        fieldSetSecurityOptions.add(couplingModeCombo);

        // Device User
        GWT_USER_SERVICE.findAll(currentSession.getSelectedAccount().getId(), new AsyncCallback<ListLoadResult<GwtUser>>() {

            @Override
            public void onFailure(Throwable caught) {
                FailureHandler.handle(caught);
            }

            @Override
            public void onSuccess(ListLoadResult<GwtUser> result) {
                reservedUserCombo.getStore().removeAll();
                reservedUserCombo.getStore().add(NO_USER);
                for (GwtUser gwtUser : result.getData()) {
                    reservedUserCombo.getStore().add(gwtUser);
                }
                setReservedUser();
            }

        });

        reservedUserCombo = new ComboBox<GwtUser>();
        reservedUserCombo.setName("connectionUserReservedUserCombo");
        reservedUserCombo.setEditable(false);
        reservedUserCombo.setTypeAhead(false);
        reservedUserCombo.setAllowBlank(false);
        reservedUserCombo.setFieldLabel(MSGS.connectionFormReservedUser());
        reservedUserCombo.setTriggerAction(TriggerAction.ALL);
        reservedUserCombo.setStore(new ListStore<GwtUser>());
        reservedUserCombo.setDisplayField("username");
        reservedUserCombo.setValueField("id");
        fieldSetSecurityOptions.add(reservedUserCombo);

        // Allow credential change
        allowUserChangeCheckbox = new CheckBox();
        allowUserChangeCheckbox.setName("connectionUserAllowUserChangeCheckbox");
        allowUserChangeCheckbox.setFieldLabel(MSGS.connectionFormAllowUserChange());
        allowUserChangeCheckbox.setToolTip(MSGS.connectionFormAllowUserChangeTooltip());
        allowUserChangeCheckbox.setBoxLabel("");
        fieldSetSecurityOptions.add(allowUserChangeCheckbox);

        groupFormPanel.add(fieldSetSecurityOptions);

        bodyPanel.add(groupFormPanel);
        populateEditDialog(selectedDeviceConnection);
    }

    @Override
    public void submit() {
        // convert the connection to connection option
        GwtDeviceConnectionOption selectedDeviceConnectionOption = new GwtDeviceConnectionOption(selectedDeviceConnection);
        selectedDeviceConnectionOption.setAllowUserChange(allowUserChangeCheckbox.getValue());
        selectedDeviceConnectionOption.setConnectionUserCouplingMode(couplingModeCombo.getValue() != null ? couplingModeCombo.getValue().getValue() : null);
        selectedDeviceConnectionOption.setReservedUserId(reservedUserCombo.getValue() != null ? reservedUserCombo.getValue().getId() : null);

        GWT_CONNECTION_OPTION_SERVICE.update(xsrfToken, selectedDeviceConnectionOption, new AsyncCallback<GwtDeviceConnectionOption>() {

            @Override
            public void onFailure(Throwable arg0) {
                exitStatus = false;
                exitMessage = MSGS.dialogEditError(arg0.getLocalizedMessage());
                hide();
            }

            @Override
            public void onSuccess(GwtDeviceConnectionOption arg0) {
                exitStatus = true;
                exitMessage = MSGS.dialogEditConfirmation();
                hide();
            }
        });
    }

    @Override
    public String getHeaderMessage() {
        return MSGS.dialogEditHeader(selectedDeviceConnection.getClientId());
    }

    @Override
    public String getInfoMessage() {
        return MSGS.dialogEditInfo();
    }

    private void populateEditDialog(GwtDeviceConnection gwtDeviceConnection) {
        if (gwtDeviceConnection.getUserId() != null) {
            GWT_USER_SERVICE.find(currentSession.getSelectedAccount().getId(), gwtDeviceConnection.getUserId(), new AsyncCallback<GwtUser>() {

                @Override
                public void onFailure(Throwable caught) {
                    FailureHandler.handle(caught);
                }

                @Override
                public void onSuccess(GwtUser gwtUser) {
                    if (gwtUser != null) {
                        lastUserField.setValue(gwtUser.getUsername());
                    } else {
                        lastUserField.setValue("N/A");
                    }
                }
            });
        } else {
            lastUserField.setValue("N/A");
        }
        GwtConnectionUserCouplingMode gwtConnectionUserCouplingMode = null;
        if (gwtDeviceConnection.getConnectionUserCouplingMode() != null) {
            gwtConnectionUserCouplingMode = GwtConnectionUserCouplingMode.getEnumFromLabel(gwtDeviceConnection.getConnectionUserCouplingMode());
        }
        couplingModeCombo.setSimpleValue(gwtConnectionUserCouplingMode != null ? gwtConnectionUserCouplingMode.getLabel() : "N/A");
        allowUserChangeCheckbox.setValue(gwtDeviceConnection.getAllowUserChange());
     }

    private void setReservedUser() {
        for (GwtUser gwtUser : reservedUserCombo.getStore().getModels()) {
            if (gwtUser.getId() == null) {
                if (selectedDeviceConnection.getReservedUserId() == null) {
                    reservedUserCombo.setValue(gwtUser);
                }
            } else if (gwtUser.getId().equals(selectedDeviceConnection.getReservedUserId())) {
                reservedUserCombo.setValue(gwtUser);
            }
        }
    }

}
