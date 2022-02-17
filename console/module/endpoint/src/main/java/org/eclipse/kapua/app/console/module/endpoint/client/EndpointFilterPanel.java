/*******************************************************************************
 * Copyright (c) 2018, 2022 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.app.console.module.endpoint.client;

import com.extjs.gxt.ui.client.widget.Label;
import com.extjs.gxt.ui.client.widget.VerticalPanel;
import com.extjs.gxt.ui.client.widget.form.SimpleComboBox;
import com.extjs.gxt.ui.client.widget.form.ComboBox.TriggerAction;
import com.google.gwt.core.client.GWT;
import org.eclipse.kapua.app.console.module.api.client.ui.grid.EntityGrid;
import org.eclipse.kapua.app.console.module.api.client.ui.panel.EntityFilterPanel;
import org.eclipse.kapua.app.console.module.api.client.ui.view.AbstractEntityView;
import org.eclipse.kapua.app.console.module.api.client.ui.widget.KapuaNumberField;
import org.eclipse.kapua.app.console.module.api.client.ui.widget.KapuaTextField;
import org.eclipse.kapua.app.console.module.api.client.util.CssLiterals;
import org.eclipse.kapua.app.console.module.api.shared.model.session.GwtSession;
import org.eclipse.kapua.app.console.module.endpoint.client.messages.ConsoleEndpointMessages;
import org.eclipse.kapua.app.console.module.endpoint.shared.model.GwtEndpoint;
import org.eclipse.kapua.app.console.module.endpoint.shared.model.GwtEndpointQuery;
import org.eclipse.kapua.app.console.module.endpoint.shared.model.GwtEndpoint.GwtEndpointSecure;

public class EndpointFilterPanel extends EntityFilterPanel<GwtEndpoint> {

    private static final ConsoleEndpointMessages MSGS = GWT.create(ConsoleEndpointMessages.class);

    private static final int WIDTH = 193;
    private static final int MAX_LEN = 255;

    private final EntityGrid<GwtEndpoint> entityGrid;
    private final GwtSession currentSession;

    private final KapuaTextField<String> schemaField;
    private final KapuaTextField<String> dnsField;
    private final KapuaNumberField portField;
    private final SimpleComboBox<GwtEndpoint.GwtEndpointSecure> secureCombo;

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
        schemaLabel.setStyleAttribute(CssLiterals.MARGIN, "5px");
        verticalPanel.add(schemaLabel);

        schemaField = new KapuaTextField<String>();
        schemaField.setName("schema");
        schemaField.setWidth(WIDTH);
        schemaField.setMaxLength(MAX_LEN);
        schemaField.setStyleAttribute(CssLiterals.MARGIN_TOP, "0px");
        schemaField.setStyleAttribute(CssLiterals.MARGIN_LEFT, "5px");
        schemaField.setStyleAttribute(CssLiterals.MARGIN_RIGHT, "5px");
        schemaField.setStyleAttribute(CssLiterals.MARGIN_BOTTOM, "10px");

        verticalPanel.add(schemaField);

        //
        // DNS
        Label nameLabel = new Label(MSGS.filterFieldEndpointDnsLabel());
        nameLabel.setWidth(WIDTH);
        nameLabel.setStyleAttribute(CssLiterals.MARGIN, "5px");
        verticalPanel.add(nameLabel);

        dnsField = new KapuaTextField<String>();
        dnsField.setName("dns");
        dnsField.setWidth(WIDTH);
        dnsField.setMaxLength(MAX_LEN);
        dnsField.setStyleAttribute(CssLiterals.MARGIN_TOP, "0px");
        dnsField.setStyleAttribute(CssLiterals.MARGIN_LEFT, "5px");
        dnsField.setStyleAttribute(CssLiterals.MARGIN_RIGHT, "5px");
        dnsField.setStyleAttribute(CssLiterals.MARGIN_BOTTOM, "10px");

        verticalPanel.add(dnsField);

        //
        // Port
        Label portLabel = new Label(MSGS.filterFieldEndpointPortLabel());
        portLabel.setWidth(WIDTH);
        portLabel.setStyleAttribute(CssLiterals.MARGIN, "5px");
        verticalPanel.add(portLabel);

        portField = new KapuaNumberField();
        portField.setName("port");
        portField.setWidth(WIDTH);
        portField.setMaxLength(5);
        portField.setMinValue(0);
        portField.setMaxValue(65536);
        portField.setStyleAttribute(CssLiterals.MARGIN_TOP, "0px");
        portField.setStyleAttribute(CssLiterals.MARGIN_LEFT, "5px");
        portField.setStyleAttribute(CssLiterals.MARGIN_RIGHT, "5px");
        portField.setStyleAttribute(CssLiterals.MARGIN_BOTTOM, "10px");

        verticalPanel.add(portField);

        Label secureLabel = new Label(MSGS.filterFieldEndpointSecureLabel());
        secureLabel.setWidth(WIDTH);
        secureLabel.setStyleAttribute(CssLiterals.MARGIN, "5px");
        verticalPanel.add(secureLabel);

        secureCombo = new SimpleComboBox<GwtEndpoint.GwtEndpointSecure>();
        secureCombo.setName("secure");
        secureCombo.setWidth(WIDTH);
        secureCombo.setTriggerAction(TriggerAction.ALL);
        secureCombo.add(GwtEndpoint.GwtEndpointSecure.ANY);
        secureCombo.add(GwtEndpoint.GwtEndpointSecure.TRUE);
        secureCombo.add(GwtEndpoint.GwtEndpointSecure.FALSE);
        secureCombo.setSimpleValue(GwtEndpointSecure.ANY);
        secureCombo.setEditable(false);
        secureCombo.setStyleAttribute(CssLiterals.MARGIN_TOP, "0px");
        secureCombo.setStyleAttribute(CssLiterals.MARGIN_LEFT, "5px");
        secureCombo.setStyleAttribute(CssLiterals.MARGIN_RIGHT, "5px");
        secureCombo.setStyleAttribute(CssLiterals.MARGIN_BOTTOM, "10px");

        verticalPanel.add(secureCombo);

    }

    @Override
    public void resetFields() {
        schemaField.setValue(null);
        dnsField.setValue(null);
        portField.setValue(null);
        secureCombo.setSimpleValue(GwtEndpointSecure.ANY);
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
        if (secureCombo.getSimpleValue().toString().equals(GwtEndpoint.GwtEndpointSecure.FALSE.toString())) {
            query.setSecure(false);
            query.setCheck(true);
        } else if (secureCombo.getSimpleValue().toString().equals(GwtEndpoint.GwtEndpointSecure.TRUE.toString())) {
            query.setSecure(true);
            query.setCheck(true);
        }

        entityGrid.refresh(query);
    }

}
