/*******************************************************************************
 * Copyright (c) 2011, 2017 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *
 *******************************************************************************/
package org.eclipse.kapua.service.authentication.token.shiro;

import java.util.Date;

import org.eclipse.kapua.locator.KapuaProvider;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.model.subject.SubjectType;
import org.eclipse.kapua.service.authentication.token.AccessTokenFactory;

/**
 * {@link AccessTokenFactory} implementation.
 * 
 * @since 1.0.0
 * 
 */
@KapuaProvider
public class AccessTokenFactoryImpl implements AccessTokenFactory {

    @Override
    public AccessTokenCreatorImpl newCreator(KapuaId scopeId, SubjectType subjectType, KapuaId subjectId, KapuaId credentialId, String tokenId, Date expiresOn) {
        AccessTokenCreatorImpl accessTokenCreator = new AccessTokenCreatorImpl(scopeId);
        accessTokenCreator.setSubjectType(subjectType);
        accessTokenCreator.setSubjectId(subjectId);
        accessTokenCreator.setCredentialId(credentialId);
        accessTokenCreator.setTokenId(tokenId);
        accessTokenCreator.setExpiresOn(expiresOn);

        return accessTokenCreator;
    }
}
