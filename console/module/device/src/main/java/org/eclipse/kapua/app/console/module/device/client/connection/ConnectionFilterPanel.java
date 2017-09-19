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
package org.eclipse.kapua.app.console.module.device.client.connection;

import org.eclipse.kapua.app.console.module.api.client.resources.icons.IconSet;
import org.eclipse.kapua.app.console.module.api.client.resources.icons.KapuaIcon;
import org.eclipse.kapua.app.console.module.api.client.ui.grid.EntityGrid;
import org.eclipse.kapua.app.console.module.api.client.ui.panel.EntityFilterPanel;
import org.eclipse.kapua.app.console.module.api.client.ui.view.AbstractEntityView;
import org.eclipse.kapua.app.console.module.device.client.messages.ConsoleConnectionMessages;
import org.eclipse.kapua.app.console.module.device.shared.model.GwtDeviceQueryPredicates;
import org.eclipse.kapua.app.console.module.api.shared.model.GwtSession;
import org.eclipse.kapua.app.console.module.device.shared.model.GwtDeviceConnection;
import org.eclipse.kapua.app.console.module.device.shared.model.GwtDeviceConnectionQuery;

import com.extjs.gxt.ui.client.widget.Label;
import com.extjs.gxt.ui.client.widget.VerticalPanel;
import com.extjs.gxt.ui.client.widget.form.ComboBox.TriggerAction;
import com.extjs.gxt.ui.client.widget.form.SimpleComboBox;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.google.gwt.core.client.GWT;

public class ConnectionFilterPanel extends EntityFilterPanel<GwtDeviceConnection> {

    private static final int WIDTH = 200;
    private static final ConsoleConnectionMessages MSGS = GWT.create(ConsoleConnectionMessages.class);

    private EntityGrid<GwtDeviceConnection> entityGrid;
    private final GwtSession currentSession;

    private final TextField<String> clientIdField;
    private final TextField<String> clientIPFilter;
    private final SimpleComboBox<GwtDeviceQueryPredicates.GwtDeviceConnectionStatus> connectionStatusCombo;

    public ConnectionFilterPanel(AbstractEntityView<GwtDeviceConnection> entityView, GwtSession currentSession) {
        super(entityView, currentSession);
        entityGrid = entityView.getEntityGrid(entityView, currentSession);
        this.currentSession = currentSession;

        VerticalPanel fieldsPanel = getFieldsPanel();
        setIcon(new KapuaIcon(IconSet.FILTER));
        setHeading(MSGS.connectionFilterHeader());
        final Label clientIdLabel = new Label(MSGS.connectionFilterClientIdLabel());
        clientIdLabel.setWidth(WIDTH);
        clientIdLabel.setStyleAttribute("margin", "5px");
        fieldsPanel.add(clientIdLabel);

        clientIdField = new TextField<String>();
        clientIdField.setName("name");
        clientIdField.setWidth(WIDTH);
        clientIdField.setStyleAttribute("margin-top", "0px");
        clientIdField.setStyleAttribute("margin-left", "5px");
        clientIdField.setStyleAttribute("margin-right", "5px");
        clientIdField.setStyleAttribute("margin-bottom", "10px");
        fieldsPanel.add(clientIdField);

        final Label connectionStatusLabel = new Label(MSGS.connectionFilterConnectionStatus());
        connectionStatusLabel.setWidth(WIDTH);
        connectionStatusLabel.setStyleAttribute("margin", "5px");
        fieldsPanel.add(connectionStatusLabel);

        connectionStatusCombo = new SimpleComboBox<GwtDeviceQueryPredicates.GwtDeviceConnectionStatus>();
        connectionStatusCombo.setName("connectionStatus");
        connectionStatusCombo.setWidth(WIDTH);
        connectionStatusCombo.setStyleAttribute("margin-top", "0px");
        connectionStatusCombo.setStyleAttribute("margin-left", "5px");
        connectionStatusCombo.setStyleAttribute("margin-right", "5px");
        connectionStatusCombo.setStyleAttribute("margin-bottom", "10px");

        connectionStatusCombo.add(GwtDeviceQueryPredicates.GwtDeviceConnectionStatus.ANY);
        connectionStatusCombo.add(GwtDeviceQueryPredicates.GwtDeviceConnectionStatus.CONNECTED);
        connectionStatusCombo.add(GwtDeviceQueryPredicates.GwtDeviceConnectionStatus.MISSING);
        connectionStatusCombo.add(GwtDeviceQueryPredicates.GwtDeviceConnectionStatus.DISCONNECTED);

        connectionStatusCombo.setEditable(false);
        connectionStatusCombo.setTriggerAction(TriggerAction.ALL);
        connectionStatusCombo.setSimpleValue(GwtDeviceQueryPredicates.GwtDeviceConnectionStatus.ANY);

        fieldsPanel.add(connectionStatusCombo);

        final Label clientIPFilterLabel = new Label(MSGS.connectionFilterCLientIPLabel());
        clientIPFilterLabel.setWidth(WIDTH);
        clientIPFilterLabel.setStyleAttribute("margin", "5px");
        fieldsPanel.add(clientIPFilterLabel);

        clientIPFilter = new TextField<String>();
        clientIPFilter.setName("Client IP");
        clientIPFilter.setWidth(WIDTH);
        clientIPFilter.setStyleAttribute("margin-top", "0px");
        clientIPFilter.setStyleAttribute("margin-left", "5px");
        clientIPFilter.setStyleAttribute("margin-right", "5px");
        clientIPFilter.setStyleAttribute("margin-bottom", "10px");
        fieldsPanel.add(clientIPFilter);
    }

    @Override
    public void resetFields() {
        clientIdField.setValue(null);
        connectionStatusCombo.setSimpleValue(GwtDeviceQueryPredicates.GwtDeviceConnectionStatus.ANY);
        clientIPFilter.setValue(null);
        GwtDeviceConnectionQuery query = new GwtDeviceConnectionQuery();
        query.setScopeId(currentSession.getSelectedAccountId());
        entityGrid.refresh(query);
    }

    @Override
    public void doFilter() {
        GwtDeviceConnectionQuery query = new GwtDeviceConnectionQuery();
        query.setScopeId(currentSession.getSelectedAccountId());
        query.setClientId(clientIdField.getValue());
        query.setConnectionStatus(connectionStatusCombo.getSimpleValue().toString());
        query.setClientIP(clientIPFilter.getValue());
        entityGrid.refresh(query);
    }

}
