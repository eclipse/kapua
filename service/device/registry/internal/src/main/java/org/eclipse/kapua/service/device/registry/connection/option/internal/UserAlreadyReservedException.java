/*******************************************************************************
 * Copyright (c) 2011, 2018 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.service.device.registry.connection.option.internal;

import org.eclipse.kapua.ConnectionServiceErrorCodes;
import org.eclipse.kapua.model.id.KapuaId;

public class UserAlreadyReservedException extends ConnectionServiceException{

    /**
     * 
     */
    private static final long serialVersionUID = 7427228255238231063L;

    public UserAlreadyReservedException(KapuaId scopeId, KapuaId connectionId, KapuaId userId) {
        super(ConnectionServiceErrorCodes.USER_ALREADY_RESERVED_BY_ANOTHER_CONNECTION);
    }

}
