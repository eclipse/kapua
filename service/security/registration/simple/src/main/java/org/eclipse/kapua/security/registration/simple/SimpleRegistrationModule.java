/*******************************************************************************
 * Copyright (c) 2017, 2022 Red Hat Inc and others.
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Red Hat Inc - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.security.registration.simple;

import com.google.inject.multibindings.ProvidesIntoSet;
import org.eclipse.kapua.commons.core.AbstractKapuaModule;
import org.eclipse.kapua.commons.liquibase.DatabaseCheckUpdate;
import org.eclipse.kapua.security.registration.RegistrationProcessorProvider;
import org.eclipse.kapua.security.registration.simple.setting.SimpleSetting;
import org.eclipse.kapua.service.account.AccountFactory;
import org.eclipse.kapua.service.account.AccountService;
import org.eclipse.kapua.service.authentication.credential.CredentialFactory;
import org.eclipse.kapua.service.authentication.credential.CredentialService;
import org.eclipse.kapua.service.authorization.access.AccessInfoFactory;
import org.eclipse.kapua.service.authorization.access.AccessInfoService;
import org.eclipse.kapua.service.authorization.permission.PermissionFactory;
import org.eclipse.kapua.service.device.registry.DeviceRegistryService;
import org.eclipse.kapua.service.user.UserFactory;
import org.eclipse.kapua.service.user.UserService;

import javax.inject.Singleton;

public class SimpleRegistrationModule extends AbstractKapuaModule {
    @Override
    protected void configureModule() {
        bind(SimpleSetting.class).in(Singleton.class);
    }

    @ProvidesIntoSet
    @Singleton
    RegistrationProcessorProvider simpleRegistrationProcessorProvider(
            SimpleSetting simpleSetting,
            AccountService accountService,
            AccountFactory accountFactory,
            CredentialService credentialService,
            CredentialFactory credentialFactory,
            DeviceRegistryService deviceRegistryService,
            UserService userService,
            UserFactory userFactory,
            AccessInfoService accessInfoService,
            AccessInfoFactory accessInfoFactory,
            PermissionFactory permissionFactory,
            //Liquibase must start before this
            DatabaseCheckUpdate databaseCheckUpdate) {
        return new SimpleRegistrationProcessorProvider(simpleSetting,
                accountService,
                accountFactory,
                credentialService,
                credentialFactory,
                deviceRegistryService,
                userService,
                userFactory,
                accessInfoService,
                accessInfoFactory,
                permissionFactory);
    }
}
