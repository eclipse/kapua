/*******************************************************************************
 * Copyright (c) 2020 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.commons.liquibase.settings;

import org.eclipse.kapua.commons.setting.SettingKey;

/**
 * {@link LiquibaseClientSettings} {@link SettingKey}s
 *
 * @since 1.2.0
 */
public enum LiquibaseClientSettingKeys implements SettingKey {

    /**
     * Whether or not force the fix of the SQL timestamps.
     *
     * @since 1.2.0
     */
    FORCE_TIMESTAMPS_FIX("liquibaseClient.timestamps.fix.force"),

    /**
     * {@link liquibase.Liquibase} version.
     *
     * @since 1.2.0
     */
    LIQUIBASE_VERSION("liquibaseClient.liquibase.version");


    /**
     * The {@link String} {@link LiquibaseClientSettingKeys} name.
     */
    private final String key;

    /**
     * Constructor.
     *
     * @param key The {@link String} {@link LiquibaseClientSettingKeys} name.
     * @since 1.2.0
     */
    private LiquibaseClientSettingKeys(String key) {
        this.key = key;
    }

    @Override
    public String key() {
        return key;
    }
}
