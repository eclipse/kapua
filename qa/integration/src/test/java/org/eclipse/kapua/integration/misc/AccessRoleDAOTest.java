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
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.model.query.KapuaQuery;
import org.eclipse.kapua.qa.markers.junit.JUnitTests;
import org.eclipse.kapua.service.authorization.access.AccessRole;
import org.eclipse.kapua.service.authorization.access.AccessRoleCreator;
import org.eclipse.kapua.service.authorization.access.AccessRoleListResult;
import org.eclipse.kapua.service.authorization.access.shiro.AccessRoleDAO;
import org.eclipse.kapua.service.authorization.access.shiro.AccessRoleImpl;
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
import java.util.List;


@Category(JUnitTests.class)
public class AccessRoleDAOTest {

    EntityManager entityManager;
    AccessRoleCreator accessRoleCreator;
    KapuaId scopeId, accessRoleId;
    AccessRoleImpl entityToFindOrDelete;
    KapuaQuery kapuaQuery;

    @Before
    public void initialize() {
        entityManager = Mockito.mock(EntityManager.class);
        accessRoleCreator = Mockito.mock(AccessRoleCreator.class);
        scopeId = KapuaId.ONE;
        accessRoleId = KapuaId.ANY;
        entityToFindOrDelete = Mockito.mock(AccessRoleImpl.class);
        kapuaQuery = Mockito.mock(KapuaQuery.class);
    }

    @Test
    public void createTest() throws KapuaException {
        Mockito.when(accessRoleCreator.getScopeId()).thenReturn(KapuaId.ONE);
        Mockito.when(accessRoleCreator.getAccessInfoId()).thenReturn(KapuaId.ONE);
        Mockito.when(accessRoleCreator.getRoleId()).thenReturn(KapuaId.ONE);

        Assert.assertTrue("True expected.", AccessRoleDAO.create(entityManager, accessRoleCreator) instanceof AccessRole);
        Assert.assertEquals("Expected and actual values should be the same.", KapuaId.ONE, AccessRoleDAO.create(entityManager, accessRoleCreator).getScopeId());
        Assert.assertEquals("Expected and actual values should be the same.", KapuaId.ONE, AccessRoleDAO.create(entityManager, accessRoleCreator).getAccessInfoId());
        Assert.assertEquals("Expected and actual values should be the same.", KapuaId.ONE, AccessRoleDAO.create(entityManager, accessRoleCreator).getRoleId());
    }

    @Test(expected = KapuaEntityExistsException.class)
    public void createEntityExistsExceptionTest() throws KapuaException {
        Mockito.when(accessRoleCreator.getScopeId()).thenReturn(KapuaId.ONE);
        Mockito.when(accessRoleCreator.getAccessInfoId()).thenReturn(KapuaId.ONE);
        Mockito.when(accessRoleCreator.getRoleId()).thenReturn(KapuaId.ONE);
        Mockito.doThrow(new EntityExistsException()).when(entityManager).flush();

        AccessRoleDAO.create(entityManager, accessRoleCreator);
    }

    @Test(expected = PersistenceException.class)
    public void createPersistenceExceptionTest() throws KapuaException {
        Mockito.when(accessRoleCreator.getScopeId()).thenReturn(KapuaId.ONE);
        Mockito.when(accessRoleCreator.getAccessInfoId()).thenReturn(KapuaId.ONE);
        Mockito.when(accessRoleCreator.getRoleId()).thenReturn(KapuaId.ONE);
        Mockito.doThrow(new PersistenceException()).when(entityManager).flush();

        AccessRoleDAO.create(entityManager, accessRoleCreator);
    }

    @Test(expected = PersistenceException.class)
    public void createPersistenceExceptionWithNotSQLCauseTest() throws KapuaException {
        Mockito.when(accessRoleCreator.getScopeId()).thenReturn(KapuaId.ONE);
        Mockito.when(accessRoleCreator.getAccessInfoId()).thenReturn(KapuaId.ONE);
        Mockito.when(accessRoleCreator.getRoleId()).thenReturn(KapuaId.ONE);
        Mockito.doThrow(new PersistenceException(new Exception(new Exception()))).when(entityManager).flush();

        AccessRoleDAO.create(entityManager, accessRoleCreator);
    }

    @Test(expected = PersistenceException.class)
    public void createPersistenceExceptionWithSQLCauseTest() throws KapuaException {
        Mockito.when(accessRoleCreator.getScopeId()).thenReturn(KapuaId.ONE);
        Mockito.when(accessRoleCreator.getAccessInfoId()).thenReturn(KapuaId.ONE);
        Mockito.when(accessRoleCreator.getRoleId()).thenReturn(KapuaId.ONE);
        Mockito.doThrow(new PersistenceException(new SQLException("reason", "23505", new Exception()))).when(entityManager).flush();

        AccessRoleDAO.create(entityManager, accessRoleCreator);
    }

    @Test(expected = NullPointerException.class)
    public void createNullEntityManagerTest() throws KapuaException {
        Mockito.when(accessRoleCreator.getScopeId()).thenReturn(KapuaId.ONE);
        Mockito.when(accessRoleCreator.getAccessInfoId()).thenReturn(KapuaId.ONE);
        Mockito.when(accessRoleCreator.getRoleId()).thenReturn(KapuaId.ONE);

        AccessRoleDAO.create(null, accessRoleCreator);
    }

    @Test(expected = NullPointerException.class)
    public void createNullAccessRoleCreatorTest() throws KapuaException {
        Mockito.when(accessRoleCreator.getScopeId()).thenReturn(KapuaId.ONE);
        Mockito.when(accessRoleCreator.getAccessInfoId()).thenReturn(KapuaId.ONE);
        Mockito.when(accessRoleCreator.getRoleId()).thenReturn(KapuaId.ONE);

        AccessRoleDAO.create(entityManager, (AccessRoleCreator) null);
    }

    @Test
    public void findSameScopeIdsTest() {
        Mockito.when(entityManager.find(AccessRoleImpl.class, accessRoleId)).thenReturn(entityToFindOrDelete);
        Mockito.when(entityToFindOrDelete.getScopeId()).thenReturn(KapuaId.ONE);

        Assert.assertTrue("True expected.", AccessRoleDAO.find(entityManager, scopeId, accessRoleId) instanceof AccessRole);
        Assert.assertEquals("Expected and actual values should be the same.", KapuaId.ONE, AccessRoleDAO.find(entityManager, scopeId, accessRoleId).getScopeId());
    }

    @Test
    public void findDifferentScopeIdsTest() {
        Mockito.when(entityManager.find(AccessRoleImpl.class, accessRoleId)).thenReturn(entityToFindOrDelete);
        Mockito.when(entityToFindOrDelete.getScopeId()).thenReturn(KapuaId.ANY);

        Assert.assertNull("Null expected.", AccessRoleDAO.find(entityManager, scopeId, accessRoleId));
    }

    @Test
    public void findNullEntityToFindTest() {
        Mockito.when(entityManager.find(AccessRoleImpl.class, accessRoleId)).thenReturn(null);

        Assert.assertNull("Null expected.", AccessRoleDAO.find(entityManager, scopeId, accessRoleId));
    }

    @Test
    public void findNullEntityToFindScopeIdTest() {
        Mockito.when(entityManager.find(AccessRoleImpl.class, accessRoleId)).thenReturn(entityToFindOrDelete);
        Mockito.when(entityToFindOrDelete.getScopeId()).thenReturn(null);

        Assert.assertTrue("True expected.", AccessRoleDAO.find(entityManager, KapuaId.ANY, accessRoleId) instanceof AccessRole);
    }

    @Test(expected = NullPointerException.class)
    public void findNullEntityManagerTest() {
        AccessRoleDAO.find(null, scopeId, accessRoleId);
    }

    @Test
    public void findNullScopeIdTest() {
        Mockito.when(entityManager.find(AccessRoleImpl.class, accessRoleId)).thenReturn(entityToFindOrDelete);
        Mockito.when(entityToFindOrDelete.getScopeId()).thenReturn(KapuaId.ONE);

        Assert.assertTrue("True expected.", AccessRoleDAO.find(entityManager, null, accessRoleId) instanceof AccessRole);
        Assert.assertEquals("Expected and actual values should be the same.", KapuaId.ONE, AccessRoleDAO.find(entityManager, null, accessRoleId).getScopeId());
    }

    @Test
    public void findNullAccessRoleIdTest() {
        Mockito.when(entityManager.find(AccessRoleImpl.class, null)).thenReturn(entityToFindOrDelete);
        Mockito.when(entityToFindOrDelete.getScopeId()).thenReturn(KapuaId.ONE);

        Assert.assertTrue("True expected.", AccessRoleDAO.find(entityManager, scopeId, null) instanceof AccessRole);
        Assert.assertEquals("Expected and actual values should be the same.", KapuaId.ONE, AccessRoleDAO.find(entityManager, scopeId, null).getScopeId());
    }

    @Test
    public void queryTest() throws KapuaException {
        CriteriaBuilder criteriaBuilder = Mockito.mock(CriteriaBuilder.class);
        CriteriaQuery<AccessRoleImpl> criteriaQuery1 = Mockito.mock(CriteriaQuery.class);
        CriteriaQuery<AccessRoleImpl> criteriaQuery2 = Mockito.mock(CriteriaQuery.class);
        CriteriaQuery<AccessRoleImpl> criteriaQuery3 = Mockito.mock(CriteriaQuery.class);
        Root<AccessRoleImpl> entityRoot = Mockito.mock(Root.class);
        EntityType<AccessRoleImpl> entityType = Mockito.mock(EntityType.class);
        List<String> list = new ArrayList<>();
        TypedQuery<AccessRoleImpl> query = Mockito.mock(TypedQuery.class);

        Mockito.when(entityManager.getCriteriaBuilder()).thenReturn(criteriaBuilder);
        Mockito.when(criteriaBuilder.createQuery(AccessRoleImpl.class)).thenReturn(criteriaQuery1);
        Mockito.when(criteriaQuery1.from(AccessRoleImpl.class)).thenReturn(entityRoot);
        Mockito.when(entityRoot.getModel()).thenReturn(entityType);
        Mockito.when(criteriaQuery1.select(entityRoot)).thenReturn(criteriaQuery2);
        Mockito.when(criteriaQuery2.distinct(true)).thenReturn(criteriaQuery3);
        Mockito.when(kapuaQuery.getFetchAttributes()).thenReturn(list);
        Mockito.when(entityManager.createQuery(criteriaQuery1)).thenReturn(query);

        Assert.assertTrue("True expected.", AccessRoleDAO.query(entityManager, kapuaQuery) instanceof AccessRoleListResult);
    }

    @Test(expected = NullPointerException.class)
    public void queryNullEntityManagerTest() throws KapuaException {
        AccessRoleDAO.query(null, kapuaQuery);
    }

    @Test(expected = NullPointerException.class)
    public void queryNullKapuaQueryTest() throws KapuaException {
        CriteriaBuilder criteriaBuilder = Mockito.mock(CriteriaBuilder.class);
        CriteriaQuery<AccessRoleImpl> criteriaQuery1 = Mockito.mock(CriteriaQuery.class);
        CriteriaQuery<AccessRoleImpl> criteriaQuery2 = Mockito.mock(CriteriaQuery.class);
        CriteriaQuery<AccessRoleImpl> criteriaQuery3 = Mockito.mock(CriteriaQuery.class);
        Root<AccessRoleImpl> entityRoot = Mockito.mock(Root.class);
        EntityType<AccessRoleImpl> entityType = Mockito.mock(EntityType.class);
        TypedQuery<AccessRoleImpl> query = Mockito.mock(TypedQuery.class);

        Mockito.when(entityManager.getCriteriaBuilder()).thenReturn(criteriaBuilder);
        Mockito.when(criteriaBuilder.createQuery(AccessRoleImpl.class)).thenReturn(criteriaQuery1);
        Mockito.when(criteriaQuery1.from(AccessRoleImpl.class)).thenReturn(entityRoot);
        Mockito.when(entityRoot.getModel()).thenReturn(entityType);
        Mockito.when(criteriaQuery1.select(entityRoot)).thenReturn(criteriaQuery2);
        Mockito.when(criteriaQuery2.distinct(true)).thenReturn(criteriaQuery3);
        Mockito.when(entityManager.createQuery(criteriaQuery1)).thenReturn(query);

        AccessRoleDAO.query(entityManager, null);
    }

    @Test
    public void countTest() throws KapuaException {
        CriteriaBuilder criteriaBuilder = Mockito.mock(CriteriaBuilder.class);
        CriteriaQuery<Long> criteriaQuery1 = Mockito.mock(CriteriaQuery.class);
        CriteriaQuery<Long> criteriaQuery2 = Mockito.mock(CriteriaQuery.class);
        Root<AccessRoleImpl> entityRoot = Mockito.mock(Root.class);
        TypedQuery<Long> query = Mockito.mock(TypedQuery.class);
        Selection<Long> selection = Mockito.mock(Selection.class);
        Expression<Long> expression = Mockito.mock(Expression.class);
        long[] longNumberList = {0L, 10L, 100L, 1000000L, 9223372036854775807L, -100L, -9223372036854775808L};

        Mockito.when(entityManager.getCriteriaBuilder()).thenReturn(criteriaBuilder);
        Mockito.when(criteriaBuilder.createQuery(Long.class)).thenReturn(criteriaQuery1);
        Mockito.when(criteriaQuery1.from(AccessRoleImpl.class)).thenReturn(entityRoot);
        Mockito.when(criteriaBuilder.countDistinct(entityRoot)).thenReturn(expression);
        Mockito.when(criteriaQuery1.select(selection)).thenReturn(criteriaQuery2);
        Mockito.when(entityManager.createQuery(criteriaQuery1)).thenReturn(query);
        for (long number : longNumberList) {
            Mockito.doReturn(number).when(query).getSingleResult();

            Assert.assertEquals("Expected and actual values should be the same.", number, AccessRoleDAO.count(entityManager, kapuaQuery));
        }
    }

    @Test(expected = NullPointerException.class)
    public void countNullEntityManagerTest() throws KapuaException {
        AccessRoleDAO.count(null, kapuaQuery);
    }

    @Test(expected = NullPointerException.class)
    public void countNullKapuaQueryTest() throws KapuaException {
        AccessRoleDAO.count(entityManager, null);
    }

    @Test
    public void deleteTest() throws KapuaEntityNotFoundException {
        Mockito.when(entityManager.find(AccessRoleImpl.class, accessRoleId)).thenReturn(entityToFindOrDelete);
        Mockito.when(entityToFindOrDelete.getScopeId()).thenReturn(KapuaId.ONE);

        Assert.assertTrue("True expected.", AccessRoleDAO.delete(entityManager, scopeId, accessRoleId) instanceof AccessRole);
    }

    @Test(expected = NullPointerException.class)
    public void deleteNullEntityManagerTest() throws KapuaEntityNotFoundException {
        AccessRoleDAO.delete(null, scopeId, accessRoleId);
    }

    @Test
    public void deleteNullScopeIdTest() throws KapuaEntityNotFoundException {
        Mockito.when(entityManager.find(AccessRoleImpl.class, accessRoleId)).thenReturn(entityToFindOrDelete);

        Assert.assertTrue("True expected.", AccessRoleDAO.delete(entityManager, null, accessRoleId) instanceof AccessRole);
    }

    @Test(expected = NullPointerException.class)
    public void deleteNullAccessRoleIdTest() throws KapuaEntityNotFoundException {
        AccessRoleDAO.delete(entityManager, scopeId, null);
    }

    @Test(expected = KapuaEntityNotFoundException.class)
    public void deleteNullEntityToDeleteTest() throws KapuaEntityNotFoundException {
        Mockito.when(entityManager.find(AccessRoleImpl.class, accessRoleId)).thenReturn(null);

        AccessRoleDAO.delete(entityManager, scopeId, accessRoleId);
    }
} 