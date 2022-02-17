/*******************************************************************************
 * Copyright (c) 2017, 2022 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.app.console.core.server.util;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.eclipse.kapua.plugin.sso.openid.OpenIDLocator;
import org.eclipse.kapua.plugin.sso.openid.provider.ProviderOpenIDLocator;

public class SsoLocatorListener implements ServletContextListener {

    private static final String SSO_CONTEXT_KEY = "ssoLocatorListener";

    private ProviderOpenIDLocator context;

    @Override
    public void contextInitialized(final ServletContextEvent event) {
        this.context = new ProviderOpenIDLocator();
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

    static OpenIDLocator getLocator(final ServletContext context) {
        return ((SsoLocatorListener) context.getAttribute(SSO_CONTEXT_KEY)).context;
    }

}
