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
package org.eclipse.kapua.service.device.management.bundle.message.internal;

import org.eclipse.kapua.service.device.management.commons.message.request.KapuaRequestChannelImpl;

/**
 * Device bundle information request channel.
 * 
 * @since 1.0
 * 
 */
public class BundleRequestChannel extends KapuaRequestChannelImpl {

    private String bundleId;
    private boolean start;

    /**
     * Get the bundle identifier
     * 
     * @return
     */
    public String getBundleId() {
        return bundleId;
    }

    /**
     * Set the bundle identifier
     * 
     * @param bundleId
     */
    public void setBundleId(String bundleId) {
        this.bundleId = bundleId;
    }

    /**
     * Check if the bundle is started
     * 
     * @return
     */
    public boolean isStart() {
        return start;
    }

    /**
     * Set the bundle start flag
     * 
     * @param start
     */
    public void setStart(boolean start) {
        this.start = start;
    }
}
