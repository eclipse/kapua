/*******************************************************************************
 * Copyright (c) 2017 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
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
