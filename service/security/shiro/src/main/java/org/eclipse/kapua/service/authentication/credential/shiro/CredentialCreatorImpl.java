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
package org.eclipse.kapua.service.authentication.credential.shiro;

import org.eclipse.kapua.commons.model.AbstractKapuaEntityCreator;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.model.subject.SubjectType;
import org.eclipse.kapua.service.authentication.credential.Credential;
import org.eclipse.kapua.service.authentication.credential.CredentialCreator;
import org.eclipse.kapua.service.authentication.credential.CredentialType;

/**
 * {@link CredentialCreator} implementation.
 * 
 * @since 1.0.0
 */
public class CredentialCreatorImpl extends AbstractKapuaEntityCreator<Credential> implements CredentialCreator {

    private static final long serialVersionUID = -5020680413729882095L;

    private SubjectType subjectType;
    private KapuaId subjectId;
    private CredentialType type;
    private String key;
    private String plainSecret;

    /**
     * Constructor.
     * 
     * @param scopeId
     *            The scope id in which create the {@link Credential}.
     * @param subjectType
     *            The {@link SubjectType} for which create the {@link Credential}.
     * @param subjectId
     *            The subject id for which create the {@link Credential}.
     * @param type
     *            The {@link CredentialType} of the new {@link Credential}.
     * @param key
     *            The key of the {@link Credential}.
     * @param plainSecret
     *            The plain secret of the {@link Credential}.
     * @since 1.0.0
     */
    public CredentialCreatorImpl(KapuaId scopeId, //
            SubjectType subjectType,
            KapuaId subjectId,//
            CredentialType type, //
            String key,
            String plainSecret) //
    {
        super(scopeId);

        setSubjectType(subjectType);
        setSubjectId(subjectId);
        setType(type);
        setKey(key);
        setPlainSecret(plainSecret);
    }

    @Override
    public SubjectType getSubjectType() {
        return subjectType;
    }

    @Override
    public void setSubjectType(SubjectType subjectType) {
        this.subjectType = subjectType;
    }

    @Override
    public KapuaId getSubjectId() {
        return subjectId;
    }

    @Override
    public void setSubjectId(KapuaId subjectId) {
        this.subjectId = subjectId;
    }

    @Override
    public CredentialType getType() {
        return type;
    }

    @Override
    public void setType(CredentialType type) {
        this.type = type;
    }

    @Override
    public String getKey() {
        return key;
    }

    @Override
    public void setKey(String key) {
        this.key = key;
    }

    @Override
    public String getPlainSecret() {
        return plainSecret;
    }

    @Override
    public void setPlainSecret(String plainSecret) {
        this.plainSecret = plainSecret;
    }
}
