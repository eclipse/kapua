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

import org.eclipse.kapua.commons.jpa.KapuaJpaRepositoryConfiguration;
import org.eclipse.kapua.storage.TxManager;

public class ServiceEventModuleTransactionalConfiguration {

    private String internalAddress;
    private TxManager txManager;
    private ServiceEventClientConfiguration[] serviceEventClientConfigurations;
    private final KapuaJpaRepositoryConfiguration jpaConfig;

    public ServiceEventModuleTransactionalConfiguration(String internalAddress,
                                                        TxManager txManager,
                                                        ServiceEventClientConfiguration[] serviceEventListenerConfigurations,
                                                        KapuaJpaRepositoryConfiguration jpaConfig) {
        this.internalAddress = internalAddress;
        this.txManager = txManager;
        this.serviceEventClientConfigurations = serviceEventListenerConfigurations;
        this.jpaConfig = jpaConfig;
    }

    public String getInternalAddress() {
        return internalAddress;
    }

    public TxManager getTxManager() {
        return txManager;
    }

    public ServiceEventClientConfiguration[] getServiceEventClientConfigurations() {
        return serviceEventClientConfigurations;
    }

    public KapuaJpaRepositoryConfiguration getJpaConfig() {
        return jpaConfig;
    }
}
