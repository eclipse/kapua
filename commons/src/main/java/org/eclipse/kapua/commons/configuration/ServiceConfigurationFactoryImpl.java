/*******************************************************************************
 * Copyright (c) 2019 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
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
