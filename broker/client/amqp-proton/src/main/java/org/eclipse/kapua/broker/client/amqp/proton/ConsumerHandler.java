/*******************************************************************************
 * Copyright (c) 2018 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.broker.client.amqp.proton;

import org.apache.qpid.proton.engine.Delivery;
import org.apache.qpid.proton.message.Message;

public interface ConsumerHandler {

    void consumeMessage(Delivery delivery, Message message) throws Exception;

}