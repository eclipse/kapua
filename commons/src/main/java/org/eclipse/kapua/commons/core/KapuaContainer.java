/*******************************************************************************
 * Copyright (c) 2011, 2017 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.commons.core;

import java.util.List;

import org.eclipse.kapua.commons.locator.CommonsLocator;

/**
 * Kpaua container hosts a Kapua application and provides the means to configure it
 * and manage its lifecycle.
 * 
 * @since 1.0
 */
public abstract class KapuaContainer {

    private LifecycleHandler lifecycleHandler;

    public KapuaContainer() {
        this(new KapuaConfiguration());
    }

    public KapuaContainer(KapuaConfiguration configuration) {
        lifecycleHandler = CommonsLocator.getInstance().getLifecycleHandler();
    }

    public final void startup() {
        List<LifecyleListener> listeners = lifecycleHandler.getListeners();
        for (LifecyleListener listener : listeners) {
            listener.onStartup();
        }
    }

    public final void shutdown() {
        List<LifecyleListener> listeners = lifecycleHandler.getListeners();
        for (int i = listeners.size() - 1; i >= 0; i--) {
            listeners.get(i).onShutdown();
        }
    }
}
