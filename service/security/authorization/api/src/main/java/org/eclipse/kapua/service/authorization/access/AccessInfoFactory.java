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
package org.eclipse.kapua.service.authorization.access;

import org.eclipse.kapua.model.KapuaObjectFactory;
import org.eclipse.kapua.model.id.KapuaId;

/**
 * Access info factory
 * 
 * @since 1.0
 *
 */
public interface AccessInfoFactory extends KapuaObjectFactory {

    /**
     * Creates a new user permission creator for the provided scope identifier
     * 
     * @param scopeId
     * @return
     */
    public AccessInfoCreator newCreator(KapuaId scopeId);

    /**
     * Creates a new user permission query for the provided scope identifier
     * 
     * @param scopeId
     * @return
     */
    public AccessInfoQuery newQuery(KapuaId scopeId);

}
