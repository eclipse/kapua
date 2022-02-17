/*******************************************************************************
 * Copyright (c) 2016, 2022 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.app.console.module.authorization.client.role;

import com.extjs.gxt.ui.client.widget.Label;
import com.extjs.gxt.ui.client.widget.VerticalPanel;
import com.google.gwt.core.client.GWT;
import org.eclipse.kapua.app.console.module.api.client.ui.grid.EntityGrid;
import org.eclipse.kapua.app.console.module.api.client.ui.panel.EntityFilterPanel;
import org.eclipse.kapua.app.console.module.api.client.ui.view.AbstractEntityView;
import org.eclipse.kapua.app.console.module.api.client.ui.widget.KapuaTextField;
import org.eclipse.kapua.app.console.module.api.shared.model.session.GwtSession;
import org.eclipse.kapua.app.console.module.authorization.client.messages.ConsoleRoleMessages;
import org.eclipse.kapua.app.console.module.authorization.shared.model.GwtRole;
import org.eclipse.kapua.app.console.module.authorization.shared.model.GwtRoleQuery;

public class RoleFilterPanel extends EntityFilterPanel<GwtRole> {

    private static final ConsoleRoleMessages ROLE_MSGS = GWT.create(ConsoleRoleMessages.class);
    private static final int WIDTH = 193;
    private static final int MAX_LEN = 255;

    private final EntityGrid<GwtRole> entityGrid;
    private final GwtSession currentSession;

    private final KapuaTextField<String> nameField;
    private final KapuaTextField<String> descriptionField;

    public RoleFilterPanel(AbstractEntityView<GwtRole> entityView, GwtSession currentSession) {
        super(entityView, currentSession);

        entityGrid = entityView.getEntityGrid(entityView, currentSession);
        this.currentSession = currentSession;

        setHeading(ROLE_MSGS.filterHeader());

        VerticalPanel fieldsPanel = getFieldsPanel();

        Label roleNameLabel = new Label(ROLE_MSGS.filterFieldRoleNameLabel());
        roleNameLabel.setWidth(WIDTH);
        roleNameLabel.setStyleAttribute("margin", "5px");

        fieldsPanel.add(roleNameLabel);

        nameField = new KapuaTextField<String>();
        nameField.setName("name");
        nameField.setWidth(WIDTH);
        nameField.setMaxLength(MAX_LEN);
        nameField.setStyleAttribute("margin-top", "0px");
        nameField.setStyleAttribute("margin-left", "5px");
        nameField.setStyleAttribute("margin-right", "5px");
        nameField.setStyleAttribute("margin-bottom", "10px");
        fieldsPanel.add(nameField);

        Label roleDescriptionLabel = new Label(ROLE_MSGS.filterFieldRoleDescriptionLabel());
        roleDescriptionLabel.setWidth(WIDTH);
        roleDescriptionLabel.setStyleAttribute("margin", "5px");

        fieldsPanel.add(roleDescriptionLabel);

        descriptionField = new KapuaTextField<String>();
        descriptionField.setName("name");
        descriptionField.setWidth(WIDTH);
        descriptionField.setMaxLength(MAX_LEN);
        descriptionField.setStyleAttribute("margin-top", "0px");
        descriptionField.setStyleAttribute("margin-left", "5px");
        descriptionField.setStyleAttribute("margin-right", "5px");
        descriptionField.setStyleAttribute("margin-bottom", "10px");
        fieldsPanel.add(descriptionField);

    }

    @Override
    public void resetFields() {
        nameField.setValue(null);
        descriptionField.setValue(null);
        GwtRoleQuery query = new GwtRoleQuery();
        query.setScopeId(currentSession.getSelectedAccountId());
        entityGrid.refresh(query);
    }

    @Override
    public void doFilter() {
        GwtRoleQuery query = new GwtRoleQuery();
        query.setName(nameField.getValue());
        query.setDescription(descriptionField.getValue());
        query.setScopeId(currentSession.getSelectedAccountId());
        entityGrid.refresh(query);
    }

}
