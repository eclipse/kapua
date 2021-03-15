/*******************************************************************************
 * Copyright (c) 2018, 2021 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.service.device.management.message.notification;

/**
 * {@link KapuaNotifyMessage} operation status.
 *
 * @since 1.1.0
 */
public enum OperationStatus {

    /**
     * The operation is running.
     *
     * @since 1.1.0
     */
    RUNNING,

    /**
     * The operation has completed successfully.
     *
     * @since 1.1.0
     */
    COMPLETED,

    /**
     * The operation has failed.
     *
     * @since 1.1.0
     */
    FAILED,

    /**
     * The operation has unknown status.
     *
     * @since 1.1.0
     */
    STALE
}
