/*******************************************************************************
 * Copyright (c) 2021, 2022 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.app.console.module.account.client;

import org.eclipse.kapua.app.console.module.account.client.cors.CorsView;
import org.eclipse.kapua.app.console.module.account.shared.model.GwtAccount;
import org.eclipse.kapua.app.console.module.api.client.resources.icons.IconSet;
import org.eclipse.kapua.app.console.module.api.client.resources.icons.KapuaIcon;
import org.eclipse.kapua.app.console.module.api.client.ui.tab.KapuaTabItem;
import org.eclipse.kapua.app.console.module.api.shared.model.session.GwtSession;

import com.google.gwt.user.client.Element;

public class AccountDetailsTabCors extends KapuaTabItem<GwtAccount> {

    private final AccountDetailsView accountDetailsView;

    public AccountDetailsTabCors(GwtSession currentSession, AccountDetailsView accountDetailsView) {
        super(currentSession, "CORS", new KapuaIcon(IconSet.EXTERNAL_LINK));
        this.accountDetailsView = accountDetailsView;
    }

    @Override
    protected void doRefresh() {

    }

    @Override
    protected void onRender(Element parent, int index) {
        super.onRender(parent, index);
        CorsView corsView = new CorsView(currentSession);
        add(corsView);
    }

}
