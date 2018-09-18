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
package org.eclipse.kapua.service.authorization.domain.shiro;

import org.eclipse.kapua.KapuaEntityNotFoundException;
import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.commons.jpa.EntityManager;
import org.eclipse.kapua.commons.service.internal.ServiceDAO;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.model.query.KapuaQuery;
import org.eclipse.kapua.service.authorization.domain.Domain;
import org.eclipse.kapua.service.authorization.domain.DomainCreator;
import org.eclipse.kapua.service.authorization.domain.DomainListResult;
import org.eclipse.kapua.service.authorization.domain.DomainQuery;

/**
 * {@link Domain} DAO
 *
 * @since 1.0.0
 */
public class DomainDAO extends ServiceDAO {

    /**
     * Creates and returns new {@link Domain}
     *
     * @param em      The {@link EntityManager} that holds the transaction.
     * @param creator The {@link DomainCreator} object from which create the new {@link Domain}.
     * @return The newly created {@link Domain}.
     * @throws KapuaException
     * @since 1.0.0
     */
    public static Domain create(EntityManager em, DomainCreator creator)
            throws KapuaException {
        Domain domain = new DomainImpl();

        domain.setName(creator.getName());
        domain.setGroupable(creator.getGroupable());

        if (creator.getActions() != null) {
            domain.setActions(creator.getActions());
        }

        return ServiceDAO.create(em, domain);
    }

    /**
     * Finds the {@link Domain} by {@link Domain} identifier
     *
     * @param em       The {@link EntityManager} that holds the transaction.
     * @param scopeId
     * @param domainId The {@link Domain} id to search.
     * @return The found {@link Domain} or {@code null} if not found.
     * @since 1.0.0
     */
    public static Domain find(EntityManager em, KapuaId scopeId, KapuaId domainId) {
        return ServiceDAO.find(em, DomainImpl.class, scopeId, domainId);
    }

    /**
     * Returns the {@link Domain} list matching the provided query.
     *
     * @param em          The {@link EntityManager} that holds the transaction.
     * @param domainQuery The {@link DomainQuery} used to filter results.
     * @return The list of {@link Domain}s that matches the given query.
     * @throws KapuaException
     * @since 1.0.0
     */
    public static DomainListResult query(EntityManager em, KapuaQuery<Domain> domainQuery)
            throws KapuaException {
        domainQuery.setScopeId(null);
        return ServiceDAO.query(em, Domain.class, DomainImpl.class, new DomainListResultImpl(), domainQuery);
    }

    /**
     * Return the {@link Domain} count matching the provided query
     *
     * @param em          The {@link EntityManager} that holds the transaction.
     * @param domainQuery The {@link DomainQuery} used to filter results.
     * @return
     * @throws KapuaException
     * @since 1.0.0
     */
    public static long count(EntityManager em, KapuaQuery<Domain> domainQuery)
            throws KapuaException {
        domainQuery.setScopeId(null);
        return ServiceDAO.count(em, Domain.class, DomainImpl.class, domainQuery);
    }

    /**
     * Deletes the {@link Domain} by {@link Domain} identifier
     *
     * @param em       The {@link EntityManager} that holds the transaction.
     * @param scopeId
     * @param domainId The {@link Domain} id to delete.
     * @throws KapuaEntityNotFoundException If {@link Domain} is not found.
     * @since 1.0.0
     */
    public static void delete(EntityManager em, KapuaId scopeId, KapuaId domainId) throws KapuaEntityNotFoundException {
        ServiceDAO.delete(em, DomainImpl.class, scopeId, domainId);
    }
}
