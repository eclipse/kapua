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
package org.eclipse.kapua.app.console.module.authentication.client.tabs.credentials;

import com.extjs.gxt.ui.client.Style.Scroll;
import com.extjs.gxt.ui.client.widget.Dialog;
import com.google.gwt.core.client.GWT;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.user.client.Element;
import org.eclipse.kapua.app.console.module.api.client.resources.icons.IconSet;
import org.eclipse.kapua.app.console.module.api.client.resources.icons.KapuaIcon;
import org.eclipse.kapua.app.console.module.api.client.ui.dialog.KapuaDialog;
import org.eclipse.kapua.app.console.module.authentication.client.messages.ConsoleCredentialMessages;

public class ApiKeyConfirmationDialog extends KapuaDialog {

    protected static final ConsoleCredentialMessages CRED_MSGS = GWT.create(ConsoleCredentialMessages.class);

    private final String apiKey;

    public ApiKeyConfirmationDialog(String apiKey) {
        this.apiKey = apiKey;
    }

    @Override
    protected void onRender(Element parent, int pos) {
        super.onRender(parent, pos);
        setButtons(Dialog.OK);
        setHideOnButtonClick(true);
        setSize(350, 250);
        setScrollMode(Scroll.AUTO);
    }

    @Override
    public String getHeaderMessage() {
        return CRED_MSGS.dialogConfirmationAPI();
    }

    @Override
    public KapuaIcon getInfoIcon() {
        return new KapuaIcon(IconSet.KEY);
    }

    @Override
    public String getInfoMessage() {
        return new SafeHtmlBuilder().appendEscapedLines(CRED_MSGS.dialogAddConfirmationApiKey(apiKey)).toSafeHtml().asString();
    }
}
