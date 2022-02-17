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
package org.eclipse.kapua.plugin.sso.openid.provider.keycloak;

import org.eclipse.kapua.plugin.sso.openid.provider.OpenIDProvider;

public class ProviderImpl implements OpenIDProvider {

    @Override
    public String getId() {
        return "keycloak";
    }

    @Override
    public ProviderLocator createLocator() {
        return new KeycloakOpenIDLocator();
    }

}
