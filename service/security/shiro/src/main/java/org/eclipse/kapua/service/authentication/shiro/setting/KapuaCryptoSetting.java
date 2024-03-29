/*******************************************************************************
 * Copyright (c) 2016, 2022 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.service.authentication.shiro.setting;

import org.eclipse.kapua.commons.setting.AbstractKapuaSetting;

import javax.inject.Inject;

/**
 * Crypto setting implementation.
 *
 * @since 1.0
 */
public class KapuaCryptoSetting extends AbstractKapuaSetting<KapuaCryptoSettingKeys> {

    private static final String CRYPTO_CONFIG_RESOURCE = "kapua-crypto-setting.properties";

    /**
     * Construct a new crypto setting reading settings from {@link KapuaCryptoSetting#CRYPTO_CONFIG_RESOURCE}
     */
    @Inject
    public KapuaCryptoSetting() {
        super(CRYPTO_CONFIG_RESOURCE);
    }

}
