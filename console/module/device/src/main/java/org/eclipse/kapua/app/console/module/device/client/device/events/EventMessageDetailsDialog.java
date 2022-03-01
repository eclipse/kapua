/*******************************************************************************
 * Copyright (c) 2019, 2022 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.app.console.module.device.client.device.events;

import com.extjs.gxt.ui.client.Style.Scroll;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.form.TextArea;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.google.gwt.core.client.GWT;
import org.eclipse.kapua.app.console.module.api.client.resources.icons.KapuaIcon;
import org.eclipse.kapua.app.console.module.api.client.ui.dialog.SimpleDialog;
import org.eclipse.kapua.app.console.module.api.client.ui.panel.FormPanel;
import org.eclipse.kapua.app.console.module.api.client.util.DialogUtils;
import org.eclipse.kapua.app.console.module.device.client.messages.ConsoleDeviceMessages;

public class EventMessageDetailsDialog extends SimpleDialog {
    private String eventReceivedOnFormatted;
    private String eventType;
    private String eventMessage;
    private TextArea eventMessageField;
    private LayoutContainer eventOutput;

    private static final ConsoleDeviceMessages DEVICES_MSGS = GWT.create(ConsoleDeviceMessages.class);

    public EventMessageDetailsDialog(String eventReceivedOnFormatted, String eventType, String eventMessage) {
        this.eventReceivedOnFormatted = eventReceivedOnFormatted;
        this.eventType = eventType;
        this.eventMessage = eventMessage;

        DialogUtils.resizeDialog(this, 550, 320);
    }

    @Override
    public void createBody() {

        FormPanel eventsFormPanel = new FormPanel(0);
        eventsFormPanel.setHeight(270);
        eventsFormPanel.setWidth(550);
        eventsFormPanel.setScrollMode(Scroll.AUTO);
        eventsFormPanel.setPadding(15);
        submitButton.hide();

        eventOutput = new LayoutContainer();
        eventOutput.setBorders(false);
        eventOutput.setLayout(new FitLayout());

        eventMessageField = new TextArea();
        eventMessageField.setReadOnly(true);
        eventMessageField.setHeight(220);
        eventMessageField.setWidth(515);
        eventMessageField.setStyleAttribute("white-space", "pre-wrap");
        eventMessageField.setValue(eventMessage);
        eventOutput.add(eventMessageField);
        eventsFormPanel.add(eventOutput);
        bodyPanel.add(eventsFormPanel);
    }

    @Override
    protected void addListeners() {
    }

    @Override
    public void submit() {
    }

    @Override
    public String getHeaderMessage() {
        return DEVICES_MSGS.deviceEventMessageDialogHeaderMessage(eventType, eventReceivedOnFormatted);
    }

    @Override
    public KapuaIcon getInfoIcon() {
        return null;
    }

    @Override
    public String getInfoMessage() {
        return null;
    }
}
