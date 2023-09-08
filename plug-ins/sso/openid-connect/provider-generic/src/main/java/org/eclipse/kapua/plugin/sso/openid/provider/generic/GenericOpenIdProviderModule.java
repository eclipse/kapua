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
package org.eclipse.kapua.plugin.sso.openid.provider.generic;

import com.google.inject.Singleton;
import com.google.inject.multibindings.ProvidesIntoSet;
import org.eclipse.kapua.commons.core.AbstractKapuaModule;
import org.eclipse.kapua.plugin.sso.openid.JwtProcessor;
import org.eclipse.kapua.plugin.sso.openid.OpenIDService;
import org.eclipse.kapua.plugin.sso.openid.exception.OpenIDException;
import org.eclipse.kapua.plugin.sso.openid.provider.generic.jwt.GenericJwtProcessor;
import org.eclipse.kapua.plugin.sso.openid.provider.generic.setting.GenericOpenIDSetting;
import org.eclipse.kapua.plugin.sso.openid.provider.setting.OpenIDSetting;

public class GenericOpenIdProviderModule extends AbstractKapuaModule {
    @Override
    protected void configureModule() {
        bind(GenericOpenIDSetting.class).toInstance(new GenericOpenIDSetting());
    }

    @ProvidesIntoSet
    @Singleton
    JwtProcessor genericJwtProcessor(OpenIDSetting openIDSetting, GenericOpenIDSetting genericOpenIDSetting) throws OpenIDException {
        return new GenericJwtProcessor(openIDSetting, genericOpenIDSetting);
    }

    @ProvidesIntoSet
    @Singleton
    OpenIDService genericOpenIdService(OpenIDSetting openIDSetting, GenericOpenIDSetting genericOpenIDSetting) throws OpenIDException {
        return new GenericOpenIDService(openIDSetting, genericOpenIDSetting, openIDUtils);
    }
}
