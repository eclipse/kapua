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
package org.eclipse.kapua.kura.simulator;

/**
 * A simulator module
 * <p>
 * A generic module which can be used to extend the default {@link Simulator}
 * </p>
 */
public interface Module {

    public default void connected(final Transport transport) {
    }

    public default void disconnected(final Transport transport) {
    }
}
