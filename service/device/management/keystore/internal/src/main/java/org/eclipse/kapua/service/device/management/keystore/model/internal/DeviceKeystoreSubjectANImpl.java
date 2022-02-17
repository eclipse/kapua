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

import org.eclipse.kapua.service.device.management.keystore.model.DeviceKeystoreSubjectAN;

/**
 * {@link DeviceKeystoreSubjectAN} implementation.
 *
 * @since 1.5.0
 */
public class DeviceKeystoreSubjectANImpl implements DeviceKeystoreSubjectAN {

    private String anType;
    private String value;

    public DeviceKeystoreSubjectANImpl() {
    }

    /**
     * Constructor.
     *
     * @since 1.5.0
     */
    public DeviceKeystoreSubjectANImpl(String anType, String anValue) {
        setANType(anType);
        setValue(anValue);
    }

    @Override
    public String getANType() {
        return anType;
    }

    @Override
    public void setANType(String anType) {
        this.anType = anType;
    }

    @Override
    public String getValue() {
        return value;
    }

    @Override
    public void setValue(String value) {
        this.value = value;
    }
}
