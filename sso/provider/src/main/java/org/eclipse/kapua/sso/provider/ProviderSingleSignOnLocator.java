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
package org.eclipse.kapua.sso.provider;

import java.io.Closeable;
import java.util.ServiceLoader;

import org.eclipse.kapua.sso.SingleSignOnLocator;
import org.eclipse.kapua.sso.SingleSignOnService;
import org.eclipse.kapua.sso.provider.SingleSignOnProvider.ProviderLocator;
import org.eclipse.kapua.sso.provider.internal.DisabledLocator;
import org.eclipse.kapua.sso.provider.setting.SsoSetting;
import org.eclipse.kapua.sso.provider.setting.SsoSettingKeys;

public class ProviderSingleSignOnLocator implements SingleSignOnLocator, Closeable {

    private ProviderLocator locator;

    public ProviderSingleSignOnLocator(final SsoSetting settings) {
        final String providerId = settings.getString(SsoSettingKeys.SSO_PROVIDER, null);
        if (providerId == null) {
            this.locator = DisabledLocator.INSTANCE;
        } else {
            this.locator = findProvider(providerId);
        }
    }

    public ProviderSingleSignOnLocator() {
        this(SsoSetting.getInstance());
    }

    @Override
    public void close() {
        // nothing to close at the moment
    }

    @Override
    public SingleSignOnService getService() {
        return this.locator.getService();
    }

    private static ProviderLocator findProvider(final String providerId) {
        for (final SingleSignOnProvider provider : ServiceLoader.load(SingleSignOnProvider.class)) {
            if (providerId.equals(provider.getId())) {
                return provider.createLocator();
            }
        }

        throw new IllegalArgumentException(String.format("Unable to find single sign-on provider '%s'", providerId));
    }

}
