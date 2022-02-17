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
package org.eclipse.kapua.service.user;

/**
 * The device user type
 */
public enum UserType {
    /**
     * Device user type (no longer used).
     */
    DEVICE,

    /**
     * Internal user type (user credentials from Kapua)
     */
    INTERNAL,

    /**
     * External user type (user credentials from an external Single Sign-On provider)
     */
    EXTERNAL
}
