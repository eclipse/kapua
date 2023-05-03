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
 *     Eurotech - initial implementation
 *******************************************************************************/
package org.eclipse.kapua.commons.configuration;

import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.commons.security.KapuaSecurityUtils;
import org.eclipse.kapua.commons.setting.system.SystemSetting;
import org.eclipse.kapua.commons.setting.system.SystemSettingKey;
import org.eclipse.kapua.model.KapuaEntity;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.user.User;
import org.eclipse.kapua.service.user.UserService;

import javax.inject.Inject;
import java.util.Optional;

public class RootUserTesterImpl implements RootUserTester {
    private final UserService userService;
    private Optional<KapuaId> rootUserId = Optional.empty();

    @Inject
    public RootUserTesterImpl(UserService userService) {
        this.userService = userService;
    }

    private Optional<KapuaId> fetchRootUserId() throws KapuaException {
        //todo: remove me. This just converts root username to id - needs to be done elsewhere, preferrably in a once-at-startup way.
        final String rootUserName = SystemSetting.getInstance().getString(SystemSettingKey.SYS_ADMIN_USERNAME);
        final User rootUser = KapuaSecurityUtils.doPrivileged(() -> userService.findByName(rootUserName));
        return Optional.ofNullable(rootUser).map(KapuaEntity::getId);
    }

    @Override
    public boolean isRoot(KapuaId userId) throws KapuaException {
        if (!rootUserId.isPresent()) {
            this.rootUserId = fetchRootUserId();
        }
        return rootUserId.map(id -> id.equals(userId)).orElse(false);
    }
}
