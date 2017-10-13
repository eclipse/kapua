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
package org.eclipse.kapua.service.account.internal;

import org.eclipse.kapua.commons.event.module.ServiceEventModule;
import org.eclipse.kapua.commons.event.module.ServiceEventModuleConfiguration;
import org.eclipse.kapua.locator.KapuaProvider;
import org.eclipse.kapua.service.account.internal.setting.KapuaAccountSetting;
import org.eclipse.kapua.service.account.internal.setting.KapuaAccountSettingKeys;

@KapuaProvider
public class AccountServiceModule extends ServiceEventModule {

    @Override
    protected ServiceEventModuleConfiguration initializeConfiguration() {
        KapuaAccountSetting kas = KapuaAccountSetting.getInstance();
        return new ServiceEventModuleConfiguration(
                kas.getString(KapuaAccountSettingKeys.ACCOUNT_INTERNAL_EVENT_ADDRESS),
                kas.getList(String.class, KapuaAccountSettingKeys.ACCOUNT_SERVICES_NAMES),
                AccountEntityManagerFactory.getInstance(),
                null);
    }

}