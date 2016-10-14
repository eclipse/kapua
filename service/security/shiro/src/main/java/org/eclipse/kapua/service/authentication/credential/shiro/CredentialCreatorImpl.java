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

import javax.xml.bind.annotation.XmlElement;

import org.eclipse.kapua.commons.model.AbstractKapuaEntityCreator;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.authentication.credential.Credential;
import org.eclipse.kapua.service.authentication.credential.CredentialCreator;
import org.eclipse.kapua.service.authentication.credential.CredentialType;

/**
 * Credential creator service implementation.
 * 
 * @since 1.0
 *
 */
public class CredentialCreatorImpl extends AbstractKapuaEntityCreator<Credential> implements CredentialCreator
{
    private static final long serialVersionUID = -5020680413729882095L;

    @XmlElement(name = "userId")
    private KapuaId           userId;

    @XmlElement(name = "credentialType")
    private CredentialType    credentialType;

    @XmlElement(name = "credentialKey")
    private String            credentialKey;

    /**
     * Constructor
     * 
     * @param scopeId scope identifier
     * @param userId user identifier
     * @param credentialType credential type (see {@link CredentialType} for the allowed values)
     * @param credentialKey credential key
     */
    public CredentialCreatorImpl(KapuaId scopeId, KapuaId userId, CredentialType credentialType, String credentialKey)
    {
        super(scopeId);

        this.userId = userId;
        this.credentialType = credentialType;
        this.credentialKey = credentialKey;
    }

    @Override
    public KapuaId getUserId()
    {
        return userId;
    }

    @Override
    public CredentialType getCredentialType()
    {
        return credentialType;
    }

    @Override
    public String getCredentialPlainKey()
    {
        return credentialKey;
    }

}
