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
package org.eclipse.kapua.event;

import org.eclipse.kapua.KapuaException;

/**
 * Event bus listener definition
 *
 * @since 1.0
 */
public interface ServiceEventBusListener {

    /**
     * Process the on event business logic<BR>
     * <B>NOTE: This method implementation must be thread safe!</B>
     *
     * @param kapuaEvent
     * @throws KapuaException
     */
    void onKapuaEvent(ServiceEvent kapuaEvent) throws KapuaException;
}
