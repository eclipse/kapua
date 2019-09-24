/*******************************************************************************
 * Copyright (c) 2019 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
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
