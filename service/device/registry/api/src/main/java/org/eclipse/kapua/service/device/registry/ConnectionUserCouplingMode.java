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
package org.eclipse.kapua.service.device.registry;

/**
 * Defines the strategy to validate credentials during device login.
 *
 * @since 1.0
 *
 */
public enum ConnectionUserCouplingMode {
    /**
     * Strategy to use will be picked up from the default value set for the current account.
     *
     * This value cannot be used as a default for the account.
     */
    INHERITED(false),

    /**
     * Device can change login credentials between credentials and
     * can use credentials that are used by another device.<br>
     * <br>
     * This is the most insecure strategy.
     */
    LOOSE,

    /**
     * Device cannot change login credentials between logins and
     * cannot use credentials that are used by another device.<br>
     * <br>
     * This is the most secure strategy.
     */
    STRICT;

    private boolean usableAsAccountDefault;

    /**
     * Constructor
     */
    ConnectionUserCouplingMode() {
        this(true);
    }

    ConnectionUserCouplingMode(boolean usableAsAccountDefault) {
        this.usableAsAccountDefault = usableAsAccountDefault;
    }

    /**
     * Returns whether or not this value can be used as a default for the default tight value in an account.
     *
     * @return true if it is usable, false if not
     */
    public boolean isUsableAsAccountDefault() {
        return usableAsAccountDefault;
    }
}
