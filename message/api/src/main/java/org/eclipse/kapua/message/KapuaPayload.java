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
 *
 *******************************************************************************/
package org.eclipse.kapua.message;

import java.util.Map;

/**
 * Kapua message payload object definition.
 *
 * @since 1.0
 *
 */
public interface KapuaPayload extends Payload
{

    /**
     * Get the properties map
     * 
     * @return
     */
    public Map<String, Object> getProperties();

    /**
     * Set the properties map
     * 
     * @param metrics
     */
    public void setProperties(Map<String, Object> metrics);

    /**
     * Get the message body
     * 
     * @return
     */
    public byte[] getBody();

    /**
     * Set the message body
     * 
     * @param body
     */
    public void setBody(byte[] body);

    /**
     * Convert the message to a displayable String
     * 
     * @return
     */
    public String toDisplayString();
}
