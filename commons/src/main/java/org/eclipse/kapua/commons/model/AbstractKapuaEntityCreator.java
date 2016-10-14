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
package org.eclipse.kapua.commons.model;

import java.io.Serializable;

import org.eclipse.kapua.commons.model.id.KapuaEid;
import org.eclipse.kapua.model.KapuaEntity;
import org.eclipse.kapua.model.KapuaEntityCreator;
import org.eclipse.kapua.model.id.KapuaId;

@SuppressWarnings("serial")
public abstract class AbstractKapuaEntityCreator<E extends KapuaEntity> implements KapuaEntityCreator<E>, Serializable
{
    protected KapuaId scopeId;

    protected AbstractKapuaEntityCreator(KapuaId scopeId)
    {
        this.scopeId = scopeId;
    }

    public KapuaId getScopeId()
    {
        return scopeId;
    }

    public void setScopeId(KapuaId scopeId)
    {
        this.scopeId = (KapuaEid) scopeId;
    }
}
