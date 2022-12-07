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
import org.eclipse.kapua.model.KapuaNamedEntityAttributes;
import org.eclipse.kapua.model.domain.Actions;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.model.query.KapuaQuery;
import org.eclipse.kapua.qa.markers.junit.JUnitTests;
import org.eclipse.kapua.service.authorization.domain.Domain;
import org.eclipse.kapua.service.authorization.domain.DomainCreator;
import org.eclipse.kapua.service.authorization.domain.DomainListResult;
import org.eclipse.kapua.service.authorization.domain.shiro.DomainDAO;
import org.eclipse.kapua.service.authorization.domain.shiro.DomainImpl;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.mockito.Mockito;

import javax.persistence.EntityExistsException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceException;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.ParameterExpression;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Selection;
import javax.persistence.metamodel.EntityType;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


@Category(JUnitTests.class)
public class DomainDAOTest {

    EntityManager entityManager;
    DomainCreator domainCreator;
    Set<Actions> actions;
    KapuaId scopeId, domainId;
    DomainImpl entityToFindOrDelete;
    KapuaQuery kapuaQuery;

    @Before
    public void initialize() {
        entityManager = Mockito.mock(EntityManager.class);
        domainCreator = Mockito.mock(DomainCreator.class);
        actions = new HashSet<>();
        actions.add(Actions.connect);
        scopeId = KapuaId.ONE;
        domainId = KapuaId.ANY;
        entityToFindOrDelete = Mockito.mock(DomainImpl.class);
        kapuaQuery = Mockito.mock(KapuaQuery.class);
    }

    @Test
    public void createTest() {
        Mockito.when(domainCreator.getName()).thenReturn("name");
        Mockito.when(domainCreator.getGroupable()).thenReturn(true);
        Mockito.when(domainCreator.getActions()).thenReturn(actions);

        Assert.assertTrue("True expected.", DomainDAO.create(entityManager, domainCreator) instanceof Domain);
        Assert.assertEquals("Expected and actual values should be the same.", "name", DomainDAO.create(entityManager, domainCreator).getName());
        Assert.assertTrue("True expected.", DomainDAO.create(entityManager, domainCreator).getGroupable());
        Assert.assertEquals("Expected and actual values should be the same.", actions, DomainDAO.create(entityManager, domainCreator).getActions());
    }

    @Test
    public void createNullActionsTest() {
        Mockito.when(domainCreator.getName()).thenReturn("name");
        Mockito.when(domainCreator.getGroupable()).thenReturn(true);
        Mockito.when(domainCreator.getActions()).thenReturn(null);

        Assert.assertTrue("True expected.", DomainDAO.create(entityManager, domainCreator) instanceof Domain);
        Assert.assertEquals("Expected and actual values should be the same.", "name", DomainDAO.create(entityManager, domainCreator).getName());
        Assert.assertTrue("True expected.", DomainDAO.create(entityManager, domainCreator).getGroupable());
        Assert.assertNull("Null expected.", DomainDAO.create(entityManager, domainCreator).getActions());
    }

    @Test(expected = KapuaEntityExistsException.class)
    public void createEntityExistsExceptionTest() {
        Mockito.when(domainCreator.getName()).thenReturn("name");
        Mockito.when(domainCreator.getGroupable()).thenReturn(true);
        Mockito.when(domainCreator.getActions()).thenReturn(actions);
        Mockito.doThrow(new EntityExistsException()).when(entityManager).flush();

        DomainDAO.create(entityManager, domainCreator);
    }

    @Test(expected = PersistenceException.class)
    public void createPersistenceExceptionTest() {
        Mockito.when(domainCreator.getName()).thenReturn("name");
        Mockito.when(domainCreator.getGroupable()).thenReturn(true);
        Mockito.when(domainCreator.getActions()).thenReturn(actions);
        Mockito.doThrow(new PersistenceException()).when(entityManager).flush();

        DomainDAO.create(entityManager, domainCreator);
    }

    @Test(expected = PersistenceException.class)
    public void createPersistenceExceptionWithNotSQLCauseTest() {
        Mockito.when(domainCreator.getName()).thenReturn("name");
        Mockito.when(domainCreator.getGroupable()).thenReturn(true);
        Mockito.when(domainCreator.getActions()).thenReturn(actions);
        Mockito.doThrow(new PersistenceException(new Exception(new Exception()))).when(entityManager).flush();

        DomainDAO.create(entityManager, domainCreator);
    }

    @Test(expected = PersistenceException.class)
    public void createPersistenceExceptionWithSQLCauseTest() {
        Mockito.when(domainCreator.getName()).thenReturn("name");
        Mockito.when(domainCreator.getGroupable()).thenReturn(true);
        Mockito.when(domainCreator.getActions()).thenReturn(actions);
        Mockito.doThrow(new PersistenceException(new SQLException("reason", "23505", new Exception()))).when(entityManager).flush();

        DomainDAO.create(entityManager, domainCreator);
    }

    @Test(expected = NullPointerException.class)
    public void createNullEntityManagerTest() {
        Mockito.when(domainCreator.getName()).thenReturn("name");
        Mockito.when(domainCreator.getGroupable()).thenReturn(true);
        Mockito.when(domainCreator.getActions()).thenReturn(actions);

        DomainDAO.create(null, domainCreator);
    }

    @Test(expected = NullPointerException.class)
    public void createNullDomainCreatorTest() {
        Mockito.when(domainCreator.getName()).thenReturn("name");
        Mockito.when(domainCreator.getGroupable()).thenReturn(true);
        Mockito.when(domainCreator.getActions()).thenReturn(actions);

        DomainDAO.create(entityManager, (DomainCreator) null);
    }

    @Test
    public void findSameScopeIdsTest() {
        Mockito.when(entityManager.find(DomainImpl.class, domainId)).thenReturn(entityToFindOrDelete);
        Mockito.when(entityToFindOrDelete.getScopeId()).thenReturn(KapuaId.ONE);

        Assert.assertTrue("True expected.", DomainDAO.find(entityManager, scopeId, domainId) instanceof Domain);
        Assert.assertEquals("Expected and actual values should be the same.", KapuaId.ONE, DomainDAO.find(entityManager, scopeId, domainId).getScopeId());
    }

    @Test
    public void findDifferentScopeIdsTest() {
        Mockito.when(entityManager.find(DomainImpl.class, domainId)).thenReturn(entityToFindOrDelete);
        Mockito.when(entityToFindOrDelete.getScopeId()).thenReturn(KapuaId.ANY);

        Assert.assertNull("Null expected.", DomainDAO.find(entityManager, scopeId, domainId));
    }

    @Test
    public void findNullEntityToFindTest() {
        Mockito.when(entityManager.find(DomainImpl.class, domainId)).thenReturn(null);

        Assert.assertNull("Null expected.", DomainDAO.find(entityManager, scopeId, domainId));
    }

    @Test
    public void findNullEntityToFindScopeIdTest() {
        Mockito.when(entityManager.find(DomainImpl.class, domainId)).thenReturn(entityToFindOrDelete);
        Mockito.when(entityToFindOrDelete.getScopeId()).thenReturn(null);

        Assert.assertTrue("True expected.", DomainDAO.find(entityManager, KapuaId.ANY, domainId) instanceof Domain);
    }

    @Test(expected = NullPointerException.class)
    public void findNullEntityManagerTest() {
        DomainDAO.find(null, scopeId, domainId);
    }

    @Test
    public void findNullScopeIdTest() {
        Mockito.when(entityManager.find(DomainImpl.class, domainId)).thenReturn(entityToFindOrDelete);
        Mockito.when(entityToFindOrDelete.getScopeId()).thenReturn(KapuaId.ONE);

        Assert.assertTrue("True expected.", DomainDAO.find(entityManager, null, domainId) instanceof Domain);
        Assert.assertEquals("Expected and actual values should be the same.", KapuaId.ONE, DomainDAO.find(entityManager, null, domainId).getScopeId());
    }

    @Test
    public void findNullDomainIdTest() {
        Mockito.when(entityManager.find(DomainImpl.class, null)).thenReturn(entityToFindOrDelete);
        Mockito.when(entityToFindOrDelete.getScopeId()).thenReturn(KapuaId.ONE);

        Assert.assertTrue("True expected.", DomainDAO.find(entityManager, scopeId, null) instanceof Domain);
        Assert.assertEquals("Expected and actual values should be the same.", KapuaId.ONE, DomainDAO.find(entityManager, scopeId, null).getScopeId());
    }

    @Test
    public void queryTest() throws KapuaException {
        CriteriaBuilder criteriaBuilder = Mockito.mock(CriteriaBuilder.class);
        CriteriaQuery<DomainImpl> criteriaQuery1 = Mockito.mock(CriteriaQuery.class);
        CriteriaQuery<DomainImpl> criteriaQuery2 = Mockito.mock(CriteriaQuery.class);
        CriteriaQuery<DomainImpl> criteriaQuery3 = Mockito.mock(CriteriaQuery.class);
        Root<DomainImpl> entityRoot = Mockito.mock(Root.class);
        EntityType<DomainImpl> entityType = Mockito.mock(EntityType.class);
        List<String> list = new ArrayList<>();
        TypedQuery<DomainImpl> query = Mockito.mock(TypedQuery.class);

        Mockito.when(entityManager.getCriteriaBuilder()).thenReturn(criteriaBuilder);
        Mockito.when(criteriaBuilder.createQuery(DomainImpl.class)).thenReturn(criteriaQuery1);
        Mockito.when(criteriaQuery1.from(DomainImpl.class)).thenReturn(entityRoot);
        Mockito.when(entityRoot.getModel()).thenReturn(entityType);
        Mockito.when(criteriaQuery1.select(entityRoot)).thenReturn(criteriaQuery2);
        Mockito.when(criteriaQuery2.distinct(true)).thenReturn(criteriaQuery3);
        Mockito.when(kapuaQuery.getFetchAttributes()).thenReturn(list);
        Mockito.when(entityManager.createQuery(criteriaQuery1)).thenReturn(query);

        Assert.assertTrue("True expected.", DomainDAO.query(entityManager, kapuaQuery) instanceof DomainListResult);
    }

    @Test(expected = NullPointerException.class)
    public void queryNullEntityManagerTest() throws KapuaException {
        DomainDAO.query(null, kapuaQuery);
    }

    @Test(expected = NullPointerException.class)
    public void queryNullKapuaQueryTest() throws KapuaException {
        CriteriaBuilder criteriaBuilder = Mockito.mock(CriteriaBuilder.class);
        CriteriaQuery<DomainImpl> criteriaQuery1 = Mockito.mock(CriteriaQuery.class);
        CriteriaQuery<DomainImpl> criteriaQuery2 = Mockito.mock(CriteriaQuery.class);
        CriteriaQuery<DomainImpl> criteriaQuery3 = Mockito.mock(CriteriaQuery.class);
        Root<DomainImpl> entityRoot = Mockito.mock(Root.class);
        EntityType<DomainImpl> entityType = Mockito.mock(EntityType.class);
        TypedQuery<DomainImpl> query = Mockito.mock(TypedQuery.class);

        Mockito.when(entityManager.getCriteriaBuilder()).thenReturn(criteriaBuilder);
        Mockito.when(criteriaBuilder.createQuery(DomainImpl.class)).thenReturn(criteriaQuery1);
        Mockito.when(criteriaQuery1.from(DomainImpl.class)).thenReturn(entityRoot);
        Mockito.when(entityRoot.getModel()).thenReturn(entityType);
        Mockito.when(criteriaQuery1.select(entityRoot)).thenReturn(criteriaQuery2);
        Mockito.when(criteriaQuery2.distinct(true)).thenReturn(criteriaQuery3);
        Mockito.when(entityManager.createQuery(criteriaQuery1)).thenReturn(query);

        DomainDAO.query(entityManager, null);
    }

    @Test
    public void countTest() throws KapuaException {
        CriteriaBuilder criteriaBuilder = Mockito.mock(CriteriaBuilder.class);
        CriteriaQuery<Long> criteriaQuery1 = Mockito.mock(CriteriaQuery.class);
        CriteriaQuery<Long> criteriaQuery2 = Mockito.mock(CriteriaQuery.class);
        Root<DomainImpl> entityRoot = Mockito.mock(Root.class);
        TypedQuery<Long> query = Mockito.mock(TypedQuery.class);
        Selection<Long> selection = Mockito.mock(Selection.class);
        Expression<Long> expression = Mockito.mock(Expression.class);
        long[] longNumberList = {0L, 10L, 100L, 1000000L, 9223372036854775807L, -100L, -9223372036854775808L};

        Mockito.when(entityManager.getCriteriaBuilder()).thenReturn(criteriaBuilder);
        Mockito.when(criteriaBuilder.createQuery(Long.class)).thenReturn(criteriaQuery1);
        Mockito.when(criteriaQuery1.from(DomainImpl.class)).thenReturn(entityRoot);
        Mockito.when(criteriaBuilder.countDistinct(entityRoot)).thenReturn(expression);
        Mockito.when(criteriaQuery1.select(selection)).thenReturn(criteriaQuery2);
        Mockito.when(entityManager.createQuery(criteriaQuery1)).thenReturn(query);
        for (long number : longNumberList) {
            Mockito.doReturn(number).when(query).getSingleResult();

            Assert.assertEquals("Expected and actual values should be the same.", number, DomainDAO.count(entityManager, kapuaQuery));
            Assert.assertNull("Null expected.", kapuaQuery.getScopeId());
        }
    }

    @Test(expected = NullPointerException.class)
    public void countNullEntityManagerTest() throws KapuaException {
        DomainDAO.count(null, kapuaQuery);
    }

    @Test(expected = NullPointerException.class)
    public void countNullKapuaQueryTest() throws KapuaException {
        DomainDAO.count(entityManager, null);
    }

    @Test
    public void deleteTest() throws KapuaEntityNotFoundException {
        Mockito.when(entityManager.find(DomainImpl.class, domainId)).thenReturn(entityToFindOrDelete);
        Mockito.when(entityToFindOrDelete.getScopeId()).thenReturn(KapuaId.ONE);

        Assert.assertTrue("True expected.", DomainDAO.delete(entityManager, scopeId, domainId) instanceof Domain);
    }

    @Test(expected = NullPointerException.class)
    public void deleteNullEntityManagerTest() throws KapuaEntityNotFoundException {
        DomainDAO.delete(null, scopeId, domainId);
    }

    @Test
    public void deleteNullScopeIdTest() throws KapuaEntityNotFoundException {
        Mockito.when(entityManager.find(DomainImpl.class, domainId)).thenReturn(entityToFindOrDelete);

        Assert.assertTrue("True expected.", DomainDAO.delete(entityManager, null, domainId) instanceof Domain);
    }

    @Test(expected = NullPointerException.class)
    public void deleteNullDomainIdTest() throws KapuaEntityNotFoundException {
        DomainDAO.delete(entityManager, scopeId, null);
    }

    @Test(expected = KapuaEntityNotFoundException.class)
    public void deleteNullEntityToDeleteTest() throws KapuaEntityNotFoundException {
        Mockito.when(entityManager.find(DomainImpl.class, domainId)).thenReturn(null);

        DomainDAO.delete(entityManager, scopeId, domainId);
    }

    @Test
    public void findByNameEmptyListTest() {
        CriteriaBuilder criteriaBuilder = Mockito.mock(CriteriaBuilder.class);
        CriteriaQuery<DomainImpl> criteriaQuery1 = Mockito.mock(CriteriaQuery.class);
        CriteriaQuery<DomainImpl> criteriaQuery2 = Mockito.mock(CriteriaQuery.class);
        Root<DomainImpl> entityRoot = Mockito.mock(Root.class);
        ParameterExpression<String> parameterExpressionName = Mockito.mock(ParameterExpression.class);
        Predicate namePredicate = Mockito.mock(Predicate.class);
        List<DomainImpl> list = new ArrayList<>();
        TypedQuery<DomainImpl> query1 = Mockito.mock(TypedQuery.class);
        TypedQuery<DomainImpl> query2 = Mockito.mock(TypedQuery.class);

        Mockito.when(entityManager.getCriteriaBuilder()).thenReturn(criteriaBuilder);
        Mockito.when(criteriaBuilder.createQuery(DomainImpl.class)).thenReturn(criteriaQuery1);
        Mockito.when(criteriaQuery1.from(DomainImpl.class)).thenReturn(entityRoot);
        Mockito.when(criteriaQuery1.select(entityRoot)).thenReturn(criteriaQuery2);
        Mockito.when(criteriaBuilder.parameter(String.class, KapuaNamedEntityAttributes.NAME)).thenReturn(parameterExpressionName);
        Mockito.when(criteriaBuilder.equal(entityRoot.get(KapuaNamedEntityAttributes.NAME), parameterExpressionName)).thenReturn(namePredicate);
        Mockito.when(entityManager.createQuery(criteriaQuery1)).thenReturn(query1);
        Mockito.when(query1.setParameter(parameterExpressionName.getName(), "name")).thenReturn(query2);
        Mockito.when(query1.getResultList()).thenReturn(list);

        Assert.assertNull("Null expected.", DomainDAO.findByName(entityManager, "name"));
    }

    @Test
    public void findByNameTest() {
        CriteriaBuilder criteriaBuilder = Mockito.mock(CriteriaBuilder.class);
        CriteriaQuery<DomainImpl> criteriaQuery1 = Mockito.mock(CriteriaQuery.class);
        CriteriaQuery<DomainImpl> criteriaQuery2 = Mockito.mock(CriteriaQuery.class);
        Root<DomainImpl> entityRoot = Mockito.mock(Root.class);
        ParameterExpression<String> parameterExpressionName = Mockito.mock(ParameterExpression.class);
        Predicate namePredicate = Mockito.mock(Predicate.class);
        List<DomainImpl> list = new ArrayList<>();
        list.add(Mockito.mock(DomainImpl.class));
        TypedQuery<DomainImpl> query1 = Mockito.mock(TypedQuery.class);
        TypedQuery<DomainImpl> query2 = Mockito.mock(TypedQuery.class);

        Mockito.when(entityManager.getCriteriaBuilder()).thenReturn(criteriaBuilder);
        Mockito.when(criteriaBuilder.createQuery(DomainImpl.class)).thenReturn(criteriaQuery1);
        Mockito.when(criteriaQuery1.from(DomainImpl.class)).thenReturn(entityRoot);
        Mockito.when(criteriaQuery1.select(entityRoot)).thenReturn(criteriaQuery2);
        Mockito.when(criteriaBuilder.parameter(String.class, KapuaNamedEntityAttributes.NAME)).thenReturn(parameterExpressionName);
        Mockito.when(criteriaBuilder.equal(entityRoot.get(KapuaNamedEntityAttributes.NAME), parameterExpressionName)).thenReturn(namePredicate);
        Mockito.when(entityManager.createQuery(criteriaQuery1)).thenReturn(query1);
        Mockito.when(query1.setParameter(parameterExpressionName.getName(), "name")).thenReturn(query2);
        Mockito.when(query1.getResultList()).thenReturn(list);

        Assert.assertTrue("True expected.", DomainDAO.findByName(entityManager, "name") instanceof Domain);
        Assert.assertNotNull("NotNull expected.", DomainDAO.findByName(entityManager, "name"));
    }

    @Test(expected = NonUniqueResultException.class)
    public void findByNameNonUniqueResultTest() {
        CriteriaBuilder criteriaBuilder = Mockito.mock(CriteriaBuilder.class);
        CriteriaQuery<DomainImpl> criteriaQuery1 = Mockito.mock(CriteriaQuery.class);
        CriteriaQuery<DomainImpl> criteriaQuery2 = Mockito.mock(CriteriaQuery.class);
        Root<DomainImpl> entityRoot = Mockito.mock(Root.class);
        ParameterExpression<String> parameterExpressionName = Mockito.mock(ParameterExpression.class);
        Predicate namePredicate = Mockito.mock(Predicate.class);
        List<DomainImpl> list = new ArrayList<>();
        list.add(Mockito.mock(DomainImpl.class));
        list.add(Mockito.mock(DomainImpl.class));
        TypedQuery<DomainImpl> query1 = Mockito.mock(TypedQuery.class);
        TypedQuery<DomainImpl> query2 = Mockito.mock(TypedQuery.class);

        Mockito.when(entityManager.getCriteriaBuilder()).thenReturn(criteriaBuilder);
        Mockito.when(criteriaBuilder.createQuery(DomainImpl.class)).thenReturn(criteriaQuery1);
        Mockito.when(criteriaQuery1.from(DomainImpl.class)).thenReturn(entityRoot);
        Mockito.when(criteriaQuery1.select(entityRoot)).thenReturn(criteriaQuery2);
        Mockito.when(criteriaBuilder.parameter(String.class, KapuaNamedEntityAttributes.NAME)).thenReturn(parameterExpressionName);
        Mockito.when(criteriaBuilder.equal(entityRoot.get(KapuaNamedEntityAttributes.NAME), parameterExpressionName)).thenReturn(namePredicate);
        Mockito.when(entityManager.createQuery(criteriaQuery1)).thenReturn(query1);
        Mockito.when(query1.setParameter(parameterExpressionName.getName(), "name")).thenReturn(query2);
        Mockito.when(query1.getResultList()).thenReturn(list);

        DomainDAO.findByName(entityManager, "name");
    }

    @Test(expected = NullPointerException.class)
    public void findByNameNullEntityManagerTest() {
        DomainDAO.findByName(null, "name");
    }

    @Test
    public void findByNameNullNameTest() {
        CriteriaBuilder criteriaBuilder = Mockito.mock(CriteriaBuilder.class);
        CriteriaQuery<DomainImpl> criteriaQuery1 = Mockito.mock(CriteriaQuery.class);
        CriteriaQuery<DomainImpl> criteriaQuery2 = Mockito.mock(CriteriaQuery.class);
        Root<DomainImpl> entityRoot = Mockito.mock(Root.class);
        ParameterExpression<String> parameterExpressionName = Mockito.mock(ParameterExpression.class);
        Predicate namePredicate = Mockito.mock(Predicate.class);
        List<DomainImpl> list = new ArrayList<>();
        list.add(Mockito.mock(DomainImpl.class));
        TypedQuery<DomainImpl> query1 = Mockito.mock(TypedQuery.class);
        TypedQuery<DomainImpl> query2 = Mockito.mock(TypedQuery.class);

        Mockito.when(entityManager.getCriteriaBuilder()).thenReturn(criteriaBuilder);
        Mockito.when(criteriaBuilder.createQuery(DomainImpl.class)).thenReturn(criteriaQuery1);
        Mockito.when(criteriaQuery1.from(DomainImpl.class)).thenReturn(entityRoot);
        Mockito.when(criteriaQuery1.select(entityRoot)).thenReturn(criteriaQuery2);
        Mockito.when(criteriaBuilder.parameter(String.class, KapuaNamedEntityAttributes.NAME)).thenReturn(parameterExpressionName);
        Mockito.when(criteriaBuilder.equal(entityRoot.get(KapuaNamedEntityAttributes.NAME), parameterExpressionName)).thenReturn(namePredicate);
        Mockito.when(entityManager.createQuery(criteriaQuery1)).thenReturn(query1);
        Mockito.when(query1.setParameter(parameterExpressionName.getName(), null)).thenReturn(query2);
        Mockito.when(query1.getResultList()).thenReturn(list);

        Assert.assertTrue("True expected.", DomainDAO.findByName(entityManager, null) instanceof Domain);
        Assert.assertNotNull("NotNull expected.", DomainDAO.findByName(entityManager, null));
    }
}