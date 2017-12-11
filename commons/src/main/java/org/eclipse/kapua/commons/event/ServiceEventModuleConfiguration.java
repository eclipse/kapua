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

import java.util.List;

import org.eclipse.kapua.commons.jpa.EntityManagerFactory;

public class ServiceEventModuleConfiguration {

    private String internalAddress;
    private List<String> servicesNames;
    private EntityManagerFactory entityManagerFactory;
    private ServiceEventListenerConfiguration[] serviceEventListenerConfigurations;

    public ServiceEventModuleConfiguration(String internalAddress, List<String> servicesNames, EntityManagerFactory entityManagerFactory,
            ServiceEventListenerConfiguration[] serviceEventListenerConfigurations) {
        this.internalAddress = internalAddress;
        this.servicesNames = servicesNames;
        this.entityManagerFactory = entityManagerFactory;
        this.serviceEventListenerConfigurations = serviceEventListenerConfigurations;
    }

    public String getInternalAddress() {
        return internalAddress;
    }

    public List<String> getServicesNames() {
        return servicesNames;
    }

    public EntityManagerFactory getEntityManagerFactory() {
        return entityManagerFactory;
    }

    public ServiceEventListenerConfiguration[] getServiceEventListenerConfigurations() {
        return serviceEventListenerConfigurations;
    }
}
