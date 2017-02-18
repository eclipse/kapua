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
package org.eclipse.kapua.service.device.registry.connection.internal;

import org.eclipse.kapua.service.device.registry.connection.DeviceConnectionSummary;

/**
 * Device connection summary.
 *
 * @since 1.0
 */
public class DeviceConnectionSummaryImpl implements DeviceConnectionSummary {

    private long connected;
    private long disconnected;
    private long missing;
    private long enabled;
    private long disabled;

    @Override
    public long getConnected() {
        return connected;
    }

    @Override
    public void setConnected(long connected) {
        this.connected = connected;
    }

    @Override
    public long getDisconnected() {
        return disconnected;
    }

    @Override
    public void setDisconnected(long disconnected) {
        this.disconnected = disconnected;
    }

    @Override
    public long getMissing() {
        return missing;
    }

    @Override
    public void setMissing(long missing) {
        this.missing = missing;
    }

    @Override
    public long getEnabled() {
        return enabled;
    }

    @Override
    public void setEnabled(long enabled) {
        this.enabled = enabled;
    }

    @Override
    public long getDisabled() {
        return disabled;
    }

    @Override
    public void setDisabled(long disabled) {
        this.disabled = disabled;
    }
}
