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
import org.eclipse.kapua.sso.provider.SingleSignOnProvider.ProviderLocator;

public class GenericSingleSignOnLocator implements ProviderLocator {

    @Override
    public SingleSignOnService getService() {
        return new GenericSingleSignOnService();
    }

    @Override
    public JwtProcessor getProcessor() {
        // TODO: not sure this really works with only a null... Check this!
        return null;
    }

    @Override
    public void close() throws Exception {
    }

}
