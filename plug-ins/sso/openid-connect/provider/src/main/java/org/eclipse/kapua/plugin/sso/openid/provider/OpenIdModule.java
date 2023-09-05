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
package org.eclipse.kapua.plugin.sso.openid.provider;

import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.google.inject.multibindings.ProvidesIntoSet;
import org.eclipse.kapua.commons.core.AbstractKapuaModule;
import org.eclipse.kapua.plugin.sso.openid.JwtProcessor;
import org.eclipse.kapua.plugin.sso.openid.OpenIDLocator;
import org.eclipse.kapua.plugin.sso.openid.OpenIDService;
import org.eclipse.kapua.plugin.sso.openid.provider.internal.DisabledJwtProcessor;
import org.eclipse.kapua.plugin.sso.openid.provider.internal.DisabledOpenIDService;
import org.eclipse.kapua.plugin.sso.openid.provider.setting.OpenIDSetting;
import org.eclipse.kapua.plugin.sso.openid.provider.setting.OpenIDSettingKeys;

import java.util.Set;

public class OpenIdModule extends AbstractKapuaModule {
    @Override
    protected void configureModule() {
        bind(OpenIDSetting.class).toInstance(new OpenIDSetting());
    }

    @ProvidesIntoSet
    @Singleton
    OpenIDService disabledOpenIDService() {
        return new DisabledOpenIDService();
    }

    @ProvidesIntoSet
    @Singleton
    JwtProcessor disabledJwtProcessor() {
        return new DisabledJwtProcessor();
    }

    @Provides
    @Singleton
    OpenIDLocator openIDLocator(OpenIDSetting openIDSetting, Set<OpenIDService> openIDServices, Set<JwtProcessor> jwtProcessors) {
        final String providerId = openIDSetting.getString(OpenIDSettingKeys.SSO_OPENID_PROVIDER, DisabledOpenIDService.DISABLED_ID);
        return new OpenIDLocatorImpl(pickService(providerId, openIDServices), pickJwtProcessor(providerId, jwtProcessors));
    }

    private OpenIDService pickService(String providerId, Set<OpenIDService> openIDServices) {
        for (final OpenIDService service : openIDServices) {
            if (providerId.equals(service.getId())) {
                return service;
            }
        }
        throw new IllegalArgumentException(String.format("Unable to find OpenID service '%s'",
                providerId));
    }

    private JwtProcessor pickJwtProcessor(String providerId, Set<JwtProcessor> jwtProcessors) {
        for (final JwtProcessor jwtProcessor : jwtProcessors) {
            if (providerId.equals(jwtProcessor.getId())) {
                return jwtProcessor;
            }
        }
        throw new IllegalArgumentException(String.format("Unable to find OpenID jwt processor '%s'",
                providerId));
    }
}
