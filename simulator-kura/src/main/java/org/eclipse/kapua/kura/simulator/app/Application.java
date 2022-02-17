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

public interface Application {

    /**
     * Get the application descriptor
     *
     * @return the application descriptor, must never return {@code null}
     */
    public Descriptor getDescriptor();

    /**
     * Create a new application handler
     *
     * @param context
     *            the application context, which can be used to send messages
     *
     * @return a new application handler, must never return {@code null}
     */
    public Handler createHandler(ApplicationContext context);
}
