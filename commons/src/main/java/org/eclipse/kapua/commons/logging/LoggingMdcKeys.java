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
package org.eclipse.kapua.commons.logging;

import org.eclipse.kapua.service.account.Account;
import org.eclipse.kapua.service.user.User;
import org.slf4j.MDC;

/**
 * Logging {@link MDC} keys.
 *
 * @see MDC
 * @since 1.6.0
 */
public class LoggingMdcKeys {

    /**
     * Constructor.
     *
     * @since 1.6.0
     */
    private LoggingMdcKeys() {
    }

    /**
     * The {@link Account#getId()}.
     *
     * @since 1.0.0
     */
    public static final String SCOPE_ID = "scopeId";

    /**
     * The {@link Account#getName()}.
     *
     * @since 1.0.0
     */
    public static final String ACCOUNT_NAME = "scopeName";

    /**
     * The {@link User#getId()}.
     *
     * @since 1.0.0
     */
    public static final String USER_ID = "userId";

    /**
     * The {@link User#getName()}.
     *
     * @since 1.6.0
     */
    public static final String USER_NAME = "userName";
}
