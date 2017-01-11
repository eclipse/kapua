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
 *
 *******************************************************************************/
package org.eclipse.kapua.service.authentication.token;

import java.util.Date;

import org.eclipse.kapua.model.KapuaObjectFactory;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.authentication.credential.Credential;
import org.eclipse.kapua.service.authorization.subject.Subject;
import org.eclipse.kapua.service.authorization.subject.SubjectType;

/**
 * {@link AccessToken} factory definition.
 * 
 * @since 1.0
 * 
 */
public interface AccessTokenFactory extends KapuaObjectFactory {

    /**
     * Create a new {@link AccessTokenCreator} for the specific access credential type
     * 
     * @param scopeId
     *            The scopeId of the new {@link AccessToken}.
     * @param subjectType
     *            The {@link SubjectType} to set.
     * @param subjectId
     *            The {@link Subject} id to set.
     * @param credentialId
     *            The {@link Credential} id to set.
     * @param tokenId
     *            The tokenId of the new {@link AccessToken}.
     * @param expiresOn
     *            The expiration date after which the token is no longer valid.
     * 
     * @return A new instance of {@link AccessToken}.
     * 
     * @since 1.0.0
     */
    public AccessTokenCreator newCreator(
            KapuaId scopeId,
            SubjectType subjectYpe,
            KapuaId subjectId,
            KapuaId credentialId,
            String tokenId,
            Date expiresOn);

}
