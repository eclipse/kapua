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
package org.eclipse.kapua.app.console.module.user.shared.util;

import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.app.console.module.api.shared.util.KapuaGwtCommonsModelConverter;
import org.eclipse.kapua.app.console.module.user.shared.model.GwtUser;
import org.eclipse.kapua.service.user.User;

public class KapuaGwtUserModelConverter {

    private KapuaGwtUserModelConverter() { }

    /**
     * Converts a {@link User} into a {@link GwtUser} for GWT usage.
     *
     * @param user The {@link User} to convertAccessPermissionCreator.
     * @return The converted {@link GwtUser}
     * @since 1.0.0
     */
    public static GwtUser convertUser(User user) throws KapuaException {

        GwtUser gwtUser = new GwtUser();

        //
        // Convert commons attributes
        KapuaGwtCommonsModelConverter.convertUpdatableEntity(user, gwtUser);

        //
        // Convert other attributes
        gwtUser.setUsername(user.getName());
        gwtUser.setDisplayName(user.getDisplayName());
        gwtUser.setEmail(user.getEmail());
        gwtUser.setPhoneNumber(user.getPhoneNumber());
        gwtUser.setStatus(user.getStatus().toString());
        gwtUser.setExpirationDate(user.getExpirationDate());

        //
        // Return converted entity
        return gwtUser;
    }
}
