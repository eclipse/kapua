/*******************************************************************************
 * Copyright (c) 2019 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.app.console.module.api.client.ui.dialog.entity;

import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.TextArea;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Element;
import org.eclipse.kapua.app.console.module.api.client.messages.ConsoleMessages;
import org.eclipse.kapua.app.console.module.api.client.resources.icons.KapuaIcon;
import org.eclipse.kapua.app.console.module.api.client.ui.dialog.KapuaDialog;
import org.eclipse.kapua.app.console.module.api.client.util.DialogUtils;

public class EntityLogDialog extends KapuaDialog {

    protected static final ConsoleMessages MSGS = GWT.create(ConsoleMessages.class);

    protected TextArea logArea;

    protected final String header;
    protected final String log;

    private Button submitButton;

    public EntityLogDialog(String header, String log) {
        super();

        this.header = header;
        this.log = log;

        DialogUtils.resizeDialog(this, 600, 500);
    }

    @Override
    protected void onRender(Element parent, int pos) {
        super.onRender(parent, pos);

        logArea = new TextArea();
        logArea.setEmptyText(MSGS.dialogLogEmptyText());
        logArea.setStyleAttribute("background", "transparent");
        logArea.setBorders(false);
        logArea.setValue(getLog());
        logArea.setWidth(585);
        logArea.setHeight(430);
        logArea.setReadOnly(true);

        add(logArea);
    }

    @Override
    public String getHeaderMessage() {
        return header;
    }

    public String getLog() {
        return log;
    }

    @Override
    public void createButtons() {
        super.createButtons();

        submitButton = new Button(MSGS.close());
        submitButton.setSize(60, 25);
        submitButton.addSelectionListener(new SelectionListener<ButtonEvent>() {

            @Override
            public void componentSelected(ButtonEvent ce) {
                hide();
            }
        });

        addButton(submitButton);
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
