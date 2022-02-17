/*******************************************************************************
 * Copyright (c) 2020, 2022 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.app.console.core.client;

import org.eclipse.kapua.app.console.module.api.client.messages.ConsoleMessages;
import org.eclipse.kapua.app.console.module.api.client.resources.icons.KapuaIcon;
import org.eclipse.kapua.app.console.module.api.client.ui.dialog.SimpleDialog;
import org.eclipse.kapua.app.console.module.api.client.util.DialogUtils;
import org.eclipse.kapua.app.console.module.api.shared.model.session.GwtSession;
import org.eclipse.kapua.app.console.module.authentication.client.messages.ConsoleCredentialMessages;
import org.eclipse.kapua.app.console.module.authentication.client.tabs.credentials.MfaManagementPanel;

import com.extjs.gxt.ui.client.Style.Scroll;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.google.gwt.core.client.GWT;

public class MfaManagementDialog extends SimpleDialog {

    private static final ConsoleCredentialMessages CREDENTIAL_MSGS = GWT.create(ConsoleCredentialMessages.class);
    private static final ConsoleMessages MSGS = GWT.create(ConsoleMessages.class);

    private GwtSession currentSession;

    private MfaManagementPanel mfaManagementPanel;

    public MfaManagementDialog(GwtSession currentSession) {
        this.currentSession = currentSession;
        this.mfaManagementPanel = new MfaManagementPanel(currentSession, this);
        DialogUtils.resizeDialog(this, 600, 560);
        setScrollMode(Scroll.AUTOY);
    }

    @Override
    public void createBody() {
        bodyPanel.setLayout(new FitLayout());
        bodyPanel.add(mfaManagementPanel);
    }

    @Override
    protected void addListeners() {
    }

    @Override
    public void submit() {

    }

    @Override
    public String getHeaderMessage() {
        return CREDENTIAL_MSGS.mfaDialogHeader();
    }

    @Override
    public KapuaIcon getInfoIcon() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String getInfoMessage() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void createButtons() {
        getButtonBar().removeAll();

        cancelButton = new Button(getCancelButtonText());
        cancelButton.setSize(60, 25);
        cancelButton.setStyleAttribute("margin-left", "3px");
        cancelButton.addSelectionListener(new SelectionListener<ButtonEvent>() {

            @Override
            public void componentSelected(ButtonEvent ce) {
                exitStatus = null;
                hide();
            }
        });

        addButton(cancelButton);
    }

    @Override
    protected String getCancelButtonText() {
        return MSGS.closeButton();
    }

}
