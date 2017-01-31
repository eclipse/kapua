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
package org.eclipse.kapua.service.authentication.credential;

import org.eclipse.kapua.model.KapuaObjectFactory;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.model.subject.SubjectType;

/**
 * {@link Credential} factory service definition.
 * 
 * @since 1.0.0
 * 
 */
public interface CredentialFactory extends KapuaObjectFactory {

    /**
     * Creates a new {@link Credential}.
     * 
     * @return A {@link Credential} instance.
     */
    public Credential newCredential();

    /**
     * Creates a new {@link CredentialCreator} instance with the given parameters.
     * 
     * @param scopeId
     *            The scope id to set.
     * @param subjectType
     *            The {@link SubjectType} to set.
     * @param subjectId
     *            The subject id to set.
     * @param type
     *            The {@link CredentialType} to set.
     * @param key
     *            The key to set.
     * @param secret
     *            The plain secret to set.
     * @return A new {@link CredentialCreator} instance.
     */
    public CredentialCreator newCreator(
            KapuaId scopeId,
            SubjectType subjectType,
            KapuaId subjectId,
            CredentialType type,
            String key,
            String secret);

    /**
     * Creates a new {@link CredentialListResult} instance.
     * 
     * @return A new {@link CredentialListResult} instance.
     * @since 1.0.0
     */
    public CredentialListResult newCredentialListResult();

    /**
     * Creates a new {@link CredentialQuery} for the specified scope identifier
     * 
     * @param scopeId
     *            The scope id in which query.
     * @return A new instance of {@link CredentialQuery}.
     * @since 1.0.0
     */
    public CredentialQuery newQuery(KapuaId scopeId);

}
