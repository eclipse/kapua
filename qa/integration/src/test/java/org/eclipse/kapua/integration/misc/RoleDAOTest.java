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
import org.eclipse.kapua.service.authorization.role.Role;
import org.eclipse.kapua.service.authorization.role.RoleCreator;
import org.eclipse.kapua.service.authorization.role.RoleListResult;
import org.eclipse.kapua.service.authorization.role.shiro.RoleDAO;
import org.eclipse.kapua.service.authorization.role.shiro.RoleImpl;
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
public class RoleDAOTest {

    EntityManager entityManager;
    RoleCreator creator;
    RoleImpl role, entityToFindOrDelete;
    Date createdOn;
    KapuaId createdBy, scopeId, roleId;
    KapuaQuery kapuaQuery;

    @Before
    public void initialize() {
        entityManager = Mockito.mock(EntityManager.class);
        creator = Mockito.mock(RoleCreator.class);
        role = Mockito.mock(RoleImpl.class);
        entityToFindOrDelete = Mockito.mock(RoleImpl.class);
        createdOn = new Date();
        createdBy = KapuaId.ONE;
        scopeId = KapuaId.ONE;
        roleId = KapuaId.ONE;
        kapuaQuery = Mockito.mock(KapuaQuery.class);
    }

    @Test
    public void createTest() throws KapuaException {
        Mockito.when(creator.getScopeId()).thenReturn(KapuaId.ONE);
        Mockito.when(creator.getName()).thenReturn("name");
        Mockito.when(creator.getDescription()).thenReturn("description");

        Assert.assertTrue("True expected.", RoleDAO.create(entityManager, creator) instanceof Role);
        Assert.assertEquals("Expected and actual values should be the same.", KapuaId.ONE, RoleDAO.create(entityManager, creator).getScopeId());
        Assert.assertEquals("Expected and actual values should be the same.", "name", RoleDAO.create(entityManager, creator).getName());
        Assert.assertEquals("Expected and actual values should be the same.", "description", RoleDAO.create(entityManager, creator).getDescription());
    }

    @Test(expected = KapuaEntityExistsException.class)
    public void createEntityExistsExceptionTest() throws KapuaException {
        Mockito.when(creator.getScopeId()).thenReturn(KapuaId.ONE);
        Mockito.when(creator.getName()).thenReturn("name");
        Mockito.when(creator.getDescription()).thenReturn("description");
        Mockito.doThrow(new EntityExistsException()).when(entityManager).flush();

        RoleDAO.create(entityManager, creator);
    }

    @Test(expected = PersistenceException.class)
    public void createPersistenceExceptionTest() throws KapuaException {
        Mockito.when(creator.getScopeId()).thenReturn(KapuaId.ONE);
        Mockito.when(creator.getName()).thenReturn("name");
        Mockito.when(creator.getDescription()).thenReturn("description");
        Mockito.doThrow(new PersistenceException()).when(entityManager).flush();

        RoleDAO.create(entityManager, creator);
    }

    @Test(expected = PersistenceException.class)
    public void createPersistenceExceptionWithNotSQLCauseTest() throws KapuaException {
        Mockito.when(creator.getScopeId()).thenReturn(KapuaId.ONE);
        Mockito.when(creator.getName()).thenReturn("name");
        Mockito.when(creator.getDescription()).thenReturn("description");
        Mockito.doThrow(new PersistenceException(new Exception(new Exception()))).when(entityManager).flush();

        RoleDAO.create(entityManager, creator);
    }

    @Test(expected = PersistenceException.class)
    public void createPersistenceExceptionWithSQLCauseTest() throws KapuaException {
        Mockito.when(creator.getScopeId()).thenReturn(KapuaId.ONE);
        Mockito.when(creator.getName()).thenReturn("name");
        Mockito.when(creator.getDescription()).thenReturn("description");
        Mockito.doThrow(new PersistenceException(new SQLException("reason", "23505", new Exception()))).when(entityManager).flush();

        RoleDAO.create(entityManager, creator);
    }

    @Test(expected = NullPointerException.class)
    public void createNullEntityManagerTest() throws KapuaException {
        Mockito.when(creator.getScopeId()).thenReturn(KapuaId.ONE);
        Mockito.when(creator.getName()).thenReturn("name");
        Mockito.when(creator.getDescription()).thenReturn("description");

        RoleDAO.create(null, creator);
    }

    @Test(expected = NullPointerException.class)
    public void createNullRoleCreatorTest() throws KapuaException {
        RoleDAO.create(entityManager, (RoleCreator) null);
    }

    @Test
    public void updateTest() throws KapuaException {
        Mockito.when(role.getId()).thenReturn(KapuaId.ONE);
        Mockito.when(entityManager.find(RoleImpl.class, KapuaId.ONE)).thenReturn(entityToFindOrDelete);
        Mockito.when(entityToFindOrDelete.getCreatedOn()).thenReturn(createdOn);
        Mockito.when(entityToFindOrDelete.getCreatedBy()).thenReturn(createdBy);

        Assert.assertTrue("True expected.", RoleDAO.update(entityManager, role) instanceof Role);
        Assert.assertEquals("Expected and actual values should be the same.", createdBy, RoleDAO.update(entityManager, role).getCreatedBy());
        Assert.assertEquals("Expected and actual values should be the same.", createdOn, RoleDAO.update(entityManager, role).getCreatedOn());
    }

    @Test(expected = NullPointerException.class)
    public void updateNullEntityManagerTest() throws KapuaException {
        RoleDAO.update(null, role);
    }

    @Test(expected = NullPointerException.class)
    public void updateNullRoleTest() throws KapuaException {
        Mockito.when(entityManager.find(RoleImpl.class, KapuaId.ONE)).thenReturn(entityToFindOrDelete);
        Mockito.when(entityToFindOrDelete.getCreatedOn()).thenReturn(createdOn);
        Mockito.when(entityToFindOrDelete.getCreatedBy()).thenReturn(createdBy);

        RoleDAO.update(entityManager, null);
    }

    @Test
    public void findSameScopeIdsTest() {
        Mockito.when(entityManager.find(RoleImpl.class, roleId)).thenReturn(entityToFindOrDelete);
        Mockito.when(entityToFindOrDelete.getScopeId()).thenReturn(KapuaId.ONE);

        Assert.assertTrue("True expected.", RoleDAO.find(entityManager, scopeId, roleId) instanceof Role);
        Assert.assertEquals("Expected and actual values should be the same.", KapuaId.ONE, RoleDAO.find(entityManager, scopeId, roleId).getScopeId());
    }

    @Test
    public void findDifferentScopeIdsTest() {
        Mockito.when(entityManager.find(RoleImpl.class, roleId)).thenReturn(entityToFindOrDelete);
        Mockito.when(entityToFindOrDelete.getScopeId()).thenReturn(KapuaId.ANY);

        Assert.assertNull("Null expected.", RoleDAO.find(entityManager, scopeId, roleId));
    }

    @Test
    public void findNullEntityToFindTest() {
        Mockito.when(entityManager.find(RoleImpl.class, roleId)).thenReturn(null);

        Assert.assertNull("Null expected.", RoleDAO.find(entityManager, scopeId, roleId));
    }

    @Test
    public void findNullEntityToFindScopeIdTest() {
        Mockito.when(entityManager.find(RoleImpl.class, roleId)).thenReturn(entityToFindOrDelete);
        Mockito.when(entityToFindOrDelete.getScopeId()).thenReturn(null);

        Assert.assertTrue("True expected.", RoleDAO.find(entityManager, KapuaId.ANY, roleId) instanceof Role);
    }

    @Test(expected = NullPointerException.class)
    public void findNullEntityManagerTest() {
        RoleDAO.find(null, scopeId, roleId);
    }

    @Test
    public void findNullScopeIdTest() {
        Mockito.when(entityManager.find(RoleImpl.class, roleId)).thenReturn(entityToFindOrDelete);
        Mockito.when(entityToFindOrDelete.getScopeId()).thenReturn(KapuaId.ONE);

        Assert.assertTrue("True expected.", RoleDAO.find(entityManager, null, roleId) instanceof Role);
        Assert.assertEquals("Expected and actual values should be the same.", KapuaId.ONE, RoleDAO.find(entityManager, null, roleId).getScopeId());
    }

    @Test
    public void findNullRoleIdTest() {
        Mockito.when(entityManager.find(RoleImpl.class, null)).thenReturn(entityToFindOrDelete);
        Mockito.when(entityToFindOrDelete.getScopeId()).thenReturn(KapuaId.ONE);

        Assert.assertTrue("True expected.", RoleDAO.find(entityManager, scopeId, null) instanceof Role);
        Assert.assertEquals("Expected and actual values should be the same.", KapuaId.ONE, RoleDAO.find(entityManager, scopeId, null).getScopeId());
    }

    @Test
    public void queryTest() throws KapuaException {
        CriteriaBuilder criteriaBuilder = Mockito.mock(CriteriaBuilder.class);
        CriteriaQuery<RoleImpl> criteriaQuery1 = Mockito.mock(CriteriaQuery.class);
        CriteriaQuery<RoleImpl> criteriaQuery2 = Mockito.mock(CriteriaQuery.class);
        CriteriaQuery<RoleImpl> criteriaQuery3 = Mockito.mock(CriteriaQuery.class);
        Root<RoleImpl> entityRoot = Mockito.mock(Root.class);
        EntityType<RoleImpl> entityType = Mockito.mock(EntityType.class);
        List<String> list = new ArrayList<>();
        TypedQuery<RoleImpl> query = Mockito.mock(TypedQuery.class);

        Mockito.when(entityManager.getCriteriaBuilder()).thenReturn(criteriaBuilder);
        Mockito.when(criteriaBuilder.createQuery(RoleImpl.class)).thenReturn(criteriaQuery1);
        Mockito.when(criteriaQuery1.from(RoleImpl.class)).thenReturn(entityRoot);
        Mockito.when(entityRoot.getModel()).thenReturn(entityType);
        Mockito.when(criteriaQuery1.select(entityRoot)).thenReturn(criteriaQuery2);
        Mockito.when(criteriaQuery2.distinct(true)).thenReturn(criteriaQuery3);
        Mockito.when(kapuaQuery.getFetchAttributes()).thenReturn(list);
        Mockito.when(entityManager.createQuery(criteriaQuery1)).thenReturn(query);

        Assert.assertTrue("True expected.", RoleDAO.query(entityManager, kapuaQuery) instanceof RoleListResult);
    }

    @Test(expected = NullPointerException.class)
    public void queryNullEntityManagerTest() throws KapuaException {
        RoleDAO.query(null, kapuaQuery);
    }

    @Test(expected = NullPointerException.class)
    public void queryNullKapuaQueryTest() throws KapuaException {
        CriteriaBuilder criteriaBuilder = Mockito.mock(CriteriaBuilder.class);
        CriteriaQuery<RoleImpl> criteriaQuery1 = Mockito.mock(CriteriaQuery.class);
        CriteriaQuery<RoleImpl> criteriaQuery2 = Mockito.mock(CriteriaQuery.class);
        CriteriaQuery<RoleImpl> criteriaQuery3 = Mockito.mock(CriteriaQuery.class);
        Root<RoleImpl> entityRoot = Mockito.mock(Root.class);
        EntityType<RoleImpl> entityType = Mockito.mock(EntityType.class);
        TypedQuery<RoleImpl> query = Mockito.mock(TypedQuery.class);

        Mockito.when(entityManager.getCriteriaBuilder()).thenReturn(criteriaBuilder);
        Mockito.when(criteriaBuilder.createQuery(RoleImpl.class)).thenReturn(criteriaQuery1);
        Mockito.when(criteriaQuery1.from(RoleImpl.class)).thenReturn(entityRoot);
        Mockito.when(entityRoot.getModel()).thenReturn(entityType);
        Mockito.when(criteriaQuery1.select(entityRoot)).thenReturn(criteriaQuery2);
        Mockito.when(criteriaQuery2.distinct(true)).thenReturn(criteriaQuery3);
        Mockito.when(entityManager.createQuery(criteriaQuery1)).thenReturn(query);

        RoleDAO.query(entityManager, null);
    }

    @Test
    public void countTest() throws KapuaException {
        CriteriaBuilder criteriaBuilder = Mockito.mock(CriteriaBuilder.class);
        CriteriaQuery<Long> criteriaQuery1 = Mockito.mock(CriteriaQuery.class);
        CriteriaQuery<Long> criteriaQuery2 = Mockito.mock(CriteriaQuery.class);
        Root<RoleImpl> entityRoot = Mockito.mock(Root.class);
        TypedQuery<Long> query = Mockito.mock(TypedQuery.class);
        Selection<Long> selection = Mockito.mock(Selection.class);
        Expression<Long> expression = Mockito.mock(Expression.class);
        long[] longNumberList = {0L, 10L, 100L, 1000000L, 9223372036854775807L, -100L, -9223372036854775808L};

        Mockito.when(entityManager.getCriteriaBuilder()).thenReturn(criteriaBuilder);
        Mockito.when(criteriaBuilder.createQuery(Long.class)).thenReturn(criteriaQuery1);
        Mockito.when(criteriaQuery1.from(RoleImpl.class)).thenReturn(entityRoot);
        Mockito.when(criteriaBuilder.countDistinct(entityRoot)).thenReturn(expression);
        Mockito.when(criteriaQuery1.select(selection)).thenReturn(criteriaQuery2);
        Mockito.when(entityManager.createQuery(criteriaQuery1)).thenReturn(query);
        for (long number : longNumberList) {
            Mockito.doReturn(number).when(query).getSingleResult();

            Assert.assertEquals("Expected and actual values should be the same.", number, RoleDAO.count(entityManager, kapuaQuery));
        }
    }

    @Test(expected = NullPointerException.class)
    public void countNullEntityManagerTest() throws KapuaException {
        RoleDAO.count(null, kapuaQuery);
    }

    @Test(expected = NullPointerException.class)
    public void countNullKapuaQueryTest() throws KapuaException {
        RoleDAO.count(entityManager, null);
    }

    @Test
    public void deleteTest() throws KapuaEntityNotFoundException {
        Mockito.when(entityManager.find(RoleImpl.class, roleId)).thenReturn(entityToFindOrDelete);
        Mockito.when(entityToFindOrDelete.getScopeId()).thenReturn(KapuaId.ONE);

        Assert.assertTrue("True expected.", RoleDAO.delete(entityManager, scopeId, roleId) instanceof Role);
    }

    @Test(expected = NullPointerException.class)
    public void deleteNullEntityManagerTest() throws KapuaEntityNotFoundException {
        RoleDAO.delete(null, scopeId, roleId);
    }

    @Test
    public void deleteNullScopeIdTest() throws KapuaEntityNotFoundException {
        Mockito.when(entityManager.find(RoleImpl.class, roleId)).thenReturn(entityToFindOrDelete);

        Assert.assertTrue("True expected.", RoleDAO.delete(entityManager, null, roleId) instanceof Role);
    }

    @Test(expected = NullPointerException.class)
    public void deleteNullRoleIdTest() throws KapuaEntityNotFoundException {
        RoleDAO.delete(entityManager, scopeId, null);
    }

    @Test(expected = KapuaEntityNotFoundException.class)
    public void deleteNullEntityToDeleteTest() throws KapuaEntityNotFoundException {
        Mockito.when(entityManager.find(RoleImpl.class, roleId)).thenReturn(null);

        RoleDAO.delete(entityManager, scopeId, roleId);
    }
}