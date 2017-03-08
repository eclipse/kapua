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

import java.util.Date;

import org.eclipse.kapua.message.KapuaChannel;
import org.eclipse.kapua.message.KapuaMessage;
import org.eclipse.kapua.message.KapuaPayload;

/**
 * Message returned by the data store find services
 *
 * @since 1.0
 */
public interface DatastoreMessage extends KapuaMessage<KapuaChannel, KapuaPayload>, Storable
{

    /**
     * Stored message identifier
     * 
     * @return
     */
    public StorableId getDatastoreId();

    /**
     * Stored message timestamp
     * 
     * @return
     */
    public Date getTimestamp();
}
