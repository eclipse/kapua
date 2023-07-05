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
package org.eclipse.kapua.commons.jpa;

import com.google.inject.Provides;
import com.google.inject.Singleton;
import org.eclipse.kapua.commons.core.AbstractKapuaModule;
import org.eclipse.kapua.commons.metric.MetricServiceFactory;
import org.eclipse.kapua.commons.metric.MetricsService;
import org.eclipse.kapua.commons.setting.system.SystemSetting;
import org.eclipse.kapua.commons.setting.system.SystemSettingKey;

import javax.inject.Named;

public class CommonJpaModule extends AbstractKapuaModule {
    @Override
    protected void configureModule() {
        bind(MetricsService.class).toInstance(MetricServiceFactory.getInstance());
    }

    @Provides
    @Singleton
    @Named("maxInsertAttempts")
        // Number of allowed retries in case of a key collision when persisting a new entity for the first time
    Integer maxInsertAttempts(SystemSetting systemSetting) {
        return systemSetting.getInt(SystemSettingKey.KAPUA_INSERT_MAX_RETRY);
    }

    @Provides
    @Singleton
    KapuaJpaTxManagerFactory kapuaJpaTxManagerFactory(@Named("maxInsertAttempts") Integer maxInsertAttempts) {
        return new KapuaJpaTxManagerFactory(maxInsertAttempts);
    }

    @Provides
    @Singleton
    KapuaJpaRepositoryConfiguration kapuaJpaRepositoryConfiguration(SystemSetting systemSetting) {
        return new KapuaJpaRepositoryConfiguration(
                systemSetting.getString(SystemSettingKey.DB_CHARACTER_ESCAPE, "\\"),
                systemSetting.getString(SystemSettingKey.DB_CHARACTER_WILDCARD_ANY, "%"),
                systemSetting.getString(SystemSettingKey.DB_CHARACTER_WILDCARD_SINGLE, "_")
        );
    }
}
