/*******************************************************************************
 * Copyright (c) 2021, 2022 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.service.endpoint.internal;

import com.google.inject.Provides;
import com.google.inject.Singleton;
import org.eclipse.kapua.commons.core.AbstractKapuaModule;
import org.eclipse.kapua.commons.jpa.JpaTxManager;
import org.eclipse.kapua.commons.jpa.KapuaEntityManagerFactory;
import org.eclipse.kapua.commons.jpa.KapuaJpaRepositoryConfiguration;
import org.eclipse.kapua.service.account.AccountRepository;
import org.eclipse.kapua.service.authorization.AuthorizationService;
import org.eclipse.kapua.service.authorization.permission.PermissionFactory;
import org.eclipse.kapua.service.endpoint.EndpointInfoFactory;
import org.eclipse.kapua.service.endpoint.EndpointInfoRepository;
import org.eclipse.kapua.service.endpoint.EndpointInfoService;

public class EndpointModule extends AbstractKapuaModule {
    @Override
    protected void configureModule() {
        bind(EndpointInfoFactory.class).to(EndpointInfoFactoryImpl.class);
    }

    @Provides
    @Singleton
    EndpointInfoService endpointInfoService(
            AuthorizationService authorizationService,
            PermissionFactory permissionFactory,
            EndpointInfoFactory endpointInfoFactory,
            AccountRepository accountRepository,
            EndpointInfoRepository endpointInfoRepository) {
        return new EndpointInfoServiceImpl(
                authorizationService,
                permissionFactory,
                endpointInfoFactory,
                new JpaTxManager(new KapuaEntityManagerFactory("kapua-endpoint")),
                accountRepository,
                endpointInfoRepository);
    }

    @Provides
    @Singleton
    EndpointInfoRepository endpointInfoRepository(KapuaJpaRepositoryConfiguration jpaRepoConfig) {
        return new EndpointInfoImplJpaRepository(jpaRepoConfig);
    }
}
