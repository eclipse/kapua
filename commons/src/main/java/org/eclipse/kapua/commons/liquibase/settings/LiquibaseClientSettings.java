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