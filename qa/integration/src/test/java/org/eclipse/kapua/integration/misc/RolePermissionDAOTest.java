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
import org.eclipse.kapua.service.authorization.permission.shiro.PermissionImpl;
import org.eclipse.kapua.service.authorization.role.RolePermission;
import org.eclipse.kapua.service.authorization.role.RolePermissionCreator;
import org.eclipse.kapua.service.authorization.role.RolePermissionListResult;
import org.eclipse.kapua.service.authorization.role.shiro.RolePermissionDAO;
import org.eclipse.kapua.service.authorization.role.shiro.RolePermissionImpl;
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
public class RolePermissionDAOTest {

    EntityManager entityManager;
    RolePermissionCreator creator;
    PermissionImpl permission;
    RolePermissionImpl entityToFindOrDelete;
    KapuaId scopeId, roleId;
    KapuaQuery kapuaQuery;

    @Before
    public void initialize() {
        entityManager = Mockito.mock(EntityManager.class);
        creator = Mockito.mock(RolePermissionCreator.class);
        permission = Mockito.mock(PermissionImpl.class);
        entityToFindOrDelete = Mockito.mock(RolePermissionImpl.class);
        scopeId = KapuaId.ONE;
        roleId = KapuaId.ONE;
        kapuaQuery = Mockito.mock(KapuaQuery.class);
    }

    @Test
    public void createTest() throws KapuaException {
        Mockito.when(creator.getScopeId()).thenReturn(KapuaId.ONE);
        Mockito.when(creator.getRoleId()).thenReturn(KapuaId.ONE);
        Mockito.when(creator.getPermission()).thenReturn(permission);

        Assert.assertTrue("True expected.", RolePermissionDAO.create(entityManager, creator) instanceof RolePermission);
        Assert.assertEquals("Expected and actual values should be the same.", KapuaId.ONE, RolePermissionDAO.create(entityManager, creator).getScopeId());
        Assert.assertEquals("Expected and actual values should be the same.", KapuaId.ONE, RolePermissionDAO.create(entityManager, creator).getRoleId());
        Assert.assertEquals("Expected and actual values should be the same.", permission, RolePermissionDAO.create(entityManager, creator).getPermission());
    }

    @Test(expected = KapuaEntityExistsException.class)
    public void createEntityExistsExceptionTest() throws KapuaException {
        Mockito.when(creator.getScopeId()).thenReturn(KapuaId.ONE);
        Mockito.when(creator.getRoleId()).thenReturn(KapuaId.ONE);
        Mockito.when(creator.getPermission()).thenReturn(permission);
        Mockito.doThrow(new EntityExistsException()).when(entityManager).flush();

        RolePermissionDAO.create(entityManager, creator);
    }

    @Test(expected = PersistenceException.class)
    public void createPersistenceExceptionTest() throws KapuaException {
        Mockito.when(creator.getScopeId()).thenReturn(KapuaId.ONE);
        Mockito.when(creator.getRoleId()).thenReturn(KapuaId.ONE);
        Mockito.when(creator.getPermission()).thenReturn(permission);
        Mockito.doThrow(new PersistenceException()).when(entityManager).flush();

        RolePermissionDAO.create(entityManager, creator);
    }

    @Test(expected = PersistenceException.class)
    public void createPersistenceExceptionWithNotSQLCauseTest() throws KapuaException {
        Mockito.when(creator.getScopeId()).thenReturn(KapuaId.ONE);
        Mockito.when(creator.getRoleId()).thenReturn(KapuaId.ONE);
        Mockito.when(creator.getPermission()).thenReturn(permission);
        Mockito.doThrow(new PersistenceException(new Exception(new Exception()))).when(entityManager).flush();

        RolePermissionDAO.create(entityManager, creator);
    }

    @Test(expected = PersistenceException.class)
    public void createPersistenceExceptionWithSQLCauseTest() throws KapuaException {
        Mockito.when(creator.getScopeId()).thenReturn(KapuaId.ONE);
        Mockito.when(creator.getRoleId()).thenReturn(KapuaId.ONE);
        Mockito.when(creator.getPermission()).thenReturn(permission);
        Mockito.doThrow(new PersistenceException(new SQLException("reason", "23505", new Exception()))).when(entityManager).flush();

        RolePermissionDAO.create(entityManager, creator);
    }

    @Test(expected = NullPointerException.class)
    public void createNullEntityManagerTest() throws KapuaException {
        Mockito.when(creator.getScopeId()).thenReturn(KapuaId.ONE);
        Mockito.when(creator.getRoleId()).thenReturn(KapuaId.ONE);
        Mockito.when(creator.getPermission()).thenReturn(permission);

        RolePermissionDAO.create(null, creator);
    }

    @Test(expected = NullPointerException.class)
    public void createNullRolePermissionCreatorTest() throws KapuaException {
        RolePermissionDAO.create(entityManager, (RolePermissionCreator) null);
    }

    @Test
    public void findSameScopeIdsTest() {
        Mockito.when(entityManager.find(RolePermissionImpl.class, roleId)).thenReturn(entityToFindOrDelete);
        Mockito.when(entityToFindOrDelete.getScopeId()).thenReturn(KapuaId.ONE);

        Assert.assertTrue("True expected.", RolePermissionDAO.find(entityManager, scopeId, roleId) instanceof RolePermission);
        Assert.assertEquals("Expected and actual values should be the same.", KapuaId.ONE, RolePermissionDAO.find(entityManager, scopeId, roleId).getScopeId());
    }

    @Test
    public void findDifferentScopeIdsTest() {
        Mockito.when(entityManager.find(RolePermissionImpl.class, roleId)).thenReturn(entityToFindOrDelete);
        Mockito.when(entityToFindOrDelete.getScopeId()).thenReturn(KapuaId.ANY);

        Assert.assertNull("Null expected.", RolePermissionDAO.find(entityManager, scopeId, roleId));
    }

    @Test
    public void findNullEntityToFindTest() {
        Mockito.when(entityManager.find(RolePermissionImpl.class, roleId)).thenReturn(null);

        Assert.assertNull("Null expected.", RolePermissionDAO.find(entityManager, scopeId, roleId));
    }

    @Test
    public void findNullEntityToFindScopeIdTest() {
        Mockito.when(entityManager.find(RolePermissionImpl.class, roleId)).thenReturn(entityToFindOrDelete);
        Mockito.when(entityToFindOrDelete.getScopeId()).thenReturn(null);

        Assert.assertTrue("True expected.", RolePermissionDAO.find(entityManager, KapuaId.ANY, roleId) instanceof RolePermission);
    }

    @Test(expected = NullPointerException.class)
    public void findNullEntityManagerTest() {
        RolePermissionDAO.find(null, scopeId, roleId);
    }

    @Test
    public void findNullScopeIdTest() {
        Mockito.when(entityManager.find(RolePermissionImpl.class, roleId)).thenReturn(entityToFindOrDelete);
        Mockito.when(entityToFindOrDelete.getScopeId()).thenReturn(KapuaId.ONE);

        Assert.assertTrue("True expected.", RolePermissionDAO.find(entityManager, null, roleId) instanceof RolePermission);
        Assert.assertEquals("Expected and actual values should be the same.", KapuaId.ONE, RolePermissionDAO.find(entityManager, null, roleId).getScopeId());
    }

    @Test
    public void findNullRoleIdTest() {
        Mockito.when(entityManager.find(RolePermissionImpl.class, null)).thenReturn(entityToFindOrDelete);
        Mockito.when(entityToFindOrDelete.getScopeId()).thenReturn(KapuaId.ONE);

        Assert.assertTrue("True expected.", RolePermissionDAO.find(entityManager, scopeId, null) instanceof RolePermission);
        Assert.assertEquals("Expected and actual values should be the same.", KapuaId.ONE, RolePermissionDAO.find(entityManager, scopeId, null).getScopeId());
    }

    @Test
    public void queryTest() throws KapuaException {
        CriteriaBuilder criteriaBuilder = Mockito.mock(CriteriaBuilder.class);
        CriteriaQuery<RolePermissionImpl> criteriaQuery1 = Mockito.mock(CriteriaQuery.class);
        CriteriaQuery<RolePermissionImpl> criteriaQuery2 = Mockito.mock(CriteriaQuery.class);
        CriteriaQuery<RolePermissionImpl> criteriaQuery3 = Mockito.mock(CriteriaQuery.class);
        Root<RolePermissionImpl> entityRoot = Mockito.mock(Root.class);
        EntityType<RolePermissionImpl> entityType = Mockito.mock(EntityType.class);
        List<String> list = new ArrayList<>();
        TypedQuery<RolePermissionImpl> query = Mockito.mock(TypedQuery.class);

        Mockito.when(entityManager.getCriteriaBuilder()).thenReturn(criteriaBuilder);
        Mockito.when(criteriaBuilder.createQuery(RolePermissionImpl.class)).thenReturn(criteriaQuery1);
        Mockito.when(criteriaQuery1.from(RolePermissionImpl.class)).thenReturn(entityRoot);
        Mockito.when(entityRoot.getModel()).thenReturn(entityType);
        Mockito.when(criteriaQuery1.select(entityRoot)).thenReturn(criteriaQuery2);
        Mockito.when(criteriaQuery2.distinct(true)).thenReturn(criteriaQuery3);
        Mockito.when(kapuaQuery.getFetchAttributes()).thenReturn(list);
        Mockito.when(entityManager.createQuery(criteriaQuery1)).thenReturn(query);

        Assert.assertTrue("True expected.", RolePermissionDAO.query(entityManager, kapuaQuery) instanceof RolePermissionListResult);
    }

    @Test(expected = NullPointerException.class)
    public void queryNullEntityManagerTest() throws KapuaException {
        RolePermissionDAO.query(null, kapuaQuery);
    }

    @Test(expected = NullPointerException.class)
    public void queryNullKapuaQueryTest() throws KapuaException {
        CriteriaBuilder criteriaBuilder = Mockito.mock(CriteriaBuilder.class);
        CriteriaQuery<RolePermissionImpl> criteriaQuery1 = Mockito.mock(CriteriaQuery.class);
        CriteriaQuery<RolePermissionImpl> criteriaQuery2 = Mockito.mock(CriteriaQuery.class);
        CriteriaQuery<RolePermissionImpl> criteriaQuery3 = Mockito.mock(CriteriaQuery.class);
        Root<RolePermissionImpl> entityRoot = Mockito.mock(Root.class);
        EntityType<RolePermissionImpl> entityType = Mockito.mock(EntityType.class);
        TypedQuery<RolePermissionImpl> query = Mockito.mock(TypedQuery.class);

        Mockito.when(entityManager.getCriteriaBuilder()).thenReturn(criteriaBuilder);
        Mockito.when(criteriaBuilder.createQuery(RolePermissionImpl.class)).thenReturn(criteriaQuery1);
        Mockito.when(criteriaQuery1.from(RolePermissionImpl.class)).thenReturn(entityRoot);
        Mockito.when(entityRoot.getModel()).thenReturn(entityType);
        Mockito.when(criteriaQuery1.select(entityRoot)).thenReturn(criteriaQuery2);
        Mockito.when(criteriaQuery2.distinct(true)).thenReturn(criteriaQuery3);
        Mockito.when(entityManager.createQuery(criteriaQuery1)).thenReturn(query);

        RolePermissionDAO.query(entityManager, null);
    }

    @Test
    public void countTest() throws KapuaException {
        CriteriaBuilder criteriaBuilder = Mockito.mock(CriteriaBuilder.class);
        CriteriaQuery<Long> criteriaQuery1 = Mockito.mock(CriteriaQuery.class);
        CriteriaQuery<Long> criteriaQuery2 = Mockito.mock(CriteriaQuery.class);
        Root<RolePermissionImpl> entityRoot = Mockito.mock(Root.class);
        TypedQuery<Long> query = Mockito.mock(TypedQuery.class);
        Selection<Long> selection = Mockito.mock(Selection.class);
        Expression<Long> expression = Mockito.mock(Expression.class);
        long[] longNumberList = {0L, 10L, 100L, 1000000L, 9223372036854775807L, -100L, -9223372036854775808L};

        Mockito.when(entityManager.getCriteriaBuilder()).thenReturn(criteriaBuilder);
        Mockito.when(criteriaBuilder.createQuery(Long.class)).thenReturn(criteriaQuery1);
        Mockito.when(criteriaQuery1.from(RolePermissionImpl.class)).thenReturn(entityRoot);
        Mockito.when(criteriaBuilder.countDistinct(entityRoot)).thenReturn(expression);
        Mockito.when(criteriaQuery1.select(selection)).thenReturn(criteriaQuery2);
        Mockito.when(entityManager.createQuery(criteriaQuery1)).thenReturn(query);
        for (long number : longNumberList) {
            Mockito.doReturn(number).when(query).getSingleResult();

            Assert.assertEquals("Expected and actual values should be the same.", number, RolePermissionDAO.count(entityManager, kapuaQuery));
        }
    }

    @Test(expected = NullPointerException.class)
    public void countNullEntityManagerTest() throws KapuaException {
        RolePermissionDAO.count(null, kapuaQuery);
    }

    @Test(expected = NullPointerException.class)
    public void countNullKapuaQueryTest() throws KapuaException {
        RolePermissionDAO.count(entityManager, null);
    }

    @Test
    public void deleteTest() throws KapuaEntityNotFoundException {
        Mockito.when(entityManager.find(RolePermissionImpl.class, roleId)).thenReturn(entityToFindOrDelete);
        Mockito.when(entityToFindOrDelete.getScopeId()).thenReturn(KapuaId.ONE);

        Assert.assertTrue("True expected.", RolePermissionDAO.delete(entityManager, scopeId, roleId) instanceof RolePermission);
    }

    @Test(expected = NullPointerException.class)
    public void deleteNullEntityManagerTest() throws KapuaEntityNotFoundException {
        RolePermissionDAO.delete(null, scopeId, roleId);
    }

    @Test
    public void deleteNullScopeIdTest() throws KapuaEntityNotFoundException {
        Mockito.when(entityManager.find(RolePermissionImpl.class, roleId)).thenReturn(entityToFindOrDelete);

        Assert.assertTrue("True expected.", RolePermissionDAO.delete(entityManager, null, roleId) instanceof RolePermission);
    }

    @Test(expected = NullPointerException.class)
    public void deleteNullRolePermissionIdTest() throws KapuaEntityNotFoundException {
        RolePermissionDAO.delete(entityManager, scopeId, null);
    }

    @Test(expected = KapuaEntityNotFoundException.class)
    public void deleteNullEntityToDeleteTest() throws KapuaEntityNotFoundException {
        Mockito.when(entityManager.find(RolePermissionImpl.class, roleId)).thenReturn(null);

        RolePermissionDAO.delete(entityManager, scopeId, roleId);
    }
}