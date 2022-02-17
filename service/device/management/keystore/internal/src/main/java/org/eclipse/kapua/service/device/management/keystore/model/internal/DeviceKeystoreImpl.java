/*******************************************************************************
 * Copyright (c) 2021, 2022 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.service.device.management.keystore.model.internal;

import org.eclipse.kapua.service.device.management.keystore.model.DeviceKeystore;

/**
 * {@link DeviceKeystore} implementation.
 *
 * @since 1.5.0
 */
public class DeviceKeystoreImpl implements DeviceKeystore {

    private String id;
    private String keystoreType;
    private Integer size;

    /**
     * Constructor.
     *
     * @since 1.5.0
     */
    public DeviceKeystoreImpl() {
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
    public String getKeystoreType() {
        return keystoreType;
    }

    @Override
    public void setKeystoreType(String keystoreType) {
        this.keystoreType = keystoreType;
    }

    @Override
    public Integer getSize() {
        return size;
    }

    @Override
    public void setSize(Integer size) {
        this.size = size;
    }
}
