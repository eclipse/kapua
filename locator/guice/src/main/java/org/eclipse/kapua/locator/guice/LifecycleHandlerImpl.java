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
package org.eclipse.kapua.locator.guice;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.kapua.commons.core.LifecycleHandler;
import org.eclipse.kapua.commons.core.LifecyleListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class LifecycleHandlerImpl implements LifecycleHandler {

    private static final Logger logger = LoggerFactory.getLogger(LifecycleHandlerImpl.class);

    private List<LifecyleListener> listeners;
    
    public LifecycleHandlerImpl() {
        listeners = new ArrayList<LifecyleListener>();
    }
    
    @Override
    public List<LifecyleListener> getListeners() {
        return listeners;
    }

    @Override
    public void register(LifecyleListener listener) {
        logger.warn("****** Add listener {}", listener);
        listeners.add(listener);
    }

}
