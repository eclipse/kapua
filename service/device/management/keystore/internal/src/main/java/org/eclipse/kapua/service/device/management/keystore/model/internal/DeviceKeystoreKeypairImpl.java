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

import org.eclipse.kapua.service.device.management.keystore.model.DeviceKeystoreKeypair;

/**
 * {@link DeviceKeystoreKeypair} implementation.
 *
 * @since 1.5.0
 */
public class DeviceKeystoreKeypairImpl implements DeviceKeystoreKeypair {

    private String keystoreId;
    private String alias;
    private String algorithm;
    private String signatureAlgorithm;
    private Integer size;
    private String attributes;

    /**
     * Constructor.
     *
     * @since 1.5.0
     */
    public DeviceKeystoreKeypairImpl() {
    }

    @Override
    public String getKeystoreId() {
        return keystoreId;
    }

    @Override
    public void setKeystoreId(String keystoreId) {
        this.keystoreId = keystoreId;
    }

    @Override
    public String getAlias() {
        return alias;
    }

    @Override
    public void setAlias(String alias) {
        this.alias = alias;
    }

    @Override
    public String getAlgorithm() {
        return algorithm;
    }

    @Override
    public void setAlgorithm(String algorithm) {
        this.algorithm = algorithm;
    }

    @Override
    public String getSignatureAlgorithm() {
        return signatureAlgorithm;
    }

    @Override
    public void setSignatureAlgorithm(String signatureAlgorithm) {
        this.signatureAlgorithm = signatureAlgorithm;
    }

    @Override
    public Integer getSize() {
        return size;
    }

    @Override
    public void setSize(Integer size) {
        this.size = size;
    }

    @Override
    public String getAttributes() {
        return attributes;
    }

    @Override
    public void setAttributes(String attributes) {
        this.attributes = attributes;
    }
}
