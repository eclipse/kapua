/*******************************************************************************
 * Copyright (c) 2017 Red Hat Inc and others
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
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
