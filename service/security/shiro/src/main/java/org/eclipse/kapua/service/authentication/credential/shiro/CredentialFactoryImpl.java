/*******************************************************************************
 * Copyright (c) 2011, 2016 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.service.authentication.credential.shiro;

import org.eclipse.kapua.locator.KapuaProvider;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.authentication.credential.Credential;
import org.eclipse.kapua.service.authentication.credential.CredentialCreator;
import org.eclipse.kapua.service.authentication.credential.CredentialFactory;
import org.eclipse.kapua.service.authentication.credential.CredentialListResult;
import org.eclipse.kapua.service.authentication.credential.CredentialQuery;
import org.eclipse.kapua.service.authentication.credential.CredentialStatus;
import org.eclipse.kapua.service.authentication.credential.CredentialType;

import java.util.Date;

/**
 * Credential factory service implementation.
 * 
 * @since 1.0
 * 
 */
@KapuaProvider
public class CredentialFactoryImpl implements CredentialFactory {

    @Override
    public CredentialCreatorImpl newCreator(KapuaId scopeId, KapuaId userId, CredentialType credentialType, String credentialKey, CredentialStatus credentialStatus, Date expirationDate) {
        return new CredentialCreatorImpl(scopeId, userId, credentialType, credentialKey, credentialStatus, expirationDate);
    }

    @Override
    public CredentialListResult newListResult() {
        return new CredentialListResultImpl();
    }

    @Override
    public Credential newEntity(KapuaId scopeId) {
        return new CredentialImpl(scopeId);
    }

    @Override
    public Credential newCredential(KapuaId scopeId, KapuaId userId, CredentialType credentialType, String credentialKey, CredentialStatus credentialStatus, Date expirationDate) {
        return new CredentialImpl(scopeId, userId, credentialType, credentialKey, credentialStatus, expirationDate);
    }

    @Override
    public CredentialQuery newQuery(KapuaId scopeId) {
        return new CredentialQueryImpl(scopeId);
    }

    @Override
    public CredentialCreator newCreator(KapuaId scopeId) {
        return new CredentialCreatorImpl(scopeId);
    }
}
