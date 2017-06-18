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
package org.eclipse.kapua.app.api;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.eclipse.kapua.commons.core.Container;

public class RestApiListener implements ServletContextListener {

    private Container kapuaContainer;
    
    @Override
    public void contextDestroyed(ServletContextEvent arg0) {
        if (kapuaContainer != null) {
            kapuaContainer.shutdown();
        }
    }

    @Override
    public void contextInitialized(ServletContextEvent arg0) {
        if (kapuaContainer == null) {
            kapuaContainer = new Container() {};
        }
        
        kapuaContainer.startup();
    }
}
