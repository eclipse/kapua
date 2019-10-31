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
package org.eclipse.kapua.sso.provider;

import org.eclipse.kapua.sso.JwtProcessor;
import org.eclipse.kapua.sso.SingleSignOnLocator;
import org.eclipse.kapua.sso.SingleSignOnService;
import org.eclipse.kapua.sso.exception.SsoJwtException;
import org.eclipse.kapua.sso.provider.SingleSignOnProvider.ProviderLocator;
import org.eclipse.kapua.sso.provider.internal.DisabledLocator;
import org.eclipse.kapua.sso.provider.setting.SsoSetting;
import org.eclipse.kapua.sso.provider.setting.SsoSettingKeys;

import java.io.Closeable;
import java.util.ServiceLoader;

public class ProviderSingleSignOnLocator implements SingleSignOnLocator, Closeable {

    private static ProviderLocator locator;

    public ProviderSingleSignOnLocator(final SsoSetting settings) {
        final String providerId = settings.getString(SsoSettingKeys.SSO_PROVIDER, null);
        if (providerId == null) {
            locator = DisabledLocator.INSTANCE;
        } else {
            locator = findProvider(providerId);
        }
    }

    public ProviderSingleSignOnLocator() {
        this(SsoSetting.getInstance());
    }

    private static ProviderLocator findProvider(final String providerId) {
        if (locator == null) {
            synchronized (ProviderSingleSignOnLocator.class) {
                if (locator == null) {
                    for (final SingleSignOnProvider provider : ServiceLoader.load(SingleSignOnProvider.class)) {
                        if (providerId.equals(provider.getId())) {
                            return provider.createLocator();
                        }
                    }
                    throw new IllegalArgumentException(String.format("Unable to find single sign-on provider '%s'", providerId));
                }
            }
        }
        return locator;
    }

    @Override
    public void close() {
        // nothing to close at the moment
    }

    @Override
    public SingleSignOnService getService() {
        return locator.getService();
    }

    @Override
    public JwtProcessor getProcessor() throws SsoJwtException {
        return locator.getProcessor();
    }
}
