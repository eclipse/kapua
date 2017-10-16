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

import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.KapuaRuntimeException;
import org.eclipse.kapua.commons.core.KapuaApplication;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RestApiListener implements ServletContextListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(RestApiListener.class);

    private KapuaApplication kapuaApplication;

    @Override
    public void contextDestroyed(ServletContextEvent arg0) {
        if (kapuaApplication != null) {
            try {
                kapuaApplication.shutdown();
                kapuaApplication = null;
            } catch (KapuaException e) {
                LOGGER.error(e.getMessage(), e);
                throw KapuaRuntimeException.internalError(e);
            }
        }
    }

    @Override
    public void contextInitialized(ServletContextEvent arg0) {

        try {
            kapuaApplication = new KapuaApplication() {};
            kapuaApplication.startup();
        } catch (KapuaException e) {
            LOGGER.error(e.getMessage(), e);
            throw KapuaRuntimeException.internalError(e);
        }
    }
}