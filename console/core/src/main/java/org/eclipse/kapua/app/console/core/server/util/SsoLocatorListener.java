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

import org.eclipse.kapua.locator.KapuaLocator;
import org.eclipse.kapua.plugin.sso.openid.OpenIDLocator;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class SsoLocatorListener implements ServletContextListener {

    private static final String SSO_CONTEXT_KEY = "ssoLocatorListener";

    private OpenIDLocator openIDLocator = KapuaLocator.getInstance().getComponent(OpenIDLocator.class);

    @Override
    public void contextInitialized(ServletContextEvent event) {
        event.getServletContext().setAttribute(SSO_CONTEXT_KEY, this);
    }

    @Override
    public void contextDestroyed(ServletContextEvent event) {
        event.getServletContext().removeAttribute(SSO_CONTEXT_KEY);
    }

    static OpenIDLocator getLocator(ServletContext context) {
        return ((SsoLocatorListener) context.getAttribute(SSO_CONTEXT_KEY)).openIDLocator;
    }

}
