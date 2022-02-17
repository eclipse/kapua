/*******************************************************************************
 * Copyright (c) 2016, 2022 Eurotech and/or its affiliates and others
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.transport.message.pubsub;

import org.eclipse.kapua.transport.message.TransportChannel;
import org.eclipse.kapua.transport.message.TransportMessage;
import org.eclipse.kapua.transport.message.TransportPayload;

/**
 * Marker interface for all messages that uses publish/subscribe interaction in the transport layer.
 *
 * @param <C> The {@link TransportChannel} for this {@link TransportMessage}
 * @param <P> The {@link TransportPayload} for this {@link TransportMessage}
 * @author alberto.codutti
 * @since 1.0.0
 */
public interface PubSubTransportMessage<C extends TransportChannel, P extends TransportPayload> extends TransportMessage<C, P> {

}
