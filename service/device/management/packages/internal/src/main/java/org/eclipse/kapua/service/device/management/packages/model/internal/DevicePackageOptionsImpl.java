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

import org.eclipse.kapua.service.device.management.packages.model.DevicePackageOptions;

/**
 * {@link DevicePackageOptions} {@code abstract} implementation.
 *
 * @since 1.1.0
 */
public abstract class DevicePackageOptionsImpl implements DevicePackageOptions {

    private Long timeout;

    @Override
    public Long getTimeout() {
        return timeout;
    }

    @Override
    public void setTimeout(Long timeout) {
        this.timeout = timeout;
    }
}
