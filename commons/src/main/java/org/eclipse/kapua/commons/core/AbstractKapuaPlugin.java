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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractKapuaPlugin {

    private static final Logger logger = LoggerFactory.getLogger(AbstractKapuaPlugin.class);

    public AbstractKapuaPlugin(LifecycleHandler lifecycleHandler) {
        
        final AbstractKapuaPlugin pluginInstance = this;
        lifecycleHandler.register(new LifecyleListener() {

            @Override
            public void onStartup() {
                pluginInstance.start();
            }

            @Override
            public void onShutdown() {
                pluginInstance.stop();
            }
        });
    }
    
    public abstract void start() ;

    public abstract void stop();

}
