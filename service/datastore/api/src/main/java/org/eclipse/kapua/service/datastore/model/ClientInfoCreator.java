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
 *******************************************************************************/
package org.eclipse.kapua.service.datastore.model;

import org.eclipse.kapua.model.id.KapuaId;

import java.util.Date;

/**
 * Client information schema creator definition
 *
 * @since 1.0.0
 */
public interface ClientInfoCreator extends StorableCreator<ClientInfo> {

    /**
     * Get the account
     *
     * @return
     * @since 1.0.0
     */
    KapuaId getScopeId();

    /**
     * Get the client identifier
     *
     * @return
     * @since 1.0.0
     */
    String getClientId();

    /**
     * Set the client identifier
     *
     * @param clientId
     * @since 1.0.0
     */
    void setClientId(String clientId);

    /**
     * Get the message identifier (of the first message published by this client)
     *
     * @return
     * @since 1.0.0
     */
    StorableId getMessageId();

    /**
     * Set the message identifier (of the first message published by this client)
     *
     * @param messageId
     * @since 1.0.0
     */
    void setMessageId(StorableId messageId);

    /**
     * Get the message timestamp (of the first message published by this client)
     *
     * @return
     * @since 1.0.0
     */
    Date getMessageTimestamp();

    /**
     * Set the message timestamp (of the first message published by this client)
     *
     * @param messageTimestamp
     * @since 1.0.0
     */
    void setMessageTimestamp(Date messageTimestamp);
}
