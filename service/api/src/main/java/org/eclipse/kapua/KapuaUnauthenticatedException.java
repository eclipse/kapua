/*******************************************************************************
 * Copyright (c) 2016, 2022 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua;

/**
 * KapuaUnauthenticatedException is thrown when no authenticated context is found on pre-action rights check.
 * <p>
 * TODO: Evaluate if migration to `kapua-authentication-api` module make sense.
 *
 * @since 1.0.0
 */
public class KapuaUnauthenticatedException extends KapuaException {

    private static final long serialVersionUID = -8059684526029130460L;

    /**
     * Constructor.
     *
     * @since 1.0.0.
     */
    public KapuaUnauthenticatedException() {
        super(KapuaErrorCodes.UNAUTHENTICATED);
    }
}
