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
package org.eclipse.kapua.broker.core.plugin;

import org.eclipse.kapua.KapuaErrorCode;

public enum KapuaBrokerErrorCodes implements KapuaErrorCode {

    /**
     * Duplicated client id on connection (stealing link detected)
     */
    DUPLICATE_CLIENT_ID,

    /**
     * An illegal connection was detected
     */
    ILLEGAL_CONNECTION,

    /**
     * An unexpected device status was detected
     */
    UNEXPECTED_STATUS

}
