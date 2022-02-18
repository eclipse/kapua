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

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.XMLConfiguration;
import org.eclipse.kapua.locator.KapuaLocatorErrorCodes;
import org.eclipse.kapua.locator.KapuaLocatorException;
import org.eclipse.kapua.locator.guice.GuiceLocatorImpl;

import javax.validation.constraints.NotNull;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * {@link GuiceLocatorImpl} configutation parser.
 *
 * @since 1.0.0
 */
public class LocatorConfig {

    private static final String SERVICE_RESOURCE_INTERFACES = "provided.api";
    private static final String SERVICE_RESOURCE_INCLUDED_PACKAGES = "packages.package";
    private static final String SERVICE_RESOURCE_EXCLUDED_PACKAGES = "packages.excludes.package";

    private final URL url;
    private final List<String> includedPkgNames;
    private final List<String> excludedPkgNames;
    private final List<String> providedInterfaceNames;

    /**
     * Constructor.
     *
     * @param url                    The configuration resource {@link URL}.
     * @param includedPkgNames       The {@link List} of included package names from configuration.
     * @param excludedPkgNames       The {@link List} of excluded package names from configuration.
     * @param providedInterfaceNames The {@link List} of interfaces to map from configuration.
     * @since 1.0.0
     */
    private LocatorConfig(URL url, List<String> includedPkgNames, List<String> excludedPkgNames, List<String> providedInterfaceNames) {
        this.url = url;
        this.includedPkgNames = includedPkgNames;
        this.excludedPkgNames = excludedPkgNames;
        this.providedInterfaceNames = providedInterfaceNames;
    }

    /**
     * Loads the locator configuration from the given {@link URL}.
     *
     * @param url The {@link URL} of the locator configuration.
     * @return The {@link LocatorConfig}.
     * @throws KapuaLocatorException if the configuration is invalid.
     */
    public static LocatorConfig fromURL(@NotNull URL url) throws KapuaLocatorException {
        List<String> includedPkgNames = new ArrayList<>();
        List<String> excludedPkgNames = new ArrayList<>();
        List<String> providedInterfaceNames = new ArrayList<>();

        XMLConfiguration xmlConfig;
        try {
            xmlConfig = new XMLConfiguration(url);
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

        props = xmlConfig.getProperty(SERVICE_RESOURCE_INTERFACES);
        if (props instanceof Collection<?>) {
            addAllStrings(providedInterfaceNames, (Collection<?>) props);
        }
        if (props instanceof String) {
            providedInterfaceNames.add((String) props);
        }

        return new LocatorConfig(url, Collections.unmodifiableList(includedPkgNames), Collections.unmodifiableList(excludedPkgNames), Collections.unmodifiableList(providedInterfaceNames));
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

    /**
     * Gets the configuration resource {@link URL}.
     *
     * @return The configuration resource {@link URL}.
     * @since 1.0.0
     */
    public URL getURL() {
        return url;
    }

    /**
     * Gets the {@link Collection} of included package names.
     *
     * @return The {@link Collection} of included package names.
     * @since 1.0.0
     */
    public Collection<String> getIncludedPackageNames() {
        return includedPkgNames;
    }

    /**
     * Gets the {@link Collection} of excluded package names.
     *
     * @return The {@link Collection} of excluded package names.
     * @since 1.0.0
     */
    public Collection<String> getExcludedPackageNames() {
        return excludedPkgNames;
    }

    /**
     * Gets the {@link Collection} of mapped interfaces.
     *
     * @return The {@link Collection} of mapped interfaces.
     * @since 1.0.0
     */
    public Collection<String> getProvidedInterfaceNames() {
        return providedInterfaceNames;
    }
}
