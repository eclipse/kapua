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
 *******************************************************************************/
package org.eclipse.kapua.service.device.management;

/**
 * Kapua request/reply method definition.<br>
 * This object defines the command types that should be supported by a Kapua device.
 * 
 * @since 1.0
 *
 */
public enum KapuaMethod {
    /**
     * Read
     */
    READ,
    /**
     * Create
     */
    CREATE,
    /**
     * Write
     */
    WRITE,
    /**
     * Delete
     */
    DELETE,
    /**
     * Options
     */
    OPTIONS,
    /**
     * Execute
     */
    EXECUTE;
}
