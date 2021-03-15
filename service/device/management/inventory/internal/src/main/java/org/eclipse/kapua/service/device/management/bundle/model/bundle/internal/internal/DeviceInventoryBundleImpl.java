/*******************************************************************************
 * Copyright (c) 2021 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.service.device.management.bundle.model.bundle.internal.internal;

import org.eclipse.kapua.service.device.management.inventory.model.bundle.inventory.DeviceInventoryBundle;

/**
 * {@link DeviceInventoryBundle} implementation.
 *
 * @since 1.5.0
 */
public class DeviceInventoryBundleImpl implements DeviceInventoryBundle {

    private String id;
    private String name;
    private String version;
    private String status;

    /**
     * Constructor.
     *
     * @since 1.5.0
     */
    public DeviceInventoryBundleImpl() {
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getVersion() {
        return version;
    }

    @Override
    public void setVersion(String version) {
        this.version = version;
    }

    @Override
    public String getStatus() {
        return status;
    }

    @Override
    public void setStatus(String status) {
        this.status = status;
    }
}
