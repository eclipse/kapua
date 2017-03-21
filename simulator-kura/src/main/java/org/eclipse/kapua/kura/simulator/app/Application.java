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
