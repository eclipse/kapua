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
package org.eclipse.kapua.service.authorization.permission;

import org.eclipse.kapua.service.KapuaEntityService;
import org.eclipse.kapua.service.KapuaNamedEntityService;
import org.eclipse.kapua.service.KapuaUpdatableEntityService;

/**
 * {@link Action} implementation {@code enum}.<br>
 * Available actions defined are:
 * <ul>
 * <li>{@link Actions#read}: Represent any type of fetching data from the system.</li>
 * <li>{@link Actions#write}: Represent any type of writing data into the system.</li>
 * <li>{@link Actions#delete}: Represent any type of deleting data from the system.</li>
 * <li>{@link Actions#connect}: Represent any type of connect to resources of the system.</li>
 * <li>{@link Actions#execute}: Represent any type of execution of task in the system.</li>
 * </ul>
 * 
 * @since 1.0.0
 *
 */
public enum Actions implements Action {
    /**
     * Represent any type of fetching data from the system. <br>
     * Common usage is on:
     * <ul>
     * <li>{@link KapuaEntityService#find(org.eclipse.kapua.model.id.KapuaId, org.eclipse.kapua.model.id.KapuaId)}</li>
     * <li>{@link KapuaEntityService#query(org.eclipse.kapua.model.query.KapuaQuery)}</li>
     * <li>{@link KapuaEntityService#count(org.eclipse.kapua.model.query.KapuaQuery)}</li>
     * <li>{@link KapuaNamedEntityService#findByName(String)}</li>
     * </ul>
     * 
     * @since 1.0.0
     */
    read,

    /**
     * Represent any type of writing data into the system.
     * Common usage is on:
     * <ul>
     * <li>{@link KapuaEntityService#create(org.eclipse.kapua.model.KapuaEntityCreator)}</li>
     * <li>{@link KapuaUpdatableEntityService#update(org.eclipse.kapua.model.KapuaEntity)}</li>
     * </ul>
     * 
     * @since 1.0.0
     */
    write,

    /**
     * Represent any type of deleting data from the system. <br>
     * Common usage is on:
     * <ul>
     * <li>{@link KapuaEntityService#delete(org.eclipse.kapua.model.id.KapuaId, org.eclipse.kapua.model.id.KapuaId)}</li>
     * </ul>
     * 
     * @since 1.0.0
     */
    delete,

    /**
     * Represent any type of connect to resources of the system.<br>
     * Examples are:
     * <ul>
     * <li>Connecting to the message broker.</li>
     * </ul>
     * 
     * @since 1.0.0
     */
    connect,

    /**
     * Represent any type of execution of task in the system.
     * 
     * @since 1.0.0
     */
    execute,
    ;
}
