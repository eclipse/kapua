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
import org.eclipse.kapua.service.authorization.group.Group;
import org.eclipse.kapua.service.authorization.group.GroupCreator;
import org.eclipse.kapua.service.authorization.group.GroupListResult;
import org.eclipse.kapua.service.authorization.group.shiro.GroupDAO;
import org.eclipse.kapua.service.authorization.group.shiro.GroupImpl;
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
public class GroupDAOTest {

    EntityManager entityManager;
    GroupCreator groupCreator;
    GroupImpl group, entityToFindOrDelete;
    Date createdOn;
    KapuaId createdBy, scopeId, groupId;
    KapuaQuery kapuaQuery;

    @Before
    public void initialize() {
        entityManager = Mockito.mock(EntityManager.class);
        groupCreator = Mockito.mock(GroupCreator.class);
        group = Mockito.mock(GroupImpl.class);
        entityToFindOrDelete = Mockito.mock(GroupImpl.class);
        createdOn = new Date();
        createdBy = KapuaId.ONE;
        scopeId = KapuaId.ONE;
        groupId = KapuaId.ANY;
        kapuaQuery = Mockito.mock(KapuaQuery.class);
    }

    @Test
    public void createTest() throws KapuaException {
        Mockito.when(groupCreator.getScopeId()).thenReturn(KapuaId.ANY);
        Mockito.when(groupCreator.getName()).thenReturn("name");
        Mockito.when(groupCreator.getDescription()).thenReturn("description");

        Assert.assertTrue("True expected.", GroupDAO.create(entityManager, groupCreator) instanceof Group);
        Assert.assertEquals("Expected and actual values should be the same.", KapuaId.ANY, GroupDAO.create(entityManager, groupCreator).getScopeId());
        Assert.assertEquals("Expected and actual values should be the same.", "name", GroupDAO.create(entityManager, groupCreator).getName());
        Assert.assertEquals("Expected and actual values should be the same.", "description", GroupDAO.create(entityManager, groupCreator).getDescription());
    }

    @Test(expected = NullPointerException.class)
    public void createNullEntityManagerTest() throws KapuaException {
        Mockito.when(groupCreator.getScopeId()).thenReturn(KapuaId.ANY);
        Mockito.when(groupCreator.getName()).thenReturn("name");
        Mockito.when(groupCreator.getDescription()).thenReturn("description");

        GroupDAO.create(null, groupCreator);
    }

    @Test(expected = NullPointerException.class)
    public void createNullGroupCreatorTest() throws KapuaException {
        GroupDAO.create(entityManager, (GroupCreator) null);
    }

    @Test(expected = KapuaEntityExistsException.class)
    public void createEntityExistsExceptionTest() throws KapuaException {
        Mockito.when(groupCreator.getScopeId()).thenReturn(KapuaId.ANY);
        Mockito.when(groupCreator.getName()).thenReturn("name");
        Mockito.when(groupCreator.getDescription()).thenReturn("description");
        Mockito.doThrow(new EntityExistsException()).when(entityManager).flush();

        GroupDAO.create(entityManager, groupCreator);
    }

    @Test(expected = PersistenceException.class)
    public void createPersistenceExceptionTest() throws KapuaException {
        Mockito.when(groupCreator.getScopeId()).thenReturn(KapuaId.ANY);
        Mockito.when(groupCreator.getName()).thenReturn("name");
        Mockito.when(groupCreator.getDescription()).thenReturn("description");
        Mockito.doThrow(new PersistenceException()).when(entityManager).flush();

        GroupDAO.create(entityManager, groupCreator);
    }

    @Test(expected = PersistenceException.class)
    public void createPersistenceExceptionWithNotSQLCauseTest() throws KapuaException {
        Mockito.when(groupCreator.getScopeId()).thenReturn(KapuaId.ANY);
        Mockito.when(groupCreator.getName()).thenReturn("name");
        Mockito.when(groupCreator.getDescription()).thenReturn("description");
        Mockito.doThrow(new PersistenceException(new Exception(new Exception()))).when(entityManager).flush();

        GroupDAO.create(entityManager, groupCreator);
    }

    @Test(expected = PersistenceException.class)
    public void createPersistenceExceptionWithSQLCauseTest() throws KapuaException {
        Mockito.when(groupCreator.getScopeId()).thenReturn(KapuaId.ANY);
        Mockito.when(groupCreator.getName()).thenReturn("name");
        Mockito.when(groupCreator.getDescription()).thenReturn("description");
        Mockito.doThrow(new PersistenceException(new SQLException("reason", "23505", new Exception()))).when(entityManager).flush();

        GroupDAO.create(entityManager, groupCreator);
    }

    @Test
    public void updateTest() throws KapuaEntityNotFoundException {
        Mockito.when(group.getId()).thenReturn(KapuaId.ONE);
        Mockito.when(entityManager.find(GroupImpl.class, KapuaId.ONE)).thenReturn(entityToFindOrDelete);
        Mockito.when(entityToFindOrDelete.getCreatedOn()).thenReturn(createdOn);
        Mockito.when(entityToFindOrDelete.getCreatedBy()).thenReturn(createdBy);

        Assert.assertTrue("True expected.", GroupDAO.update(entityManager, group) instanceof Group);
        Assert.assertEquals("Expected and actual values should be the same.", createdBy, GroupDAO.update(entityManager, group).getCreatedBy());
        Assert.assertEquals("Expected and actual values should be the same.", createdOn, GroupDAO.update(entityManager, group).getCreatedOn());
    }

    @Test(expected = KapuaEntityNotFoundException.class)
    public void updateEntityNotFoundExceptionTest() throws KapuaEntityNotFoundException {
        Mockito.when(group.getId()).thenReturn(KapuaId.ONE);
        Mockito.when(entityManager.find(GroupImpl.class, KapuaId.ONE)).thenReturn(null);

        GroupDAO.update(entityManager, group);
    }

    @Test(expected = NullPointerException.class)
    public void updateNullEntityManagerTest() throws KapuaEntityNotFoundException {
        Mockito.when(group.getId()).thenReturn(KapuaId.ONE);

        GroupDAO.update(null, group);
    }

    @Test(expected = NullPointerException.class)
    public void updateNullGroupTest() throws KapuaEntityNotFoundException {
        Mockito.when(entityManager.find(GroupImpl.class, KapuaId.ONE)).thenReturn(entityToFindOrDelete);
        Mockito.when(entityToFindOrDelete.getCreatedOn()).thenReturn(createdOn);
        Mockito.when(entityToFindOrDelete.getCreatedBy()).thenReturn(createdBy);

        GroupDAO.update(entityManager, null);
    }

    @Test
    public void findSameScopeIdsTest() {
        Mockito.when(entityManager.find(GroupImpl.class, groupId)).thenReturn(entityToFindOrDelete);
        Mockito.when(entityToFindOrDelete.getScopeId()).thenReturn(KapuaId.ONE);

        Assert.assertTrue("True expected.", GroupDAO.find(entityManager, scopeId, groupId) instanceof Group);
        Assert.assertEquals("Expected and actual values should be the same.", KapuaId.ONE, GroupDAO.find(entityManager, scopeId, groupId).getScopeId());
    }

    @Test
    public void findDifferentScopeIdsTest() {
        Mockito.when(entityManager.find(GroupImpl.class, groupId)).thenReturn(entityToFindOrDelete);
        Mockito.when(entityToFindOrDelete.getScopeId()).thenReturn(KapuaId.ANY);

        Assert.assertNull("Null expected.", GroupDAO.find(entityManager, scopeId, groupId));
    }

    @Test
    public void findNullEntityToFindTest() {
        Mockito.when(entityManager.find(GroupImpl.class, groupId)).thenReturn(null);

        Assert.assertNull("Null expected.", GroupDAO.find(entityManager, scopeId, groupId));
    }

    @Test
    public void findNullEntityToFindScopeIdTest() {
        Mockito.when(entityManager.find(GroupImpl.class, groupId)).thenReturn(entityToFindOrDelete);
        Mockito.when(entityToFindOrDelete.getScopeId()).thenReturn(null);

        Assert.assertTrue("True expected.", GroupDAO.find(entityManager, KapuaId.ANY, groupId) instanceof Group);
        Assert.assertNull("Null expected.", GroupDAO.find(entityManager, scopeId, groupId));
    }

    @Test(expected = NullPointerException.class)
    public void findNullEntityManagerTest() {
        GroupDAO.find(null, scopeId, groupId);
    }

    @Test
    public void findNullScopeIdTest() {
        Mockito.when(entityManager.find(GroupImpl.class, groupId)).thenReturn(entityToFindOrDelete);
        Mockito.when(entityToFindOrDelete.getScopeId()).thenReturn(KapuaId.ONE);

        Assert.assertTrue("True expected.", GroupDAO.find(entityManager, null, groupId) instanceof Group);
        Assert.assertEquals("Expected and actual values should be the same.", KapuaId.ONE, GroupDAO.find(entityManager, scopeId, groupId).getScopeId());
    }

    @Test
    public void findNullGroupIdTest() {
        Mockito.when(entityManager.find(GroupImpl.class, null)).thenReturn(entityToFindOrDelete);
        Mockito.when(entityToFindOrDelete.getScopeId()).thenReturn(KapuaId.ONE);

        Assert.assertTrue("True expected.", GroupDAO.find(entityManager, scopeId, null) instanceof Group);
        Assert.assertEquals("Expected and actual values should be the same.", KapuaId.ONE, GroupDAO.find(entityManager, scopeId, null).getScopeId());
    }

    @Test
    public void queryTest() throws KapuaException {
        CriteriaBuilder criteriaBuilder = Mockito.mock(CriteriaBuilder.class);
        CriteriaQuery<GroupImpl> criteriaQuery1 = Mockito.mock(CriteriaQuery.class);
        CriteriaQuery<GroupImpl> criteriaQuery2 = Mockito.mock(CriteriaQuery.class);
        CriteriaQuery<GroupImpl> criteriaQuery3 = Mockito.mock(CriteriaQuery.class);
        Root<GroupImpl> entityRoot = Mockito.mock(Root.class);
        EntityType<GroupImpl> entityType = Mockito.mock(EntityType.class);
        List<String> list = new ArrayList<>();
        TypedQuery<GroupImpl> query = Mockito.mock(TypedQuery.class);

        Mockito.when(entityManager.getCriteriaBuilder()).thenReturn(criteriaBuilder);
        Mockito.when(criteriaBuilder.createQuery(GroupImpl.class)).thenReturn(criteriaQuery1);
        Mockito.when(criteriaQuery1.from(GroupImpl.class)).thenReturn(entityRoot);
        Mockito.when(entityRoot.getModel()).thenReturn(entityType);
        Mockito.when(criteriaQuery1.select(entityRoot)).thenReturn(criteriaQuery2);
        Mockito.when(criteriaQuery2.distinct(true)).thenReturn(criteriaQuery3);
        Mockito.when(kapuaQuery.getFetchAttributes()).thenReturn(list);
        Mockito.when(entityManager.createQuery(criteriaQuery1)).thenReturn(query);

        Assert.assertTrue("True expected.", GroupDAO.query(entityManager, kapuaQuery) instanceof GroupListResult);
    }

    @Test(expected = NullPointerException.class)
    public void queryNullEntityManagerTest() throws KapuaException {
        GroupDAO.query(null, kapuaQuery);
    }

    @Test(expected = NullPointerException.class)
    public void queryNullKapuaQueryTest() throws KapuaException {
        CriteriaBuilder criteriaBuilder = Mockito.mock(CriteriaBuilder.class);
        CriteriaQuery<GroupImpl> criteriaQuery1 = Mockito.mock(CriteriaQuery.class);
        CriteriaQuery<GroupImpl> criteriaQuery2 = Mockito.mock(CriteriaQuery.class);
        CriteriaQuery<GroupImpl> criteriaQuery3 = Mockito.mock(CriteriaQuery.class);
        Root<GroupImpl> entityRoot = Mockito.mock(Root.class);
        EntityType<GroupImpl> entityType = Mockito.mock(EntityType.class);

        Mockito.when(entityManager.getCriteriaBuilder()).thenReturn(criteriaBuilder);
        Mockito.when(criteriaBuilder.createQuery(GroupImpl.class)).thenReturn(criteriaQuery1);
        Mockito.when(criteriaQuery1.from(GroupImpl.class)).thenReturn(entityRoot);
        Mockito.when(entityRoot.getModel()).thenReturn(entityType);
        Mockito.when(criteriaQuery1.select(entityRoot)).thenReturn(criteriaQuery2);
        Mockito.when(criteriaQuery2.distinct(true)).thenReturn(criteriaQuery3);

        GroupDAO.query(entityManager, null);
    }

    @Test
    public void countTest() throws KapuaException {
        CriteriaBuilder criteriaBuilder = Mockito.mock(CriteriaBuilder.class);
        CriteriaQuery<Long> criteriaQuery1 = Mockito.mock(CriteriaQuery.class);
        CriteriaQuery<Long> criteriaQuery2 = Mockito.mock(CriteriaQuery.class);
        Root<GroupImpl> entityRoot = Mockito.mock(Root.class);
        TypedQuery<Long> query = Mockito.mock(TypedQuery.class);
        Selection<Long> selection = Mockito.mock(Selection.class);
        Expression<Long> expression = Mockito.mock(Expression.class);
        long[] longNumberList = {0L, 10L, 100L, 1000000L, 9223372036854775807L, -100L, -9223372036854775808L};

        Mockito.when(entityManager.getCriteriaBuilder()).thenReturn(criteriaBuilder);
        Mockito.when(criteriaBuilder.createQuery(Long.class)).thenReturn(criteriaQuery1);
        Mockito.when(criteriaQuery1.from(GroupImpl.class)).thenReturn(entityRoot);
        Mockito.when(criteriaBuilder.countDistinct(entityRoot)).thenReturn(expression);
        Mockito.when(criteriaQuery1.select(selection)).thenReturn(criteriaQuery2);
        Mockito.when(entityManager.createQuery(criteriaQuery1)).thenReturn(query);
        for (long number : longNumberList) {
            Mockito.doReturn(number).when(query).getSingleResult();

            Assert.assertEquals("Expected and actual values should be the same.", number, GroupDAO.count(entityManager, kapuaQuery));
        }
    }

    @Test(expected = NullPointerException.class)
    public void countNullEntityManagerTest() throws KapuaException {
        GroupDAO.count(null, kapuaQuery);
    }

    @Test(expected = NullPointerException.class)
    public void countNullKapuaQueryTest() throws KapuaException {
        GroupDAO.count(entityManager, null);
    }

    @Test
    public void deleteTest() throws KapuaEntityNotFoundException {
        Mockito.when(entityManager.find(GroupImpl.class, groupId)).thenReturn(entityToFindOrDelete);
        Mockito.when(entityToFindOrDelete.getScopeId()).thenReturn(KapuaId.ONE);

        Assert.assertTrue("True expected.", GroupDAO.delete(entityManager, scopeId, groupId) instanceof Group);
    }

    @Test(expected = NullPointerException.class)
    public void deleteNullEntityManagerTest() throws KapuaEntityNotFoundException {
        GroupDAO.delete(null, scopeId, groupId);
    }

    @Test
    public void deleteNullScopeIdTest() throws KapuaEntityNotFoundException {
        Mockito.when(entityManager.find(GroupImpl.class, groupId)).thenReturn(entityToFindOrDelete);
        Assert.assertTrue("True expected.", GroupDAO.delete(entityManager, null, groupId) instanceof Group);
    }

    @Test(expected = NullPointerException.class)
    public void deleteNullGroupIdIdTest() throws KapuaEntityNotFoundException {
        GroupDAO.delete(entityManager, scopeId, null);
    }

    @Test(expected = KapuaEntityNotFoundException.class)
    public void deleteNullEntityToDeleteTest() throws KapuaEntityNotFoundException {
        Mockito.when(entityManager.find(GroupImpl.class, groupId)).thenReturn(null);

        GroupDAO.delete(entityManager, scopeId, groupId);
    }
}