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
package org.eclipse.kapua.service;

import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.model.KapuaEntity;

/**
 * Common interface for all KapuaService that are managing identifiable entities.
 *
 * @param <E> - Class of the KapuaEntity being managed by this Service
 * @since 1.0
 */
public interface KapuaNamedEntityService<E extends KapuaEntity> extends KapuaService {

    /**
     * Find the entity by name
     *
     * @param name
     * @return
     * @throws KapuaException
     */
    E findByName(String name) throws KapuaException;
}
