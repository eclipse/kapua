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
package org.eclipse.kapua.service.authorization.domain;

import org.eclipse.kapua.model.KapuaObjectFactory;
import org.eclipse.kapua.service.KapuaService;

/**
 * {@link Domain} object factory.
 * 
 * @since 1.0.0
 *
 */
public interface DomainFactory extends KapuaObjectFactory {

    /**
     * Instantiate a new {@link DomainCreator} implementing object with the provided parameters.
     * 
     * @param name
     *            The {@link Domain} name to set.
     * @param serviceName
     *            The {@link KapuaService} the uses this {@link Domain}.
     * @return A instance of the implementing class of {@link Domain}.
     * @since 1.0.0
     */
    public DomainCreator newCreator(String name, String serviceName);

    /**
     * Instantiate a new {@link DomainQuery} implementing object.
     * 
     * @return A instance of the implementing class of {@link DomainQuery}.
     * @since 1.0.0
     */
    public DomainQuery newQuery();
}
