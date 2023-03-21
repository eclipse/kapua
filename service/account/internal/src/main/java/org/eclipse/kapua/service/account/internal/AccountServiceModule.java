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
package org.eclipse.kapua.service.account.internal;

import org.eclipse.kapua.commons.core.ServiceModule;
import org.eclipse.kapua.commons.event.ServiceEventClientConfiguration;
import org.eclipse.kapua.commons.event.ServiceEventModuleTransactionalConfiguration;
import org.eclipse.kapua.commons.event.ServiceEventTransactionalModule;
import org.eclipse.kapua.commons.event.ServiceInspector;
import org.eclipse.kapua.commons.jpa.JpaTxManager;
import org.eclipse.kapua.commons.jpa.KapuaEntityManagerFactory;
import org.eclipse.kapua.commons.jpa.KapuaJpaRepositoryConfiguration;
import org.eclipse.kapua.service.account.AccountService;
import org.eclipse.kapua.service.account.internal.setting.KapuaAccountSetting;
import org.eclipse.kapua.service.account.internal.setting.KapuaAccountSettingKeys;

import javax.inject.Inject;
import java.util.List;

/**
 * {@link AccountService} {@link ServiceModule} implementation.
 *
 * @since 1.0.0
 */
public class AccountServiceModule extends ServiceEventTransactionalModule implements ServiceModule {

    private static final KapuaAccountSetting ACCOUNT_SETTING = KapuaAccountSetting.getInstance();

    @Inject
    private AccountService accountService;
    @Inject
    private KapuaJpaRepositoryConfiguration jpaRepoConfig;

    @Override
    protected ServiceEventModuleTransactionalConfiguration initializeConfiguration() {
        String address = ACCOUNT_SETTING.getString(KapuaAccountSettingKeys.ACCOUNT_EVENT_ADDRESS);

        List<ServiceEventClientConfiguration> eventBusClients = ServiceInspector.getEventBusClients(accountService, AccountService.class);

        return new ServiceEventModuleTransactionalConfiguration(
                address,
                new JpaTxManager(new KapuaEntityManagerFactory("kapua-account")),
                eventBusClients.toArray(new ServiceEventClientConfiguration[0]),
                jpaRepoConfig);
    }

}
