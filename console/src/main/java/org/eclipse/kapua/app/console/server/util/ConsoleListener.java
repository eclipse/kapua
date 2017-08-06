/*******************************************************************************
 * Copyright (c) 2011, 2016 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.app.console.server.util;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.KapuaRuntimeException;
import org.eclipse.kapua.app.console.ConsoleJAXBContextProvider;
import org.eclipse.kapua.commons.core.KapuaApplication;
import org.eclipse.kapua.commons.util.xml.JAXBContextProvider;
import org.eclipse.kapua.commons.util.xml.XmlUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ConsoleListener implements ServletContextListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(ConsoleListener.class);

    KapuaApplication kapuaApplication;

    @Override
    public void contextInitialized(final ServletContextEvent event) {
        LOGGER.info("Initialize Console JABContext Provider");
        JAXBContextProvider consoleProvider = new ConsoleJAXBContextProvider();
        XmlUtil.setContextProvider(consoleProvider);

        try {
            kapuaApplication = new KapuaApplication();
            kapuaApplication.startup();
        } catch (KapuaException e) {
            LOGGER.error(e.getMessage(), e);
            throw KapuaRuntimeException.internalError(e);
        }
    }

    @Override
    public void contextDestroyed(final ServletContextEvent event) {
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

}
