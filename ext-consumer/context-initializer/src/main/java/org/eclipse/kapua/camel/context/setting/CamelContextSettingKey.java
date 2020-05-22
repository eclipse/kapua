/*******************************************************************************
 * Copyright (c) 2018 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.camel.context.setting;

import org.eclipse.kapua.commons.setting.SettingKey;

/**
 * Camel context setting keys.
 * 
 * @since 1.0
 *
 */
public enum CamelContextSettingKey implements SettingKey {

    /**
     * Camel configuration file
     */
    CAMEL_CONTEXT_CONFIGURATION_RESOURCE("camel_context.configuration.resource"),
    /**
     * Tell if Camel configuration file is external (so in the os filesystem) or internal (in the application packaging)
     */
    CAMEL_CONTEXT_IS_LOCAL_CONFIGURATION_RESOURCE("camel_context.configuration.is_local_resource");

    private String key;

    private CamelContextSettingKey(String key) {
        this.key = key;
    }

    @Override
    public String key() {
        return key;
    }
}
