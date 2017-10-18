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
 *******************************************************************************/
package org.eclipse.kapua.message.device.lifecycle;

import org.eclipse.kapua.message.KapuaChannel;

/**
 * Kapua notify message channel object definition.
 *
 * @since 1.0
 */
public interface KapuaNotifyChannel extends KapuaChannel {

    /**
     * Set client identifier
     *
     * @return
     */
    public String getClientId();

    /**
     * Set client identifier
     *
     * @param clientId
     */
    public void setClientId(String clientId);

    public String getResources();

    public void setResources(String resources);
}
