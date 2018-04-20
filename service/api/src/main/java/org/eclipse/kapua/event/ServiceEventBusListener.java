/*******************************************************************************
 * Copyright (c) 2017 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
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
    public void onKapuaEvent(ServiceEvent kapuaEvent) throws KapuaException;
}