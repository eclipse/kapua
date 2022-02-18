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

import javax.xml.bind.annotation.XmlRegistry;

import org.eclipse.kapua.locator.KapuaLocator;

/**
 * {@link ServiceConfiguration} xml factory class
 *
 * @since 1.0
 */
@XmlRegistry
public class ServiceConfigurationXmlRegistry {

    private static final KapuaLocator LOCATOR = KapuaLocator.getInstance();
    private static final ServiceConfigurationFactory SERVICE_CONFIGURATION_FACTORY = LOCATOR.getFactory(ServiceConfigurationFactory.class);

    /**
     * Creates a new service configuration
     *
     * @return
     */
    public ServiceConfiguration newConfiguration() {
        return SERVICE_CONFIGURATION_FACTORY.newConfigurationInstance();
    }

    /**
     * Creates a new service component configuration
     *
     * @return
     */
    public ServiceComponentConfiguration newComponentConfiguration() {
        return SERVICE_CONFIGURATION_FACTORY.newComponentConfigurationInstance(null);
    }
}
