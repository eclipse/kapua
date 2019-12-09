/*******************************************************************************
 * Copyright (c) 2017, 2020 Eurotech and/or its affiliates and others
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

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;

import org.eclipse.kapua.plugin.sso.openid.SingleSignOnLocator;

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
