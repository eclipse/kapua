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
package org.eclipse.kapua.app.console.module.user.client;

import org.eclipse.kapua.app.console.module.api.client.resources.icons.IconSet;
import org.eclipse.kapua.app.console.module.api.client.resources.icons.KapuaIcon;
import org.eclipse.kapua.app.console.module.api.client.ui.grid.EntityGrid;
import org.eclipse.kapua.app.console.module.api.client.ui.panel.EntityFilterPanel;
import org.eclipse.kapua.app.console.module.api.client.ui.view.AbstractEntityView;
import org.eclipse.kapua.app.console.module.api.shared.model.GwtSession;
import org.eclipse.kapua.app.console.module.user.client.messages.ConsoleUserMessages;
import org.eclipse.kapua.app.console.module.user.shared.model.user.GwtUser;

import com.extjs.gxt.ui.client.widget.Label;
import com.extjs.gxt.ui.client.widget.VerticalPanel;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.google.gwt.core.client.GWT;
import com.extjs.gxt.ui.client.widget.form.ComboBox.TriggerAction;
import com.extjs.gxt.ui.client.widget.form.SimpleComboBox;
import org.eclipse.kapua.app.console.module.user.shared.model.user.GwtUserQuery;

public class UserFilterPanel extends EntityFilterPanel<GwtUser> {

    private final static ConsoleUserMessages USER_MSGS = GWT.create(ConsoleUserMessages.class);
    private final static int WIDTH = 200;

    private final EntityGrid<GwtUser> entityGrid;
    private final GwtSession currentSession;

    private final TextField<String> nameField;
    private final SimpleComboBox<GwtUser.GwtUserStatus> statusCombo;

    public UserFilterPanel(AbstractEntityView<GwtUser> entityView, GwtSession currentSession) {
        super(entityView, currentSession);

        entityGrid = entityView.getEntityGrid(entityView, currentSession);
        this.currentSession = currentSession;
        setIcon(new KapuaIcon(IconSet.FILTER));

        setHeading(USER_MSGS.filterHeader());

        VerticalPanel fieldsPanel = getFieldsPanel();

        final Label clientIdLabel = new Label(USER_MSGS.filterFieldUsernameLabel());
        clientIdLabel.setWidth(WIDTH);
        clientIdLabel.setStyleAttribute("margin", "5px");

        fieldsPanel.add(clientIdLabel);

        nameField = new TextField<String>();
        nameField.setName("name");
        nameField.setWidth(WIDTH);
        nameField.setStyleAttribute("margin-top", "0px");
        nameField.setStyleAttribute("margin-left", "5px");
        nameField.setStyleAttribute("margin-right", "5px");
        nameField.setStyleAttribute("margin-bottom", "10px");
        fieldsPanel.add(nameField);

        final Label userStatusLabel = new Label(USER_MSGS.filterFieldStatusLabel());
        userStatusLabel.setWidth(WIDTH);
        userStatusLabel.setStyleAttribute("margin", "5px");

        fieldsPanel.add(userStatusLabel);

        statusCombo = new SimpleComboBox<GwtUser.GwtUserStatus>();
        statusCombo.setName("status");
        statusCombo.setWidth(WIDTH);
        statusCombo.setStyleAttribute("margin-top", "0px");
        statusCombo.setStyleAttribute("margin-left", "5px");
        statusCombo.setStyleAttribute("margin-right", "5px");
        statusCombo.setStyleAttribute("margin-bottom", "10px");
        statusCombo.add(GwtUser.GwtUserStatus.ANY);
        statusCombo.add(GwtUser.GwtUserStatus.ENABLED);
        statusCombo.add(GwtUser.GwtUserStatus.DISABLED);
        statusCombo.setEditable(false);
        statusCombo.setTriggerAction(TriggerAction.ALL);
        statusCombo.setSimpleValue(GwtUser.GwtUserStatus.ANY);

        fieldsPanel.add(statusCombo);
    }

    @Override
    public void resetFields() {
        nameField.setValue(null);
        statusCombo.setValue(null);
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
        entityGrid.refresh(query);
    }

}
