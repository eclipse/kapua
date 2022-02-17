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

import java.util.function.Consumer;

import org.eclipse.kapua.kura.simulator.payload.Message;
import org.eclipse.kapua.kura.simulator.topic.Topic;

public interface Transport {

    /**
     * Connect
     */
    public void connect();

    /**
     * Disconnect gracefully <br>
     * A later call to {@link #connect()} must be possible.
     */
    public void disconnect();

    /**
     * Set the runnable which will be called when a connection was established
     * <p>
     * <strong>Note:</strong> When the connection is already established then
     * this method won't be called again.
     * </p>
     */
    public void whenConnected(Runnable runnable);

    /**
     * Set the runnable which will be called when a connection was lost
     */
    public void whenDisconnected(Runnable runnable);

    /**
     * Subscribe to a topic or topic pattern
     *
     * @param topic
     *            the topic to subscribe to
     * @param consumer
     *            the consumer to call when a message was received
     */
    public void subscribe(Topic topic, Consumer<Message> consumer);

    /**
     * Unsubscribe from a topic or topic pattern
     *
     * @param topic
     *            to unsubscribe from
     */
    public void unsubscribe(Topic topic);

    /**
     * Send a message to a topic
     *
     * @param topic
     *            the topic to send the message to
     * @param payload
     *            the payload to send
     */
    public void sendMessage(Topic topic, byte[] payload);
}
