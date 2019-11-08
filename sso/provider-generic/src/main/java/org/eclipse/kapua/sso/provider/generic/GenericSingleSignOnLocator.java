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
package org.eclipse.kapua.sso.provider.generic;

import org.eclipse.kapua.sso.JwtProcessor;
import org.eclipse.kapua.sso.SingleSignOnService;
import org.eclipse.kapua.sso.exception.SsoJwtException;
import org.eclipse.kapua.sso.provider.SingleSignOnProvider.ProviderLocator;
import org.eclipse.kapua.sso.provider.generic.jwt.GenericJwtProcessor;

/**
 * The generic SingleSignOn service provider locator.
 */
public class GenericSingleSignOnLocator implements ProviderLocator {

    @Override
    public SingleSignOnService getService() {
        return new GenericSingleSignOnService();
    }

    @Override
    public JwtProcessor getProcessor() throws SsoJwtException {
        return new GenericJwtProcessor();
    }

    @Override
    public void close() throws Exception {
    }

}
