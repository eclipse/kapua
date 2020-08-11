/*******************************************************************************
 * Copyright (c) 2017, 2020 Red Hat Inc and others.
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

import org.eclipse.kapua.sso.JwtProcessor;
import org.eclipse.kapua.sso.exception.SsoException;
import org.eclipse.kapua.sso.provider.ProviderSingleSignOnLocator;

public final class JwtProcessors {

    private JwtProcessors() {
    }

    public static JwtProcessor createDefault() throws SsoException {
        ProviderSingleSignOnLocator singleSignOnLocator = new ProviderSingleSignOnLocator();
        return singleSignOnLocator.getProcessor();
    }
}
