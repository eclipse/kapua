/*******************************************************************************
 * Copyright (c) 2024, 2022 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.translator.setting;

import org.eclipse.kapua.commons.cache.CacheConfig;
import org.eclipse.kapua.commons.cache.ExpiryPolicy;
import org.eclipse.kapua.commons.setting.AbstractKapuaSetting;

import java.util.Arrays;
import java.util.Optional;

/**
 * {@link TranslatorKapuaKuraSettings} for {@code kapua-translator-kapua-kura} module.
 *
 * @see AbstractKapuaSetting
 * @since 2.1.0
 */
public class TranslatorKapuaKuraSettings extends AbstractKapuaSetting<TranslatorKapuaKuraSettingKeys> {

    /**
     * Setting filename.
     *
     * @since 2.1.0
     */
    private static final String TRANSLATOR_KAPUA_KURA_SETTING_RESOURCE = "translator-kapua-kura-settings.properties";

    /**
     * Constructor.
     *
     * @since 2.1.0
     */
    public TranslatorKapuaKuraSettings() {
        super(TRANSLATOR_KAPUA_KURA_SETTING_RESOURCE);
    }

    /**
     * Gets the {@link CacheConfig} of a local cache.
     * <p>
     * Specific values are used when set, otherwise the defaults ({@link TranslatorKapuaKuraSettingKeys#TRANSLATOR_KURA_KAPUA_CACHE_LOCAL_SIZE_MAXIMUM},
     * {@link TranslatorKapuaKuraSettingKeys#TRANSLATOR_KURA_KAPUA_CACHE_LOCAL_EXPIRE_AFTER} and {@link ExpiryPolicy#MODIFIED}) are used.
     *
     * @param specificMaxSizeKey          The specific maximum size key.
     * @param specificExpireAfterKey      The specific expiration time key.
     * @param specificExpireStrategyKey   The specific expiration strategy key.
     * @return The {@link CacheConfig}, or {@link Optional#empty()} if the cache is disabled (maximum size or expiration time less than or equal to 0).
     * @since 2.1.0
     */
    public Optional<CacheConfig> getCacheConfig(
            TranslatorKapuaKuraSettingKeys specificMaxSizeKey,
            TranslatorKapuaKuraSettingKeys specificExpireAfterKey,
            TranslatorKapuaKuraSettingKeys specificExpireStrategyKey) {
        int maxSize = getInteger(specificMaxSizeKey)
                .orElseGet(() -> getInteger(TranslatorKapuaKuraSettingKeys.TRANSLATOR_KURA_KAPUA_CACHE_LOCAL_SIZE_MAXIMUM).orElse(0));
        int expireAfter = getInteger(specificExpireAfterKey)
                .orElseGet(() -> getInteger(TranslatorKapuaKuraSettingKeys.TRANSLATOR_KURA_KAPUA_CACHE_LOCAL_EXPIRE_AFTER).orElse(0));
        ExpiryPolicy expiryPolicy = Optional.ofNullable(getString(specificExpireStrategyKey))
                .flatMap(v -> Arrays.stream(ExpiryPolicy.values())
                        .filter(e -> e.name().equalsIgnoreCase(v.trim()))
                        .findFirst())
                .orElse(ExpiryPolicy.MODIFIED);

        if (maxSize <= 0 || expireAfter <= 0) {
            return Optional.empty();
        }
        return Optional.of(new CacheConfig(maxSize, expireAfter, expiryPolicy));
    }
}
