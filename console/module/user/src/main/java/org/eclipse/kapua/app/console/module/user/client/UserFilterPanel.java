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

import org.eclipse.kapua.app.console.commons.client.ui.grid.EntityGrid;
import org.eclipse.kapua.app.console.commons.client.ui.panel.EntityFilterPanel;
import org.eclipse.kapua.app.console.commons.client.ui.view.AbstractEntityView;
import org.eclipse.kapua.app.console.commons.shared.model.GwtSession;
import org.eclipse.kapua.app.console.module.user.client.messages.ConsoleUserMessages;
import org.eclipse.kapua.app.console.module.user.shared.model.user.GwtUser;

import com.extjs.gxt.ui.client.widget.Label;
import com.extjs.gxt.ui.client.widget.VerticalPanel;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.google.gwt.core.client.GWT;
import org.eclipse.kapua.app.console.module.user.shared.model.user.GwtUserQuery;

public class UserFilterPanel extends EntityFilterPanel<GwtUser> {

    private final static ConsoleUserMessages USER_MSGS = GWT.create(ConsoleUserMessages.class);
    private final static int WIDTH = 200;

    private final EntityGrid<GwtUser> entityGrid;
    private final GwtSession currentSession;

    private final TextField<String> nameField;

    public UserFilterPanel(AbstractEntityView<GwtUser> entityView, GwtSession currentSession) {
        super(entityView, currentSession);

        entityGrid = entityView.getEntityGrid(entityView, currentSession);
        this.currentSession = currentSession;

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
    }

    @Override
    public void resetFields() {
        nameField.setValue(null);
        GwtUserQuery query = new GwtUserQuery();
        query.setScopeId(currentSession.getSelectedAccountId());
        entityGrid.refresh(query);
    }

    @Override
    public void doFilter() {
        GwtUserQuery query = new GwtUserQuery();
        query.setName(nameField.getValue());
        query.setScopeId(currentSession.getSelectedAccountId());
        entityGrid.refresh(query);
    }

}
