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
package org.eclipse.kapua.client.gateway.spi;

import java.util.Collection;
import java.util.Optional;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.ScheduledExecutorService;

import org.eclipse.kapua.client.gateway.ErrorHandler;
import org.eclipse.kapua.client.gateway.MessageHandler;
import org.eclipse.kapua.client.gateway.Payload;
import org.eclipse.kapua.client.gateway.Topic;

public interface Channel {

    public interface Context {

        public void notifyConnected();

        public void notifyDisconnected();

        public ScheduledExecutorService executor();
    }

    public void handleInit(Context context);

    public void handleClose(Context context);

    public <T> Optional<T> adapt(Class<T> clazz);

    public CompletionStage<?> handleSubscribe(String applicationId, Topic topic, MessageHandler messageHandler, ErrorHandler<? extends Throwable> errorHandler);

    public CompletionStage<?> handlePublish(String applicationId, Topic topic, Payload payload);

    public void handleUnsubscribe(String applicationId, Collection<Topic> topics) throws Exception;

}
