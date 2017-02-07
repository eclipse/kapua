/*******************************************************************************
 * Copyright (c) 2011, 2016 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *
 *******************************************************************************/
package org.eclipse.kapua.app.console.client.device;

import org.eclipse.kapua.app.console.client.messages.ConsoleMessages;
import org.eclipse.kapua.app.console.client.util.KapuaSafeHtmlUtils;
import org.eclipse.kapua.app.console.shared.model.GwtDeviceQueryPredicates;
import org.eclipse.kapua.app.console.shared.model.GwtSession;

import com.extjs.gxt.ui.client.Style.HorizontalAlignment;
import com.extjs.gxt.ui.client.Style.Orientation;
import com.extjs.gxt.ui.client.Style.Scroll;
import com.extjs.gxt.ui.client.event.BaseEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.HorizontalPanel;
import com.extjs.gxt.ui.client.widget.Label;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.Text;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.ComboBox.TriggerAction;
import com.extjs.gxt.ui.client.widget.form.SimpleComboBox;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.layout.RowLayout;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Element;

public class DeviceFilterPanel extends LayoutContainer {

    private static final ConsoleMessages MSGS = GWT.create(ConsoleMessages.class);
    private final int WIDTH = 200;

    @SuppressWarnings("unused")
    private GwtSession m_currentSession;
    private DevicesTable m_deviceTable;

    public DeviceFilterPanel(GwtSession gwtSession) {
        m_currentSession = gwtSession;
    }

    public void setDeviceTable(DevicesTable devicesTable) {
        m_deviceTable = devicesTable;
    }

    protected void onRender(final Element parent, int index) {
        super.onRender(parent, index);

        setLayout(new FitLayout());

        ContentPanel formPanel = new ContentPanel();
        formPanel.setScrollMode(Scroll.AUTOY);
        formPanel.setBorders(false);
        formPanel.setHeaderVisible(false);
        formPanel.setLayout(new RowLayout(Orientation.VERTICAL));
        formPanel.setBodyStyle("background-color:#F0F0F0");

        //
        // Top explanation
        final Text infoLabel = new Text(MSGS.deviceFilteringPanelInfo());
        infoLabel.setWidth(WIDTH + 5);
        infoLabel.setStyleAttribute("margin", "5px");

        formPanel.add(infoLabel);

        //
        // ClientId
        final Label clientIdLabel = new Label(MSGS.deviceFilteringPanelClientId());
        clientIdLabel.setWidth(WIDTH);
        clientIdLabel.setStyleAttribute("margin", "5px");

        formPanel.add(clientIdLabel);

        final TextField<String> clientIdField = new TextField<String>();
        clientIdField.setName("clientId");
        clientIdField.setWidth(WIDTH);
        clientIdField.setStyleAttribute("margin-top", "0px");
        clientIdField.setStyleAttribute("margin-left", "5px");
        clientIdField.setStyleAttribute("margin-right", "5px");
        clientIdField.setStyleAttribute("margin-bottom", "10px");
        formPanel.add(clientIdField);

        //
        // DisplayName
        final Label displayNameLabel = new Label(MSGS.deviceFilteringPanelDisplayName());
        displayNameLabel.setWidth(WIDTH);
        displayNameLabel.setStyleAttribute("margin", "5px");
        formPanel.add(displayNameLabel);

        final TextField<String> displayNameField = new TextField<String>();
        displayNameField.setName("displayName");
        displayNameField.setWidth(WIDTH);
        displayNameField.setStyleAttribute("margin-top", "0px");
        displayNameField.setStyleAttribute("margin-left", "5px");
        displayNameField.setStyleAttribute("margin-right", "5px");
        displayNameField.setStyleAttribute("margin-bottom", "10px");
        formPanel.add(displayNameField);

        //
        // Serial Number
        final Label serialNumberLabel = new Label(MSGS.deviceFilteringPanelSerialNumber());
        serialNumberLabel.setWidth(WIDTH);
        serialNumberLabel.setStyleAttribute("margin", "5px");
        formPanel.add(serialNumberLabel);

        final TextField<String> serialNumberField = new TextField<String>();
        serialNumberField.setName("serialNumber");
        serialNumberField.setWidth(WIDTH);
        serialNumberField.setStyleAttribute("margin-top", "0px");
        serialNumberField.setStyleAttribute("margin-left", "5px");
        serialNumberField.setStyleAttribute("margin-right", "5px");
        serialNumberField.setStyleAttribute("margin-bottom", "10px");
        formPanel.add(serialNumberField);

        //
        // Status
        final Label statusLabel = new Label(MSGS.deviceFilteringPanelStatus());
        statusLabel.setWidth(WIDTH);
        statusLabel.setStyleAttribute("margin", "5px");
        formPanel.add(statusLabel);

        final SimpleComboBox<GwtDeviceQueryPredicates.GwtDeviceStatus> statusCombo = new SimpleComboBox<GwtDeviceQueryPredicates.GwtDeviceStatus>();
        statusCombo.setName("status");
        statusCombo.setWidth(WIDTH);
        statusCombo.setStyleAttribute("margin-top", "0px");
        statusCombo.setStyleAttribute("margin-left", "5px");
        statusCombo.setStyleAttribute("margin-right", "5px");
        statusCombo.setStyleAttribute("margin-bottom", "10px");

        statusCombo.setEmptyText(MSGS.deviceFilteringPanelStatusEmptyText());
        statusCombo.add(GwtDeviceQueryPredicates.GwtDeviceStatus.ANY);
        statusCombo.add(GwtDeviceQueryPredicates.GwtDeviceStatus.ENABLED);
        statusCombo.add(GwtDeviceQueryPredicates.GwtDeviceStatus.DISABLED);

        statusCombo.setEditable(false);
        statusCombo.setTriggerAction(TriggerAction.ALL);
        statusCombo.setSimpleValue(GwtDeviceQueryPredicates.GwtDeviceStatus.ANY);

        formPanel.add(statusCombo);

        //
        // Connection Status
        final Label connectionStatusLabel = new Label(MSGS.deviceFilteringPanelConnectionStatus());
        connectionStatusLabel.setWidth(WIDTH);
        connectionStatusLabel.setStyleAttribute("margin", "5px");
        formPanel.add(connectionStatusLabel);

        final SimpleComboBox<GwtDeviceQueryPredicates.GwtDeviceConnectionStatus> connectionStatusCombo = new SimpleComboBox<GwtDeviceQueryPredicates.GwtDeviceConnectionStatus>();
        connectionStatusCombo.setName("status");
        connectionStatusCombo.setWidth(WIDTH);
        connectionStatusCombo.setStyleAttribute("margin-top", "0px");
        connectionStatusCombo.setStyleAttribute("margin-left", "5px");
        connectionStatusCombo.setStyleAttribute("margin-right", "5px");
        connectionStatusCombo.setStyleAttribute("margin-bottom", "10px");

        connectionStatusCombo.setEmptyText(MSGS.deviceFilteringPanelConnectionStatusEmptyText());
        connectionStatusCombo.add(GwtDeviceQueryPredicates.GwtDeviceConnectionStatus.ANY);
        connectionStatusCombo.add(GwtDeviceQueryPredicates.GwtDeviceConnectionStatus.CONNECTED);
        connectionStatusCombo.add(GwtDeviceQueryPredicates.GwtDeviceConnectionStatus.MISSING);
        connectionStatusCombo.add(GwtDeviceQueryPredicates.GwtDeviceConnectionStatus.DISCONNECTED);

        connectionStatusCombo.setEditable(false);
        connectionStatusCombo.setTriggerAction(TriggerAction.ALL);
        connectionStatusCombo.setSimpleValue(GwtDeviceQueryPredicates.GwtDeviceConnectionStatus.ANY);

        formPanel.add(connectionStatusCombo);

        //
        // Esf Version
        final Label iotFrameworkVersionLabel = new Label(MSGS.deviceFilteringPanelESFVersion());
        iotFrameworkVersionLabel.setWidth(WIDTH);
        iotFrameworkVersionLabel.setStyleAttribute("margin", "5px");
        formPanel.add(iotFrameworkVersionLabel);

        final TextField<String> iotFrameworkVersionField = new TextField<String>();
        iotFrameworkVersionField.setName("iotFrameworkVersion");
        iotFrameworkVersionField.setWidth(WIDTH);
        iotFrameworkVersionField.setStyleAttribute("margin-top", "0px");
        iotFrameworkVersionField.setStyleAttribute("margin-left", "5px");
        iotFrameworkVersionField.setStyleAttribute("margin-right", "5px");
        iotFrameworkVersionField.setStyleAttribute("margin-bottom", "10px");
        formPanel.add(iotFrameworkVersionField);

        //
        // Application Identifiers
        final Label applicationIdentifiersLabel = new Label(MSGS.deviceFilteringPanelApplications());
        applicationIdentifiersLabel.setWidth(WIDTH);
        applicationIdentifiersLabel.setStyleAttribute("margin", "5px");
        formPanel.add(applicationIdentifiersLabel);

        final TextField<String> applicationIdentifiersField = new TextField<String>();
        applicationIdentifiersField.setName("applicationIdentifiers");
        applicationIdentifiersField.setWidth(WIDTH);
        applicationIdentifiersField.setStyleAttribute("margin-top", "0px");
        applicationIdentifiersField.setStyleAttribute("margin-left", "5px");
        applicationIdentifiersField.setStyleAttribute("margin-right", "5px");
        applicationIdentifiersField.setStyleAttribute("margin-bottom", "10px");
        formPanel.add(applicationIdentifiersField);

        //
        // Custom Attribute 1
        final Label customAttribute1Label = new Label(MSGS.deviceFilteringPanelCustomAttribute1());
        customAttribute1Label.setWidth(WIDTH);
        customAttribute1Label.setStyleAttribute("margin", "5px");
        formPanel.add(customAttribute1Label);

        final TextField<String> customAttribute1Field = new TextField<String>();
        customAttribute1Field.setName("customAttribute1");
        customAttribute1Field.setWidth(WIDTH);
        customAttribute1Field.setStyleAttribute("margin-top", "0px");
        customAttribute1Field.setStyleAttribute("margin-left", "5px");
        customAttribute1Field.setStyleAttribute("margin-right", "5px");
        customAttribute1Field.setStyleAttribute("margin-bottom", "10px");
        formPanel.add(customAttribute1Field);

        //
        // Custom Attribute 2
        final Label customAttribute2Label = new Label(MSGS.deviceFilteringPanelCustomAttribute2());
        customAttribute2Label.setWidth(WIDTH);
        customAttribute2Label.setStyleAttribute("margin", "5px");
        formPanel.add(customAttribute2Label);

        final TextField<String> customAttribute2Field = new TextField<String>();
        customAttribute2Field.setName("customAttribute1");
        customAttribute2Field.setWidth(WIDTH);
        customAttribute2Field.setStyleAttribute("margin-top", "0px");
        customAttribute2Field.setStyleAttribute("margin-left", "5px");
        customAttribute2Field.setStyleAttribute("margin-right", "5px");
        customAttribute2Field.setStyleAttribute("margin-bottom", "5px");
        formPanel.add(customAttribute2Field);

        //
        // Buttons
        HorizontalPanel buttonPanel = new HorizontalPanel();
        buttonPanel.setBorders(false);
        buttonPanel.setStyleAttribute("background-color", "#F0F0F0");
        buttonPanel.setStyleAttribute("margin-left", "5px");
        buttonPanel.setStyleAttribute("margin-top", "5px");
        buttonPanel.setHorizontalAlign(HorizontalAlignment.RIGHT);
        buttonPanel.setHeight(50);

        final Button searchButton = new Button(MSGS.deviceFilteringPanelSearch());
        searchButton.setStyleAttribute("margin-left", "5px");

        searchButton.addListener(Events.OnClick, new Listener<BaseEvent>() {

            @Override
            public void handleEvent(BaseEvent be) {
                GwtDeviceQueryPredicates predicates = new GwtDeviceQueryPredicates();

                if (clientIdField.getValue() != null && !clientIdField.getValue().trim().isEmpty()) {
                    predicates.setClientId(unescapeValue(clientIdField.getValue()));
                }
                if (displayNameField.getValue() != null && !displayNameField.getValue().trim().isEmpty()) {
                    predicates.setDisplayName(unescapeValue(displayNameField.getValue()));
                }
                if (serialNumberField.getValue() != null && !serialNumberField.getValue().trim().isEmpty()) {
                    predicates.setSerialNumber(unescapeValue(serialNumberField.getValue()));
                }
                if (!statusCombo.getValue().getValue().equals(GwtDeviceQueryPredicates.GwtDeviceStatus.ANY)) {
                    predicates.setDeviceStatus(statusCombo.getValue().getValue().name());
                }
                if (!connectionStatusCombo.getValue().getValue().equals(GwtDeviceQueryPredicates.GwtDeviceConnectionStatus.ANY)) {
                    predicates.setDeviceConnectionStatus(connectionStatusCombo.getValue().getValue().name());
                }
                if (iotFrameworkVersionField.getValue() != null && !iotFrameworkVersionField.getValue().trim().isEmpty()) {
                    predicates.setIotFrameworkVersion(unescapeValue(iotFrameworkVersionField.getValue()));
                }
                if (applicationIdentifiersField.getValue() != null && !applicationIdentifiersField.getValue().trim().isEmpty()) {
                    predicates.setApplicationIdentifiers(unescapeValue(applicationIdentifiersField.getValue()));
                }
                if (customAttribute1Field.getValue() != null && !customAttribute1Field.getValue().trim().isEmpty()) {
                    predicates.setCustomAttribute1(unescapeValue(customAttribute1Field.getValue()));
                }
                if (customAttribute2Field.getValue() != null && !customAttribute2Field.getValue().trim().isEmpty()) {
                    predicates.setCustomAttribute2(unescapeValue(customAttribute2Field.getValue()));
                }

                m_deviceTable.refreshAll(predicates);
            }
        });

        final Button resetButton = new Button(MSGS.deviceFilteringPanelReset());
        resetButton.addListener(Events.OnClick, new Listener<BaseEvent>() {

            @Override
            public void handleEvent(BaseEvent be) {

                clientIdField.setValue("");
                displayNameField.setValue("");
                serialNumberField.setValue("");
                statusCombo.setSimpleValue(GwtDeviceQueryPredicates.GwtDeviceStatus.ANY);
                connectionStatusCombo.setSimpleValue(GwtDeviceQueryPredicates.GwtDeviceConnectionStatus.ANY);
                iotFrameworkVersionField.setValue("");
                applicationIdentifiersField.setValue("");
                customAttribute1Field.setValue("");
                customAttribute2Field.setValue("");

                m_deviceTable.refreshAll(new GwtDeviceQueryPredicates());
            }
        });

        buttonPanel.add(resetButton);
        buttonPanel.add(searchButton);

        formPanel.add(buttonPanel);

        add(formPanel);
    }

    public String unescapeValue(String value) {
        return KapuaSafeHtmlUtils.htmlUnescape(value);
    }
}
