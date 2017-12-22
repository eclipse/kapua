/*******************************************************************************
 * Copyright (c) 2011, 2017 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.service.authorization.domain;

import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.model.query.KapuaQuery;
import org.eclipse.kapua.service.KapuaEntityService;

/**
 * {@link Domain} service definition.
 *
 * @since 1.0.0
 */
public interface DomainService extends KapuaEntityService<Domain, DomainCreator> {

    /**
     * Creates a new {@link Domain} based on the parameters provided in the {@link DomainCreator}.<br>
     * {@link Domain} must have a unique name.
     *
     * @param domainCreator The creator object from which to create the {@link Domain}.
     * @return The created {@link Domain}
     * @throws KapuaException When there is an oeror22
     * @since 1.0.0
     */
    @Override
    public Domain create(DomainCreator domainCreator)
            throws KapuaException;

    /**
     * Finds the {@link Domain} by scope identifier and {@link Domain} id.
     *
     * @param scopeId  The scope id in which to search.
     * @param domainId The {@link Domain} id to search.
     * @return The {@link Domain} found or {@code null} if no entity was found.
     * @throws KapuaException
     * @since 1.0.0
     */
    @Override
    public Domain find(KapuaId scopeId, KapuaId domainId)
            throws KapuaException;

    /**
     * Finds the {@link Domain} by the service name.
     *
     * @param servicename The service name to search.
     * @return The {@link Domain} found or {@code null} if no entity was found.
     * @throws KapuaException
     * @since 1.0.0
     */
    public Domain findByServiceName(String servicename)
            throws KapuaException;

    /**
     * Returns the {@link DomainListResult} with elements matching the provided query.
     *
     * @param query The {@link DomainQuery} used to filter results.
     * @return The {@link DomainListResult} with elements matching the query parameter.
     * @throws KapuaException
     * @since 1.0.0
     */
    @Override
    public DomainListResult query(KapuaQuery<Domain> query)
            throws KapuaException;

    /**
     * Returns the count of the {@link Domain} elements matching the provided query.
     *
     * @param query The {@link DomainQuery} used to filter results.
     * @return The count of the {@link Domain} elements matching the provided query.
     * @throws KapuaException
     * @since 1.0.0
     */
    @Override
    public long count(KapuaQuery<Domain> query)
            throws KapuaException;

    /**
     * Delete the {@link Domain} by scope id and {@link Domain} id.
     *
     * @param scopeId The scope id in which to delete.
     * @param roleId  The {@link Domain} id to delete.
     * @throws KapuaException
     * @since 1.0.0
     */
    @Override
    public void delete(KapuaId scopeId, KapuaId roleId)
            throws KapuaException;

}
