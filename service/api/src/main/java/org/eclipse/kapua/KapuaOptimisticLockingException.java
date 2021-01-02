/*******************************************************************************
 * Copyright (c) 2016, 2021 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua;

/**
 * KapuaOptimisticLockingException is thrown when an update operation cannot
 * be completed because performed on an entity instance which has been outdated
 * by a earlier modification.
 *
 * @since 1.0
 *
 */
public class KapuaOptimisticLockingException extends KapuaException {

    private static final long serialVersionUID = -4694476988566169899L;

    /**
     * Constructor
     *
     * @param e
     */
    public KapuaOptimisticLockingException(Exception e) {
        super(KapuaErrorCodes.OPTIMISTIC_LOCKING, e);
    }
}
