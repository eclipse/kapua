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
package org.eclipse.kapua.commons.jpa;

import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;

import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.model.KapuaEntity;
import org.eclipse.kapua.model.id.KapuaId;

/**
 * Kapua JPA entity manager wrapper
 * 
 * @since 1.0
 */
public interface EntityManager {

    /**
     * Opens a Jpa Transaction.
     * 
     * @throws KapuaException
     *             if {@link org.eclipse.kapua.commons.jpa.EntityManager} is {@code null}
     */
    public void beginTransaction()
            throws KapuaException;

    /**
     * Commits the current Jpa Transaction.
     * 
     * @throws KapuaException
     */
    public void commit()
            throws KapuaException;

    /**
     * Rollbacks the current Jpa Transaction. No exception will be thrown when rolling back so that the original exception that caused the rollback can be thrown.
     */
    public void rollback();

    /**
     * Return the transaction status
     * 
     * @return
     */
    public boolean isTransactionActive();

    /**
     * Closes the EntityManager
     */
    public void close();

    /**
     * Persist the entity
     * 
     * @param entity
     */
    public <E extends KapuaEntity> void persist(E entity);

    /**
     * Flush the entity manager
     */
    public void flush();

    /**
     * Find the entity by the given id and type
     * 
     * @param clazz
     * @param id
     * @return
     */
    public <E extends KapuaEntity> E find(Class<E> clazz, KapuaId id);

    /**
     * Merge the entity
     * 
     * @param entity
     */
    public <E extends KapuaEntity> void merge(E entity);

    /**
     * Refresh the entity
     * 
     * @param entity
     */
    public <E extends KapuaEntity> void refresh(E entity);

    /**
     * Remove the entity
     * 
     * @param entity
     */
    public <E extends KapuaEntity> void remove(E entity);

    /**
     * Return the {@link javax.persistence.criteria.CriteriaBuilder}
     * 
     * @return
     */
    public CriteriaBuilder getCriteriaBuilder();

    /**
     * Return the typed query based on the criteria
     * 
     * @param criteriaSelectQuery
     * @return
     */
    public <E> TypedQuery<E> createQuery(CriteriaQuery<E> criteriaSelectQuery);

    /**
     * Return the typed query based on the query name
     * 
     * @param queryName
     * @param clazz
     * @return
     */
    public <E> TypedQuery<E> createNamedQuery(String queryName, Class<E> clazz);

    /**
     * Return native query based on provided sql query
     * 
     * @param querySelectUuidShort
     * @return
     */
    public <E> Query createNativeQuery(String querySelectUuidShort);
}