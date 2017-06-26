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
import org.eclipse.kapua.KapuaRuntimeException;
import org.eclipse.kapua.commons.core.ComponentProvider;
import org.eclipse.kapua.locator.inject.Service;
import org.eclipse.kapua.model.KapuaEntity;
import org.eclipse.kapua.model.id.KapuaId;

@ComponentProvider
@Service(provides = {EntityManager.class, TransactionScope.class})
public class TransactionScopeImpl implements EntityManager, TransactionScope {
    
    ThreadLocal<TransactionContext> txnContextThdLocal = new ThreadLocal<>();
    ThreadLocal<Integer> referenceCountThdLocal = new ThreadLocal<Integer>() {
        @Override
        protected Integer initialValue() {
            return 0;
        }
    };
    
    public TransactionScopeImpl() {    
    }
    
    @Override
    public void begin(EntityManagerFactory factory) throws KapuaException {
        if (txnContextThdLocal.get() == null) {
            EntityManager em = factory.createEntityManager();
            txnContextThdLocal.set(new TransactionContext(factory, em));
        }
        
        TransactionContext ctx = txnContextThdLocal.get();
        if (!ctx.getEntityManagerFactory().getClass().equals(factory.getClass())) {
            throw KapuaRuntimeException.internalError(
                    String.format("Detected a tentative to open a nested transaction using %s,"
                            + " nested distributed transactions are not allowed", factory.getClass().getName()));
        }
        
        referenceCountThdLocal.set(referenceCountThdLocal.get()+1);
    }
    
    @Override
    public EntityManager get() {
        TransactionContext ctx = txnContextThdLocal.get();
        return ctx.getEntityManager();
    }

    @Override
    public void end() {
        if (referenceCountThdLocal.get() > 0) {
            referenceCountThdLocal.set(referenceCountThdLocal.get()-1);
            if (referenceCountThdLocal.get() == 0) {
                if (txnContextThdLocal.get() != null) {
                    TransactionContext ctx = txnContextThdLocal.get();
                    ctx.getEntityManager().close();
                    txnContextThdLocal.set(null);
                }
            }
        }
    }

    @Override
    public void beginTransaction() throws KapuaException {
        TransactionContext ctx = txnContextThdLocal.get();
        ctx.getEntityManager().beginTransaction();
    }

    @Override
    public void commit() throws KapuaException {
        TransactionContext ctx = txnContextThdLocal.get();
        ctx.getEntityManager().commit();
    }

    @Override
    public void rollback() {
        TransactionContext ctx = txnContextThdLocal.get();
        ctx.getEntityManager().rollback();
    }

    @Override
    public boolean isTransactionActive() {
        TransactionContext ctx = txnContextThdLocal.get();
        return ctx.getEntityManager().isTransactionActive();
    }

    @Override
    public void close() {
        TransactionContext ctx = txnContextThdLocal.get();
        ctx.getEntityManager().close();
    }

    @Override
    public <E extends KapuaEntity> void persist(E entity) {
        TransactionContext ctx = txnContextThdLocal.get();
        ctx.getEntityManager().persist(entity);
    }

    @Override
    public void flush() {
        TransactionContext ctx = txnContextThdLocal.get();
        ctx.getEntityManager().flush();
    }

    @Override
    public <E extends KapuaEntity> E find(Class<E> clazz, KapuaId id) {
        TransactionContext ctx = txnContextThdLocal.get();
        return ctx.getEntityManager().find(clazz, id);
    }

    @Override
    public <E extends KapuaEntity> void merge(E entity) {
        TransactionContext ctx = txnContextThdLocal.get();
        ctx.getEntityManager().merge(entity);
    }

    @Override
    public <E extends KapuaEntity> void refresh(E entity) {
        TransactionContext ctx = txnContextThdLocal.get();
        ctx.getEntityManager().refresh(entity);
    }

    @Override
    public <E extends KapuaEntity> void remove(E entity) {
        TransactionContext ctx = txnContextThdLocal.get();
        ctx.getEntityManager().remove(entity);
    }

    @Override
    public CriteriaBuilder getCriteriaBuilder() {
        TransactionContext ctx = txnContextThdLocal.get();
        return ctx.getEntityManager().getCriteriaBuilder();
    }

    @Override
    public <E> TypedQuery<E> createQuery(CriteriaQuery<E> criteriaSelectQuery) {
        TransactionContext ctx = txnContextThdLocal.get();
        return ctx.getEntityManager().createQuery(criteriaSelectQuery);
    }

    @Override
    public <E> TypedQuery<E> createNamedQuery(String queryName, Class<E> clazz) {
        TransactionContext ctx = txnContextThdLocal.get();
        return ctx.getEntityManager().createNamedQuery(queryName, clazz);
    }

    @Override
    public <E> Query createNativeQuery(String querySelectUuidShort) {
        TransactionContext ctx = txnContextThdLocal.get();
        return ctx.getEntityManager().createNativeQuery(querySelectUuidShort);
    }
}
