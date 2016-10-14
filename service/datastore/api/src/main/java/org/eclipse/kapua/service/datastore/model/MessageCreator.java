/*******************************************************************************
 * Copyright (c) 2011, 2016 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *     Red Hat - API documentation, refactoring
 *
 *******************************************************************************/
package org.eclipse.kapua.service.datastore.model;

import java.util.Date;

/**
 * Builder object for telemetry message that is supposed to be stored by Kapua.
 */
public interface MessageCreator extends StorableCreator<Message>
{

    /**
     * Timestamp indicating when telemetry value has been read on the device. The timestamp is expressed using server
     * clock time - if time on the device is different than on the server, then the timestamp should be converted before
     * to server time before it is passed to the creator.
     *
     * Cannot be null.
     */
    Date getTimestamp();

    /**
     * Setter for timestamp. Cannot be null.
     */
    void setTimestamp(Date timestamp);

    Date getReceivedOn();

    void setReceivedOn(Date receivedOn);

    String getTopic();

    void setTopic(String topic);

    Payload getPayload();

    void setPayload(Payload payload);
}
