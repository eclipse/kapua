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
import com.google.gwt.core.client.GWT;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.user.client.Element;
import org.eclipse.kapua.app.console.module.api.client.resources.icons.IconSet;
import org.eclipse.kapua.app.console.module.api.client.resources.icons.KapuaIcon;
import org.eclipse.kapua.app.console.module.api.client.ui.dialog.InfoDialog;
import org.eclipse.kapua.app.console.module.authentication.client.messages.ConsoleCredentialMessages;

public class ApiKeyConfirmationDialog extends InfoDialog {

    protected static final ConsoleCredentialMessages CRED_MSGS = GWT.create(ConsoleCredentialMessages.class);

    public ApiKeyConfirmationDialog(String apiKey) {
        super(CRED_MSGS.dialogConfirmationAPI(), new KapuaIcon(IconSet.KEY), new SafeHtmlBuilder().appendEscapedLines(CRED_MSGS.dialogAddConfirmationApiKey(apiKey)).toSafeHtml().asString());
        setStyleAttribute("background-color", "#F0F0F0");
        setBodyStyle("background-color: #F0F0F0");
    }

    @Override
    protected void onRender(Element parent, int pos) {
        super.onRender(parent, pos);
        setSize(350, 200);
        setScrollMode(Scroll.AUTO);
    }

}
