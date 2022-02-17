/*******************************************************************************
 * Copyright (c) 2017, 2022 Eurotech and/or its affiliates and others
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

import org.eclipse.kapua.app.console.module.account.shared.model.GwtAccount;
import org.eclipse.kapua.app.console.module.account.shared.model.permission.AccountSessionPermission;
import org.eclipse.kapua.app.console.module.api.client.ui.view.descriptor.AbstractEntityTabDescriptor;
import org.eclipse.kapua.app.console.module.api.shared.model.session.GwtSession;

public class AccountDescriptionTabDescriptor extends AbstractEntityTabDescriptor<GwtAccount, AccountDescriptionTab, AccountView> {

    @Override
    public AccountDescriptionTab getTabViewInstance(AccountView view, GwtSession currentSession) {
        return new AccountDescriptionTab(currentSession);
    }

    @Override
    public String getViewId() {
        return "account.description";
    }

    @Override
    public Integer getOrder() {
        return 100;
    }

    @Override
    public Boolean isEnabled(GwtSession currentSession) {
        return currentSession.hasPermission(AccountSessionPermission.read());
    }
}
