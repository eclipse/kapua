/*******************************************************************************
 * Copyright (c) 2017, 2022 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.commons.event;

import org.eclipse.kapua.commons.jpa.EntityManagerFactory;
import org.eclipse.kapua.commons.jpa.KapuaJpaRepositoryConfiguration;
import org.eclipse.kapua.commons.setting.system.SystemSetting;
import org.eclipse.kapua.commons.setting.system.SystemSettingKey;

/**
 * @deprecated since 2.0.0 - use {@link ServiceEventModuleTransactionalConfiguration} instead
 */
@Deprecated
public class ServiceEventModuleConfiguration {

    private String internalAddress;
    private EntityManagerFactory entityManagerFactory;

    public KapuaJpaRepositoryConfiguration kapuaJpaRepositoryConfiguration;
    private ServiceEventClientConfiguration[] serviceEventClientConfigurations;
    public int maxInsertAttempts;

    public ServiceEventModuleConfiguration(
            String internalAddress,
            EntityManagerFactory entityManagerFactory,
            ServiceEventClientConfiguration[] serviceEventListenerConfigurations) {
        this.internalAddress = internalAddress;
        this.entityManagerFactory = entityManagerFactory;
        this.kapuaJpaRepositoryConfiguration = new KapuaJpaRepositoryConfiguration();
        this.serviceEventClientConfigurations = serviceEventListenerConfigurations;
        this.maxInsertAttempts = SystemSetting.getInstance().getInt(SystemSettingKey.KAPUA_INSERT_MAX_RETRY);
    }

    public String getInternalAddress() {
        return internalAddress;
    }

    public EntityManagerFactory getEntityManagerFactory() {
        return entityManagerFactory;
    }

    public KapuaJpaRepositoryConfiguration getKapuaJpaRepositoryConfiguration() {
        return kapuaJpaRepositoryConfiguration;
    }

    public ServiceEventClientConfiguration[] getServiceEventClientConfigurations() {
        return serviceEventClientConfigurations;
    }
}
