/*******************************************************************************
 * Copyright (c) 2017 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.app.console.core.server.util;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.eclipse.kapua.sso.SingleSignOnLocator;
import org.eclipse.kapua.sso.provider.ProviderSingleSignOnLocator;

public class SsoLocatorListener implements ServletContextListener {

    private static final String SSO_CONTEXT_KEY = "ssoLocatorListener";

    private ProviderSingleSignOnLocator context;

    @Override
    public void contextInitialized(final ServletContextEvent event) {
        this.context = new ProviderSingleSignOnLocator();
        event.getServletContext().setAttribute(SSO_CONTEXT_KEY, this);
    }

    @Override
    public void contextDestroyed(final ServletContextEvent event) {
        event.getServletContext().removeAttribute(SSO_CONTEXT_KEY);
        try {
            this.context.close();
        } catch (final Exception e) {
            throw new RuntimeException(e);
        }
    }

    static SingleSignOnLocator getLocator(final ServletContext context) {
        return ((SsoLocatorListener) context.getAttribute(SSO_CONTEXT_KEY)).context;
    }

}
