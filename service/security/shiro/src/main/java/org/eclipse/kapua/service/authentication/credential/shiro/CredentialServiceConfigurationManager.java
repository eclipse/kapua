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
package org.eclipse.kapua.service.authentication.credential.shiro;

import org.eclipse.kapua.commons.configuration.RootUserTester;
import org.eclipse.kapua.commons.configuration.ServiceConfigurationManagerBase;
import org.eclipse.kapua.commons.jpa.EntityManagerSession;
import org.eclipse.kapua.service.authentication.AuthenticationDomains;
import org.eclipse.kapua.service.authentication.credential.CredentialService;
import org.eclipse.kapua.service.authentication.shiro.AuthenticationEntityManagerFactory;
import org.eclipse.kapua.service.authorization.AuthorizationService;
import org.eclipse.kapua.service.authorization.permission.PermissionFactory;

import javax.inject.Inject;

public class CredentialServiceConfigurationManager extends ServiceConfigurationManagerBase {

    @Inject
    public CredentialServiceConfigurationManager(AuthenticationEntityManagerFactory authenticationEntityManagerFactory,
                                                 PermissionFactory permissionFactory,
                                                 AuthorizationService authorizationService,
                                                 RootUserTester rootUserTester) {
        super(CredentialService.class.getName(),
                AuthenticationDomains.CREDENTIAL_DOMAIN,
                new EntityManagerSession(authenticationEntityManagerFactory),
                permissionFactory,
                authorizationService,
                rootUserTester);
    }
}
