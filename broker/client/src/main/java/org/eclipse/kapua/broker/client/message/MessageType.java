/*******************************************************************************
 * Copyright (c) 2020, 2021 Eurotech and/or its affiliates and others
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *     Red Hat Inc
 *******************************************************************************/
package org.eclipse.kapua.broker.client.message;

/**
 * Allowed message types
 */
public enum MessageType {

    /**
     * Application message type
     */
    APP,
    /**
     * Birth message type
     */
    BIRTH,
    /**
     * Disconnect message type
     */
    DISCONNECT,
    /**
     * Missing message type
     */
    MISSING,
    /**
     * Notify message type
     */
    NOTIFY,
    /**
     * Data message type
     */
    DATA

}
