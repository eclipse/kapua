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
 * System message creator (i.e message sent by the broker on device connect)
 * 
 * @since 1.0
 */
public interface SystemMessageCreator {

    enum SystemMessageType {
        CONNECT
    }

    /**
     * Create a system message
     * 
     * @param systemMessageType
     * @param kbcc
     * @return
     */
    String createMessage(SystemMessageType systemMessageType, KapuaBrokerContextContainer kbcc);

}
