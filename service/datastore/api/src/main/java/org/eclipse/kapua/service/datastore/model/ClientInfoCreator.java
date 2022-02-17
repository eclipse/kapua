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
package org.eclipse.kapua.service.datastore.model;

import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.storable.model.StorableCreator;
import org.eclipse.kapua.service.storable.model.id.StorableId;

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
