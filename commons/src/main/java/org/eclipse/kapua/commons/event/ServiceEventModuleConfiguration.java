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

public class ServiceEventModuleConfiguration {

    private String internalAddress;
    private EntityManagerFactory entityManagerFactory;
    private ServiceEventClientConfiguration[] serviceEventClientConfigurations;

    public ServiceEventModuleConfiguration(String internalAddress, EntityManagerFactory entityManagerFactory,
            ServiceEventClientConfiguration[] serviceEventListenerConfigurations) {
        this.internalAddress = internalAddress;
        this.entityManagerFactory = entityManagerFactory;
        this.serviceEventClientConfigurations = serviceEventListenerConfigurations;
    }

    public String getInternalAddress() {
        return internalAddress;
    }

    public EntityManagerFactory getEntityManagerFactory() {
        return entityManagerFactory;
    }

    public ServiceEventClientConfiguration[] getServiceEventClientConfigurations() {
        return serviceEventClientConfigurations;
    }
}
