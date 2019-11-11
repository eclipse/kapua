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
package org.eclipse.kapua.sso.provider.keycloak;

import org.eclipse.kapua.sso.provider.SingleSignOnProvider;

public class ProviderImpl implements SingleSignOnProvider {

    @Override
    public String getId() {
        return "keycloak";
    }

    @Override
    public ProviderLocator createLocator() {
        return new KeycloakSingleSignOnLocator();
    }

}
