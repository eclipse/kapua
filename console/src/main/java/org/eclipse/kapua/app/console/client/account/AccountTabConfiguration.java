
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
package org.eclipse.kapua.app.console.client.account;

import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Element;
import org.eclipse.kapua.app.console.client.messages.ConsoleMessages;
import org.eclipse.kapua.app.console.shared.model.GwtSession;
import org.eclipse.kapua.app.console.shared.model.account.GwtAccount;

public class AccountTabConfiguration extends LayoutContainer {

    private final ConsoleMessages msgs = GWT.create(ConsoleMessages.class);

    @SuppressWarnings("unused")
    private GwtSession m_currentSession;

    private AccountConfigComponents m_configComponents;

    public AccountTabConfiguration(GwtSession currentSession) {
        m_currentSession = currentSession;
        m_configComponents = new AccountConfigComponents(currentSession, this);
    }

    public void setAccount(GwtAccount selectedAccount) {
        m_configComponents.setAccount(selectedAccount);
    }

    public void refresh() {
        m_configComponents.refresh();
    }

    protected void onRender(Element parent, int index) {

        super.onRender(parent, index);

        setId("AccountTabsContainer");
        setLayout(new FitLayout());

        add(m_configComponents);
    }
}
