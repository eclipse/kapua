/*******************************************************************************
 * Copyright (c) 2016, 2021 Eurotech and/or its affiliates and others
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *      Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.commons.service.internal;

import org.eclipse.kapua.commons.event.ServiceEventBusManager;
import org.eclipse.kapua.commons.jpa.AbstractEntityCacheFactory;
import org.eclipse.kapua.commons.jpa.EntityManagerFactory;
import org.eclipse.kapua.commons.jpa.EntityManagerSession;
import org.eclipse.kapua.commons.service.internal.cache.EntityCache;
import org.eclipse.kapua.event.ServiceEventBus;
import org.eclipse.kapua.event.ServiceEventBusException;
import org.eclipse.kapua.event.ServiceEventBusListener;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.KapuaService;

import javax.validation.constraints.NotNull;

/**
 * {@code abstract} {@link KapuaService} implementation.
 * <p>
 * It handles the {@link EntityManagerFactory} and {@link EntityManagerSession} to avoid redefining each time in the {@link KapuaService}s.
 *
 * @since 1.0.0
 */
public abstract class AbstractKapuaService implements KapuaService {

    protected final EntityManagerFactory entityManagerFactory;
    protected final EntityManagerSession entityManagerSession;
    protected final EntityCache entityCache;

    /**
     * Constructor
     *
     * @param entityManagerFactory The {@link EntityManagerFactory}.
     * @since 1.0.0
     * @deprecated Since 1.2.0. Please make use of {@link #AbstractKapuaService(EntityManagerFactory, AbstractEntityCacheFactory)}. This constructor will be removed in a next release (may be).
     */
    @Deprecated
    protected AbstractKapuaService(@NotNull EntityManagerFactory entityManagerFactory) {
        this(entityManagerFactory, null);
    }

    /**
     * Constructor.
     *
     * @param entityManagerFactory The {@link EntityManagerFactory}.
     * @param abstractCacheFactory The {@link AbstractEntityCacheFactory}.
     * @since 1.2.0
     */
    protected AbstractKapuaService(@NotNull EntityManagerFactory entityManagerFactory, AbstractEntityCacheFactory abstractCacheFactory) {
        this.entityManagerFactory = entityManagerFactory;
        this.entityManagerSession = new EntityManagerSession(entityManagerFactory);

        if (abstractCacheFactory != null) {
            this.entityCache = abstractCacheFactory.createCache();
        } else {
            this.entityCache = null;
        }
    }

    /**
     * Gets the {@link EntityManagerSession} of this {@link AbstractKapuaService}.
     *
     * @return The {@link EntityManagerSession} of this {@link AbstractKapuaService}.
     * @since 1.0.0
     */
    public EntityManagerSession getEntityManagerSession() {
        return entityManagerSession;
    }

    /**
     * Registers a {@link ServiceEventBusListener} into the {@link org.eclipse.kapua.event.ServiceEventBus}.
     *
     * @param listener The {@link ServiceEventBusListener} to register.
     * @param address  The {@link ServiceEventBus} address to subscribe to.
     * @param clazz    The {@link KapuaService} owner of the {@link ServiceEventBusListener}.
     * @throws ServiceEventBusException If any error occurs during subscription to the address.
     * @since 1.0.0
     */
    protected void registerEventListener(@NotNull ServiceEventBusListener listener, @NotNull String address, @NotNull Class<? extends KapuaService> clazz) throws ServiceEventBusException {
        ServiceEventBusManager.getInstance().subscribe(address, clazz.getName(), listener);
    }

    /**
     * Whether this {@link KapuaService} is enabled for the given scope {@link KapuaId}.
     *
     * @param scopeId The scope {@link KapuaId} for which to check.
     * @return {@code true} if the {@link KapuaService} is enabled, {@code false} otherwise.
     * @since 1.2.0
     */
    protected boolean isServiceEnabled(KapuaId scopeId) {
        return true;
    }
}
