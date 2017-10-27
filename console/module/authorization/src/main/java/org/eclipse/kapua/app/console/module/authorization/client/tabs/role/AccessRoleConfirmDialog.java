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
package org.eclipse.kapua.app.console.module.authorization.client.tabs.role;

import org.eclipse.kapua.app.console.module.api.client.resources.icons.KapuaIcon;
import org.eclipse.kapua.app.console.module.api.client.ui.dialog.ActionDialog;
import org.eclipse.kapua.app.console.module.authorization.client.messages.ConsoleRoleMessages;

import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.FormPanel;
import com.extjs.gxt.ui.client.widget.form.FormPanel.Encoding;
import com.extjs.gxt.ui.client.widget.form.FormPanel.Method;
import com.extjs.gxt.ui.client.widget.layout.FormLayout;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Element;

public class AccessRoleConfirmDialog extends ActionDialog {

    private final static ConsoleRoleMessages MSGS = GWT.create(ConsoleRoleMessages.class);

    public Button yesButton;
    public Button noButton;
    protected static final int FORM_LABEL_WIDTH = 120;
    protected FormPanel formPanel;

    @Override
    protected void addListeners() {
        // TODO Auto-generated method stub

    }

    @Override
    public void submit() {
        // TODO Auto-generated method stub

    }

    @Override
    public String getHeaderMessage() {
        // TODO Auto-generated method stub
        return MSGS.confirmationDialogAccessRoleHeader();
    }

    @Override
    public KapuaIcon getInfoIcon() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String getInfoMessage() {
        // TODO Auto-generated method stub
        return MSGS.confirmationDialogAccessRoleInfo();
    }

    @Override
    protected void onRender(Element parent, int pos) {
        // TODO Auto-generated method stub
        super.onRender(parent, pos);
        FormLayout formLayout = new FormLayout();
        formLayout.setLabelWidth(FORM_LABEL_WIDTH);

        formPanel = new FormPanel();
        formPanel.setPadding(0);
        formPanel.setFrame(false);
        formPanel.setHeaderVisible(false);
        formPanel.setBodyBorder(false);
        formPanel.setBorders(false);
        formPanel.setLayout(formLayout);
        formPanel.setEncoding(Encoding.MULTIPART);
        formPanel.setMethod(Method.POST);

        addListeners();

        add(formPanel);

        //
        // Buttons setup
        createButtons();
    }

    @Override
    public void createButtons() {
        super.createButtons();
        yesButton = new Button("Yes");
        yesButton.setSize(60, 25);
        yesButton.setStyleAttribute("margin-right", "2px");
        noButton = new Button("No");
        noButton.setSize(60, 25);
        noButton.setStyleAttribute("margin-left", "3px");
        submitButton.hide();
        cancelButton.hide();
        addButton(yesButton);
        addButton(noButton);
    }
}