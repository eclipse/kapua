/*******************************************************************************
 * Copyright (c) 2017 Red Hat Inc and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Red Hat Inc - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.broker.core.setting;

import org.eclipse.kapua.broker.core.router.CamelKapuaDefaultRouter;
import org.eclipse.kapua.commons.setting.SettingKey;

/**
 * Broker core settings
 */
public enum BrokerCoreSettingKey implements SettingKey {
    /**
     * Camel default route configuration file name. (please specify just the name. The file path will be discovered by the class loader)
     * Used by the {@link CamelKapuaDefaultRouter} to load the routing configuration.
     */
    CAMEL_DEFAULT_ROUTE_CONFIGURATION_FILE_NAME("camel.default_route.configuration_file_name");

    private String key;

    private BrokerCoreSettingKey(String key) {
        this.key = key;
    }

    @Override
    public String key() {
        return key;
    }
}
