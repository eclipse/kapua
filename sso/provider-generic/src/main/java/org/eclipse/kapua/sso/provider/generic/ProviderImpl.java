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
package org.eclipse.kapua.sso.provider.generic;

import org.eclipse.kapua.sso.provider.SingleSignOnProvider;

public class ProviderImpl implements SingleSignOnProvider {

    @Override
    public String getId() {
        return "generic";
    }

    // note that even if one might want to avoid creating each time a new locator, each time the createLocator method is
    // called inside a new ProviderImpl object, so it is not possible to create only one instance of the locator
    @Override
    public ProviderLocator createLocator() {
        return new GenericSingleSignOnLocator();
    }

}
