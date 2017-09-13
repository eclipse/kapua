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

import org.eclipse.kapua.app.console.module.api.client.ui.grid.EntityGrid;
import org.eclipse.kapua.app.console.module.api.client.ui.panel.EntityFilterPanel;
import org.eclipse.kapua.app.console.module.api.client.ui.view.AbstractEntityView;
import org.eclipse.kapua.app.console.module.authorization.client.messages.ConsoleGroupMessages;
import org.eclipse.kapua.app.console.module.authorization.shared.model.GwtGroup;
import org.eclipse.kapua.app.console.module.api.shared.model.GwtSession;

import com.extjs.gxt.ui.client.widget.Label;
import com.extjs.gxt.ui.client.widget.VerticalPanel;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.google.gwt.core.client.GWT;
import org.eclipse.kapua.app.console.module.authorization.shared.model.GwtGroupQuery;

public class GroupFilterPanel extends EntityFilterPanel<GwtGroup> {

    private static final int WIDTH = 200;
    private final EntityGrid<GwtGroup> entityGrid;
    private final GwtSession currentSession;
    private final TextField<String> nameField;
    private static final ConsoleGroupMessages MSGS = GWT.create(ConsoleGroupMessages.class);

    public GroupFilterPanel(AbstractEntityView<GwtGroup> entityView, GwtSession currentSession) {
        super(entityView, currentSession);
        entityGrid = entityView.getEntityGrid(entityView, currentSession);
        this.currentSession = currentSession;

        setHeading(MSGS.filterHeader());

        VerticalPanel verticalPanel = getFieldsPanel();
        final Label nameLabel = new Label(MSGS.filterFieldGroupNameLabel());
        nameLabel.setWidth(WIDTH);
        nameLabel.setStyleAttribute("margin", "5px");
        verticalPanel.add(nameLabel);
        nameField = new TextField<String>();
        nameField.setName("name");
        nameField.setWidth(WIDTH);
        nameField.setStyleAttribute("margin-top", "0px");
        nameField.setStyleAttribute("margin-left", "5px");
        nameField.setStyleAttribute("margin-right", "5px");
        nameField.setStyleAttribute("margin-bottom", "10px");
        verticalPanel.add(nameField);

    }

    @Override
    public void resetFields() {
        nameField.setValue(null);
        GwtGroupQuery query = new GwtGroupQuery();
        query.setScopeId(currentSession.getSelectedAccountId());
        entityGrid.refresh(query);

    }

    @Override
    public void doFilter() {
        GwtGroupQuery query = new GwtGroupQuery();
        query.setName(nameField.getValue());
        query.setScopeId(currentSession.getSelectedAccountId());
        entityGrid.refresh(query);

    }

}
