/*******************************************************************************
 * Copyright (c) 2017, 2022 Red Hat Inc and others
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Red Hat Inc - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.kura.simulator.app.deploy;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DeploymentPackageInformation {

    public static class BundleInformation {

        private final String symbolicName;

        private final String version;

        public BundleInformation(final String symbolicName, final String version) {
            this.symbolicName = symbolicName;
            this.version = version;
        }

        public String getSymbolicName() {
            return this.symbolicName;
        }

        public String getVersion() {
            return this.version;
        }
    }

    private final String symbolicName;

    private final String version;

    private final Instant installDate;

    private final List<BundleInformation> bundles;

    public DeploymentPackageInformation(final String symbolicName, final String version, final Instant installDate,
            final List<BundleInformation> bundles) {
        this.symbolicName = symbolicName;
        this.version = version;
        this.installDate = installDate;
        this.bundles = Collections.unmodifiableList(new ArrayList<>(bundles));
    }

    public String getSymbolicName() {
        return this.symbolicName;
    }

    public String getVersion() {
        return this.version;
    }

    public Instant getInstallDate() {
        return this.installDate;
    }

    public List<BundleInformation> getBundles() {
        return this.bundles;
    }
}
