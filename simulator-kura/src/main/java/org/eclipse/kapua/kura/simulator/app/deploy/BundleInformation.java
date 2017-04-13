/*******************************************************************************
 * Copyright (c) 2017 Red Hat Inc and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Red Hat Inc - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.kura.simulator.app.deploy;

import org.osgi.framework.Bundle;

public class BundleInformation {

    private final String symbolicName;

    private final String version;

    private final long id;

    private final int state;

    public BundleInformation(final String symbolicName, final String version, final long id, final int state) {
        this.symbolicName = symbolicName;
        this.version = version;
        this.id = id;
        this.state = state;
    }

    public String getSymbolicName() {
        return symbolicName;
    }

    public String getVersion() {
        return version;
    }

    public long getId() {
        return id;
    }

    public int getState() {
        return state;
    }

    public String getStateString() {
        switch (state) {
        case Bundle.UNINSTALLED:
            return "UNINSTALLED";

        case Bundle.INSTALLED:
            return "INSTALLED";

        case Bundle.RESOLVED:
            return "RESOLVED";

        case Bundle.STARTING:
            return "STARTING";

        case Bundle.STOPPING:
            return "STOPPING";

        case Bundle.ACTIVE:
            return "ACTIVE";

        default:
            return Integer.toString(state);
        }
    }
}