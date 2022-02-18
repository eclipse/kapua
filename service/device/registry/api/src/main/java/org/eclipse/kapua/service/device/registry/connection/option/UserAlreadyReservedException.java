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
package org.eclipse.kapua.service.device.registry.connection.option;

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
