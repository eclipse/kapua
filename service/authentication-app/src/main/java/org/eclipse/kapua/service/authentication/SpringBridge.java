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
package org.eclipse.kapua.service.authentication;

import com.google.inject.Key;
import com.google.inject.TypeLiteral;
import com.google.inject.util.Types;
import org.eclipse.kapua.locator.KapuaLocator;
import org.eclipse.kapua.locator.guice.GuiceLocatorImpl;
import org.eclipse.kapua.service.device.authentication.api.DeviceConnectionCredentialAdapter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

/**
 * Guice's injection bridge for Spring.
 *
 * @since 2.0.0
 */
@Configuration
public class SpringBridge {

    @Bean
    Map<String, DeviceConnectionCredentialAdapter> deviceConnectionCredentialAdapterMap() {
        return ((GuiceLocatorImpl) KapuaLocator.getInstance())
                .getInjector()
                .getInstance(
                        Key.get(
                                (TypeLiteral<Map<String, DeviceConnectionCredentialAdapter>>) TypeLiteral.get(Types.mapOf(String.class, DeviceConnectionCredentialAdapter.class))
                        )
                );
    }
}
