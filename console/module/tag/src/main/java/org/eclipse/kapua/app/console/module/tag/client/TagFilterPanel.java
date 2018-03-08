/*******************************************************************************
 * Copyright (c) 2017, 2018 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.app.console.module.tag.client;

import com.extjs.gxt.ui.client.widget.Label;
import com.extjs.gxt.ui.client.widget.VerticalPanel;
import com.google.gwt.core.client.GWT;
import org.eclipse.kapua.app.console.module.api.client.ui.grid.EntityGrid;
import org.eclipse.kapua.app.console.module.api.client.ui.panel.EntityFilterPanel;
import org.eclipse.kapua.app.console.module.api.client.ui.view.AbstractEntityView;
import org.eclipse.kapua.app.console.module.api.client.ui.widget.KapuaTextField;
import org.eclipse.kapua.app.console.module.api.shared.model.session.GwtSession;
import org.eclipse.kapua.app.console.module.tag.client.messages.ConsoleTagMessages;
import org.eclipse.kapua.app.console.module.tag.shared.model.GwtTag;
import org.eclipse.kapua.app.console.module.tag.shared.model.GwtTagQuery;

public class TagFilterPanel extends EntityFilterPanel<GwtTag> {

    private static final int WIDTH = 193;
    private static final int MAX_LEN = 255;
    private final EntityGrid<GwtTag> entityGrid;
    private final GwtSession currentSession;
    private final KapuaTextField<String> nameField;
    private static final ConsoleTagMessages MSGS = GWT.create(ConsoleTagMessages.class);

    public TagFilterPanel(AbstractEntityView<GwtTag> entityView, GwtSession currentSession) {
        super(entityView, currentSession);
        entityGrid = entityView.getEntityGrid(entityView, currentSession);
        this.currentSession = currentSession;
        setHeading(MSGS.filterHeader());

        VerticalPanel verticalPanel = getFieldsPanel();
        Label nameLabel = new Label(MSGS.filterFieldTagNameLabel());
        nameLabel.setWidth(WIDTH);
        nameLabel.setStyleAttribute("margin", "5px");
        verticalPanel.add(nameLabel);
        nameField = new KapuaTextField<String>();
        nameField.setName("name");
        nameField.setWidth(WIDTH);
        nameField.setMaxLength(MAX_LEN);
        nameField.setStyleAttribute("margin-top", "0px");
        nameField.setStyleAttribute("margin-left", "5px");
        nameField.setStyleAttribute("margin-right", "5px");
        nameField.setStyleAttribute("margin-bottom", "10px");
        verticalPanel.add(nameField);

    }

    @Override
    public void resetFields() {
        nameField.setValue(null);
        GwtTagQuery query = new GwtTagQuery();
        query.setScopeId(currentSession.getSelectedAccountId());
        entityGrid.refresh(query);

    }

    @Override
    public void doFilter() {
        GwtTagQuery query = new GwtTagQuery();
        query.setName(nameField.getValue());
        query.setScopeId(currentSession.getSelectedAccountId());
        entityGrid.refresh(query);

    }

}
