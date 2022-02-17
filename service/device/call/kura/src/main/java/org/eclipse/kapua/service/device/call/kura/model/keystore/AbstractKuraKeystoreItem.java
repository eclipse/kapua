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

/**
 * {@link AbstractKuraKeystoreItem} definition.
 * <p>
 * Base for keystore objects that have {@link #getKeystoreServicePid()} an {@link #getAlias()} fields.
 *
 * @since 1.5.0
 */
public abstract class AbstractKuraKeystoreItem {

    @JsonProperty("keystoreServicePid")
    private String keystoreServicePid;

    @JsonProperty("alias")
    private String alias;

    /**
     * Constructor.
     *
     * @since 1.5.0
     */
    public AbstractKuraKeystoreItem() {
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
     * Gets the alias.
     *
     * @return The alias.
     * @since 1.5.0
     */
    public String getAlias() {
        return alias;
    }

    /**
     * Sets the alias.
     *
     * @param alias The alias.
     * @since 1.5.0
     */
    public void setAlias(String alias) {
        this.alias = alias;
    }
}
