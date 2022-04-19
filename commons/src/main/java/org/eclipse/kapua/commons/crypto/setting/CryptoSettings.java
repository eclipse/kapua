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
package org.eclipse.kapua.commons.crypto.setting;

import org.eclipse.kapua.commons.setting.AbstractKapuaSetting;

/**
 * {@link CryptoSettings} for {@code kapua-commons} module.
 *
 * @see AbstractKapuaSetting
 * @since 2.0.0
 */
public class CryptoSettings extends AbstractKapuaSetting<CryptoSettingKeys> {

    /**
     * Settings filename.
     *
     * @since 2.0.0
     */
    private static final String CRYPTO_SETTING_RESOURCE = "crypto-settings.properties";

    /**
     * Singleton instance.
     *
     * @since 2.0.0
     */
    private static final CryptoSettings INSTANCE = new CryptoSettings();

    /**
     * Constructor.
     *
     * @since 2.0.0
     */
    private CryptoSettings() {
        super(CRYPTO_SETTING_RESOURCE);
    }

    /**
     * Gets a singleton instance of {@link CryptoSettings}.
     *
     * @return A singleton instance of {@link CryptoSettings}.
     * @since 2.0.0
     */
    public static CryptoSettings getInstance() {
        return INSTANCE;
    }
}
