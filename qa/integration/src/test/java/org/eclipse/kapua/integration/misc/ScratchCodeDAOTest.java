/*******************************************************************************
 * Copyright (c) 2021, 2022 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.integration.misc;

import org.eclipse.kapua.KapuaEntityExistsException;
import org.eclipse.kapua.KapuaEntityNotFoundException;
import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.commons.jpa.EntityManager;
import org.eclipse.kapua.commons.model.id.KapuaEid;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.model.query.KapuaQuery;
import org.eclipse.kapua.qa.markers.junit.JUnitTests;
import org.eclipse.kapua.service.authentication.credential.mfa.ScratchCode;
import org.eclipse.kapua.service.authentication.credential.mfa.ScratchCodeCreator;
import org.eclipse.kapua.service.authentication.credential.mfa.ScratchCodeListResult;
import org.eclipse.kapua.service.authentication.credential.mfa.shiro.ScratchCodeDAO;
import org.eclipse.kapua.service.authentication.credential.mfa.shiro.ScratchCodeImpl;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.mockito.Mockito;

import javax.persistence.EntityExistsException;
import javax.persistence.PersistenceException;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Selection;
import javax.persistence.metamodel.EntityType;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@Category(JUnitTests.class)
public class ScratchCodeDAOTest {

    EntityManager entityManager;
    ScratchCodeCreator scratchCodeCreator;
    ScratchCodeImpl code, entityToFindOrDelete;
    Date createdOn;
    KapuaId createdBy, scopeId, scratchCodeId;
    KapuaEid mfaOptionId;
    KapuaQuery kapuaQuery;

    @Before
    public void initialize() {
        entityManager = Mockito.mock(EntityManager.class);
        scratchCodeCreator = Mockito.mock(ScratchCodeCreator.class);
        code = Mockito.mock(ScratchCodeImpl.class);
        entityToFindOrDelete = Mockito.mock(ScratchCodeImpl.class);
        createdOn = new Date();
        createdBy = KapuaId.ONE;
        scopeId = KapuaId.ONE;
        mfaOptionId = new KapuaEid(KapuaId.ONE);
        scratchCodeId = KapuaId.ONE;
        kapuaQuery = Mockito.mock(KapuaQuery.class);
    }

    @Test
    public void createTest() throws KapuaException {
        Mockito.when(scratchCodeCreator.getCode()).thenReturn("code");
        Mockito.when(scratchCodeCreator.getScopeId()).thenReturn(scopeId);
        Mockito.when(scratchCodeCreator.getMfaOptionId()).thenReturn(mfaOptionId);

        Assert.assertTrue("True expected.", ScratchCodeDAO.create(entityManager, scratchCodeCreator) instanceof ScratchCode);
        Assert.assertTrue("True expected.", ScratchCodeDAO.create(entityManager, scratchCodeCreator).getCode().startsWith("$2a$12$"));
        Assert.assertEquals("Expected and actual values should be the same.", scopeId, ScratchCodeDAO.create(entityManager, scratchCodeCreator).getScopeId());
        Assert.assertEquals("Expected and actual values should be the same.", mfaOptionId, ScratchCodeDAO.create(entityManager, scratchCodeCreator).getMfaOptionId());
    }

    @Test(expected = KapuaEntityExistsException.class)
    public void createEntityExistsExceptionTest() throws KapuaException {
        Mockito.when(scratchCodeCreator.getCode()).thenReturn("code");
        Mockito.when(scratchCodeCreator.getScopeId()).thenReturn(scopeId);
        Mockito.when(scratchCodeCreator.getMfaOptionId()).thenReturn(mfaOptionId);
        Mockito.doThrow(new EntityExistsException()).when(entityManager).flush();

        ScratchCodeDAO.create(entityManager, scratchCodeCreator);
    }

    @Test(expected = PersistenceException.class)
    public void createPersistenceExceptionTest() throws KapuaException {
        Mockito.when(scratchCodeCreator.getCode()).thenReturn("code");
        Mockito.when(scratchCodeCreator.getScopeId()).thenReturn(scopeId);
        Mockito.when(scratchCodeCreator.getMfaOptionId()).thenReturn(mfaOptionId);
        Mockito.doThrow(new PersistenceException()).when(entityManager).flush();

        ScratchCodeDAO.create(entityManager, scratchCodeCreator);
    }

    @Test(expected = PersistenceException.class)
    public void createPersistenceExceptionWithCauseTest() throws KapuaException {
        Mockito.when(scratchCodeCreator.getCode()).thenReturn("code");
        Mockito.when(scratchCodeCreator.getScopeId()).thenReturn(scopeId);
        Mockito.when(scratchCodeCreator.getMfaOptionId()).thenReturn(mfaOptionId);
        Mockito.doThrow(new PersistenceException(new Exception(new Exception()))).when(entityManager).flush();

        ScratchCodeDAO.create(entityManager, scratchCodeCreator);
    }

    @Test(expected = PersistenceException.class)
    public void createPersistenceExceptionWithSQLCauseTest() throws KapuaException {
        Mockito.when(scratchCodeCreator.getCode()).thenReturn("code");
        Mockito.when(scratchCodeCreator.getScopeId()).thenReturn(scopeId);
        Mockito.when(scratchCodeCreator.getMfaOptionId()).thenReturn(mfaOptionId);
        Mockito.doThrow(new PersistenceException(new SQLException("reason", "23505", new Exception()))).when(entityManager).flush();

        ScratchCodeDAO.create(entityManager, scratchCodeCreator);
    }

    @Test(expected = NullPointerException.class)
    public void createNullEntityManagerTest() throws KapuaException {
        Mockito.when(scratchCodeCreator.getCode()).thenReturn("code");
        Mockito.when(scratchCodeCreator.getScopeId()).thenReturn(scopeId);
        Mockito.when(scratchCodeCreator.getMfaOptionId()).thenReturn(mfaOptionId);

        ScratchCodeDAO.create(null, scratchCodeCreator);
    }

    @Test(expected = NullPointerException.class)
    public void createNullMfaOptionCreatorTest() throws KapuaException {
        ScratchCodeDAO.create(entityManager, (ScratchCodeCreator) null);
    }

    @Test
    public void updateTest() throws KapuaException {
        Mockito.when(code.getId()).thenReturn(KapuaId.ONE);
        Mockito.when(entityManager.find(ScratchCodeImpl.class, KapuaId.ONE)).thenReturn(entityToFindOrDelete);
        Mockito.when(entityToFindOrDelete.getCreatedOn()).thenReturn(createdOn);
        Mockito.when(entityToFindOrDelete.getCreatedBy()).thenReturn(createdBy);

        Assert.assertTrue("True expected.", ScratchCodeDAO.update(entityManager, code) instanceof ScratchCode);
        Assert.assertEquals("Expected and actual values should be the same.", createdBy, ScratchCodeDAO.update(entityManager, code).getCreatedBy());
        Assert.assertEquals("Expected and actual values should be the same.", createdOn, ScratchCodeDAO.update(entityManager, code).getCreatedOn());
    }

    @Test(expected = NullPointerException.class)
    public void updateNullEntityManagerTest() throws KapuaException {
        Mockito.when(code.getId()).thenReturn(KapuaId.ONE);
        ScratchCodeDAO.update(null, code);
    }

    @Test(expected = NullPointerException.class)
    public void updateNullCodeTest() throws KapuaException {
        Mockito.when(entityManager.find(ScratchCodeImpl.class, KapuaId.ONE)).thenReturn(entityToFindOrDelete);
        Mockito.when(entityToFindOrDelete.getCreatedOn()).thenReturn(createdOn);
        Mockito.when(entityToFindOrDelete.getCreatedBy()).thenReturn(createdBy);

        ScratchCodeDAO.update(entityManager, null);
    }

    @Test
    public void findSameScopeIdsTest() {
        Mockito.when(entityManager.find(ScratchCodeImpl.class, scratchCodeId)).thenReturn(entityToFindOrDelete);
        Mockito.when(entityToFindOrDelete.getScopeId()).thenReturn(KapuaId.ONE);

        Assert.assertTrue("True expected.", ScratchCodeDAO.find(entityManager, scopeId, scratchCodeId) instanceof ScratchCode);
        Assert.assertEquals("Expected and actual values should be the same.", KapuaId.ONE, ScratchCodeDAO.find(entityManager, scopeId, scratchCodeId).getScopeId());
    }

    @Test
    public void findDifferentScopeIdsTest() {
        Mockito.when(entityManager.find(ScratchCodeImpl.class, scratchCodeId)).thenReturn(entityToFindOrDelete);
        Mockito.when(entityToFindOrDelete.getScopeId()).thenReturn(KapuaId.ANY);

        Assert.assertNull("Null expected.", ScratchCodeDAO.find(entityManager, scopeId, scratchCodeId));
    }

    @Test
    public void findNullEntityToFindTest() {
        Mockito.when(entityManager.find(ScratchCodeImpl.class, scratchCodeId)).thenReturn(null);

        Assert.assertNull("Null expected.", ScratchCodeDAO.find(entityManager, scopeId, scratchCodeId));
    }

    @Test
    public void findNullEntityToFindScopeIdTest() {
        Mockito.when(entityManager.find(ScratchCodeImpl.class, scratchCodeId)).thenReturn(entityToFindOrDelete);
        Mockito.when(entityToFindOrDelete.getScopeId()).thenReturn(null);

        Assert.assertTrue("True expected.", ScratchCodeDAO.find(entityManager, KapuaId.ANY, scratchCodeId) instanceof ScratchCode);
    }

    @Test(expected = NullPointerException.class)
    public void findNullEntityManagerTest() {
        ScratchCodeDAO.find(null, scopeId, scratchCodeId);
    }

    @Test
    public void findNullScopeIdTest() {
        Mockito.when(entityManager.find(ScratchCodeImpl.class, scratchCodeId)).thenReturn(entityToFindOrDelete);
        Mockito.when(entityToFindOrDelete.getScopeId()).thenReturn(KapuaId.ONE);

        Assert.assertTrue("True expected.", ScratchCodeDAO.find(entityManager, null, scratchCodeId) instanceof ScratchCode);
        Assert.assertEquals("Expected and actual values should be the same.", KapuaId.ONE, ScratchCodeDAO.find(entityManager, null, scratchCodeId).getScopeId());
    }

    @Test
    public void findNullMfaOptionIdTest() {
        Mockito.when(entityManager.find(ScratchCodeImpl.class, null)).thenReturn(entityToFindOrDelete);
        Mockito.when(entityToFindOrDelete.getScopeId()).thenReturn(KapuaId.ONE);

        Assert.assertTrue("True expected.", ScratchCodeDAO.find(entityManager, scopeId, null) instanceof ScratchCode);
        Assert.assertEquals("Expected and actual values should be the same.", KapuaId.ONE, ScratchCodeDAO.find(entityManager, scopeId, null).getScopeId());
    }

    @Test
    public void queryTest() throws KapuaException {
        CriteriaBuilder criteriaBuilder = Mockito.mock(CriteriaBuilder.class);
        CriteriaQuery<ScratchCodeImpl> criteriaQuery1 = Mockito.mock(CriteriaQuery.class);
        CriteriaQuery<ScratchCodeImpl> criteriaQuery2 = Mockito.mock(CriteriaQuery.class);
        CriteriaQuery<ScratchCodeImpl> criteriaQuery3 = Mockito.mock(CriteriaQuery.class);
        Root<ScratchCodeImpl> entityRoot = Mockito.mock(Root.class);
        EntityType<ScratchCodeImpl> entityType = Mockito.mock(EntityType.class);
        List<String> list = new ArrayList<>();
        TypedQuery<ScratchCodeImpl> query = Mockito.mock(TypedQuery.class);

        Mockito.when(entityManager.getCriteriaBuilder()).thenReturn(criteriaBuilder);
        Mockito.when(criteriaBuilder.createQuery(ScratchCodeImpl.class)).thenReturn(criteriaQuery1);
        Mockito.when(criteriaQuery1.from(ScratchCodeImpl.class)).thenReturn(entityRoot);
        Mockito.when(entityRoot.getModel()).thenReturn(entityType);
        Mockito.when(criteriaQuery1.select(entityRoot)).thenReturn(criteriaQuery2);
        Mockito.when(criteriaQuery2.distinct(true)).thenReturn(criteriaQuery3);
        Mockito.when(kapuaQuery.getFetchAttributes()).thenReturn(list);
        Mockito.when(entityManager.createQuery(criteriaQuery1)).thenReturn(query);

        Assert.assertTrue("True expected.", ScratchCodeDAO.query(entityManager, kapuaQuery) instanceof ScratchCodeListResult);
    }

    @Test(expected = NullPointerException.class)
    public void queryNullEntityManagerTest() throws KapuaException {
        ScratchCodeDAO.query(null, kapuaQuery);
    }

    @Test(expected = NullPointerException.class)
    public void queryNullKapuaQueryTest() throws KapuaException {
        CriteriaBuilder criteriaBuilder = Mockito.mock(CriteriaBuilder.class);
        CriteriaQuery<ScratchCodeImpl> criteriaQuery1 = Mockito.mock(CriteriaQuery.class);
        CriteriaQuery<ScratchCodeImpl> criteriaQuery2 = Mockito.mock(CriteriaQuery.class);
        CriteriaQuery<ScratchCodeImpl> criteriaQuery3 = Mockito.mock(CriteriaQuery.class);
        Root<ScratchCodeImpl> entityRoot = Mockito.mock(Root.class);
        EntityType<ScratchCodeImpl> entityType = Mockito.mock(EntityType.class);
        TypedQuery<ScratchCodeImpl> query = Mockito.mock(TypedQuery.class);

        Mockito.when(entityManager.getCriteriaBuilder()).thenReturn(criteriaBuilder);
        Mockito.when(criteriaBuilder.createQuery(ScratchCodeImpl.class)).thenReturn(criteriaQuery1);
        Mockito.when(criteriaQuery1.from(ScratchCodeImpl.class)).thenReturn(entityRoot);
        Mockito.when(entityRoot.getModel()).thenReturn(entityType);
        Mockito.when(criteriaQuery1.select(entityRoot)).thenReturn(criteriaQuery2);
        Mockito.when(criteriaQuery2.distinct(true)).thenReturn(criteriaQuery3);
        Mockito.when(entityManager.createQuery(criteriaQuery1)).thenReturn(query);

        ScratchCodeDAO.query(entityManager, null);
    }

    @Test
    public void countTest() throws KapuaException {
        CriteriaBuilder criteriaBuilder = Mockito.mock(CriteriaBuilder.class);
        CriteriaQuery<Long> criteriaQuery1 = Mockito.mock(CriteriaQuery.class);
        CriteriaQuery<Long> criteriaQuery2 = Mockito.mock(CriteriaQuery.class);
        Root<ScratchCodeImpl> entityRoot = Mockito.mock(Root.class);
        TypedQuery<Long> query = Mockito.mock(TypedQuery.class);
        Selection<Long> selection = Mockito.mock(Selection.class);
        Expression<Long> expression = Mockito.mock(Expression.class);
        long[] longNumberList = {0L, 10L, 100L, 1000000L, 9223372036854775807L, -100L, -9223372036854775808L};

        Mockito.when(entityManager.getCriteriaBuilder()).thenReturn(criteriaBuilder);
        Mockito.when(criteriaBuilder.createQuery(Long.class)).thenReturn(criteriaQuery1);
        Mockito.when(criteriaQuery1.from(ScratchCodeImpl.class)).thenReturn(entityRoot);
        Mockito.when(criteriaBuilder.countDistinct(entityRoot)).thenReturn(expression);
        Mockito.when(criteriaQuery1.select(selection)).thenReturn(criteriaQuery2);
        Mockito.when(entityManager.createQuery(criteriaQuery1)).thenReturn(query);
        for (long number : longNumberList) {
            Mockito.doReturn(number).when(query).getSingleResult();

            Assert.assertEquals("Expected and actual values should be the same.", number, ScratchCodeDAO.count(entityManager, kapuaQuery));
        }
    }

    @Test(expected = NullPointerException.class)
    public void countNullEntityManagerTest() throws KapuaException {
        ScratchCodeDAO.count(null, kapuaQuery);
    }

    @Test(expected = NullPointerException.class)
    public void countNullKapuaQueryTest() throws KapuaException {
        ScratchCodeDAO.count(entityManager, null);
    }

    @Test
    public void deleteTest() throws KapuaEntityNotFoundException {
        Mockito.when(entityManager.find(ScratchCodeImpl.class, scratchCodeId)).thenReturn(entityToFindOrDelete);
        Mockito.when(entityToFindOrDelete.getScopeId()).thenReturn(KapuaId.ONE);

        Assert.assertTrue("True expected.", ScratchCodeDAO.delete(entityManager, scopeId, scratchCodeId) instanceof ScratchCode);
    }

    @Test(expected = NullPointerException.class)
    public void deleteNullEntityManagerTest() throws KapuaEntityNotFoundException {
        ScratchCodeDAO.delete(null, scopeId, scratchCodeId);
    }

    @Test
    public void deleteNullScopeIdTest() throws KapuaEntityNotFoundException {
        Mockito.when(entityManager.find(ScratchCodeImpl.class, scratchCodeId)).thenReturn(entityToFindOrDelete);

        Assert.assertTrue("True expected.", ScratchCodeDAO.delete(entityManager, null, scratchCodeId) instanceof ScratchCode);
    }

    @Test(expected = NullPointerException.class)
    public void deleteNullMfaOptionIdTest() throws KapuaEntityNotFoundException {
        ScratchCodeDAO.delete(entityManager, scopeId, null);
    }

    @Test(expected = KapuaEntityNotFoundException.class)
    public void deleteNullEntityToDeleteTest() throws KapuaEntityNotFoundException {
        Mockito.when(entityManager.find(ScratchCodeImpl.class, scratchCodeId)).thenReturn(null);

        ScratchCodeDAO.delete(entityManager, scopeId, scratchCodeId);
    }
}