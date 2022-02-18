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
package org.eclipse.kapua.service.datastore.internal.model;

import javax.xml.bind.annotation.XmlEnum;

/**
 * Data index by options
 *
 * @since 1.0.0
 */
@XmlEnum
public enum DataIndexBy {

    /**
     * Server timestamp.
     * <p>
     * The message will be indexed by the timestamp of the server at the processing time
     *
     * @since 1.0.0
     */
    SERVER_TIMESTAMP,

    /**
     * Device timestamp.
     * <p>
     * The message will be indexed by the timestamp of the device (capturedOn message field)
     *
     * @since 1.0.0
     */
    DEVICE_TIMESTAMP
}
