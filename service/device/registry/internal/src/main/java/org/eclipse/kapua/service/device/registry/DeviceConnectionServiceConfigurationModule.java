/*******************************************************************************
 * Copyright (c) 2023, 2022 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.service.device.registry;

import com.google.inject.Provides;
import org.eclipse.kapua.commons.configuration.AbstractKapuaConfigurableServiceCache;
import org.eclipse.kapua.commons.configuration.CachingServiceConfigRepository;
import org.eclipse.kapua.commons.configuration.RootUserTester;
import org.eclipse.kapua.commons.configuration.ServiceConfigImplJpaRepository;
import org.eclipse.kapua.commons.configuration.ServiceConfigurationManager;
import org.eclipse.kapua.commons.configuration.ServiceConfigurationManagerCachingWrapper;
import org.eclipse.kapua.commons.core.AbstractKapuaModule;
import org.eclipse.kapua.commons.jpa.KapuaJpaRepositoryConfiguration;
import org.eclipse.kapua.model.config.metatype.KapuaMetatypeFactory;
import org.eclipse.kapua.service.device.authentication.api.DeviceConnectionCredentialAdapter;
import org.eclipse.kapua.service.device.registry.connection.internal.DeviceConnectionServiceConfigurationManager;

import javax.inject.Named;
import javax.inject.Singleton;
import java.util.Map;

/**
 * {@link DeviceConnectionServiceConfigurationManager}'s {@link AbstractKapuaModule}.
 *
 * @since 2.0.0
 */
public class DeviceConnectionServiceConfigurationModule extends AbstractKapuaModule {

    @Override
    protected void configureModule() {
        // Nothing to bind here
    }

    @Provides
    @Singleton
    @Named("DeviceConnectionServiceConfigurationManager")
    ServiceConfigurationManager deviceConnectionServiceConfigurationManager(
            RootUserTester rootUserTester,
            KapuaJpaRepositoryConfiguration jpaRepoConfig,
            Map<String, DeviceConnectionCredentialAdapter> availableDeviceConnectionAdapters,
            KapuaMetatypeFactory kapuaMetatypeFactory) {
        return new ServiceConfigurationManagerCachingWrapper(
                new DeviceConnectionServiceConfigurationManager(
                        new CachingServiceConfigRepository(
                                new ServiceConfigImplJpaRepository(jpaRepoConfig),
                                new AbstractKapuaConfigurableServiceCache().createCache()
                        ),
                        rootUserTester,
                        availableDeviceConnectionAdapters,
                        kapuaMetatypeFactory)
        );
    }
}
