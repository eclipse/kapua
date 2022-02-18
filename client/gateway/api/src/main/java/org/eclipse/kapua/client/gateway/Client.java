/*******************************************************************************
 * Copyright (c) 2017, 2022 Red Hat Inc and others.
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
package org.eclipse.kapua.client.gateway;

/**
 * A client connection
 * <p>
 * An instance of a client can be obtained by building and instance
 * via different protocol stacks. e.g. see the {@code KuraMqttProfile}.
 * </p>
 * <p>
 * In order to free resources the instance has to be closed when it is no
 * longer needed.
 * </p>
 * <p>
 * Data is exchanged by the use of {@link Application}s.
 * </p>
 */
public interface Client extends AutoCloseable {

    public interface Builder {

        public Client build() throws Exception;
    }

    /**
     * Get control over the transport
     *
     * @return The transport control instance
     */
    public Transport transport();

    /**
     * Create a new application instance
     * <p>
     * This method only returns a new builder which will
     * create a new instance once {@link Application.Builder#build()} is called.
     * Before that the application is not built and no resources are claimed.
     * </p>
     * <p>
     * Application IDs are unique. If an application ID is already allocated it
     * cannot be allocated a second time. The second call to {@link Application.Builder#build()} will fail.
     * However this application ID only allocated once the call to {@link Application.Builder#build()}
     * succeeded.
     * </p>
     *
     * @param applicationId
     *            The ID of the application to create
     * @return the new {@link Application.Builder} instance
     */
    public Application.Builder buildApplication(String applicationId);
}
