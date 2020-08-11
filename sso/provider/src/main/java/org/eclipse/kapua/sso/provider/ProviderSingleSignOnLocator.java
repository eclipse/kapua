/*******************************************************************************
 * Copyright (c) 2017, 2020 Red Hat Inc and others.
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
package org.eclipse.kapua.sso.provider;

import org.eclipse.kapua.sso.JwtProcessor;
import org.eclipse.kapua.sso.SingleSignOnLocator;
import org.eclipse.kapua.sso.SingleSignOnService;
import org.eclipse.kapua.sso.exception.SsoException;
import org.eclipse.kapua.sso.provider.SingleSignOnProvider.ProviderLocator;
import org.eclipse.kapua.sso.provider.internal.DisabledLocator;
import org.eclipse.kapua.sso.provider.setting.SsoSetting;
import org.eclipse.kapua.sso.provider.setting.SsoSettingKeys;

import java.io.Closeable;
import java.util.ServiceLoader;

/**
 * The SingleSignOn service provider locator.
 */
public class ProviderSingleSignOnLocator implements SingleSignOnLocator, Closeable {

    private static ProviderLocator locator;

    /**
     * The SignleSignOn provider locator constructor.
     *
     * @param settings the {@link SsoSetting} instance.
     */
    public ProviderSingleSignOnLocator(final SsoSetting settings) {
        final String providerId = settings.getString(SsoSettingKeys.SSO_PROVIDER, null);
        if (providerId == null) {
            locator = DisabledLocator.INSTANCE;
        } else {
            locator = findProvider(providerId);
        }
    }

    /**
     * The public SignleSignOn provider locator constructor.
     */
    public ProviderSingleSignOnLocator() {
        this(SsoSetting.getInstance());
    }

    /**
     * Find the provider, given a provider id, among the existing ones.
     *
     * @param providerId a String reperesenting the provider ID
     * @return a {@link ProviderLocator} instance.
     */
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
    public JwtProcessor getProcessor() throws SsoException {
        return locator.getProcessor();
    }
}
