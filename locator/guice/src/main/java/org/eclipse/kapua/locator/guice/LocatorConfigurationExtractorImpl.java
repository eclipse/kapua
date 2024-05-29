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

import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.XMLConfiguration;
import org.eclipse.kapua.commons.util.ResourceUtils;
import org.eclipse.kapua.locator.KapuaLocatorErrorCodes;
import org.eclipse.kapua.locator.KapuaLocatorException;
import org.eclipse.kapua.locator.LocatorConfig;
import org.eclipse.kapua.locator.LocatorConfigurationExtractor;

public class LocatorConfigurationExtractorImpl implements LocatorConfigurationExtractor {

    private static final String SERVICE_RESOURCE_INCLUDED_PACKAGES = "packages.package";
    private static final String SERVICE_RESOURCE_EXCLUDED_PACKAGES = "packages.excludes.package";

    private final LocatorConfig locatorConfig;

    public LocatorConfigurationExtractorImpl(String locatorConfigName) throws Exception {
        this(ResourceUtils.getResource(locatorConfigName));
    }

    public LocatorConfigurationExtractorImpl(URL locatorConfigUrl) throws Exception {
        if (locatorConfigUrl == null) {
            throw new Exception("Locator configuration cannot be found: " + locatorConfigUrl);
        }
        List<String> includedPkgNames = new ArrayList<>();
        List<String> excludedPkgNames = new ArrayList<>();

        XMLConfiguration xmlConfig;
        try {
            xmlConfig = new XMLConfiguration(locatorConfigUrl);
        } catch (ConfigurationException e) {
            throw new KapuaLocatorException(KapuaLocatorErrorCodes.INVALID_CONFIGURATION, e);
        }

        Object props = xmlConfig.getProperty(SERVICE_RESOURCE_INCLUDED_PACKAGES);
        if (props instanceof Collection<?>) {
            addAllStrings(includedPkgNames, (Collection<?>) props);
        }
        if (props instanceof String) {
            includedPkgNames.add((String) props);
        }

        props = xmlConfig.getProperty(SERVICE_RESOURCE_EXCLUDED_PACKAGES);
        if (props instanceof Collection<?>) {
            addAllStrings(excludedPkgNames, (Collection<?>) props);
        }
        if (props instanceof String) {
            excludedPkgNames.add((String) props);
        }

        this.locatorConfig = new LocatorConfig(Collections.unmodifiableList(includedPkgNames), Collections.unmodifiableList(excludedPkgNames));
    }

    /**
     * @param list
     * @param other
     * @since 1.0.0
     */
    private static void addAllStrings(List<String> list, Collection<?> other) {
        for (Object entry : other) {
            if (entry instanceof String) {
                list.add((String) entry);
            }
        }
    }

    @Override
    public LocatorConfig fetchLocatorConfig() {
        return locatorConfig;
    }

}
