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
 * {@link AccessInfo} object factory.
 * 
 * @since 1.0.0
 */
public interface AccessInfoFactory extends KapuaObjectFactory {

    /**
     * Instantiate a new {@link AccessInfoCreator} implementing object with the provided scope id.
     * 
     * @param scopeId
     *            The scope id to set.
     * @return A instance of the implementing class of {@link AccessInfoCreator}.
     * @since 1.0.0
     */
    public AccessInfoCreator newCreator(KapuaId scopeId);

    /**
     * Instantiate a new {@link AccessInfoQuery} implementing object with the provided scope id.
     * 
     * @param scopeId
     *            The scope id to set.
     * @return A instance of the implementing class of {@link AccessInfoCreator}.
     * @since 1.0.0
     */
    public AccessInfoQuery newQuery(KapuaId scopeId);

}
