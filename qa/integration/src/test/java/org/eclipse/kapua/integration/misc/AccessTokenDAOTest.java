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
import org.eclipse.kapua.service.authentication.token.AccessToken;
import org.eclipse.kapua.service.authentication.token.AccessTokenAttributes;
import org.eclipse.kapua.service.authentication.token.AccessTokenCreator;
import org.eclipse.kapua.service.authentication.token.AccessTokenListResult;
import org.eclipse.kapua.service.authentication.token.shiro.AccessTokenDAO;
import org.eclipse.kapua.service.authentication.token.shiro.AccessTokenImpl;
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
import java.util.Date;
import java.util.List;


@Category(JUnitTests.class)
public class AccessTokenDAOTest {

    EntityManager entityManager;
    AccessTokenCreator accessTokenCreator;
    Date expirationDate, refreshExpirationDate, createdOn;
    AccessTokenImpl accessToken, entityToFindOrDelete;
    KapuaId createdBy, scopeId, accessTokenId;
    KapuaQuery kapuaQuery;

    @Before
    public void initialize() {
        entityManager = Mockito.mock(EntityManager.class);
        accessTokenCreator = Mockito.mock(AccessTokenCreator.class);
        expirationDate = new Date();
        refreshExpirationDate = new Date();
        accessToken = Mockito.mock(AccessTokenImpl.class);
        entityToFindOrDelete = Mockito.mock(AccessTokenImpl.class);
        createdOn = new Date();
        createdBy = KapuaId.ONE;
        scopeId = KapuaId.ONE;
        accessTokenId = KapuaId.ONE;
        kapuaQuery = Mockito.mock(KapuaQuery.class);
    }

    @Test
    public void createTest() throws KapuaException {
        Mockito.when(accessTokenCreator.getScopeId()).thenReturn(KapuaId.ONE);
        Mockito.when(accessTokenCreator.getUserId()).thenReturn(KapuaId.ONE);
        Mockito.when(accessTokenCreator.getTokenId()).thenReturn("token id");
        Mockito.when(accessTokenCreator.getExpiresOn()).thenReturn(expirationDate);
        Mockito.when(accessTokenCreator.getRefreshToken()).thenReturn("refresh token");
        Mockito.when(accessTokenCreator.getRefreshExpiresOn()).thenReturn(refreshExpirationDate);

        Assert.assertTrue("True expected.", AccessTokenDAO.create(entityManager, accessTokenCreator) instanceof AccessToken);
        Assert.assertEquals("Expected and actual values should be the same.", KapuaId.ONE, AccessTokenDAO.create(entityManager, accessTokenCreator).getScopeId());
        Assert.assertEquals("Expected and actual values should be the same.", KapuaId.ONE, AccessTokenDAO.create(entityManager, accessTokenCreator).getUserId());
        Assert.assertEquals("Expected and actual values should be the same.", "token id", AccessTokenDAO.create(entityManager, accessTokenCreator).getTokenId());
        Assert.assertEquals("Expected and actual values should be the same.", expirationDate, AccessTokenDAO.create(entityManager, accessTokenCreator).getExpiresOn());
        Assert.assertEquals("Expected and actual values should be the same.", "refresh token", AccessTokenDAO.create(entityManager, accessTokenCreator).getRefreshToken());
        Assert.assertEquals("Expected and actual values should be the same.", refreshExpirationDate, AccessTokenDAO.create(entityManager, accessTokenCreator).getRefreshExpiresOn());
    }

    @Test(expected = KapuaEntityExistsException.class)
    public void createEntityExistsExceptionTest() throws KapuaException {
        Mockito.when(accessTokenCreator.getScopeId()).thenReturn(KapuaId.ONE);
        Mockito.when(accessTokenCreator.getUserId()).thenReturn(KapuaId.ONE);
        Mockito.when(accessTokenCreator.getTokenId()).thenReturn("token id");
        Mockito.when(accessTokenCreator.getExpiresOn()).thenReturn(expirationDate);
        Mockito.when(accessTokenCreator.getRefreshToken()).thenReturn("refresh token");
        Mockito.when(accessTokenCreator.getRefreshExpiresOn()).thenReturn(refreshExpirationDate);
        Mockito.doThrow(new EntityExistsException()).when(entityManager).flush();

        AccessTokenDAO.create(entityManager, accessTokenCreator);
    }

    @Test(expected = PersistenceException.class)
    public void createPersistenceExceptionTest() throws KapuaException {
        Mockito.when(accessTokenCreator.getScopeId()).thenReturn(KapuaId.ONE);
        Mockito.when(accessTokenCreator.getUserId()).thenReturn(KapuaId.ONE);
        Mockito.when(accessTokenCreator.getTokenId()).thenReturn("token id");
        Mockito.when(accessTokenCreator.getExpiresOn()).thenReturn(expirationDate);
        Mockito.when(accessTokenCreator.getRefreshToken()).thenReturn("refresh token");
        Mockito.when(accessTokenCreator.getRefreshExpiresOn()).thenReturn(refreshExpirationDate);
        Mockito.doThrow(new PersistenceException()).when(entityManager).flush();

        AccessTokenDAO.create(entityManager, accessTokenCreator);
    }

    @Test(expected = PersistenceException.class)
    public void createPersistenceExceptionWithNotSQLCauseTest() throws KapuaException {
        Mockito.when(accessTokenCreator.getScopeId()).thenReturn(KapuaId.ONE);
        Mockito.when(accessTokenCreator.getUserId()).thenReturn(KapuaId.ONE);
        Mockito.when(accessTokenCreator.getTokenId()).thenReturn("token id");
        Mockito.when(accessTokenCreator.getExpiresOn()).thenReturn(expirationDate);
        Mockito.when(accessTokenCreator.getRefreshToken()).thenReturn("refresh token");
        Mockito.when(accessTokenCreator.getRefreshExpiresOn()).thenReturn(refreshExpirationDate);
        Mockito.doThrow(new PersistenceException(new Exception(new Exception()))).when(entityManager).flush();

        AccessTokenDAO.create(entityManager, accessTokenCreator);
    }

    @Test(expected = PersistenceException.class)
    public void createPersistenceExceptionWithSQLCauseTest() throws KapuaException {
        Mockito.when(accessTokenCreator.getScopeId()).thenReturn(KapuaId.ONE);
        Mockito.when(accessTokenCreator.getUserId()).thenReturn(KapuaId.ONE);
        Mockito.when(accessTokenCreator.getTokenId()).thenReturn("token id");
        Mockito.when(accessTokenCreator.getExpiresOn()).thenReturn(expirationDate);
        Mockito.when(accessTokenCreator.getRefreshToken()).thenReturn("refresh token");
        Mockito.when(accessTokenCreator.getRefreshExpiresOn()).thenReturn(refreshExpirationDate);
        Mockito.doThrow(new PersistenceException(new SQLException("reason", "23505", new Exception()))).when(entityManager).flush();

        AccessTokenDAO.create(entityManager, accessTokenCreator);
    }

    @Test(expected = NullPointerException.class)
    public void createNullEntityManagerTest() throws KapuaException {
        Mockito.when(accessTokenCreator.getScopeId()).thenReturn(KapuaId.ONE);
        Mockito.when(accessTokenCreator.getUserId()).thenReturn(KapuaId.ONE);
        Mockito.when(accessTokenCreator.getTokenId()).thenReturn("token id");
        Mockito.when(accessTokenCreator.getExpiresOn()).thenReturn(expirationDate);
        Mockito.when(accessTokenCreator.getRefreshToken()).thenReturn("refresh token");
        Mockito.when(accessTokenCreator.getRefreshExpiresOn()).thenReturn(refreshExpirationDate);

        AccessTokenDAO.create(null, accessTokenCreator);
    }

    @Test(expected = NullPointerException.class)
    public void createNullAccessTokenCreatorTest() throws KapuaException {
        AccessTokenDAO.create(entityManager, (AccessTokenCreator) null);
    }

    @Test
    public void updateTest() throws KapuaException {
        Mockito.when(accessToken.getId()).thenReturn(KapuaId.ONE);
        Mockito.when(entityManager.find(AccessTokenImpl.class, KapuaId.ONE)).thenReturn(entityToFindOrDelete);
        Mockito.when(entityToFindOrDelete.getCreatedOn()).thenReturn(createdOn);
        Mockito.when(entityToFindOrDelete.getCreatedBy()).thenReturn(createdBy);

        Assert.assertTrue("True expected.", AccessTokenDAO.update(entityManager, accessToken) instanceof AccessToken);
        Assert.assertEquals("Expected and actual values should be the same.", createdBy, AccessTokenDAO.update(entityManager, accessToken).getCreatedBy());
        Assert.assertEquals("Expected and actual values should be the same.", createdOn, AccessTokenDAO.update(entityManager, accessToken).getCreatedOn());
    }

    @Test(expected = NullPointerException.class)
    public void updateNullEntityManagerTest() throws KapuaException {
        AccessTokenDAO.update(null, accessToken);
    }

    @Test(expected = NullPointerException.class)
    public void updateNullAccessTokenTest() throws KapuaException {
        Mockito.when(entityManager.find(AccessTokenImpl.class, KapuaId.ONE)).thenReturn(entityToFindOrDelete);
        Mockito.when(entityToFindOrDelete.getCreatedOn()).thenReturn(createdOn);
        Mockito.when(entityToFindOrDelete.getCreatedBy()).thenReturn(createdBy);

        AccessTokenDAO.update(entityManager, null);
    }

    @Test
    public void findSameScopeIdsTest() {
        Mockito.when(entityManager.find(AccessTokenImpl.class, accessTokenId)).thenReturn(entityToFindOrDelete);
        Mockito.when(entityToFindOrDelete.getScopeId()).thenReturn(KapuaId.ONE);

        Assert.assertTrue("True expected.", AccessTokenDAO.find(entityManager, scopeId, accessTokenId) instanceof AccessToken);
        Assert.assertEquals("Expected and actual values should be the same.", KapuaId.ONE, AccessTokenDAO.find(entityManager, scopeId, accessTokenId).getScopeId());
    }

    @Test
    public void findDifferentScopeIdsTest() {
        Mockito.when(entityManager.find(AccessTokenImpl.class, accessTokenId)).thenReturn(entityToFindOrDelete);
        Mockito.when(entityToFindOrDelete.getScopeId()).thenReturn(KapuaId.ANY);

        Assert.assertNull("Null expected.", AccessTokenDAO.find(entityManager, scopeId, accessTokenId));
    }

    @Test
    public void findNullEntityToFindTest() {
        Mockito.when(entityManager.find(AccessTokenImpl.class, accessTokenId)).thenReturn(null);

        Assert.assertNull("Null expected.", AccessTokenDAO.find(entityManager, scopeId, accessTokenId));
    }

    @Test
    public void findNullEntityToFindScopeIdTest() {
        Mockito.when(entityManager.find(AccessTokenImpl.class, accessTokenId)).thenReturn(entityToFindOrDelete);
        Mockito.when(entityToFindOrDelete.getScopeId()).thenReturn(null);

        Assert.assertTrue("True expected.", AccessTokenDAO.find(entityManager, KapuaId.ANY, accessTokenId) instanceof AccessToken);
    }

    @Test(expected = NullPointerException.class)
    public void findNullEntityManagerTest() {
        AccessTokenDAO.find(null, scopeId, accessTokenId);
    }

    @Test
    public void findNullScopeIdTest() {
        Mockito.when(entityManager.find(AccessTokenImpl.class, accessTokenId)).thenReturn(entityToFindOrDelete);
        Mockito.when(entityToFindOrDelete.getScopeId()).thenReturn(KapuaId.ONE);

        Assert.assertTrue("True expected.", AccessTokenDAO.find(entityManager, null, accessTokenId) instanceof AccessToken);
        Assert.assertEquals("Expected and actual values should be the same.", KapuaId.ONE, AccessTokenDAO.find(entityManager, null, accessTokenId).getScopeId());
    }

    @Test
    public void findNullAccessTokenIdTest() {
        Mockito.when(entityManager.find(AccessTokenImpl.class, null)).thenReturn(entityToFindOrDelete);
        Mockito.when(entityToFindOrDelete.getScopeId()).thenReturn(KapuaId.ONE);

        Assert.assertTrue("True expected.", AccessTokenDAO.find(entityManager, scopeId, null) instanceof AccessToken);
        Assert.assertEquals("Expected and actual values should be the same.", KapuaId.ONE, AccessTokenDAO.find(entityManager, scopeId, null).getScopeId());
    }

    @Test
    public void findByTokenIdEmptyListTest() {
        CriteriaBuilder criteriaBuilder = Mockito.mock(CriteriaBuilder.class);
        CriteriaQuery<AccessTokenImpl> criteriaQuery1 = Mockito.mock(CriteriaQuery.class);
        CriteriaQuery<AccessTokenImpl> criteriaQuery2 = Mockito.mock(CriteriaQuery.class);
        Root<AccessTokenImpl> entityRoot = Mockito.mock(Root.class);
        ParameterExpression<String> parameterExpressionName = Mockito.mock(ParameterExpression.class);
        Predicate namePredicate = Mockito.mock(Predicate.class);
        List<AccessTokenImpl> list = new ArrayList<>();
        TypedQuery<AccessTokenImpl> query = Mockito.mock(TypedQuery.class);
        TypedQuery<AccessTokenImpl> query1 = Mockito.mock(TypedQuery.class);

        Mockito.when(entityManager.getCriteriaBuilder()).thenReturn(criteriaBuilder);
        Mockito.when(criteriaBuilder.createQuery(AccessTokenImpl.class)).thenReturn(criteriaQuery1);
        Mockito.when(criteriaQuery1.from(AccessTokenImpl.class)).thenReturn(entityRoot);
        Mockito.when(criteriaQuery1.select(entityRoot)).thenReturn(criteriaQuery2);
        Mockito.when(criteriaBuilder.parameter(String.class, AccessTokenAttributes.TOKEN_ID)).thenReturn(parameterExpressionName);
        Mockito.when(criteriaBuilder.equal(entityRoot.get(AccessTokenAttributes.TOKEN_ID), parameterExpressionName)).thenReturn(namePredicate);
        Mockito.when(entityManager.createQuery(criteriaQuery1)).thenReturn(query);
        Mockito.when(query.setParameter(parameterExpressionName.getName(), "token id")).thenReturn(query1);
        Mockito.when(query.getResultList()).thenReturn(list);

        Assert.assertNull("Null expected.", AccessTokenDAO.findByTokenId(entityManager, "token id"));
    }

    @Test
    public void findByTokenIdTest() {
        CriteriaBuilder criteriaBuilder = Mockito.mock(CriteriaBuilder.class);
        CriteriaQuery<AccessTokenImpl> criteriaQuery1 = Mockito.mock(CriteriaQuery.class);
        CriteriaQuery<AccessTokenImpl> criteriaQuery2 = Mockito.mock(CriteriaQuery.class);
        Root<AccessTokenImpl> entityRoot = Mockito.mock(Root.class);
        ParameterExpression<String> parameterExpressionName = Mockito.mock(ParameterExpression.class);
        Predicate namePredicate = Mockito.mock(Predicate.class);
        List<AccessTokenImpl> list = new ArrayList<>();
        list.add(Mockito.mock(AccessTokenImpl.class));
        TypedQuery<AccessTokenImpl> query1 = Mockito.mock(TypedQuery.class);
        TypedQuery<AccessTokenImpl> query2 = Mockito.mock(TypedQuery.class);

        Mockito.when(entityManager.getCriteriaBuilder()).thenReturn(criteriaBuilder);
        Mockito.when(criteriaBuilder.createQuery(AccessTokenImpl.class)).thenReturn(criteriaQuery1);
        Mockito.when(criteriaQuery1.from(AccessTokenImpl.class)).thenReturn(entityRoot);
        Mockito.when(criteriaQuery1.select(entityRoot)).thenReturn(criteriaQuery2);
        Mockito.when(criteriaBuilder.parameter(String.class, AccessTokenAttributes.TOKEN_ID)).thenReturn(parameterExpressionName);
        Mockito.when(criteriaBuilder.equal(entityRoot.get(AccessTokenAttributes.TOKEN_ID), parameterExpressionName)).thenReturn(namePredicate);
        Mockito.when(entityManager.createQuery(criteriaQuery1)).thenReturn(query1);
        Mockito.when(query1.setParameter(parameterExpressionName.getName(), "token id")).thenReturn(query2);
        Mockito.when(query1.getResultList()).thenReturn(list);

        Assert.assertTrue("True expected.", AccessTokenDAO.findByTokenId(entityManager, "token id") instanceof AccessToken);
        Assert.assertNotNull("NotNull expected.", AccessTokenDAO.findByTokenId(entityManager, "token id"));
    }

    @Test(expected = NonUniqueResultException.class)
    public void findByTokenIdNonUniqueResultTest() {
        CriteriaBuilder criteriaBuilder = Mockito.mock(CriteriaBuilder.class);
        CriteriaQuery<AccessTokenImpl> criteriaQuery1 = Mockito.mock(CriteriaQuery.class);
        CriteriaQuery<AccessTokenImpl> criteriaQuery2 = Mockito.mock(CriteriaQuery.class);
        Root<AccessTokenImpl> entityRoot = Mockito.mock(Root.class);
        ParameterExpression<String> parameterExpressionName = Mockito.mock(ParameterExpression.class);
        Predicate namePredicate = Mockito.mock(Predicate.class);
        List<AccessTokenImpl> list = new ArrayList<>();
        list.add(Mockito.mock(AccessTokenImpl.class));
        list.add(Mockito.mock(AccessTokenImpl.class));
        TypedQuery<AccessTokenImpl> query1 = Mockito.mock(TypedQuery.class);
        TypedQuery<AccessTokenImpl> query2 = Mockito.mock(TypedQuery.class);

        Mockito.when(entityManager.getCriteriaBuilder()).thenReturn(criteriaBuilder);
        Mockito.when(criteriaBuilder.createQuery(AccessTokenImpl.class)).thenReturn(criteriaQuery1);
        Mockito.when(criteriaQuery1.from(AccessTokenImpl.class)).thenReturn(entityRoot);
        Mockito.when(criteriaQuery1.select(entityRoot)).thenReturn(criteriaQuery2);
        Mockito.when(criteriaBuilder.parameter(String.class, AccessTokenAttributes.TOKEN_ID)).thenReturn(parameterExpressionName);
        Mockito.when(criteriaBuilder.equal(entityRoot.get(AccessTokenAttributes.TOKEN_ID), parameterExpressionName)).thenReturn(namePredicate);
        Mockito.when(entityManager.createQuery(criteriaQuery1)).thenReturn(query1);
        Mockito.when(query1.setParameter(parameterExpressionName.getName(), "token id")).thenReturn(query2);
        Mockito.when(query1.getResultList()).thenReturn(list);
        AccessTokenDAO.findByTokenId(entityManager, "token id");
    }

    @Test(expected = NullPointerException.class)
    public void findByTokenIdNullEntityManagerTest() {
        AccessTokenDAO.findByTokenId(null, "tokenId");
    }

    @Test
    public void findByNullTokenIdTest() {
        CriteriaBuilder criteriaBuilder = Mockito.mock(CriteriaBuilder.class);
        CriteriaQuery<AccessTokenImpl> criteriaQuery1 = Mockito.mock(CriteriaQuery.class);
        CriteriaQuery<AccessTokenImpl> criteriaQuery2 = Mockito.mock(CriteriaQuery.class);
        Root<AccessTokenImpl> entityRoot = Mockito.mock(Root.class);
        ParameterExpression<String> parameterExpressionName = Mockito.mock(ParameterExpression.class);
        Predicate namePredicate = Mockito.mock(Predicate.class);
        List<AccessTokenImpl> list = new ArrayList<>();
        list.add(Mockito.mock(AccessTokenImpl.class));
        TypedQuery<AccessTokenImpl> query1 = Mockito.mock(TypedQuery.class);
        TypedQuery<AccessTokenImpl> query2 = Mockito.mock(TypedQuery.class);

        Mockito.when(entityManager.getCriteriaBuilder()).thenReturn(criteriaBuilder);
        Mockito.when(criteriaBuilder.createQuery(AccessTokenImpl.class)).thenReturn(criteriaQuery1);
        Mockito.when(criteriaQuery1.from(AccessTokenImpl.class)).thenReturn(entityRoot);
        Mockito.when(criteriaQuery1.select(entityRoot)).thenReturn(criteriaQuery2);
        Mockito.when(criteriaBuilder.parameter(String.class, AccessTokenAttributes.TOKEN_ID)).thenReturn(parameterExpressionName);
        Mockito.when(criteriaBuilder.equal(entityRoot.get(AccessTokenAttributes.TOKEN_ID), parameterExpressionName)).thenReturn(namePredicate);
        Mockito.when(entityManager.createQuery(criteriaQuery1)).thenReturn(query1);
        Mockito.when(query1.setParameter(parameterExpressionName.getName(), null)).thenReturn(query2);
        Mockito.when(query1.getResultList()).thenReturn(list);

        Assert.assertTrue("True expected.", AccessTokenDAO.findByTokenId(entityManager, null) instanceof AccessToken);
        Assert.assertNotNull("NotNull expected.", AccessTokenDAO.findByTokenId(entityManager, null));
    }

    @Test
    public void queryTest() throws KapuaException {
        CriteriaBuilder criteriaBuilder = Mockito.mock(CriteriaBuilder.class);
        CriteriaQuery<AccessTokenImpl> criteriaQuery1 = Mockito.mock(CriteriaQuery.class);
        CriteriaQuery<AccessTokenImpl> criteriaQuery2 = Mockito.mock(CriteriaQuery.class);
        CriteriaQuery<AccessTokenImpl> criteriaQuery3 = Mockito.mock(CriteriaQuery.class);
        Root<AccessTokenImpl> entityRoot = Mockito.mock(Root.class);
        EntityType<AccessTokenImpl> entityType = Mockito.mock(EntityType.class);
        List<String> list = new ArrayList<>();
        TypedQuery<AccessTokenImpl> query = Mockito.mock(TypedQuery.class);

        Mockito.when(entityManager.getCriteriaBuilder()).thenReturn(criteriaBuilder);
        Mockito.when(criteriaBuilder.createQuery(AccessTokenImpl.class)).thenReturn(criteriaQuery1);
        Mockito.when(criteriaQuery1.from(AccessTokenImpl.class)).thenReturn(entityRoot);
        Mockito.when(entityRoot.getModel()).thenReturn(entityType);
        Mockito.when(criteriaQuery1.select(entityRoot)).thenReturn(criteriaQuery2);
        Mockito.when(criteriaQuery2.distinct(true)).thenReturn(criteriaQuery3);
        Mockito.when(kapuaQuery.getFetchAttributes()).thenReturn(list);
        Mockito.when(entityManager.createQuery(criteriaQuery1)).thenReturn(query);

        Assert.assertTrue("True expected.", AccessTokenDAO.query(entityManager, kapuaQuery) instanceof AccessTokenListResult);
    }

    @Test(expected = NullPointerException.class)
    public void queryNullEntityManagerTest() throws KapuaException {
        AccessTokenDAO.query(null, kapuaQuery);
    }

    @Test(expected = NullPointerException.class)
    public void queryNullKapuaQueryTest() throws KapuaException {
        CriteriaBuilder criteriaBuilder = Mockito.mock(CriteriaBuilder.class);
        CriteriaQuery<AccessTokenImpl> criteriaQuery1 = Mockito.mock(CriteriaQuery.class);
        CriteriaQuery<AccessTokenImpl> criteriaQuery2 = Mockito.mock(CriteriaQuery.class);
        CriteriaQuery<AccessTokenImpl> criteriaQuery3 = Mockito.mock(CriteriaQuery.class);
        Root<AccessTokenImpl> entityRoot = Mockito.mock(Root.class);
        EntityType<AccessTokenImpl> entityType = Mockito.mock(EntityType.class);
        TypedQuery<AccessTokenImpl> query = Mockito.mock(TypedQuery.class);

        Mockito.when(entityManager.getCriteriaBuilder()).thenReturn(criteriaBuilder);
        Mockito.when(criteriaBuilder.createQuery(AccessTokenImpl.class)).thenReturn(criteriaQuery1);
        Mockito.when(criteriaQuery1.from(AccessTokenImpl.class)).thenReturn(entityRoot);
        Mockito.when(entityRoot.getModel()).thenReturn(entityType);
        Mockito.when(criteriaQuery1.select(entityRoot)).thenReturn(criteriaQuery2);
        Mockito.when(criteriaQuery2.distinct(true)).thenReturn(criteriaQuery3);
        Mockito.when(entityManager.createQuery(criteriaQuery1)).thenReturn(query);

        AccessTokenDAO.query(entityManager, null);
    }

    @Test
    public void countTest() throws KapuaException {
        CriteriaBuilder criteriaBuilder = Mockito.mock(CriteriaBuilder.class);
        CriteriaQuery<Long> criteriaQuery1 = Mockito.mock(CriteriaQuery.class);
        CriteriaQuery<Long> criteriaQuery2 = Mockito.mock(CriteriaQuery.class);
        Root<AccessTokenImpl> entityRoot = Mockito.mock(Root.class);
        TypedQuery<Long> query = Mockito.mock(TypedQuery.class);
        Selection<Long> selection = Mockito.mock(Selection.class);
        Expression<Long> expression = Mockito.mock(Expression.class);
        long[] longNumberList = {0L, 10L, 100L, 1000000L, 9223372036854775807L, -100L, -9223372036854775808L};

        Mockito.when(entityManager.getCriteriaBuilder()).thenReturn(criteriaBuilder);
        Mockito.when(criteriaBuilder.createQuery(Long.class)).thenReturn(criteriaQuery1);
        Mockito.when(criteriaQuery1.from(AccessTokenImpl.class)).thenReturn(entityRoot);
        Mockito.when(criteriaBuilder.countDistinct(entityRoot)).thenReturn(expression);
        Mockito.when(criteriaQuery1.select(selection)).thenReturn(criteriaQuery2);
        Mockito.when(entityManager.createQuery(criteriaQuery1)).thenReturn(query);
        for (long number : longNumberList) {
            Mockito.doReturn(number).when(query).getSingleResult();

            Assert.assertEquals("Expected and actual values should be the same.", number, AccessTokenDAO.count(entityManager, kapuaQuery));
        }
    }

    @Test(expected = NullPointerException.class)
    public void countNullEntityManagerTest() throws KapuaException {
        AccessTokenDAO.count(null, kapuaQuery);
    }

    @Test(expected = NullPointerException.class)
    public void countNullKapuaQueryTest() throws KapuaException {
        AccessTokenDAO.count(entityManager, null);
    }

    @Test
    public void deleteTest() throws KapuaEntityNotFoundException {
        Mockito.when(entityManager.find(AccessTokenImpl.class, accessTokenId)).thenReturn(entityToFindOrDelete);
        Mockito.when(entityToFindOrDelete.getScopeId()).thenReturn(KapuaId.ONE);

        Assert.assertTrue("True expected.", AccessTokenDAO.delete(entityManager, scopeId, accessTokenId) instanceof AccessToken);
    }

    @Test(expected = NullPointerException.class)
    public void deleteNullEntityManagerTest() throws KapuaEntityNotFoundException {
        AccessTokenDAO.delete(null, scopeId, accessTokenId);
    }

    @Test
    public void deleteNullScopeIdTest() throws KapuaEntityNotFoundException {
        Mockito.when(entityManager.find(AccessTokenImpl.class, accessTokenId)).thenReturn(entityToFindOrDelete);

        Assert.assertTrue("True expected.", AccessTokenDAO.delete(entityManager, null, accessTokenId) instanceof AccessToken);
    }

    @Test(expected = NullPointerException.class)
    public void deleteNullAccessTokenIdTest() throws KapuaEntityNotFoundException {
        AccessTokenDAO.delete(entityManager, scopeId, null);
    }

    @Test(expected = KapuaEntityNotFoundException.class)
    public void deleteNullEntityToDeleteTest() throws KapuaEntityNotFoundException {
        Mockito.when(entityManager.find(AccessTokenImpl.class, accessTokenId)).thenReturn(null);

        AccessTokenDAO.delete(entityManager, scopeId, accessTokenId);
    }
}