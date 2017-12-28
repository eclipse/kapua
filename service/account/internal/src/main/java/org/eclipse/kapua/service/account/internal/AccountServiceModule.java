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

import java.util.List;

import javax.inject.Inject;

import org.eclipse.kapua.commons.event.ServiceEventClientConfiguration;
import org.eclipse.kapua.commons.event.ServiceEventModule;
import org.eclipse.kapua.commons.event.ServiceEventModuleConfiguration;
import org.eclipse.kapua.commons.event.ServiceInspector;
import org.eclipse.kapua.service.account.AccountService;
import org.eclipse.kapua.service.account.internal.setting.KapuaAccountSetting;
import org.eclipse.kapua.service.account.internal.setting.KapuaAccountSettingKeys;

//@KapuaProvider
public class AccountServiceModule extends ServiceEventModule {

    @Inject
    private AccountService accountService;

    @Override
    protected ServiceEventModuleConfiguration initializeConfiguration() {
        KapuaAccountSetting settings = KapuaAccountSetting.getInstance();
        String address = settings.getString(KapuaAccountSettingKeys.ACCOUNT_EVENT_ADDRESS);
        List<ServiceEventClientConfiguration> secc = ServiceInspector.getEventBusClients(accountService, AccountService.class);
        return new ServiceEventModuleConfiguration(
                address,
                AccountEntityManagerFactory.getInstance(),
                secc.toArray(new ServiceEventClientConfiguration[0]));
    }

}