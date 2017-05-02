/*******************************************************************************
 * Copyright (c) 2011, 2017 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *     Red Hat Inc
 *******************************************************************************/
package org.eclipse.kapua.transport.message;

import org.eclipse.kapua.message.Message;

/**
 * Marker interface for all messages at the transport layer.
 *
 * @param <C>
 *            The {@link TransportChannel} for this {@link TransportMessage}
 * @param <P>
 *            The {@link TransportPayload} for this {@link TransportMessage}
 * @author alberto.codutti
 * @since 1.0.0
 */
public interface TransportMessage<C extends TransportChannel, P extends TransportPayload> extends Message<C, P> {
}
