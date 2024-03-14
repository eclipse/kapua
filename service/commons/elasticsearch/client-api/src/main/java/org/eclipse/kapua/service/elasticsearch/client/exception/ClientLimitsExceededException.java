/*******************************************************************************
 * Copyright (c) 2017, 2022 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.service.elasticsearch.client.exception;

import org.eclipse.kapua.service.elasticsearch.client.ElasticsearchClient;

/**
 * {@link ClientException} to throw when {@link ElasticsearchClient} received a request that is not able to process correctly for internal technical limits problems.
 *
 * @since 2.0.0
 */
public class ClientLimitsExceededException extends ClientException {

    private static final long serialVersionUID = 2212581033816589804L;

    private final String reason;

    /**
     * Constructor.
     *
     * @param reason The reason of the exception.
     * @since 2.0.0
     */
    public ClientLimitsExceededException(String reason) {
        this(null, reason);
    }

    /**
     * Constructor.
     *
     * @param cause  The root {@link Throwable} of this {@link ClientLimitsExceededException}.
     * @param reason The reason of the exception.
     * @since 2.0.0
     */
    public ClientLimitsExceededException(Throwable cause, String reason) {
        super(ClientErrorCodes.LIMITS_EXCEEDED, cause, reason);

        this.reason = reason;
    }

    /**
     * Gets the reason of the initialization error.
     *
     * @return The reason of the initialization error.
     * @since 2.0.0
     */
    public String getReason() {
        return reason;
    }


}
