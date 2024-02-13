/*******************************************************************************
 * Copyright (c) 2016, 2022 Eurotech and/or its affiliates and others
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *      Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.service.device.call.kura;

import org.eclipse.kapua.service.account.AccountService;
import org.eclipse.kapua.service.device.call.DeviceCallFactory;
import org.eclipse.kapua.service.device.registry.DeviceRegistryService;
import org.eclipse.kapua.translator.TranslatorHub;
import org.eclipse.kapua.transport.TransportClientFactory;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * {@link DeviceCallFactory} {@link Kura} implementation.
 *
 * @since 1.0.0
 */
@Singleton
public class KuraDeviceCallFactoryImpl implements DeviceCallFactory {
    private final AccountService accountService;
    private final DeviceRegistryService deviceRegistryService;
    private final TransportClientFactory transportClientFactory;
    private final TranslatorHub translatorHub;

    @Inject
    public KuraDeviceCallFactoryImpl(AccountService accountService,
                                     DeviceRegistryService deviceRegistryService,
                                     TransportClientFactory transportClientFactory, TranslatorHub translatorHub) {
        this.accountService = accountService;
        this.deviceRegistryService = deviceRegistryService;
        this.transportClientFactory = transportClientFactory;
        this.translatorHub = translatorHub;
    }

    @Override
    public KuraDeviceCallImpl newDeviceCall() {
        return new KuraDeviceCallImpl(accountService, deviceRegistryService, transportClientFactory, translatorHub);
    }
}
