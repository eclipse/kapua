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

import org.eclipse.kapua.plugin.sso.openid.JwtProcessor;
import org.eclipse.kapua.plugin.sso.openid.OpenIDLocator;
import org.eclipse.kapua.plugin.sso.openid.OpenIDService;
import org.eclipse.kapua.plugin.sso.openid.exception.OpenIDException;
import org.eclipse.kapua.plugin.sso.openid.provider.OpenIDProvider.ProviderLocator;
import org.eclipse.kapua.plugin.sso.openid.provider.internal.DisabledLocator;
import org.eclipse.kapua.plugin.sso.openid.provider.setting.OpenIDSetting;
import org.eclipse.kapua.plugin.sso.openid.provider.setting.OpenIDSettingKeys;

import java.io.Closeable;
import java.util.ServiceLoader;

/**
 * The OpenID Connect service provider locator.
 */
public class ProviderOpenIDLocator implements OpenIDLocator, Closeable {

    private static ProviderLocator locator;

    /**
     * The SignleSignOn provider locator constructor.
     *
     * @param settings the {@link OpenIDSetting} instance.
     */
    public ProviderOpenIDLocator(final OpenIDSetting settings) {
        final String providerId = settings.getString(OpenIDSettingKeys.SSO_OPENID_PROVIDER
                , null);
        if (providerId == null) {
            locator = DisabledLocator.INSTANCE;
        } else {
            locator = findProvider(providerId);
        }
    }

    /**
     * The public SignleSignOn provider locator constructor.
     */
    public ProviderOpenIDLocator() {
        this(OpenIDSetting.getInstance());
    }

    /**
     * Find the provider, given a provider id, among the existing ones.
     *
     * @param providerId a String reperesenting the provider ID
     * @return a {@link ProviderLocator} instance.
     */
    private static ProviderLocator findProvider(final String providerId) {
        if (locator == null) {
            synchronized (ProviderOpenIDLocator.class) {
                if (locator == null) {
                    for (final OpenIDProvider provider : ServiceLoader.load(OpenIDProvider.class)) {
                        if (providerId.equals(provider.getId())) {
                            return provider.createLocator();
                        }
                    }
                    throw new IllegalArgumentException(String.format("Unable to find OpenID provider '%s'",
                            providerId));
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
    public OpenIDService getService() {
        return locator.getService();
    }

    @Override
    public JwtProcessor getProcessor() throws OpenIDException {
        return locator.getProcessor();
    }
}
