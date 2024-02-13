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
package org.eclipse.kapua.commons.service.internal.cache;

import org.eclipse.kapua.commons.core.AbstractKapuaModule;
import org.eclipse.kapua.commons.jpa.NamedCacheFactory;
import org.eclipse.kapua.commons.jpa.NamedEntityCacheFactory;

import javax.cache.CacheManager;
import javax.inject.Singleton;

public class CacheModule extends AbstractKapuaModule {
    @Override
    protected void configureModule() {
        bind(CacheManager.class).toProvider(CacheManagerProvider.class).in(Singleton.class);
        bind(KapuaCacheManager.class).in(Singleton.class);
        bind(NamedCacheFactory.class).to(NamedEntityCacheFactory.class).in(Singleton.class);
    }
}
