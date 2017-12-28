/*******************************************************************************
 * Copyright (c) 2011, 2017 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *      Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.commons.service.internal;

import org.eclipse.kapua.commons.event.ServiceEventBusManager;
import org.eclipse.kapua.commons.jpa.EntityManagerFactory;
import org.eclipse.kapua.commons.jpa.EntityManagerSession;
import org.eclipse.kapua.event.ServiceEventBusException;
import org.eclipse.kapua.event.ServiceEventBusListener;
import org.eclipse.kapua.service.KapuaService;

/**
 * Abstract Kapua service.<br>
 * It handles the {@link EntityManagerFactory} and {@link EntityManagerSession} to avoid to redefine each time in the subclasses.
 * 
 * @since 1.0
 *
 */
public class AbstractKapuaService {

    protected EntityManagerFactory entityManagerFactory;
    protected EntityManagerSession entityManagerSession;

    /**
     * Constructor
     * 
     * @param entityManagerFactory
     */
    protected AbstractKapuaService(EntityManagerFactory entityManagerFactory) {
        this.entityManagerFactory = entityManagerFactory;
        this.entityManagerSession = new EntityManagerSession(entityManagerFactory);
    }

    public EntityManagerSession getEntityManagerSession() {
        return entityManagerSession;
    }

    protected void registerEventListener(ServiceEventBusListener listener, String address, Class<? extends KapuaService> clazz) throws ServiceEventBusException {
        ServiceEventBusManager.getInstance().subscribe(address, clazz.getName(), listener);
    }
}
