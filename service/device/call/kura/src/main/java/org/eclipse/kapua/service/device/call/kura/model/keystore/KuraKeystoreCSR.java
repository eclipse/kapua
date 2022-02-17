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
 * {@link KuraKeystoreCSR} definition.
 *
 * @since 1.5.0
 */
@JsonRootName("keystoreCSR")
public class KuraKeystoreCSR {

    @JsonProperty("signingRequest")
    private String signingRequest;

    /**
     * Constructor.
     *
     * @since 1.5.0
     */
    public KuraKeystoreCSR() {
    }

    /**
     * Gets the signing request.
     *
     * @return The signing request.
     * @since 1.5.0
     */
    public String getSigningRequest() {
        return signingRequest;
    }

    /**
     * Sets the signing request.
     *
     * @param signingRequest The signing request.
     * @since 1.5.0
     */
    public void setSigningRequest(String signingRequest) {
        this.signingRequest = signingRequest;
    }
}
