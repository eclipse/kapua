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
package org.eclipse.kapua.service.datastore.internal.mediator;

import org.eclipse.kapua.service.datastore.internal.schema.MessageSchema;
import org.eclipse.kapua.service.datastore.model.query.StorableField;

/**
 * This enumeration defines the fields names used in the {@link MessageField} client schema
 *
 * @since 1.0.0
 */
public enum MessageField implements StorableField {
    /**
     * Message identifier
     */
    MESSAGE_ID(MessageSchema.MESSAGE_ID),
    /**
     * Account identifier
     */
    SCOPE_ID(MessageSchema.MESSAGE_SCOPE_ID),
    /**
     * Device identifier (physical device)
     */
    DEVICE_ID(MessageSchema.MESSAGE_DEVICE_ID),
    /**
     * Client identifier
     */
    CLIENT_ID(MessageSchema.MESSAGE_CLIENT_ID),
    /**
     * Channel
     */
    CHANNEL(MessageSchema.MESSAGE_CHANNEL),
    /**
     * Message indexing timestamp
     */
    TIMESTAMP(MessageSchema.MESSAGE_TIMESTAMP),
    /**
     * Received on timestamp
     */
    RECEIVED_ON(MessageSchema.MESSAGE_RECEIVED_ON),
    /**
     * Remote address
     */
    FROM_IP_ADDRESS(MessageSchema.MESSAGE_IP_ADDRESS),
    /**
     * Captured on timestamp
     */
    CAPTURED_ON(MessageSchema.MESSAGE_CAPTURED_ON),
    /**
     * Sent on timestamp
     */
    SENT_ON(MessageSchema.MESSAGE_SENT_ON),
    /**
     * Device position
     */
    POSITION(MessageSchema.MESSAGE_POSITION),
    /**
     * Device position - location
     */
    POSITION_LOCATION(MessageSchema.MESSAGE_POS_LOCATION_FULL),
    /**
     * Device position - altitude
     */
    POSITION_ALT(MessageSchema.MESSAGE_POS_ALT_FULL),
    /**
     * Device position - precision
     */
    POSITION_PRECISION(MessageSchema.MESSAGE_POS_PRECISION_FULL),
    /**
     * Device position - heading
     */
    POSITION_HEADING(MessageSchema.MESSAGE_POS_HEADING_FULL),
    /**
     * Device position - speed
     */
    POSITION_POS_SPEED(MessageSchema.MESSAGE_POS_SPEED_FULL),
    /**
     * Device position - timestamp
     */
    POSITION_TIMESTAMP(MessageSchema.MESSAGE_POS_TIMESTAMP_FULL),
    /**
     * Device position - satellites
     */
    POSITION_SATELLITES(MessageSchema.MESSAGE_POS_SATELLITES_FULL),
    /**
     * Device position - status
     */
    POSITION_STATUS(MessageSchema.MESSAGE_POS_STATUS_FULL),
    /**
     * Message metrics
     */
    METRICS(MessageSchema.MESSAGE_METRICS),
    /**
     * Message body
     */
    BODY(MessageSchema.MESSAGE_BODY);

    private String field;

    private MessageField(String name) {
        this.field = name;
    }

    @Override
    public String field() {
        return field;
    }
}
