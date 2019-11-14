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
