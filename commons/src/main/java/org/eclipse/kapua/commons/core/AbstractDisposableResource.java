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

public abstract class AbstractDisposableResource {

    public AbstractDisposableResource(LifecycleHandler lifecycleHandler) {
        
        final AbstractDisposableResource resourceInstance = this;
        
        lifecycleHandler.register(new LifecyleListener() {

            @Override
            public void onStartup() {
            }

            @Override
            public void onShutdown() {
                resourceInstance.close();
            }
            
        });
    }
    
    public abstract void close();
}
