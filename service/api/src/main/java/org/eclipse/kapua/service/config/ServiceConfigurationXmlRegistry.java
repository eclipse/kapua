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
package org.eclipse.kapua.service.config;

import org.eclipse.kapua.locator.KapuaLocator;

import javax.xml.bind.annotation.XmlRegistry;

/**
 * {@link ServiceConfiguration} xml factory class
 *
 * @since 1.0
 */
@XmlRegistry
public class ServiceConfigurationXmlRegistry {

    private final ServiceConfigurationFactory serviceConfigurationFactory = KapuaLocator.getInstance().getFactory(ServiceConfigurationFactory.class);

    /**
     * Creates a new service configuration
     *
     * @return
     */
    public ServiceConfiguration newConfiguration() {
        return serviceConfigurationFactory.newConfigurationInstance();
    }

    /**
     * Creates a new service component configuration
     *
     * @return
     */
    public ServiceComponentConfiguration newComponentConfiguration() {
        return serviceConfigurationFactory.newComponentConfigurationInstance(null);
    }
}
