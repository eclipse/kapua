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

import java.io.Serializable;

import javax.persistence.LockModeType;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;

import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.commons.model.id.KapuaEid;
import org.eclipse.kapua.model.KapuaEntity;
import org.eclipse.kapua.model.id.KapuaId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Kapua JPA entity manager wrapper
 * 
 * @since 1.0
 */
public class EntityManager {

    private static final Logger LOG = LoggerFactory.getLogger(AbstractEntityManagerFactory.class);

    private javax.persistence.EntityManager javaxPersitenceEntityManager;

    /**
     * Constructs a new entity manager wrapping the given {@link javax.persistence.EntityManager}
     * 
     * @param javaxPersitenceEntityManager
     */
    public EntityManager(javax.persistence.EntityManager javaxPersitenceEntityManager) {
        this.javaxPersitenceEntityManager = javaxPersitenceEntityManager;
    }

    /**
     * Find the entity by the given id and type
     * 
     * @param clazz
     * @param id
     * @return
     */
    public <E extends Serializable> E findWithLock(Class<E> clazz, Object id) {
        return javaxPersitenceEntityManager.find(clazz, id, LockModeType.PESSIMISTIC_WRITE);
    }

    /**
     * Persist the entity
     * 
     * @param entity
     */
    public <E extends Serializable> void persist(E entity) {
        javaxPersitenceEntityManager.persist(entity);
    }

    /**
     * Opens a Jpa Transaction.
     * 
     * @throws KapuaException
     *             if {@link org.eclipse.kapua.commons.jpa.EntityManager} is {@code null}
     */
    public void beginTransaction()
            throws KapuaException {
        if (javaxPersitenceEntityManager == null) {
            throw KapuaException.internalError(new NullPointerException(), "null EntityManager");
        }
        javaxPersitenceEntityManager.getTransaction().begin();
    }

    /**
     * Commits the current Jpa Transaction.
     * 
     * @throws KapuaException
     */
    public void commit()
            throws KapuaException {
        if (javaxPersitenceEntityManager == null) {
            throw KapuaException.internalError("null EntityManager");
        }
        if (!javaxPersitenceEntityManager.getTransaction().isActive()) {
            throw KapuaException.internalError("Transaction Not Active");
        }

        try {
            javaxPersitenceEntityManager.getTransaction().commit();
        } catch (Exception e) {
            throw KapuaException.internalError(e, "Commit Error");
        }
    }

    /**
     * Rollbacks the current Jpa Transaction. No exception will be thrown when rolling back so that the original exception that caused the rollback can be thrown.
     */
    public void rollback() {
        try {
            if (javaxPersitenceEntityManager != null &&
                    javaxPersitenceEntityManager.getTransaction().isActive()) {
                javaxPersitenceEntityManager.getTransaction().rollback();
            }
        } catch (Exception e) {
            LOG.warn("Rollback Error", e);
        }
    }

    /**
     * Return the transaction status
     * 
     * @return
     */
    public boolean isTransactionActive() {
        return (javaxPersitenceEntityManager != null &&
                javaxPersitenceEntityManager.getTransaction().isActive());
    }

    /**
     * Closes the EntityManager
     */
    public void close() {
        if (javaxPersitenceEntityManager != null) {
            javaxPersitenceEntityManager.close();
        }
    }

    /**
     * Persist the entity
     * 
     * @param entity
     */
    public <E extends KapuaEntity> void persist(E entity) {
        javaxPersitenceEntityManager.persist(entity);
    }

    /**
     * Flush the entity manager
     */
    public void flush() {
        javaxPersitenceEntityManager.flush();
    }

    /**
     * Find the entity by the given id and type
     * 
     * @param clazz
     * @param id
     * @return
     */
    public <E extends KapuaEntity> E find(Class<E> clazz, KapuaId id) {
        KapuaEid eid = KapuaEid.parseKapuaId(id);
        return javaxPersitenceEntityManager.find(clazz, eid);
    }

    /**
     * Merge the entity
     * 
     * @param entity
     */
    public <E extends KapuaEntity> void merge(E entity) {
        javaxPersitenceEntityManager.merge(entity);
    }

    /**
     * Refresh the entity
     * 
     * @param entity
     */
    public <E extends KapuaEntity> void refresh(E entity) {
        javaxPersitenceEntityManager.refresh(entity);
    }

    /**
     * Remove the entity
     * 
     * @param entity
     */
    public <E extends KapuaEntity> void remove(E entity) {
        javaxPersitenceEntityManager.remove(entity);
    }

    /**
     * Return the {@link javax.persistence.criteria.CriteriaBuilder}
     * 
     * @return
     */
    public CriteriaBuilder getCriteriaBuilder() {
        return javaxPersitenceEntityManager.getCriteriaBuilder();
    }

    /**
     * Return the typed query based on the criteria
     * 
     * @param criteriaSelectQuery
     * @return
     */
    public <E> TypedQuery<E> createQuery(CriteriaQuery<E> criteriaSelectQuery) {
        return javaxPersitenceEntityManager.createQuery(criteriaSelectQuery);
    }

    /**
     * Return the typed query based on the query name
     * 
     * @param queryName
     * @param clazz
     * @return
     */
    public <E> TypedQuery<E> createNamedQuery(String queryName, Class<E> clazz) {
        return javaxPersitenceEntityManager.createNamedQuery(queryName, clazz);
    }

    /**
     * Return native query based on provided sql query
     * 
     * @param querySelectUuidShort
     * @return
     */
    public <E> Query createNativeQuery(String querySelectUuidShort) {
        return javaxPersitenceEntityManager.createNativeQuery(querySelectUuidShort);
    }
}
