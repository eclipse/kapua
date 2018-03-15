/*******************************************************************************
 * Copyright (c) 2018 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.app.console.module.endpoint.client;

import com.extjs.gxt.ui.client.widget.Label;
import com.extjs.gxt.ui.client.widget.VerticalPanel;
import com.google.gwt.core.client.GWT;
import org.eclipse.kapua.app.console.module.api.client.ui.grid.EntityGrid;
import org.eclipse.kapua.app.console.module.api.client.ui.panel.EntityFilterPanel;
import org.eclipse.kapua.app.console.module.api.client.ui.view.AbstractEntityView;
import org.eclipse.kapua.app.console.module.api.client.ui.widget.KapuaNumberField;
import org.eclipse.kapua.app.console.module.api.client.ui.widget.KapuaTextField;
import org.eclipse.kapua.app.console.module.api.shared.model.session.GwtSession;
import org.eclipse.kapua.app.console.module.endpoint.client.messages.ConsoleEndpointMessages;
import org.eclipse.kapua.app.console.module.endpoint.shared.model.GwtEndpoint;
import org.eclipse.kapua.app.console.module.endpoint.shared.model.GwtEndpointQuery;

public class EndpointFilterPanel extends EntityFilterPanel<GwtEndpoint> {

    private static final ConsoleEndpointMessages MSGS = GWT.create(ConsoleEndpointMessages.class);

    private static final int WIDTH = 193;
    private static final int MAX_LEN = 255;

    private final EntityGrid<GwtEndpoint> entityGrid;
    private final GwtSession currentSession;

    private final KapuaTextField<String> schemaField;
    private final KapuaTextField<String> dnsField;
    private final KapuaNumberField portField;

    public EndpointFilterPanel(AbstractEntityView<GwtEndpoint> entityView, GwtSession currentSession) {
        super(entityView, currentSession);
        entityGrid = entityView.getEntityGrid(entityView, currentSession);
        this.currentSession = currentSession;
        setHeading(MSGS.filterHeader());

        VerticalPanel verticalPanel = getFieldsPanel();

        //
        // Schema
        Label schemaLabel = new Label(MSGS.filterFieldEndpointSchemaLabel());
        schemaLabel.setWidth(WIDTH);
        schemaLabel.setStyleAttribute("margin", "5px");
        verticalPanel.add(schemaLabel);

        schemaField = new KapuaTextField<String>();
        schemaField.setName("schema");
        schemaField.setWidth(WIDTH);
        schemaField.setMaxLength(MAX_LEN);
        schemaField.setStyleAttribute("margin-top", "0px");
        schemaField.setStyleAttribute("margin-left", "5px");
        schemaField.setStyleAttribute("margin-right", "5px");
        schemaField.setStyleAttribute("margin-bottom", "10px");

        verticalPanel.add(schemaField);

        //
        // DNS
        Label nameLabel = new Label(MSGS.filterFieldEndpointDnsLabel());
        nameLabel.setWidth(WIDTH);
        nameLabel.setStyleAttribute("margin", "5px");
        verticalPanel.add(nameLabel);

        dnsField = new KapuaTextField<String>();
        dnsField.setName("dns");
        dnsField.setWidth(WIDTH);
        dnsField.setMaxLength(MAX_LEN);
        dnsField.setStyleAttribute("margin-top", "0px");
        dnsField.setStyleAttribute("margin-left", "5px");
        dnsField.setStyleAttribute("margin-right", "5px");
        dnsField.setStyleAttribute("margin-bottom", "10px");

        verticalPanel.add(dnsField);

        //
        // DNS
        Label portLabel = new Label(MSGS.filterFieldEndpointPortLabel());
        nameLabel.setWidth(WIDTH);
        nameLabel.setStyleAttribute("margin", "5px");
        verticalPanel.add(nameLabel);

        portField = new KapuaNumberField();
        portField.setName("port");
        portField.setWidth(WIDTH);
        portField.setMaxLength(5);
        portField.setMinValue(0);
        portField.setMaxValue(65536);
        portField.setStyleAttribute("margin-top", "0px");
        portField.setStyleAttribute("margin-left", "5px");
        portField.setStyleAttribute("margin-right", "5px");
        portField.setStyleAttribute("margin-bottom", "10px");

        verticalPanel.add(dnsField);

    }

    @Override
    public void resetFields() {
        schemaField.setValue(null);
        dnsField.setValue(null);
        portField.setValue(null);

        GwtEndpointQuery query = new GwtEndpointQuery();
        query.setScopeId(currentSession.getSelectedAccountId());

        entityGrid.refresh(query);
    }

    @Override
    public void doFilter() {
        GwtEndpointQuery query = new GwtEndpointQuery();
        query.setScopeId(currentSession.getSelectedAccountId());
        query.setSchema(schemaField.getValue());
        query.setDns(dnsField.getValue());
        query.setPort(portField.getValue());

        entityGrid.refresh(query);
    }

}
