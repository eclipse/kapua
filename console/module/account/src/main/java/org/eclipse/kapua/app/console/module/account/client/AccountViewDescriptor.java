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
import org.eclipse.kapua.app.console.module.account.shared.model.permission.AccountSessionPermission;
import org.eclipse.kapua.app.console.module.api.client.resources.icons.IconSet;
import org.eclipse.kapua.app.console.module.api.client.ui.view.EntityView;
import org.eclipse.kapua.app.console.module.api.client.ui.view.descriptor.AbstractEntityViewDescriptor;
import org.eclipse.kapua.app.console.module.api.shared.model.session.GwtSession;

public class AccountViewDescriptor extends AbstractEntityViewDescriptor<GwtAccount> {

    @Override
    public String getViewId() {
        return "childaccounts";
    }

    @Override
    public IconSet getIcon() {
        return IconSet.SITEMAP;
    }

    @Override
    public Integer getOrder() {
        return 1000;
    }

    @Override
    public String getName() {
        return AccountView.getName();
    }

    @Override
    public EntityView<GwtAccount> getViewInstance(GwtSession currentSession) {
        return new AccountView(currentSession);
    }

    @Override
    public Boolean isEnabled(GwtSession currentSession) {
        return currentSession.hasPermission(AccountSessionPermission.read());
    }
}
