/*******************************************************************************
 * Copyright (c) 2011, 2016 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.service.authentication.shiro.setting;

import org.eclipse.kapua.commons.setting.AbstractKapuaSetting;

/**
 * Crypto setting implementation.
 * 
 * @since 1.0
 *
 */
public class KapuaCryptoSetting extends AbstractKapuaSetting<KapuaCryptoSettingKeys> {

    private static final String CRYPTO_CONFIG_RESOURCE = "kapua-crypto-setting.properties";

    private static final KapuaCryptoSetting instance = new KapuaCryptoSetting();

    /**
     * Construct a new crypto setting reading settings from {@link KapuaCryptoSetting#CRYPTO_CONFIG_RESOURCE}
     */
    private KapuaCryptoSetting() {
        super(CRYPTO_CONFIG_RESOURCE);
    }

    /**
     * Return the crypto setting instance (singleton)
     * 
     * @return
     */
    public static KapuaCryptoSetting getInstance() {
        return instance;
    }
}
