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
package org.eclipse.kapua.service.device.management.asset.message.internal;

import org.eclipse.kapua.service.device.management.KapuaMethod;
import org.eclipse.kapua.service.device.management.commons.message.response.KapuaAppChannelImpl;
import org.eclipse.kapua.service.device.management.request.KapuaRequestChannel;

/**
 * Device asset information request channel.
 * 
 * @since 1.0
 * 
 */
public class AssetRequestChannel extends KapuaAppChannelImpl implements KapuaRequestChannel {

    private KapuaMethod method;
    private boolean read = false;
    private boolean write = false;

    @Override
    public KapuaMethod getMethod() {
        return method;
    }

    @Override
    public void setMethod(KapuaMethod method) {
        this.method = method;
    }

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
