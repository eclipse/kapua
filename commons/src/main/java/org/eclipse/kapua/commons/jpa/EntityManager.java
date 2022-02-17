/*******************************************************************************
 * Copyright (c) 2016, 2022 Eurotech and/or its affiliates and others
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.commons.jpa;

import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.commons.model.id.KapuaEid;
import org.eclipse.kapua.model.KapuaEntity;
import org.eclipse.kapua.model.id.KapuaId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.LockModeType;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import java.io.Serializable;

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
     * Opens a Jpa Transaction.
     *
     * @throws KapuaException if {@link org.eclipse.kapua.commons.jpa.EntityManager} is {@code null}
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
     * Persist a {@link Serializable} entity;
     *
     * @param entity
     */
    public <E extends Serializable> void persist(E entity) {
        javaxPersitenceEntityManager.persist(entity);
    }

    /**
     * Persist a {@link KapuaEntity}
     *
     * @param entity
     * @since 1.0.0
     */
    public <E extends KapuaEntity> void persist(E entity) {
        persist((Serializable) entity);
    }

    /**
     * Flush the {@link EntityManager}
     *
     * @since 1.0.0}
     */
    public void flush() {
        javaxPersitenceEntityManager.flush();
    }

    /**
     * Finds a {@link KapuaEntity} by the given id and type
     *
     * @param clazz The {@link Serializable} entity type.
     * @param id    The id of the entity.
     * @return The found {@link Serializable} or {@code null} otherwise.
     * @since 1.2.0
     */
    public <E extends Serializable> E find(Class<E> clazz, Object id) {
        return javaxPersitenceEntityManager.find(clazz, id);
    }

    /**
     * Finds a {@link KapuaEntity} by the given id and type
     *
     * @param clazz The {@link KapuaEntity} type.
     * @param id    The {@link KapuaEntity#getId()}
     * @return The found {@link KapuaEntity} or {@code null} otherwise.
     * @since 1.0.0
     */
    public <E extends KapuaEntity> E find(Class<E> clazz, KapuaId id) {
        KapuaEid eid = KapuaEid.parseKapuaId(id);
        return javaxPersitenceEntityManager.find(clazz, eid);
    }

    /**
     * Find a {@link Serializable} by the given id and type
     *
     * @param clazz
     * @param id
     * @return
     * @since 1.0.0
     */
    public <E extends Serializable> E findWithLock(Class<E> clazz, Object id) {
        return javaxPersitenceEntityManager.find(clazz, id, LockModeType.PESSIMISTIC_WRITE);
    }

    /**
     * Merges a {@link Serializable} entity.
     *
     * @param entity The {@link Serializable} entity to merge.
     * @since 1.2.0.
     */
    public <E extends Serializable> void merge(E entity) {
        javaxPersitenceEntityManager.merge(entity);
    }

    /**
     * Merges a {@link KapuaEntity}
     *
     * @param entity The {@link KapuaEntity} to merge.
     * @since 1.0.0.
     */
    public <E extends KapuaEntity> void merge(E entity) {
        merge((Serializable) entity);
    }


    /**
     * Refresh a {@link Serializable} entity
     *
     * @param entity The {@link Serializable} entity to refresh.
     * @since 1.2.0
     */
    public <E extends Serializable> void refresh(E entity) {
        javaxPersitenceEntityManager.refresh(entity);
    }

    /**
     * Refresh a {@link KapuaEntity}
     *
     * @param entity The {@link KapuaEntity} to refresh.
     * @since 1.0.0
     */
    public <E extends KapuaEntity> void refresh(E entity) {
        refresh((Serializable) entity);
    }

    /**
     * Detach the entity
     *
     * @param entity
     */
    public <E extends KapuaEntity> void detach(E entity) {
        javaxPersitenceEntityManager.detach(entity);
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
