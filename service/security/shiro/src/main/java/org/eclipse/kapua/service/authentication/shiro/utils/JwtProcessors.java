/*******************************************************************************
 * Copyright (c) 2017, 2019 Red Hat Inc and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
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
