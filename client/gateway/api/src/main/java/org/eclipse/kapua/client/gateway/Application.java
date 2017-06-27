/*******************************************************************************
 * Copyright (c) 2017 Red Hat Inc and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Red Hat Inc - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.client.gateway;

/**
 * An application is a sub-unit of a client, focused on handling data
 * <p>
 * The {@link Client} instance is more technical unit, where the {@link Application}
 * is more a data oriented unit.
 * </p>
 */
public interface Application extends AutoCloseable {

    public interface Builder {

        public Application build();
    }

    /**
     * Lookup a data controller to an application topic
     *
     * @param topic
     *            the topic the controller is bound to, must never be {@code null}
     * @return the data controller
     */
    public Data data(Topic topic);

    /**
     * Lookup a transport controller
     * <p>
     * A transport controller for the client's underlying transport mechanism.
     * </p>
     * <p>
     * <b>Note:</b> Each application has its own instance and thus can set
     * events handler independent of the root client or other applications.
     * </p>
     *
     * @return the transport controller
     */
    public Transport transport();
}
