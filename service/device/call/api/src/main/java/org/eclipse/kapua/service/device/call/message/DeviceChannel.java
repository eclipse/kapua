/*******************************************************************************
 * Copyright (c) 2011, 2018 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.service.device.call.message;

import org.eclipse.kapua.message.Channel;

/**
 * Device {@link Channel} definition.
 */
public interface DeviceChannel extends Channel {

    /**
     * Gets the message classification.<br>
     * This field it is used to distinguish between command messages and "standard" messages such as telemetry messages.<br>
     * The domain values can be customized via configuration parameter.
     *
     * @return
     */
    public String getMessageClassification();

    /**
     * Sets the message classification.<br>
     * This field it is used to distinguish between command messages and "standard" messages such as telemetry messages.<br>
     * The domain values can be customized via configuration parameter.
     *
     * @param messageClassification
     */
    public void setMessageClassification(String messageClassification);

    /**
     * Get the message scope
     *
     * @return
     */
    public String getScope();

    /**
     * Set th emessage scope
     *
     * @param scope
     */
    public void setScope(String scope);

    /**
     * Get the client identifier
     *
     * @return
     */
    public String getClientId();

    /**
     * Set the client identifier
     *
     * @param clientId
     */
    public void setClientId(String clientId);

}
