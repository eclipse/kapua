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

import java.util.concurrent.CompletionStage;

/**
 * An interface for control data
 * <p>
 * An instance of this is bound to a topic and can be looked up by a call
 * to {@link Application#data(Topic)}.
 * </p>
 */
public interface Data extends Sender<Exception> {

    /**
     * Receive messages on this data topic
     * <p>
     * Subscriptions will be automatically re-established after a connection loss.
     * </p>
     *
     * @param handler
     *            the handler which should process received messages
     * @return a {@link CompletionStage} for the operation
     * @throws Exception
     *             if anything goes wrong on the subscription process
     */
    public default CompletionStage<?> subscribe(final MessageHandler handler) throws Exception {
        return subscribe(handler, Errors::ignore);
    }

    /**
     * Receive messages and handle reception errors on this data topic
     * <p>
     * Subscriptions will be automatically re-established after a connection loss.
     * </p>
     *
     * @param handler
     *            the handler which should process received messages
     * @param errorHandler
     *            the handler which should process received messages which got received
     *            but could not be properly parsed
     * @return a {@link CompletionStage} for the operation
     * @throws Exception
     *             if anything goes wrong on the subscription process
     */
    public CompletionStage<?> subscribe(MessageHandler handler, ErrorHandler<? extends Throwable> errorHandler) throws Exception;
}
