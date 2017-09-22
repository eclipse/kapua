/*******************************************************************************
 * Copyright (c) 2017 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.broker.core.message.system;

import org.eclipse.kapua.broker.core.plugin.KapuaBrokerContextContainer;

/**
 * Default system message creator
 *
 * @since 1.0
 */
public class DefaultSystemMessageCreator implements SystemMessageCreator {

    private final static String CONNECT_MESSAGE_TEMPLATE = "Device: [%s] - connected by user: [%s]";

    @Override
    public String createMessage(SystemMessageType systemMessageType, KapuaBrokerContextContainer kbcc) {
        return String.format(CONNECT_MESSAGE_TEMPLATE,
                kbcc.getClientId(),
                kbcc.getUserName());
    }

}
