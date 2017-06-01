/*******************************************************************************
 * Copyright (c) 2017 Red Hat Inc and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Red Hat Inc - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.app.console.server.util;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;

import org.eclipse.kapua.sso.SingleSignOnLocator;

public final class SsoLocator {

    private SsoLocator() {
    }

    public static SingleSignOnLocator getLocator(final ServletContext context) {
        return SsoLocatorListener.getLocator(context);
    }
    
    public static SingleSignOnLocator getLocator(final ServletConfig config) {
        return SsoLocatorListener.getLocator(config.getServletContext());
    }
}
