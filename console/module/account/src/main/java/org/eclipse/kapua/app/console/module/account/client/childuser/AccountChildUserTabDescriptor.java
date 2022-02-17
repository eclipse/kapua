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
package org.eclipse.kapua.app.console.module.account.client.childuser;

import org.eclipse.kapua.app.console.module.account.client.AccountView;
import org.eclipse.kapua.app.console.module.account.shared.model.GwtAccount;
import org.eclipse.kapua.app.console.module.api.client.ui.view.descriptor.AbstractEntityTabDescriptor;
import org.eclipse.kapua.app.console.module.api.shared.model.session.GwtSession;
import org.eclipse.kapua.app.console.module.user.shared.model.permission.UserSessionPermission;

public class AccountChildUserTabDescriptor extends AbstractEntityTabDescriptor<GwtAccount, AccountChildUserTab, AccountView> {

    @Override
    public AccountChildUserTab getTabViewInstance(AccountView view, GwtSession currentSession) {
        return new AccountChildUserTab(currentSession);
    }

    @Override
    public String getViewId() {
        return "childaccount.users";
    }

    @Override
    public Integer getOrder() {
        return 200;
    }

    @Override
    public Boolean isEnabled(GwtSession currentSession) {
        return currentSession.hasPermission(UserSessionPermission.read());
    }
}
