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
package org.eclipse.kapua.app.console.module.authorization.client.role;

import org.eclipse.kapua.app.console.module.api.client.resources.icons.IconSet;
import org.eclipse.kapua.app.console.module.api.client.resources.icons.KapuaIcon;
import org.eclipse.kapua.app.console.module.api.client.ui.grid.EntityGrid;
import org.eclipse.kapua.app.console.module.api.client.ui.panel.EntityFilterPanel;
import org.eclipse.kapua.app.console.module.api.client.ui.view.AbstractEntityView;
import org.eclipse.kapua.app.console.module.api.shared.model.GwtSession;
import org.eclipse.kapua.app.console.module.authorization.client.messages.ConsoleRoleMessages;
import org.eclipse.kapua.app.console.module.authorization.shared.model.GwtRole;
import org.eclipse.kapua.app.console.module.authorization.shared.model.GwtRoleQuery;

import com.extjs.gxt.ui.client.widget.Label;
import com.extjs.gxt.ui.client.widget.VerticalPanel;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.google.gwt.core.client.GWT;

public class RoleFilterPanel extends EntityFilterPanel<GwtRole> {

    private final static ConsoleRoleMessages ROLE_MSGS = GWT.create(ConsoleRoleMessages.class);
    private final static int WIDTH = 200;

    private final EntityGrid<GwtRole> entityGrid;
    private final GwtSession currentSession;

    private final TextField<String> nameField;

    public RoleFilterPanel(AbstractEntityView<GwtRole> entityView, GwtSession currentSession) {
        super(entityView, currentSession);

        entityGrid = entityView.getEntityGrid(entityView, currentSession);
        this.currentSession = currentSession;

        setIcon(new KapuaIcon(IconSet.FILTER));
        setHeading(ROLE_MSGS.filterHeader());

        VerticalPanel fieldsPanel = getFieldsPanel();

        final Label roleNameLabel = new Label(ROLE_MSGS.filterFieldRoleNameLabel());
        roleNameLabel.setWidth(WIDTH);
        roleNameLabel.setStyleAttribute("margin", "5px");

        fieldsPanel.add(roleNameLabel);

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
        GwtRoleQuery query = new GwtRoleQuery();
        query.setScopeId(currentSession.getSelectedAccountId());
        entityGrid.refresh(query);
    }

    @Override
    public void doFilter() {
        GwtRoleQuery query = new GwtRoleQuery();
        query.setName(nameField.getValue());
        query.setScopeId(currentSession.getSelectedAccountId());
        entityGrid.refresh(query);
    }

}
