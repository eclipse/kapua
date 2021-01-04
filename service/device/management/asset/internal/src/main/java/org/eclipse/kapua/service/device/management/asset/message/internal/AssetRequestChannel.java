/*******************************************************************************
 * Copyright (c) 2016, 2021 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.service.device.management.asset.message.internal;

import org.eclipse.kapua.service.device.management.commons.message.request.KapuaRequestChannelImpl;

/**
 * Device asset information request channel.
 *
 * @since 1.0
 *
 */
public class AssetRequestChannel extends KapuaRequestChannelImpl {

    private boolean read;
    private boolean write;

    public boolean isRead() {
        return read;
    }

    public void setRead(boolean read) {
        this.read = read;
        this.write = !read;
    }

    public boolean isWrite() {
        return write;
    }

    public void setWrite(boolean write) {
        this.write = write;
        this.read = !write;
    }
}
