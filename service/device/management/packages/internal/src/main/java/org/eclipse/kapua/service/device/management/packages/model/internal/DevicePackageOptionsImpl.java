/*******************************************************************************
 * Copyright (c) 2019 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.service.device.management.packages.model.internal;

import org.eclipse.kapua.model.id.KapuaId;

public abstract class DevicePackageOptionsImpl {

    Long timeout;

    private KapuaId forcedOperationId;

    public Long getTimeout() {
        return timeout;
    }

    public void setTimeout(Long timeout) {
        this.timeout = timeout;
    }

    public KapuaId getForcedOperationId() {
        return forcedOperationId;
    }

    public void setForcedOperationId(KapuaId forcedOperationId) {
        this.forcedOperationId = forcedOperationId;
    }
}
