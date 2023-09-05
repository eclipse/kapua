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

import com.google.inject.Singleton;
import com.google.inject.multibindings.ProvidesIntoSet;
import org.eclipse.kapua.commons.core.AbstractKapuaModule;
import org.eclipse.kapua.plugin.sso.openid.JwtProcessor;
import org.eclipse.kapua.plugin.sso.openid.OpenIDService;
import org.eclipse.kapua.plugin.sso.openid.exception.OpenIDException;
import org.eclipse.kapua.plugin.sso.openid.provider.keycloak.jwt.KeycloakJwtProcessor;
import org.eclipse.kapua.plugin.sso.openid.provider.keycloak.setting.KeycloakOpenIDSetting;
import org.eclipse.kapua.plugin.sso.openid.provider.setting.OpenIDSetting;

public class KeycloakOpenIdProviderModule extends AbstractKapuaModule {
    @Override
    protected void configureModule() {
        bind(KeycloakOpenIDSetting.class).toInstance(new KeycloakOpenIDSetting());
        bind(KeycloakOpenIDUtils.class).in(Singleton.class);
    }

    @ProvidesIntoSet
    @Singleton
    JwtProcessor keycloakJwtProcessor(OpenIDSetting openIDSetting, KeycloakOpenIDUtils keycloakOpenIDUtils) throws OpenIDException {
        return new KeycloakJwtProcessor(openIDSetting, keycloakOpenIDUtils);
    }

    @ProvidesIntoSet
    @Singleton
    OpenIDService keycloakOpenIDService(final OpenIDSetting openIDSetting,
                                        KeycloakOpenIDUtils keycloakOpenIDUtils) throws OpenIDException {
        return new KeycloakOpenIDService(openIDSetting, keycloakOpenIDUtils);
    }
}
