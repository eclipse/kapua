/*******************************************************************************
 * Copyright (c) 2011, 2016 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *
 *******************************************************************************/
package org.eclipse.kapua.service.device.management.packages.model.uninstall;

/**
 * Device package uninstall status.
 * 
 * @since 1.0
 *
 */
public enum DevicePackageUninstallStatus
{
    /**
     * In progress
     */
    IN_PROGRESS,
    /**
     * Completed
     */
    COMPLETED,
    /**
     * Failed
     */
    FAILED
}
