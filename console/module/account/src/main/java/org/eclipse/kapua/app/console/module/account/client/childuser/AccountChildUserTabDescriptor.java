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
package org.eclipse.kapua.app.console.module.account.client.childuser;

import org.eclipse.kapua.app.console.module.api.client.ui.view.descriptor.AbstractEntityTabDescriptor;
import org.eclipse.kapua.app.console.module.api.shared.model.GwtSession;
import org.eclipse.kapua.app.console.module.account.client.AccountView;
import org.eclipse.kapua.app.console.module.account.shared.model.GwtAccount;

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
        return true;
    }
}
