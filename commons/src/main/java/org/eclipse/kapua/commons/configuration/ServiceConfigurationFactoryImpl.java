/*******************************************************************************
 * Copyright (c) 2019, 2022 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.commons.configuration;

import org.eclipse.kapua.service.config.ServiceComponentConfiguration;
import org.eclipse.kapua.service.config.ServiceConfiguration;
import org.eclipse.kapua.service.config.ServiceConfigurationFactory;
import org.eclipse.kapua.locator.KapuaProvider;

/**
 * Service configuration entity service factory implementation.
 *
 * @since 1.0
 */
@KapuaProvider
public class ServiceConfigurationFactoryImpl implements ServiceConfigurationFactory {

    @Override
    public ServiceComponentConfiguration newComponentConfigurationInstance(String componentConfigurationId) {
        return new ServiceComponentConfigurationImpl(componentConfigurationId);
    }

    @Override
    public ServiceConfiguration newConfigurationInstance() {
        return new ServiceConfigurationImpl();
    }

}
