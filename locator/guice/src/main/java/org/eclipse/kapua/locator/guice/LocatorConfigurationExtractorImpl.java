/*******************************************************************************
 * Copyright (c) 2016, 2022 Eurotech and/or its affiliates and others
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *     Red Hat Inc
 *******************************************************************************/
package org.eclipse.kapua.locator.guice;

import org.eclipse.kapua.commons.util.ResourceUtils;

import java.net.URL;

public class LocatorConfigurationExtractorImpl implements LocatorConfigurationExtractor {
    private final LocatorConfig locatorConfig;

    public LocatorConfigurationExtractorImpl(String locatorConfigName) throws Exception {
        this(ResourceUtils.getResource(locatorConfigName));
    }

    public LocatorConfigurationExtractorImpl(URL locatorConfigUrl) throws Exception {
        if (locatorConfigUrl == null) {
            throw new Exception("Locator configuration cannot be found: " + locatorConfigUrl);
        }
        this.locatorConfig = LocatorConfig.fromURL(locatorConfigUrl);
    }

    @Override
    public LocatorConfig fetchLocatorConfig() {
        return locatorConfig;
    }

}
