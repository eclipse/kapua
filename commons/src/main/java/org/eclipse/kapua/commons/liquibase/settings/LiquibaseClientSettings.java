/*******************************************************************************
 * Copyright (c) 2020, 2022 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.commons.liquibase.settings;

import org.eclipse.kapua.commons.setting.AbstractKapuaSetting;

/**
 * @since 1.2.0
 */
public class LiquibaseClientSettings extends AbstractKapuaSetting<LiquibaseClientSettingKeys> {

    private static final String CONFIG_RESOURCE_NAME = "liquibase-client-settings.properties";

    private static final LiquibaseClientSettings INSTANCE = new LiquibaseClientSettings();

    /**
     * Constructor.
     *
     * @since 1.2.0
     */
    private LiquibaseClientSettings() {
        super(CONFIG_RESOURCE_NAME);
    }

    /**
     * Gets the {@link LiquibaseClientSettings} singleton instance.
     *
     * @return The {@link LiquibaseClientSettings} singleton instance.
     * @since 1.2.0
     */
    public static LiquibaseClientSettings getInstance() {
        return INSTANCE;
    }

}
