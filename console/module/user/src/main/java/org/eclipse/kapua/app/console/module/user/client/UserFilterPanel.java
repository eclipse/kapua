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
package org.eclipse.kapua.app.console.module.user.client;

import com.extjs.gxt.ui.client.widget.Label;
import com.extjs.gxt.ui.client.widget.VerticalPanel;
import com.extjs.gxt.ui.client.widget.form.ComboBox.TriggerAction;
import com.extjs.gxt.ui.client.widget.form.SimpleComboBox;
import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.client.DateTimeFormat;

import org.eclipse.kapua.app.console.module.api.client.ui.grid.EntityGrid;
import org.eclipse.kapua.app.console.module.api.client.ui.panel.EntityFilterPanel;
import org.eclipse.kapua.app.console.module.api.client.ui.view.AbstractEntityView;
import org.eclipse.kapua.app.console.module.api.client.ui.widget.KapuaDateField;
import org.eclipse.kapua.app.console.module.api.client.ui.widget.KapuaTextField;
import org.eclipse.kapua.app.console.module.api.client.util.CssLiterals;
import org.eclipse.kapua.app.console.module.api.shared.model.session.GwtSession;
import org.eclipse.kapua.app.console.module.user.client.messages.ConsoleUserMessages;
import org.eclipse.kapua.app.console.module.user.shared.model.GwtUser;
import org.eclipse.kapua.app.console.module.user.shared.model.GwtUserQuery;

public class UserFilterPanel extends EntityFilterPanel<GwtUser> {

    private static final ConsoleUserMessages USER_MSGS = GWT.create(ConsoleUserMessages.class);
    private static final int WIDTH = 193;
    private static final int MAX_LEN = 255;

    private final EntityGrid<GwtUser> entityGrid;
    private final GwtSession currentSession;

    private final KapuaTextField<String> nameField;
    private final KapuaTextField<String> phoneNumberField;
    private final SimpleComboBox<GwtUser.GwtUserStatus> statusCombo;
    private final SimpleComboBox<GwtUser.GwtUserType> userTypeCombo;
    private final KapuaDateField expirationDate;
    private final KapuaTextField<String> emailField;
    private final KapuaTextField<String> displayNameField;


    public UserFilterPanel(AbstractEntityView<GwtUser> entityView, GwtSession currentSession) {
        super(entityView, currentSession);

        entityGrid = entityView.getEntityGrid(entityView, currentSession);
        this.currentSession = currentSession;

        setHeading(USER_MSGS.filterHeader());

        VerticalPanel fieldsPanel = getFieldsPanel();

        Label clientIdLabel = new Label(USER_MSGS.filterFieldUsernameLabel());
        clientIdLabel.setWidth(WIDTH);
        clientIdLabel.setStyleAttribute(CssLiterals.MARGIN, "5px");

        fieldsPanel.add(clientIdLabel);

        nameField = new KapuaTextField<String>();
        nameField.setName("name");
        nameField.setWidth(WIDTH);
        nameField.setMaxLength(MAX_LEN);
        nameField.setStyleAttribute(CssLiterals.MARGIN_TOP, "0px");
        nameField.setStyleAttribute(CssLiterals.MARGIN_LEFT, "5px");
        nameField.setStyleAttribute(CssLiterals.MARGIN_RIGHT, "5px");
        nameField.setStyleAttribute(CssLiterals.MARGIN_BOTTOM, "10px");
        fieldsPanel.add(nameField);

        Label phoneNumberLabel = new Label(USER_MSGS.filterFieldPhoneNumber());
        phoneNumberLabel.setWidth(WIDTH);
        phoneNumberLabel.setStyleAttribute(CssLiterals.MARGIN, "5px");

        fieldsPanel.add(phoneNumberLabel);

        phoneNumberField = new KapuaTextField<String>();
        phoneNumberField.setName("phoneNumber");
        phoneNumberField.setWidth(WIDTH);
        phoneNumberField.setMaxLength(MAX_LEN);
        phoneNumberField.setStyleAttribute(CssLiterals.MARGIN_TOP, "0px");
        phoneNumberField.setStyleAttribute(CssLiterals.MARGIN_LEFT, "5px");
        phoneNumberField.setStyleAttribute(CssLiterals.MARGIN_RIGHT, "5px");
        phoneNumberField.setStyleAttribute(CssLiterals.MARGIN_BOTTOM, "10px");
        fieldsPanel.add(phoneNumberField);

        Label userStatusLabel = new Label(USER_MSGS.filterFieldStatusLabel());
        userStatusLabel.setWidth(WIDTH);
        userStatusLabel.setStyleAttribute(CssLiterals.MARGIN, "5px");

        fieldsPanel.add(userStatusLabel);

        statusCombo = new SimpleComboBox<GwtUser.GwtUserStatus>();
        statusCombo.setName("status");
        statusCombo.setWidth(WIDTH);
        statusCombo.setStyleAttribute(CssLiterals.MARGIN_TOP, "0px");
        statusCombo.setStyleAttribute(CssLiterals.MARGIN_LEFT, "5px");
        statusCombo.setStyleAttribute(CssLiterals.MARGIN_RIGHT, "5px");
        statusCombo.setStyleAttribute(CssLiterals.MARGIN_BOTTOM, "10px");
        statusCombo.add(GwtUser.GwtUserStatus.ANY);
        statusCombo.add(GwtUser.GwtUserStatus.ENABLED);
        statusCombo.add(GwtUser.GwtUserStatus.DISABLED);
        statusCombo.setEditable(false);
        statusCombo.setTriggerAction(TriggerAction.ALL);
        statusCombo.setSimpleValue(GwtUser.GwtUserStatus.ANY);

        fieldsPanel.add(statusCombo);

        Label userTypeLabel = new Label(USER_MSGS.filterFieldUserTypeLabel());
        userTypeCombo = new SimpleComboBox<GwtUser.GwtUserType>();
        if (currentSession.isSsoEnabled()) {

            userTypeLabel.setWidth(WIDTH);
            userTypeLabel.setStyleAttribute("margin", "5px");

            fieldsPanel.add(userTypeLabel);

            userTypeCombo.setName("userType");
            userTypeCombo.setWidth(WIDTH);
            userTypeCombo.setStyleAttribute("margin-top", "0px");
            userTypeCombo.setStyleAttribute("margin-left", "5px");
            userTypeCombo.setStyleAttribute("margin-right", "5px");
            userTypeCombo.setStyleAttribute("margin-bottom", "10px");
            userTypeCombo.add(GwtUser.GwtUserType.ANY);
            userTypeCombo.add(GwtUser.GwtUserType.INTERNAL);
            userTypeCombo.add(GwtUser.GwtUserType.EXTERNAL);
            userTypeCombo.setEditable(false);
            userTypeCombo.setTriggerAction(TriggerAction.ALL);
            userTypeCombo.setSimpleValue(GwtUser.GwtUserType.ANY);

            fieldsPanel.add(userTypeCombo);
        } else {
            userTypeCombo.hide();
            userTypeCombo.disable();
        }

        Label expirationDateLabel = new Label(USER_MSGS.filterFieldExpirationDate());
        expirationDateLabel.setWidth(WIDTH);
        expirationDateLabel.setStyleAttribute(CssLiterals.MARGIN, "5px");

        fieldsPanel.add(expirationDateLabel);

        expirationDate = new KapuaDateField();
        expirationDate.setName("expirationDate");
        expirationDate.setFormatValue(true);
        expirationDate.getPropertyEditor().setFormat(DateTimeFormat.getFormat("dd/MM/yyyy"));
        expirationDate.setMaxLength(10);
        expirationDate.setWidth(WIDTH);
        expirationDate.setStyleAttribute(CssLiterals.MARGIN_TOP, "0px");
        expirationDate.setStyleAttribute(CssLiterals.MARGIN_LEFT, "5px");
        expirationDate.setStyleAttribute(CssLiterals.MARGIN_RIGHT, "5px");
        expirationDate.setStyleAttribute(CssLiterals.MARGIN_BOTTOM, "10px");

        fieldsPanel.add(expirationDate);

        Label emailLabel = new Label(USER_MSGS.filterFieldEmail());
        emailLabel.setWidth(WIDTH);
        emailLabel.setStyleAttribute(CssLiterals.MARGIN, "5px");

        fieldsPanel.add(emailLabel);

        emailField = new KapuaTextField<String>();
        emailField.setName("email");
        emailField.setWidth(WIDTH);
        emailField.setMaxLength(MAX_LEN);
        emailField.setStyleAttribute(CssLiterals.MARGIN_TOP, "0px");
        emailField.setStyleAttribute(CssLiterals.MARGIN_LEFT, "5px");
        emailField.setStyleAttribute(CssLiterals.MARGIN_RIGHT, "5px");
        emailField.setStyleAttribute(CssLiterals.MARGIN_BOTTOM, "10px");

        fieldsPanel.add(emailField);

        Label displayNameLabel = new Label(USER_MSGS.filterFieldDisplayName());
        displayNameLabel.setWidth(WIDTH);
        displayNameLabel.setStyleAttribute(CssLiterals.MARGIN, "5px");
        fieldsPanel.add(displayNameLabel);

        displayNameField = new KapuaTextField<String>();
        displayNameField.setName("displayName");
        displayNameField.setWidth(WIDTH);
        displayNameField.setMaxLength(MAX_LEN);
        displayNameField.setStyleAttribute(CssLiterals.MARGIN_TOP, "0px");
        displayNameField.setStyleAttribute(CssLiterals.MARGIN_LEFT, "5px");
        displayNameField.setStyleAttribute(CssLiterals.MARGIN_RIGHT, "5px");
        displayNameField.setStyleAttribute(CssLiterals.MARGIN_BOTTOM, "10px");

        fieldsPanel.add(displayNameField);

    }

    @Override
    public void resetFields() {
        nameField.setValue(null);
        statusCombo.setSimpleValue(GwtUser.GwtUserStatus.ANY);
        displayNameField.setValue(null);
        phoneNumberField.setValue(null);
        expirationDate.setValue(null);
        emailField.setValue(null);
        if (currentSession.isSsoEnabled()) {
            userTypeCombo.setSimpleValue(GwtUser.GwtUserType.ANY);
        }

        GwtUserQuery query = new GwtUserQuery();
        query.setScopeId(currentSession.getSelectedAccountId());
        entityGrid.refresh(query);
    }

    @Override
    public void doFilter() {
        GwtUserQuery query = new GwtUserQuery();
        query.setName(nameField.getValue());
        query.setUserStatus(statusCombo.getSimpleValue().toString());
        query.setScopeId(currentSession.getSelectedAccountId());
        query.setDisplayName(displayNameField.getValue());
        query.setPhoneNumber(phoneNumberField.getValue());
        query.setExpirationDate(expirationDate.getValue());
        query.setEmail(emailField.getValue());
        if (currentSession.isSsoEnabled()) {
            query.setUserType(userTypeCombo.getSimpleValue().toString());
        }

        entityGrid.refresh(query);
    }
}
