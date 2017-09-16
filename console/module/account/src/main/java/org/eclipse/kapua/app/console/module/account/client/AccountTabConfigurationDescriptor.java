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
package org.eclipse.kapua.app.console.module.account.client;

import org.eclipse.kapua.app.console.module.account.shared.model.GwtAccount;
import org.eclipse.kapua.app.console.module.api.client.ui.view.descriptor.AbstractEntityTabDescriptor;
import org.eclipse.kapua.app.console.module.api.shared.model.GwtSession;

public class AccountTabConfigurationDescriptor extends AbstractEntityTabDescriptor<GwtAccount, AccountTabConfiguration, AccountView> {

    @Override
    public AccountTabConfiguration getTabViewInstance(AccountView view, GwtSession currentSession) {
        return new AccountTabConfiguration(currentSession, null);
    }

    @Override
    public String getViewId() {
        return "account.configuration";
    }

    @Override
    public Integer getOrder() {
        return 300;
    }

    @Override
    public Boolean isEnabled(GwtSession currentSession) {
        return true;
    }
}
