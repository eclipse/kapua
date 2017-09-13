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
package org.eclipse.kapua.app.console.module.device.client.device.assets;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.kapua.app.console.module.api.client.messages.ConsoleMessages;
import org.eclipse.kapua.app.console.module.device.client.messages.ConsoleDeviceMessages;
import org.eclipse.kapua.app.console.module.api.client.util.Constants;
import org.eclipse.kapua.app.console.module.api.client.util.FormUtils;
import org.eclipse.kapua.app.console.module.api.client.util.UserAgentUtils;
import org.eclipse.kapua.app.console.module.device.shared.model.device.management.assets.GwtDeviceAsset;
import org.eclipse.kapua.app.console.module.device.shared.model.device.management.assets.GwtDeviceAssetChannel;
import org.eclipse.kapua.app.console.module.device.shared.model.device.management.assets.GwtDeviceAssetChannel.GwtDeviceAssetChannelMode;

import com.extjs.gxt.ui.client.Style.LayoutRegion;
import com.extjs.gxt.ui.client.Style.Scroll;
import com.extjs.gxt.ui.client.core.El;
import com.extjs.gxt.ui.client.event.BaseEvent;
import com.extjs.gxt.ui.client.event.ComponentEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.util.Margins;
import com.extjs.gxt.ui.client.widget.Component;
import com.extjs.gxt.ui.client.widget.ComponentPlugin;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.form.Field;
import com.extjs.gxt.ui.client.widget.form.FieldSet;
import com.extjs.gxt.ui.client.widget.form.FormPanel;
import com.extjs.gxt.ui.client.widget.form.NumberField;
import com.extjs.gxt.ui.client.widget.form.Radio;
import com.extjs.gxt.ui.client.widget.form.RadioGroup;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.extjs.gxt.ui.client.widget.form.Validator;
import com.extjs.gxt.ui.client.widget.layout.BorderLayout;
import com.extjs.gxt.ui.client.widget.layout.BorderLayoutData;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.layout.FlowLayout;
import com.extjs.gxt.ui.client.widget.layout.FormData;
import com.extjs.gxt.ui.client.widget.layout.FormLayout;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.NodeList;

public class DeviceAssetsPanel extends LayoutContainer {

    private static final ConsoleMessages MSGS = GWT.create(ConsoleMessages.class);
    private static final ConsoleDeviceMessages DEVICE_MSGS = GWT.create(ConsoleDeviceMessages.class);

    private GwtDeviceAsset asset;
    private FormPanel actionFormPanel;
    private FieldSet actionFieldSet;

    private ComponentPlugin dirtyPlugin;

    public DeviceAssetsPanel(GwtDeviceAsset asset) {
        super(new FitLayout());
        setScrollMode(Scroll.AUTO);
        setBorders(false);

        this.asset = asset;

        final DeviceAssetsPanel thePanel = this;
        dirtyPlugin = new ComponentPlugin() {

            public void init(Component component) {
                component.addListener(Events.Change, new Listener<ComponentEvent>() {

                    public void handleEvent(ComponentEvent be) {
                        El elem = be.getComponent().el().findParent(".x-form-element", 7);
                        if (elem != null) {
                            El dirtyIcon = elem.createChild("");
                            dirtyIcon.setStyleName("x-grid3-dirty-cell");
                            dirtyIcon.setStyleAttribute("top", "0");
                            dirtyIcon.setStyleAttribute("position", "absolute");
                            dirtyIcon.setSize(10, 10);
                            dirtyIcon.show();
                        }
                        thePanel.fireEvent(Events.Change);
                    }
                });
            }
        };

        paintAsset();
    }

    public boolean isValid() {
        return FormUtils.isValid(actionFieldSet);
    }

    public boolean isDirty() {
        return FormUtils.isDirty(actionFieldSet);
    }

    public GwtDeviceAsset getAsset() {
        return asset;
    }

    public GwtDeviceAsset getUpdatedAsset() {

        List<Component> fields = actionFieldSet.getItems();
        List<GwtDeviceAssetChannel> updatedChannelsList = new ArrayList<GwtDeviceAssetChannel>();
        for (int i = 0; i < fields.size(); i++) {
            if (fields.get(i) instanceof Field<?>) {
                Field<?> field = (Field<?>) fields.get(i);
                String fieldName = field.getItemId();
                GwtDeviceAssetChannel channel = asset.getChannel(fieldName);
                if (channel == null) {
                    System.err.println(field);
                }
                String value = getUpdatedChannel(channel, field);
                if (value != null) {
                    channel.setValue(value);
                    updatedChannelsList.add(channel);
                }
            }
        }
        asset.setChannels(updatedChannelsList);
        return asset;
    }

    private String getUpdatedChannel(GwtDeviceAssetChannel channel, Field<?> field) {
        try {
            if (!field.isDirty()) {
                return null;
            }
            switch (channel.getTypeEnum()) {
            case BOOLEAN:
                RadioGroup radioGroup = (RadioGroup) field;
                Radio radio = radioGroup.getValue();
                String booleanValue = radio.getItemId();
                return booleanValue;
            case LONG:
                NumberField longField = (NumberField) field;
                Number longNumber = longField.getValue();
                if (longNumber != null) {
                    return String.valueOf(longNumber.longValue());
                } else {
                    return null;
                }
            case DOUBLE:
                NumberField doubleField = (NumberField) field;
                Number doubleNumber = doubleField.getValue();
                if (doubleNumber != null) {
                    return String.valueOf(doubleNumber.doubleValue());
                } else {
                    return null;
                }
            case FLOAT:
                NumberField floatField = (NumberField) field;
                Number floatNumber = floatField.getValue();
                if (floatNumber != null) {
                    return String.valueOf(floatNumber.floatValue());
                } else {
                    return null;
                }
            case INT:
            case INTEGER:
                NumberField integerField = (NumberField) field;
                Number integerNumber = integerField.getValue();
                if (integerNumber != null) {
                    return String.valueOf(integerNumber.intValue());
                } else {
                    return null;
                }
            case STRING:
            default:
                return (String) field.getValue();
            }
        } catch (Throwable t) {
            t.printStackTrace();
            return null;
        }

    }

    private void paintAsset() {

        LayoutContainer lcAction = new LayoutContainer();
        lcAction.setLayout(new BorderLayout());
        lcAction.setBorders(true);
        lcAction.setSize(475, -1);
        add(lcAction);

        // center panel: action form
        BorderLayoutData centerData = new BorderLayoutData(LayoutRegion.CENTER, .75F);
        centerData.setSplit(false);
        centerData.setMargins(new Margins(0, 0, 0, 0));

        FormData formData = new FormData("-20");
        formData.setMargins(new Margins(0, 10, 0, 0));

        if (!UserAgentUtils.isIE()) {
            actionFormPanel = new FormPanel();
            actionFormPanel.setFrame(false);
            actionFormPanel.setBodyBorder(false);
            actionFormPanel.setHeaderVisible(false);
            actionFormPanel.setLabelWidth(Constants.LABEL_WIDTH_CONFIG_FORM);
            actionFormPanel.setStyleAttribute("padding", "0px");
            actionFormPanel.setScrollMode(Scroll.AUTO);
            actionFormPanel.setLayout(new FlowLayout());
            actionFormPanel.addListener(Events.Render, new Listener<BaseEvent>() {

                public void handleEvent(BaseEvent be) {
                    NodeList<Element> nl = actionFormPanel.getElement().getElementsByTagName("form");
                    if (nl.getLength() > 0) {
                        Element elemForm = nl.getItem(0);
                        elemForm.setAttribute("autocomplete", "off");
                    }
                }
            });
        }

        actionFieldSet = new FieldSet();
        actionFieldSet.setId("configuration-form");
        actionFieldSet.setBorders(false);
        actionFieldSet.setStyleAttribute("padding", "0px");
        actionFieldSet.setScrollMode(Scroll.AUTO);

        FormLayout layout = new FormLayout();
        layout.setLabelWidth(Constants.LABEL_WIDTH_CONFIG_FORM);
        actionFieldSet.setLayout(layout);

        Field<?> field = null;
        for (GwtDeviceAssetChannel channel : asset.getChannels()) {
            field = paintChannel(channel);
            actionFieldSet.add(field, formData);
        }

        if (!UserAgentUtils.isIE()) {
            actionFormPanel.add(actionFieldSet, formData);
            lcAction.add(actionFormPanel, centerData);
        } else {
            lcAction.add(actionFieldSet, centerData);
        }
    }

    private Field<?> paintChannel(GwtDeviceAssetChannel channel) {
        Field<?> field = null;
        switch (channel.getTypeEnum()) {
        case LONG:
        case DOUBLE:
        case FLOAT:
        case INT:
        case INTEGER:
            field = paintNumberChannel(channel, null);
            break;
        case BOOLEAN:
            field = paintBooleanChannel(channel);
            break;
        case STRING:
        default:
            field = paintTextChannel(channel, null);
            break;
        }
        field.setName(channel.getName());
        field.setItemId(channel.getName());
        field.setReadOnly(channel.getModeEnum().equals(GwtDeviceAssetChannelMode.READ));
        return field;
    }

    private Field<?> paintTextChannel(GwtDeviceAssetChannel channel, Validator validator) {

        TextField<String> field = new TextField<String>();
        field.setName(channel.getName());
        field.setValue((String) channel.getValue());
        field.setAllowBlank(true);
        field.setFieldLabel(channel.getName() + " (" + channel.getType() + " - " + channel.getMode() + ")");
        field.addPlugin(dirtyPlugin);

        if (channel.getValue() != null) {
            field.setValue((String) channel.getValue());
            field.setOriginalValue((String) channel.getValue());
        }
        return field;
    }

    private Field<?> paintNumberChannel(GwtDeviceAssetChannel channel, Validator validator) {
        NumberField field = new NumberField();
        field.setName(channel.getName());
        field.setAllowBlank(true);
        field.setFieldLabel(channel.getName() + " (" + channel.getType() + " - " + channel.getMode() + ")");
        field.addPlugin(dirtyPlugin);

        switch (channel.getTypeEnum()) {
        case LONG:
            field.setPropertyEditorType(Long.class);
            if (channel.getValue() != null) {
                field.setValue(Long.parseLong(channel.getValue()));
                field.setOriginalValue(Long.parseLong(channel.getValue()));
            }
            break;
        case DOUBLE:
            field.setPropertyEditorType(Double.class);
            if (channel.getValue() != null) {
                field.setValue(Double.parseDouble(channel.getValue()));
                field.setOriginalValue(Double.parseDouble(channel.getValue()));
            }
            break;
        case FLOAT:
            field.setPropertyEditorType(Float.class);
            if (channel.getValue() != null) {
                field.setValue(Float.parseFloat(channel.getValue()));
                field.setOriginalValue(Float.parseFloat(channel.getValue()));
            }
            break;
        case INTEGER:
        case INT:
            field.setPropertyEditorType(Integer.class);
            if (channel.getValue() != null) {
                field.setValue(Integer.parseInt(channel.getValue()));
                field.setOriginalValue(Integer.parseInt(channel.getValue()));
            }
            break;
        default:
            break;
        }
        return field;
    }

    private Field<?> paintBooleanChannel(GwtDeviceAssetChannel channel) {

        Radio radioTrue = new Radio();
        radioTrue.setBoxLabel(MSGS.trueLabel());
        radioTrue.setItemId("true");

        Radio radioFalse = new Radio();
        radioFalse.setBoxLabel(MSGS.falseLabel());
        radioFalse.setItemId("false");

        RadioGroup radioGroup = new RadioGroup();
        radioGroup.setName(channel.getName());
        radioGroup.setItemId(channel.getName());
        radioGroup.setFieldLabel(channel.getName() + " (" + channel.getType() + " - " + channel.getMode() + ")");
        radioGroup.add(radioTrue);
        radioGroup.add(radioFalse);

        radioGroup.addPlugin(dirtyPlugin);

        boolean bool = Boolean.parseBoolean(channel.getValue());
        if (bool) {
            radioTrue.setValue(true);
            radioGroup.setOriginalValue(radioTrue);
        } else {
            radioFalse.setValue(true);
            radioGroup.setOriginalValue(radioFalse);
        }

        return radioGroup;
    }

}
