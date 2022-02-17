/*******************************************************************************
 * Copyright (c) 2017, 2022 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.broker.core.message.system;

import org.eclipse.kapua.broker.core.plugin.KapuaSecurityContext;

/**
 * System message creator (i.e message sent by the broker on device connect)
 *
 * @since 1.0
 */
public interface SystemMessageCreator {

    enum SystemMessageType {
        CONNECT,
        DISCONNECT
    }

    /**
     * Create a system message
     *
     * @param systemMessageType
     * @param kapuaSecurityContext
     * @return
     */
    String createMessage(SystemMessageType systemMessageType, KapuaSecurityContext kapuaSecurityContext);

}
