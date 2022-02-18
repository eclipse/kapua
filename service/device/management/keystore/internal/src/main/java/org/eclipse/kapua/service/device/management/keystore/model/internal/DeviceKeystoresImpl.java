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
import org.eclipse.kapua.service.device.management.keystore.model.DeviceKeystores;

import java.util.ArrayList;
import java.util.List;

/**
 * {@link DeviceKeystores} implementation.
 *
 * @since 1.5.0
 */
public class DeviceKeystoresImpl implements DeviceKeystores {

    private static final long serialVersionUID = 1318691706440015345L;

    private List<DeviceKeystore> keystores;

    /**
     * Constructor.
     *
     * @since 1.5.0
     */
    public DeviceKeystoresImpl() {
    }

    @Override
    public List<DeviceKeystore> getKeystores() {
        if (keystores == null) {
            keystores = new ArrayList<>();
        }

        return keystores;
    }

    @Override
    public void addKeystore(DeviceKeystore keystore) {
        getKeystores().add(keystore);
    }

    @Override
    public void setKeystores(List<DeviceKeystore> keystores) {
        this.keystores = keystores;
    }
}
