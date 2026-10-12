/*******************************************************************************
 * Copyright (c) 2026 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.consumer.telemetry;

import org.eclipse.kapua.commons.core.AbstractKapuaModule;
import org.eclipse.kapua.locator.guice.OverridingModule;
import org.eclipse.kapua.service.account.AccountService;
import org.eclipse.kapua.service.device.registry.DeviceRegistryService;
import org.eclipse.kapua.translator.KapuaKuraTranslatorsModule;

import com.google.inject.Key;
import com.google.inject.name.Names;

/**
 * Makes the {@link org.eclipse.kapua.translator.kura.kapua.TranslatorDataKuraKapua} use the cached {@link AccountService} and {@link DeviceRegistryService},
 * since it is invoked for each telemetry message.
 *
 * @since 2.1.0
 */
@OverridingModule
public class TranslatorCachedServicesModule extends AbstractKapuaModule {

    @Override
    protected void configureModule() {
        bind(AccountService.class)
                .annotatedWith(Names.named(KapuaKuraTranslatorsModule.TRANSLATOR_DATA_KURA_KAPUA_ACCOUNT_SERVICE))
                .to(Key.get(AccountService.class, Names.named("cachedAccountService")));
        bind(DeviceRegistryService.class)
                .annotatedWith(Names.named(KapuaKuraTranslatorsModule.TRANSLATOR_DATA_KURA_KAPUA_DEVICE_REGISTRY_SERVICE))
                .to(Key.get(DeviceRegistryService.class, Names.named("cachedDeviceRegistryService")));
    }
}
