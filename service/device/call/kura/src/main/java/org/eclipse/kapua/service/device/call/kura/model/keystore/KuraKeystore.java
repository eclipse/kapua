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
package org.eclipse.kapua.service.device.call.kura.model.keystore;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

/**
 * {@link KuraKeystore} definition.
 *
 * @since 1.5.0
 */
@JsonRootName("keystore")
public class KuraKeystore {

    @JsonProperty("keystoreServicePid")
    private String keystoreServicePid;

    @JsonProperty("type")
    private String type;

    @JsonProperty("size")
    private Integer size;

    /**
     * Constructor.
     *
     * @since 1.5.0
     */
    public KuraKeystore() {
    }

    /**
     * Gets the keystore service pid.
     *
     * @return The keystore service pid.
     * @since 1.5.0
     */
    public String getKeystoreServicePid() {
        return keystoreServicePid;
    }

    /**
     * Sets the keystore service pid.
     *
     * @param keystoreServicePid The keystore service pid.
     * @since 1.5.0
     */
    public void setKeystoreServicePid(String keystoreServicePid) {
        this.keystoreServicePid = keystoreServicePid;
    }

    /**
     * Gets the type.
     *
     * @return The type.
     * @since 1.5.0
     */
    public String getType() {
        return type;
    }

    /**
     * Sets the type.
     *
     * @param type The type.
     * @since 1.5.0
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * Gets the size.
     *
     * @return The size.
     * @since 1.5.0
     */
    public Integer getSize() {
        return size;
    }

    /**
     * Gets the size.
     *
     * @param size The size.
     * @since 1.5.0
     */
    public void setSize(Integer size) {
        this.size = size;
    }
}
