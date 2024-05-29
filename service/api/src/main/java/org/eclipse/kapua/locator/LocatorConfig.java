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
package org.eclipse.kapua.locator;

import java.util.Collection;
import java.util.List;

/**
 * Locator configuration holder.
 *
 * @since 1.0.0
 */
public class LocatorConfig {

    private final List<String> includedPkgNames;
    private final List<String> excludedPkgNames;

    /**
     * Constructor.
     *
     * @param includedPkgNames
     *         The {@link List} of included package names from configuration.
     * @param excludedPkgNames
     *         The {@link List} of excluded package names from configuration.
     * @since 1.0.0
     */
    public LocatorConfig(List<String> includedPkgNames, List<String> excludedPkgNames) {
        this.includedPkgNames = includedPkgNames;
        this.excludedPkgNames = excludedPkgNames;
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
}
