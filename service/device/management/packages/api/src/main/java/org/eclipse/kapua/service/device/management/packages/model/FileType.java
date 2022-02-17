/*******************************************************************************
 * Copyright (c) 2019, 2022 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.service.device.management.packages.model;


/**
 * Available types of file that can be sent to the {@link org.eclipse.kapua.service.device.registry.Device} to deploy/execute
 *
 * @since 1.1.0
 */
public enum FileType {

    /**
     * Identifies an OSGI deployment package.
     *
     * @since 1.1.0
     */
    DEPLOYMENT_PACKAGE,

    /**
     * Identifies an executable script like a shell script.
     *
     * @since 1.1.0
     */
    EXECUTABLE_SCRIPT
}
