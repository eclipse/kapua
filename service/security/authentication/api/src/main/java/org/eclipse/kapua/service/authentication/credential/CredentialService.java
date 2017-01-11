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

import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.KapuaEntityService;
import org.eclipse.kapua.service.KapuaUpdatableEntityService;
import org.eclipse.kapua.service.authorization.subject.Subject;
import org.eclipse.kapua.service.authorization.subject.SubjectType;

/**
 * {@link Credential} service definition.
 * 
 * @since 1.0.0
 */
public interface CredentialService extends KapuaEntityService<Credential, CredentialCreator>, KapuaUpdatableEntityService<Credential> {

    /**
     * Returns the {@link CredentialListResult} searching by the {@link SubjectType} and the subject {@link KapuaId}.
     * 
     * @param scopeId
     *            The scope id in which to search.
     * @param subject
     *            The {@link Subject} to filter results.
     * @param type
     *            The {@link CredentialType} to filter results. Optional.
     * 
     * @return The {@link CredentialListResult} matching the given parameters.
     * @throws KapuaException
     * 
     * @since 1.0.0
     */
    public CredentialListResult findBySubject(KapuaId scopeId, Subject subject, CredentialType type)
            throws KapuaException;

    /**
     * Returns the {@link Credential} found for the given parameters.
     * 
     * @param key
     *            The {@link Credential} key to match.
     * @param subjectType
     *            The {@link SubjectType} to match.
     * @param type
     *            The {@link CredentialType} to match.
     * @return The {@link Credential} found for the given parameters.
     * @throws KapuaException
     * @since 1.0.0
     */
    public Credential findByKey(String key, SubjectType subjectType, CredentialType type) throws KapuaException;
}
