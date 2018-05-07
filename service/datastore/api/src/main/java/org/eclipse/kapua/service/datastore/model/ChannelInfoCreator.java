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
 * Channel information schema creator definition
 *
 * @since 1.0.0
 */
public interface ChannelInfoCreator extends StorableCreator<ChannelInfo> {

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
     * Get the name
     *
     * @return
     * @since 1.0.0
     */
    String getName();

    /**
     * Set the channel name
     *
     * @param name
     * @since 1.0.0
     */
    void setName(String name);

    /**
     * Get the message identifier (of the first message published on this channel)
     *
     * @return
     * @since 1.0.0
     */
    StorableId getMessageId();

    /**
     * Set the message identifier (of the first message published on this channel)
     *
     * @param messageId
     * @since 1.0.0
     */
    void setMessageId(StorableId messageId);

    /**
     * Get the message timestamp (of the first message published on this channel)
     *
     * @return
     * @since 1.0.0
     */
    Date getMessageTimestamp();

    /**
     * Set the message timestamp (of the first message published on this channel)
     *
     * @param messageTimestamp
     * @since 1.0.0
     */
    void setMessageTimestamp(Date messageTimestamp);
}
