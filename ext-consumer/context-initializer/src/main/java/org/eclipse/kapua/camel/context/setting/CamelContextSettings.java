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

import org.eclipse.kapua.commons.setting.AbstractKapuaSetting;

/**
 * Camel context settings implementation
 * 
 * @since 1.0
 */
public class CamelContextSettings extends AbstractKapuaSetting<CamelContextSettingKey> {

    private static final String CAMEL_CONTEXT_CONFIG_RESOURCE = "kapua-camel-context-setting.properties";

    private static final CamelContextSettings INSTANCE = new CamelContextSettings();

    private CamelContextSettings() {
        super(CAMEL_CONTEXT_CONFIG_RESOURCE);
    }

    /**
     * Get the datastore setting instance
     * 
     * @return
     */
    public static CamelContextSettings getInstance() {
        return INSTANCE;
    }
}
