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
package org.eclipse.kapua.app.console.module.device.client.device;

import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.Label;
import com.extjs.gxt.ui.client.widget.VerticalPanel;
import com.extjs.gxt.ui.client.widget.form.ComboBox;
import com.extjs.gxt.ui.client.widget.form.ComboBox.TriggerAction;
import com.extjs.gxt.ui.client.widget.form.SimpleComboBox;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import org.eclipse.kapua.app.console.module.api.client.messages.ConsoleMessages;
import org.eclipse.kapua.app.console.module.api.client.ui.grid.EntityGrid;
import org.eclipse.kapua.app.console.module.api.client.ui.panel.EntityFilterPanel;
import org.eclipse.kapua.app.console.module.api.client.ui.view.AbstractEntityView;
import org.eclipse.kapua.app.console.module.api.client.ui.widget.KapuaTextField;
import org.eclipse.kapua.app.console.module.api.client.util.ConsoleInfo;
import org.eclipse.kapua.app.console.module.api.client.util.KapuaSafeHtmlUtils;
import org.eclipse.kapua.app.console.module.api.shared.model.session.GwtSession;
import org.eclipse.kapua.app.console.module.authorization.shared.model.GwtGroup;
import org.eclipse.kapua.app.console.module.authorization.shared.model.permission.GroupSessionPermission;
import org.eclipse.kapua.app.console.module.authorization.shared.service.GwtGroupService;
import org.eclipse.kapua.app.console.module.authorization.shared.service.GwtGroupServiceAsync;
import org.eclipse.kapua.app.console.module.device.client.messages.ConsoleDeviceMessages;
import org.eclipse.kapua.app.console.module.device.shared.model.GwtDevice;
import org.eclipse.kapua.app.console.module.device.shared.model.GwtDeviceQuery;
import org.eclipse.kapua.app.console.module.device.shared.model.GwtDeviceQueryPredicates;
import org.eclipse.kapua.app.console.module.tag.shared.model.GwtTag;
import org.eclipse.kapua.app.console.module.tag.shared.model.permission.TagSessionPermission;
import org.eclipse.kapua.app.console.module.tag.shared.service.GwtTagService;
import org.eclipse.kapua.app.console.module.tag.shared.service.GwtTagServiceAsync;

import java.util.List;

public class DeviceFilterPanel extends EntityFilterPanel<GwtDevice> {

    private static final ConsoleMessages MSGS = GWT.create(ConsoleMessages.class);
    private static final ConsoleDeviceMessages DEVICE_MSGS = GWT.create(ConsoleDeviceMessages.class);
    private static final int WIDTH = 193;
    private static final int MAX_LEN = 255;

    private EntityGrid<GwtDevice> entityGrid;

    private final GwtGroupServiceAsync groupService = GWT.create(GwtGroupService.class);
    private final GwtTagServiceAsync tagService = GWT.create(GwtTagService.class);
    private final GwtSession currentSession;

    private final KapuaTextField<String> clientIdField;
    private final KapuaTextField<String> serialNumberField;
    private final KapuaTextField<String> displayNameField;
    private final SimpleComboBox<GwtDeviceQueryPredicates.GwtDeviceStatus> statusCombo;
    private final SimpleComboBox<GwtDeviceQueryPredicates.GwtDeviceConnectionStatus> connectionStatusCombo;
    private final KapuaTextField<String> iotFrameworkVersionField;
    private final KapuaTextField<String> applicationIdentifiersField;
    private final KapuaTextField<String> customAttribute1Field;
    private final KapuaTextField<String> customAttribute2Field;

    private ComboBox<GwtGroup> groupsCombo;
    private GwtGroup allGroup;
    private GwtGroup noGroup;

    private ComboBox<GwtTag> tagsCombo;
    private GwtTag allTag;

    public DeviceFilterPanel(AbstractEntityView<GwtDevice> entityView, GwtSession currentSession) {
        super(entityView, currentSession);
        entityGrid = entityView.getEntityGrid(entityView, currentSession);
        this.currentSession = currentSession;

        setHeading(DEVICE_MSGS.deviceFilteringPanelHeading());

        VerticalPanel fieldsPanel = getFieldsPanel();

        //
        // ClientId
        Label clientIdLabel = new Label(DEVICE_MSGS.deviceFilteringPanelClientId());
        clientIdLabel.setWidth(WIDTH);
        clientIdLabel.setStyleAttribute("margin", "5px");

        fieldsPanel.add(clientIdLabel);

        clientIdField = new KapuaTextField<String>();
        clientIdField.setName("clientId");
        clientIdField.setWidth(WIDTH);
        clientIdField.setMaxLength(MAX_LEN);
        clientIdField.setStyleAttribute("margin-top", "0px");
        clientIdField.setStyleAttribute("margin-left", "5px");
        clientIdField.setStyleAttribute("margin-right", "5px");
        clientIdField.setStyleAttribute("margin-bottom", "10px");
        fieldsPanel.add(clientIdField);

        //
        // DisplayName
        Label displayNameLabel = new Label(DEVICE_MSGS.deviceFilteringPanelDisplayName());
        displayNameLabel.setWidth(WIDTH);
        displayNameLabel.setStyleAttribute("margin", "5px");
        fieldsPanel.add(displayNameLabel);

        displayNameField = new KapuaTextField<String>();
        displayNameField.setName("displayName");
        displayNameField.setWidth(WIDTH);
        displayNameField.setMaxLength(MAX_LEN);
        displayNameField.setStyleAttribute("margin-top", "0px");
        displayNameField.setStyleAttribute("margin-left", "5px");
        displayNameField.setStyleAttribute("margin-right", "5px");
        displayNameField.setStyleAttribute("margin-bottom", "10px");
        fieldsPanel.add(displayNameField);

        //
        // Serial Number
        Label serialNumberLabel = new Label(DEVICE_MSGS.deviceFilteringPanelSerialNumber());
        serialNumberLabel.setWidth(WIDTH);
        serialNumberLabel.setStyleAttribute("margin", "5px");
        fieldsPanel.add(serialNumberLabel);

        serialNumberField = new KapuaTextField<String>();
        serialNumberField.setName("serialNumber");
        serialNumberField.setWidth(WIDTH);
        serialNumberField.setMaxLength(MAX_LEN);
        serialNumberField.setStyleAttribute("margin-top", "0px");
        serialNumberField.setStyleAttribute("margin-left", "5px");
        serialNumberField.setStyleAttribute("margin-right", "5px");
        serialNumberField.setStyleAttribute("margin-bottom", "10px");
        fieldsPanel.add(serialNumberField);

        //
        // Status
        Label statusLabel = new Label(DEVICE_MSGS.deviceFilteringPanelStatus());
        statusLabel.setWidth(WIDTH);
        statusLabel.setStyleAttribute("margin", "5px");
        fieldsPanel.add(statusLabel);

        statusCombo = new SimpleComboBox<GwtDeviceQueryPredicates.GwtDeviceStatus>();
        statusCombo.setName("status");
        statusCombo.setWidth(WIDTH);
        statusCombo.setStyleAttribute("margin-top", "0px");
        statusCombo.setStyleAttribute("margin-left", "5px");
        statusCombo.setStyleAttribute("margin-right", "5px");
        statusCombo.setStyleAttribute("margin-bottom", "10px");

        statusCombo.setEmptyText(DEVICE_MSGS.deviceFilteringPanelStatusEmptyText());
        statusCombo.add(GwtDeviceQueryPredicates.GwtDeviceStatus.ANY);
        statusCombo.add(GwtDeviceQueryPredicates.GwtDeviceStatus.ENABLED);
        statusCombo.add(GwtDeviceQueryPredicates.GwtDeviceStatus.DISABLED);

        statusCombo.setEditable(false);
        statusCombo.setTriggerAction(TriggerAction.ALL);
        statusCombo.setSimpleValue(GwtDeviceQueryPredicates.GwtDeviceStatus.ANY);

        fieldsPanel.add(statusCombo);

        //
        // Connection Status
        Label connectionStatusLabel = new Label(DEVICE_MSGS.deviceFilteringPanelConnectionStatus());
        connectionStatusLabel.setWidth(WIDTH);
        connectionStatusLabel.setStyleAttribute("margin", "5px");
        fieldsPanel.add(connectionStatusLabel);

        connectionStatusCombo = new SimpleComboBox<GwtDeviceQueryPredicates.GwtDeviceConnectionStatus>();
        connectionStatusCombo.setName("status");
        connectionStatusCombo.setWidth(WIDTH);
        connectionStatusCombo.setStyleAttribute("margin-top", "0px");
        connectionStatusCombo.setStyleAttribute("margin-left", "5px");
        connectionStatusCombo.setStyleAttribute("margin-right", "5px");
        connectionStatusCombo.setStyleAttribute("margin-bottom", "10px");

        connectionStatusCombo.setEmptyText(DEVICE_MSGS.deviceFilteringPanelConnectionStatusEmptyText());
        connectionStatusCombo.add(GwtDeviceQueryPredicates.GwtDeviceConnectionStatus.ANY);
        connectionStatusCombo.add(GwtDeviceQueryPredicates.GwtDeviceConnectionStatus.CONNECTED);
        connectionStatusCombo.add(GwtDeviceQueryPredicates.GwtDeviceConnectionStatus.MISSING);
        connectionStatusCombo.add(GwtDeviceQueryPredicates.GwtDeviceConnectionStatus.DISCONNECTED);
        connectionStatusCombo.add(GwtDeviceQueryPredicates.GwtDeviceConnectionStatus.UNKNOWN);

        connectionStatusCombo.setEditable(false);
        connectionStatusCombo.setTriggerAction(TriggerAction.ALL);
        connectionStatusCombo.setSimpleValue(GwtDeviceQueryPredicates.GwtDeviceConnectionStatus.ANY);

        fieldsPanel.add(connectionStatusCombo);

        //
        // Esf Version
        Label iotFrameworkVersionLabel = new Label(DEVICE_MSGS.deviceFilteringPanelESFVersion());
        iotFrameworkVersionLabel.setWidth(WIDTH);
        iotFrameworkVersionLabel.setStyleAttribute("margin", "5px");
        fieldsPanel.add(iotFrameworkVersionLabel);

        iotFrameworkVersionField = new KapuaTextField<String>();
        iotFrameworkVersionField.setName("iotFrameworkVersion");
        iotFrameworkVersionField.setWidth(WIDTH);
        iotFrameworkVersionField.setMaxLength(MAX_LEN);
        iotFrameworkVersionField.setStyleAttribute("margin-top", "0px");
        iotFrameworkVersionField.setStyleAttribute("margin-left", "5px");
        iotFrameworkVersionField.setStyleAttribute("margin-right", "5px");
        iotFrameworkVersionField.setStyleAttribute("margin-bottom", "10px");
        fieldsPanel.add(iotFrameworkVersionField);

        //
        // Application Identifiers
        Label applicationIdentifiersLabel = new Label(DEVICE_MSGS.deviceFilteringPanelApplications());
        applicationIdentifiersLabel.setWidth(WIDTH);
        applicationIdentifiersLabel.setStyleAttribute("margin", "5px");
        fieldsPanel.add(applicationIdentifiersLabel);

        applicationIdentifiersField = new KapuaTextField<String>();
        applicationIdentifiersField.setName("applicationIdentifiers");
        applicationIdentifiersField.setWidth(WIDTH);
        applicationIdentifiersField.setMaxLength(MAX_LEN);
        applicationIdentifiersField.setStyleAttribute("margin-top", "0px");
        applicationIdentifiersField.setStyleAttribute("margin-left", "5px");
        applicationIdentifiersField.setStyleAttribute("margin-right", "5px");
        applicationIdentifiersField.setStyleAttribute("margin-bottom", "10px");
        fieldsPanel.add(applicationIdentifiersField);

        //
        // Custom Attribute 1
        Label customAttribute1Label = new Label(DEVICE_MSGS.deviceFilteringPanelCustomAttribute1());
        customAttribute1Label.setWidth(WIDTH);
        customAttribute1Label.setStyleAttribute("margin", "5px");
        fieldsPanel.add(customAttribute1Label);

        customAttribute1Field = new KapuaTextField<String>();
        customAttribute1Field.setName("customAttribute1");
        customAttribute1Field.setWidth(WIDTH);
        customAttribute1Field.setMaxLength(MAX_LEN);
        customAttribute1Field.setStyleAttribute("margin-top", "0px");
        customAttribute1Field.setStyleAttribute("margin-left", "5px");
        customAttribute1Field.setStyleAttribute("margin-right", "5px");
        customAttribute1Field.setStyleAttribute("margin-bottom", "10px");
        fieldsPanel.add(customAttribute1Field);

        //
        // Custom Attribute 2
        Label customAttribute2Label = new Label(DEVICE_MSGS.deviceFilteringPanelCustomAttribute2());
        customAttribute2Label.setWidth(WIDTH);
        customAttribute2Label.setStyleAttribute("margin", "5px");
        fieldsPanel.add(customAttribute2Label);

        customAttribute2Field = new KapuaTextField<String>();
        customAttribute2Field.setName("customAttribute1");
        customAttribute2Field.setWidth(WIDTH);
        customAttribute2Field.setMaxLength(MAX_LEN);
        customAttribute2Field.setStyleAttribute("margin-top", "0px");
        customAttribute2Field.setStyleAttribute("margin-left", "5px");
        customAttribute2Field.setStyleAttribute("margin-right", "5px");
        customAttribute2Field.setStyleAttribute("margin-bottom", "5px");
        fieldsPanel.add(customAttribute2Field);

        //
        // Groups
        if (currentSession.hasPermission(GroupSessionPermission.read())) {
            Label groupLabel = new Label(DEVICE_MSGS.deviceFilteringPanelGroup());
            groupLabel.setWidth(WIDTH);
            groupLabel.setStyleAttribute("margin", "5px");
            fieldsPanel.add(groupLabel);

            allGroup = new GwtGroup();
            allGroup.setGroupName("ANY GROUP");
            allGroup.setId(null);

            noGroup = new GwtGroup();
            noGroup.setGroupName("NO GROUP");
            noGroup.setId(null);

            groupsCombo = new ComboBox<GwtGroup>();
            groupsCombo.setStore(new ListStore<GwtGroup>());
            groupsCombo.disable();
            groupsCombo.setEditable(false);
            groupsCombo.setTypeAhead(false);
            groupsCombo.setAllowBlank(false);
            groupsCombo.setEmptyText(DEVICE_MSGS.deviceFilteringPanelLoading());
            groupsCombo.setDisplayField("groupName");
            groupsCombo.setTemplate("<tpl for=\".\"><div role=\"listitem\" class=\"x-combo-list-item\" title={groupName}>{groupName}</div></tpl>");
            groupsCombo.setValueField("id");
            groupsCombo.setName("groupId");
            groupsCombo.setWidth(WIDTH);
            groupsCombo.setStyleAttribute("margin-top", "0px");
            groupsCombo.setStyleAttribute("margin-left", "5px");
            groupsCombo.setStyleAttribute("margin-right", "5px");
            groupsCombo.setStyleAttribute("margin-bottom", "10px");
            groupsCombo.setTriggerAction(TriggerAction.ALL);
            groupsCombo.setValue(allGroup);
            groupService.findAll(currentSession.getSelectedAccountId(), new AsyncCallback<List<GwtGroup>>() {

                @Override
                public void onFailure(Throwable caught) {
                    ConsoleInfo.display(MSGS.popupError(), DEVICE_MSGS.deviceFilteringPanelGroupsError());
                }

                @Override
                public void onSuccess(List<GwtGroup> result) {
                    groupsCombo.getStore().removeAll();
                    groupsCombo.getStore().add(allGroup);
                    groupsCombo.getStore().add(result);
                    groupsCombo.getStore().add(noGroup);
                    groupsCombo.setValue(allGroup);
                    groupsCombo.enable();
                }
            });

            fieldsPanel.add(groupsCombo);
        }

        //
        // Tags
        if (currentSession.hasPermission(TagSessionPermission.read())) {
            Label tagLabel = new Label(DEVICE_MSGS.deviceFilteringPanelTag());
            tagLabel.setWidth(WIDTH);
            tagLabel.setStyleAttribute("margin", "5px");
            fieldsPanel.add(tagLabel);

            allTag = new GwtTag();
            allTag.setTagName("ANY TAG");
            allTag.setId(null);

            tagsCombo = new ComboBox<GwtTag>();
            tagsCombo.setStore(new ListStore<GwtTag>());
            tagsCombo.disable();
            tagsCombo.setEditable(false);
            tagsCombo.setTypeAhead(false);
            tagsCombo.setAllowBlank(false);
            tagsCombo.setEmptyText(DEVICE_MSGS.deviceFilteringPanelLoading());
            tagsCombo.setDisplayField("tagName");
            tagsCombo.setTemplate("<tpl for=\".\"><div role=\"listitem\" class=\"x-combo-list-item\" title={tagName}>{tagName}</div></tpl>");
            tagsCombo.setValueField("id");
            tagsCombo.setName("tagId");
            tagsCombo.setWidth(WIDTH);
            tagsCombo.setStyleAttribute("margin-top", "0px");
            tagsCombo.setStyleAttribute("margin-left", "5px");
            tagsCombo.setStyleAttribute("margin-right", "5px");
            tagsCombo.setStyleAttribute("margin-bottom", "10px");
            tagsCombo.setTriggerAction(TriggerAction.ALL);
            tagsCombo.setValue(allTag);
            tagService.findAll(currentSession.getSelectedAccountId(), new AsyncCallback<List<GwtTag>>() {

                @Override
                public void onFailure(Throwable caught) {
                    ConsoleInfo.display(MSGS.popupError(), DEVICE_MSGS.deviceFilteringPanelTagsError());
                }

                @Override
                public void onSuccess(List<GwtTag> result) {
                    tagsCombo.getStore().removeAll();
                    tagsCombo.getStore().add(allTag);
                    tagsCombo.getStore().add(result);
                    tagsCombo.setValue(allTag);
                    tagsCombo.enable();
                }
            });

            fieldsPanel.add(tagsCombo);
        }
    }

    public String unescapeValue(String value) {
        return KapuaSafeHtmlUtils.htmlUnescape(value);
    }

    @Override
    public void resetFields() {
        clientIdField.setValue("");
        displayNameField.setValue("");
        serialNumberField.setValue("");
        statusCombo.setSimpleValue(GwtDeviceQueryPredicates.GwtDeviceStatus.ANY);
        connectionStatusCombo.setSimpleValue(GwtDeviceQueryPredicates.GwtDeviceConnectionStatus.ANY);
        iotFrameworkVersionField.setValue("");
        applicationIdentifiersField.setValue("");
        customAttribute1Field.setValue("");
        customAttribute2Field.setValue("");

        if (currentSession.hasPermission(GroupSessionPermission.read())) {
            groupsCombo.setValue(allGroup);
        }
        if (currentSession.hasPermission(TagSessionPermission.read())) {
            tagsCombo.setValue(allTag);
        }

        GwtDeviceQuery query = new GwtDeviceQuery();
        query.setScopeId(currentSession.getSelectedAccountId());
        query.setPredicates(new GwtDeviceQueryPredicates());
        entityGrid.refresh(query);
    }

    @Override
    public void doFilter() {
        GwtDeviceQuery query = new GwtDeviceQuery();
        query.setScopeId(currentSession.getSelectedAccountId());

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
        if (groupsCombo != null && !groupsCombo.getValue().equals(allGroup) && !groupsCombo.getValue().equals(noGroup)) {
            predicates.setGroupId(groupsCombo.getValue().getId());
            predicates.setGroupDevice("ANY");
        }
        if (groupsCombo != null && groupsCombo.getValue().equals(noGroup)) {
            predicates.setGroupDevice("NO_GROUP");
        }
        if (tagsCombo != null && !tagsCombo.getValue().equals(allTag)) {
            predicates.setTagId(tagsCombo.getValue().getId());
        }

        query.setPredicates(predicates);

        entityGrid.refresh(query);
    }
}
