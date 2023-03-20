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
import org.eclipse.kapua.commons.setting.system.SystemSetting;
import org.eclipse.kapua.commons.setting.system.SystemSettingKey;
import org.eclipse.kapua.model.KapuaEntity;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.user.User;
import org.eclipse.kapua.service.user.UserRepository;
import org.eclipse.kapua.storage.TxManager;

import javax.inject.Inject;
import java.util.Optional;

public class RootUserTesterImpl implements RootUserTester {
    private final TxManager txManager;
    private final UserRepository userRepository;

    @Inject
    public RootUserTesterImpl(TxManager txManager, UserRepository userRepository) {
        this.txManager = txManager;
        this.userRepository = userRepository;
    }

    private KapuaId fetchRootUserId() throws KapuaException {
        //todo: remove me. This just converts root username to id - needs to be done elsewhere, preferrably in a once-at-startup way.
        final String rootUserName = SystemSetting.getInstance().getString(SystemSettingKey.SYS_ADMIN_USERNAME);
        final Optional<User> rootUser = txManager.execute(tx -> userRepository.findByName(tx, rootUserName));
        final KapuaId rootUserId = rootUser.map(KapuaEntity::getId).orElse(null);
        return rootUserId;
    }

    @Override
    public boolean isRoot(KapuaId userId) throws KapuaException {
        final KapuaId rootUserId = fetchRootUserId();
        return userId.equals(rootUserId);
    }
}
