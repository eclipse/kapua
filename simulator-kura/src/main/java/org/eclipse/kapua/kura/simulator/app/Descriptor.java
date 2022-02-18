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
package org.eclipse.kapua.kura.simulator.app;

public class Descriptor {

    private final String id;

    public Descriptor(final String id) {
        this.id = id;
    }

    /**
     * Get the ID of the application
     *
     * @return the application ID, must never return {@code null}
     */
    public String getId() {
        return id;
    }
}
