/*******************************************************************************
 * Copyright (c) 2021, 2022 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.client.security.amqpclient;

import javax.jms.MessageListener;
import javax.jms.MessageProducer;
import javax.jms.Session;

public abstract class ClientMessageListener implements MessageListener {

    protected Session session;
    protected MessageProducer producer;

    /**
     * Helpful method to enrich the lister with Session and MessageProducer useful in a request reply context
     * (so the lister should send a reply once a message is received)
     *
     * @param session
     * @param producer
     */
    public void init(Session session, MessageProducer producer) {
        this.session = session;
        this.producer = producer;
    }

}
