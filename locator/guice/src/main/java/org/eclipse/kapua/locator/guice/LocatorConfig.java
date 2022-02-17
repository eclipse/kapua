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
import org.eclipse.kapua.locator.KapuaLocatorErrorCodes;
import org.eclipse.kapua.locator.KapuaLocatorException;

public class LocatorConfig {

    private static final String SERVICE_RESOURCE_INTERFACES = "provided.api";
    private static final String SERVICE_RESOURCE_PACKAGES = "packages.package";

    private final URL url;
    private final List<String> packageNames;
    private final List<String> providedInterfaceNames;

    private LocatorConfig(final URL url, final List<String> packageNames, final List<String> providedInterfaceNames) {
        this.url = url;
        this.packageNames = packageNames;
        this.providedInterfaceNames = providedInterfaceNames;
    }

    public static LocatorConfig fromURL(final URL url) throws KapuaLocatorException {

        if (url == null) {
            throw new IllegalArgumentException("'url' must not be null");
        }

        final List<String> packageNames = new ArrayList<>();
        final List<String> providedInterfaceNames = new ArrayList<>();

        final XMLConfiguration xmlConfig;
        try {
            xmlConfig = new XMLConfiguration(url);
        } catch (ConfigurationException e) {
            throw new KapuaLocatorException(KapuaLocatorErrorCodes.INVALID_CONFIGURATION, e);
        }

        Object props = xmlConfig.getProperty(SERVICE_RESOURCE_PACKAGES);
        if (props instanceof Collection<?>) {
            addAllStrings(packageNames, (Collection<?>) props);
        }
        if (props instanceof String) {
            packageNames.add((String) props);
        }

        props = xmlConfig.getProperty(SERVICE_RESOURCE_INTERFACES);
        if (props instanceof Collection<?>) {
            addAllStrings(providedInterfaceNames, (Collection<?>) props);
        }
        if (props instanceof String) {
            providedInterfaceNames.add((String) props);
        }

        return new LocatorConfig(url, Collections.unmodifiableList(packageNames), Collections.unmodifiableList(providedInterfaceNames));
    }

    private static void addAllStrings(final List<String> list, Collection<?> other) {
        for (Object entry : other) {
            if (entry instanceof String) {
                list.add((String) entry);
            }
        }
    }

    public URL getURL() {
        return url;
    }

    public Collection<String> getPackageNames() {
        return packageNames;
    }

    public Collection<String> getProvidedInterfaceNames() {
        return providedInterfaceNames;
    }
}
