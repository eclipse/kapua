/*******************************************************************************
 * Copyright (c) 2017, 2022 Red Hat Inc and others.
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Red Hat Inc - initial API and implementation
 *     Eurotech
 *******************************************************************************/
package org.eclipse.kapua.service.authentication.shiro.utils;

import org.eclipse.kapua.plugin.sso.openid.JwtProcessor;
import org.eclipse.kapua.plugin.sso.openid.exception.OpenIDException;
import org.eclipse.kapua.plugin.sso.openid.provider.ProviderOpenIDLocator;

public final class JwtProcessors {

    private JwtProcessors() {
    }

    public static JwtProcessor createDefault() throws OpenIDException {
        ProviderOpenIDLocator singleSignOnLocator = new ProviderOpenIDLocator();
        return singleSignOnLocator.getProcessor();
    }
}
