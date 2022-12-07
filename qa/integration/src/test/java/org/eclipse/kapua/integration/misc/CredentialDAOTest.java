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
import org.eclipse.kapua.service.authentication.credential.Credential;
import org.eclipse.kapua.service.authentication.credential.CredentialCreator;
import org.eclipse.kapua.service.authentication.credential.CredentialListResult;
import org.eclipse.kapua.service.authentication.credential.CredentialStatus;
import org.eclipse.kapua.service.authentication.credential.CredentialType;
import org.eclipse.kapua.service.authentication.credential.shiro.CredentialDAO;
import org.eclipse.kapua.service.authentication.credential.shiro.CredentialImpl;
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
public class CredentialDAOTest {

    EntityManager entityManager;
    CredentialCreator credentialCreator;
    CredentialImpl credential, entityToFindOrDelete;
    Date createdOn, expirationDate;
    KapuaId createdBy, scopeId, credentialId;
    KapuaQuery kapuaQuery;

    @Before
    public void initialize() {
        entityManager = Mockito.mock(EntityManager.class);
        credentialCreator = Mockito.mock(CredentialCreator.class);
        credential = Mockito.mock(CredentialImpl.class);
        entityToFindOrDelete = Mockito.mock(CredentialImpl.class);
        createdOn = new Date();
        createdBy = KapuaId.ONE;
        scopeId = KapuaId.ONE;
        credentialId = KapuaId.ONE;
        kapuaQuery = Mockito.mock(KapuaQuery.class);
        expirationDate = new Date();
    }

    @Test
    public void createApiKeyTypeTest() throws KapuaException {
        Mockito.when(credentialCreator.getCredentialType()).thenReturn(CredentialType.API_KEY);
        Mockito.when(credentialCreator.getCredentialPlainKey()).thenReturn("credentialAPIkey");
        Mockito.when(credentialCreator.getScopeId()).thenReturn(KapuaId.ONE);
        Mockito.when(credentialCreator.getUserId()).thenReturn(new KapuaEid(KapuaId.ONE));
        Mockito.when(credentialCreator.getCredentialStatus()).thenReturn(CredentialStatus.ENABLED);
        Mockito.when(credentialCreator.getExpirationDate()).thenReturn(expirationDate);

        Assert.assertTrue("True expected.", CredentialDAO.create(entityManager, credentialCreator) instanceof Credential);
        Assert.assertTrue("True expected.", CredentialDAO.create(entityManager, credentialCreator).getCredentialKey().startsWith("credenti"));
        Assert.assertEquals("Expected and actual values should be the same.", CredentialType.API_KEY, CredentialDAO.create(entityManager, credentialCreator).getCredentialType());
        Assert.assertEquals("Expected and actual values should be the same.", KapuaId.ONE, CredentialDAO.create(entityManager, credentialCreator).getScopeId());
        Assert.assertEquals("Expected and actual values should be the same.", new KapuaEid(KapuaId.ONE), CredentialDAO.create(entityManager, credentialCreator).getUserId());
        Assert.assertEquals("Expected and actual values should be the same.", CredentialStatus.ENABLED, CredentialDAO.create(entityManager, credentialCreator).getStatus());
        Assert.assertEquals("Expected and actual values should be the same.", expirationDate, CredentialDAO.create(entityManager, credentialCreator).getExpirationDate());
    }

    @Test
    public void createPasswordTypeTest() throws KapuaException {
        Mockito.when(credentialCreator.getCredentialType()).thenReturn(CredentialType.PASSWORD);
        Mockito.when(credentialCreator.getCredentialPlainKey()).thenReturn("credentialAPIkey");
        Mockito.when(credentialCreator.getScopeId()).thenReturn(KapuaId.ONE);
        Mockito.when(credentialCreator.getUserId()).thenReturn(new KapuaEid(KapuaId.ONE));
        Mockito.when(credentialCreator.getCredentialStatus()).thenReturn(CredentialStatus.ENABLED);
        Mockito.when(credentialCreator.getExpirationDate()).thenReturn(expirationDate);

        Assert.assertTrue("True expected.", CredentialDAO.create(entityManager, credentialCreator) instanceof Credential);
        Assert.assertTrue("True expected.", CredentialDAO.create(entityManager, credentialCreator).getCredentialKey().startsWith("$2a$12$"));
        Assert.assertEquals("Expected and actual values should be the same.", CredentialType.PASSWORD, CredentialDAO.create(entityManager, credentialCreator).getCredentialType());
        Assert.assertEquals("Expected and actual values should be the same.", KapuaId.ONE, CredentialDAO.create(entityManager, credentialCreator).getScopeId());
        Assert.assertEquals("Expected and actual values should be the same.", new KapuaEid(KapuaId.ONE), CredentialDAO.create(entityManager, credentialCreator).getUserId());
        Assert.assertEquals("Expected and actual values should be the same.", CredentialStatus.ENABLED, CredentialDAO.create(entityManager, credentialCreator).getStatus());
        Assert.assertEquals("Expected and actual values should be the same.", expirationDate, CredentialDAO.create(entityManager, credentialCreator).getExpirationDate());
    }

    @Test
    public void createJwtTypeTest() throws KapuaException {
        Mockito.when(credentialCreator.getCredentialType()).thenReturn(CredentialType.JWT);
        Mockito.when(credentialCreator.getCredentialPlainKey()).thenReturn("credentialAPIkey");
        Mockito.when(credentialCreator.getScopeId()).thenReturn(KapuaId.ONE);
        Mockito.when(credentialCreator.getUserId()).thenReturn(new KapuaEid(KapuaId.ONE));
        Mockito.when(credentialCreator.getCredentialStatus()).thenReturn(CredentialStatus.ENABLED);
        Mockito.when(credentialCreator.getExpirationDate()).thenReturn(expirationDate);

        Assert.assertTrue("True expected.", CredentialDAO.create(entityManager, credentialCreator) instanceof Credential);
        Assert.assertTrue("True expected.", CredentialDAO.create(entityManager, credentialCreator).getCredentialKey().startsWith("$2a$12$"));
        Assert.assertEquals("Expected and actual values should be the same.", CredentialType.JWT, CredentialDAO.create(entityManager, credentialCreator).getCredentialType());
        Assert.assertEquals("Expected and actual values should be the same.", KapuaId.ONE, CredentialDAO.create(entityManager, credentialCreator).getScopeId());
        Assert.assertEquals("Expected and actual values should be the same.", new KapuaEid(KapuaId.ONE), CredentialDAO.create(entityManager, credentialCreator).getUserId());
        Assert.assertEquals("Expected and actual values should be the same.", CredentialStatus.ENABLED, CredentialDAO.create(entityManager, credentialCreator).getStatus());
        Assert.assertEquals("Expected and actual values should be the same.", expirationDate, CredentialDAO.create(entityManager, credentialCreator).getExpirationDate());
    }

    @Test(expected = KapuaEntityExistsException.class)
    public void createEntityExistsExceptionTest() throws KapuaException {
        Mockito.when(credentialCreator.getCredentialType()).thenReturn(CredentialType.API_KEY);
        Mockito.when(credentialCreator.getCredentialPlainKey()).thenReturn("credentialAPIkey");
        Mockito.when(credentialCreator.getScopeId()).thenReturn(KapuaId.ONE);
        Mockito.when(credentialCreator.getUserId()).thenReturn(new KapuaEid(KapuaId.ONE));
        Mockito.when(credentialCreator.getCredentialStatus()).thenReturn(CredentialStatus.ENABLED);
        Mockito.when(credentialCreator.getExpirationDate()).thenReturn(expirationDate);
        Mockito.doThrow(new EntityExistsException()).when(entityManager).flush();

        CredentialDAO.create(entityManager, credentialCreator);
    }

    @Test(expected = PersistenceException.class)
    public void createPersistenceExceptionTest() throws KapuaException {
        Mockito.when(credentialCreator.getCredentialType()).thenReturn(CredentialType.API_KEY);
        Mockito.when(credentialCreator.getCredentialPlainKey()).thenReturn("credentialAPIkey");
        Mockito.when(credentialCreator.getScopeId()).thenReturn(KapuaId.ONE);
        Mockito.when(credentialCreator.getUserId()).thenReturn(new KapuaEid(KapuaId.ONE));
        Mockito.when(credentialCreator.getCredentialStatus()).thenReturn(CredentialStatus.ENABLED);
        Mockito.when(credentialCreator.getExpirationDate()).thenReturn(expirationDate);
        Mockito.doThrow(new PersistenceException()).when(entityManager).flush();

        CredentialDAO.create(entityManager, credentialCreator);
    }

    @Test(expected = PersistenceException.class)
    public void createPersistenceExceptionWithNotSQLCauseTest() throws KapuaException {
        Mockito.when(credentialCreator.getCredentialType()).thenReturn(CredentialType.API_KEY);
        Mockito.when(credentialCreator.getCredentialPlainKey()).thenReturn("credentialAPIkey");
        Mockito.when(credentialCreator.getScopeId()).thenReturn(KapuaId.ONE);
        Mockito.when(credentialCreator.getUserId()).thenReturn(new KapuaEid(KapuaId.ONE));
        Mockito.when(credentialCreator.getCredentialStatus()).thenReturn(CredentialStatus.ENABLED);
        Mockito.when(credentialCreator.getExpirationDate()).thenReturn(expirationDate);
        Mockito.doThrow(new PersistenceException(new Exception(new Exception()))).when(entityManager).flush();

        CredentialDAO.create(entityManager, credentialCreator);
    }

    @Test(expected = PersistenceException.class)
    public void createPersistenceExceptionWithSQLCauseTest() throws KapuaException {
        Mockito.when(credentialCreator.getCredentialType()).thenReturn(CredentialType.API_KEY);
        Mockito.when(credentialCreator.getCredentialPlainKey()).thenReturn("credentialAPIkey");
        Mockito.when(credentialCreator.getScopeId()).thenReturn(KapuaId.ONE);
        Mockito.when(credentialCreator.getUserId()).thenReturn(new KapuaEid(KapuaId.ONE));
        Mockito.when(credentialCreator.getCredentialStatus()).thenReturn(CredentialStatus.ENABLED);
        Mockito.when(credentialCreator.getExpirationDate()).thenReturn(expirationDate);
        Mockito.doThrow(new PersistenceException(new SQLException("reason", "23505", new Exception()))).when(entityManager).flush();

        CredentialDAO.create(entityManager, credentialCreator);
    }

    @Test(expected = NullPointerException.class)
    public void createNullEntityManagerTest() throws KapuaException {
        Mockito.when(credentialCreator.getCredentialType()).thenReturn(CredentialType.PASSWORD);
        Mockito.when(credentialCreator.getCredentialPlainKey()).thenReturn("credentialAPIkey");
        Mockito.when(credentialCreator.getScopeId()).thenReturn(KapuaId.ONE);
        Mockito.when(credentialCreator.getUserId()).thenReturn(new KapuaEid(KapuaId.ONE));
        Mockito.when(credentialCreator.getCredentialStatus()).thenReturn(CredentialStatus.ENABLED);
        Mockito.when(credentialCreator.getExpirationDate()).thenReturn(expirationDate);

        CredentialDAO.create(null, credentialCreator);
    }

    @Test(expected = NullPointerException.class)
    public void createNullMfaOptionCreatorTest() throws KapuaException {
        CredentialDAO.create(entityManager, (CredentialCreator) null);
    }

    @Test
    public void updateTest() throws KapuaException {
        Mockito.when(credential.getId()).thenReturn(KapuaId.ONE);
        Mockito.when(entityManager.find(CredentialImpl.class, KapuaId.ONE)).thenReturn(entityToFindOrDelete);
        Mockito.when(entityToFindOrDelete.getCreatedOn()).thenReturn(createdOn);
        Mockito.when(entityToFindOrDelete.getCreatedBy()).thenReturn(createdBy);

        Assert.assertTrue("True expected.", CredentialDAO.update(entityManager, credential) instanceof Credential);
        Assert.assertEquals("Expected and actual values should be the same.", createdBy, CredentialDAO.update(entityManager, credential).getCreatedBy());
        Assert.assertEquals("Expected and actual values should be the same.", createdOn, CredentialDAO.update(entityManager, credential).getCreatedOn());
    }

    @Test(expected = NullPointerException.class)
    public void updateNullEntityManagerTest() throws KapuaException {
        CredentialDAO.update(null, credential);
    }

    @Test(expected = NullPointerException.class)
    public void updateNullMfaOptionTest() throws KapuaException {
        Mockito.when(entityManager.find(CredentialImpl.class, KapuaId.ONE)).thenReturn(entityToFindOrDelete);
        Mockito.when(entityToFindOrDelete.getCreatedOn()).thenReturn(createdOn);
        Mockito.when(entityToFindOrDelete.getCreatedBy()).thenReturn(createdBy);

        CredentialDAO.update(entityManager, null);
    }

    @Test
    public void findSameScopeIdsTest() {
        Mockito.when(entityManager.find(CredentialImpl.class, credentialId)).thenReturn(entityToFindOrDelete);
        Mockito.when(entityToFindOrDelete.getScopeId()).thenReturn(KapuaId.ONE);

        Assert.assertTrue("True expected.", CredentialDAO.find(entityManager, scopeId, credentialId) instanceof Credential);
        Assert.assertEquals("Expected and actual values should be the same.", KapuaId.ONE, CredentialDAO.find(entityManager, scopeId, credentialId).getScopeId());
    }

    @Test
    public void findDifferentScopeIdsTest() {
        Mockito.when(entityManager.find(CredentialImpl.class, credentialId)).thenReturn(entityToFindOrDelete);
        Mockito.when(entityToFindOrDelete.getScopeId()).thenReturn(KapuaId.ANY);

        Assert.assertNull("Null expected.", CredentialDAO.find(entityManager, scopeId, credentialId));
    }

    @Test
    public void findNullEntityToFindTest() {
        Mockito.when(entityManager.find(CredentialImpl.class, credentialId)).thenReturn(null);

        Assert.assertNull("Null expected.", CredentialDAO.find(entityManager, scopeId, credentialId));
    }

    @Test
    public void findNullEntityToFindScopeIdTest() {
        Mockito.when(entityManager.find(CredentialImpl.class, credentialId)).thenReturn(entityToFindOrDelete);
        Mockito.when(entityToFindOrDelete.getScopeId()).thenReturn(null);

        Assert.assertTrue("True expected.", CredentialDAO.find(entityManager, KapuaId.ANY, credentialId) instanceof Credential);
    }

    @Test(expected = NullPointerException.class)
    public void findNullEntityManagerTest() {
        CredentialDAO.find(null, scopeId, credentialId);
    }

    @Test
    public void findNullScopeIdTest() {
        Mockito.when(entityManager.find(CredentialImpl.class, credentialId)).thenReturn(entityToFindOrDelete);
        Mockito.when(entityToFindOrDelete.getScopeId()).thenReturn(KapuaId.ONE);

        Assert.assertTrue("True expected.", CredentialDAO.find(entityManager, null, credentialId) instanceof Credential);
        Assert.assertEquals("Expected and actual values should be the same.", KapuaId.ONE, CredentialDAO.find(entityManager, null, credentialId).getScopeId());
    }

    @Test
    public void findNullMfaOptionIdTest() {
        Mockito.when(entityManager.find(CredentialImpl.class, null)).thenReturn(entityToFindOrDelete);
        Mockito.when(entityToFindOrDelete.getScopeId()).thenReturn(KapuaId.ONE);

        Assert.assertTrue("True expected.", CredentialDAO.find(entityManager, scopeId, null) instanceof Credential);
        Assert.assertEquals("Expected and actual values should be the same.", KapuaId.ONE, CredentialDAO.find(entityManager, scopeId, null).getScopeId());
    }

    @Test
    public void queryTest() throws KapuaException {
        CriteriaBuilder criteriaBuilder = Mockito.mock(CriteriaBuilder.class);
        CriteriaQuery<CredentialImpl> criteriaQuery1 = Mockito.mock(CriteriaQuery.class);
        CriteriaQuery<CredentialImpl> criteriaQuery2 = Mockito.mock(CriteriaQuery.class);
        CriteriaQuery<CredentialImpl> criteriaQuery3 = Mockito.mock(CriteriaQuery.class);
        Root<CredentialImpl> entityRoot = Mockito.mock(Root.class);
        EntityType<CredentialImpl> entityType = Mockito.mock(EntityType.class);
        List<String> list = new ArrayList<>();
        TypedQuery<CredentialImpl> query = Mockito.mock(TypedQuery.class);

        Mockito.when(entityManager.getCriteriaBuilder()).thenReturn(criteriaBuilder);
        Mockito.when(criteriaBuilder.createQuery(CredentialImpl.class)).thenReturn(criteriaQuery1);
        Mockito.when(criteriaQuery1.from(CredentialImpl.class)).thenReturn(entityRoot);
        Mockito.when(entityRoot.getModel()).thenReturn(entityType);
        Mockito.when(criteriaQuery1.select(entityRoot)).thenReturn(criteriaQuery2);
        Mockito.when(criteriaQuery2.distinct(true)).thenReturn(criteriaQuery3);
        Mockito.when(kapuaQuery.getFetchAttributes()).thenReturn(list);
        Mockito.when(entityManager.createQuery(criteriaQuery1)).thenReturn(query);

        Assert.assertTrue("True expected.", CredentialDAO.query(entityManager, kapuaQuery) instanceof CredentialListResult);
    }

    @Test(expected = NullPointerException.class)
    public void queryNullEntityManagerTest() throws KapuaException {
        CredentialDAO.query(null, kapuaQuery);
    }

    @Test(expected = NullPointerException.class)
    public void queryNullKapuaQueryTest() throws KapuaException {
        CriteriaBuilder criteriaBuilder = Mockito.mock(CriteriaBuilder.class);
        CriteriaQuery<CredentialImpl> criteriaQuery1 = Mockito.mock(CriteriaQuery.class);
        CriteriaQuery<CredentialImpl> criteriaQuery2 = Mockito.mock(CriteriaQuery.class);
        CriteriaQuery<CredentialImpl> criteriaQuery3 = Mockito.mock(CriteriaQuery.class);
        Root<CredentialImpl> entityRoot = Mockito.mock(Root.class);
        EntityType<CredentialImpl> entityType = Mockito.mock(EntityType.class);
        TypedQuery<CredentialImpl> query = Mockito.mock(TypedQuery.class);

        Mockito.when(entityManager.getCriteriaBuilder()).thenReturn(criteriaBuilder);
        Mockito.when(criteriaBuilder.createQuery(CredentialImpl.class)).thenReturn(criteriaQuery1);
        Mockito.when(criteriaQuery1.from(CredentialImpl.class)).thenReturn(entityRoot);
        Mockito.when(entityRoot.getModel()).thenReturn(entityType);
        Mockito.when(criteriaQuery1.select(entityRoot)).thenReturn(criteriaQuery2);
        Mockito.when(criteriaQuery2.distinct(true)).thenReturn(criteriaQuery3);
        Mockito.when(entityManager.createQuery(criteriaQuery1)).thenReturn(query);

        CredentialDAO.query(entityManager, null);
    }

    @Test
    public void countTest() throws KapuaException {
        CriteriaBuilder criteriaBuilder = Mockito.mock(CriteriaBuilder.class);
        CriteriaQuery<Long> criteriaQuery1 = Mockito.mock(CriteriaQuery.class);
        CriteriaQuery<Long> criteriaQuery2 = Mockito.mock(CriteriaQuery.class);
        Root<CredentialImpl> entityRoot = Mockito.mock(Root.class);
        TypedQuery<Long> query = Mockito.mock(TypedQuery.class);
        Selection<Long> selection = Mockito.mock(Selection.class);
        Expression<Long> expression = Mockito.mock(Expression.class);
        long[] longNumberList = {0L, 10L, 100L, 1000000L, 9223372036854775807L, -100L, -9223372036854775808L};

        Mockito.when(entityManager.getCriteriaBuilder()).thenReturn(criteriaBuilder);
        Mockito.when(criteriaBuilder.createQuery(Long.class)).thenReturn(criteriaQuery1);
        Mockito.when(criteriaQuery1.from(CredentialImpl.class)).thenReturn(entityRoot);
        Mockito.when(criteriaBuilder.countDistinct(entityRoot)).thenReturn(expression);
        Mockito.when(criteriaQuery1.select(selection)).thenReturn(criteriaQuery2);
        Mockito.when(entityManager.createQuery(criteriaQuery1)).thenReturn(query);
        for (long number : longNumberList) {
            Mockito.doReturn(number).when(query).getSingleResult();

            Assert.assertEquals("Expected and actual values should be the same.", number, CredentialDAO.count(entityManager, kapuaQuery));
        }
    }

    @Test(expected = NullPointerException.class)
    public void countNullEntityManagerTest() throws KapuaException {
        CredentialDAO.count(null, kapuaQuery);
    }

    @Test(expected = NullPointerException.class)
    public void countNullKapuaQueryTest() throws KapuaException {
        CredentialDAO.count(entityManager, null);
    }

    @Test
    public void deleteTest() throws KapuaEntityNotFoundException {
        Mockito.when(entityManager.find(CredentialImpl.class, credentialId)).thenReturn(entityToFindOrDelete);
        Mockito.when(entityToFindOrDelete.getScopeId()).thenReturn(KapuaId.ONE);

        Assert.assertTrue("True expected.", CredentialDAO.delete(entityManager, scopeId, credentialId) instanceof Credential);
    }

    @Test(expected = NullPointerException.class)
    public void deleteNullEntityManagerTest() throws KapuaEntityNotFoundException {
        CredentialDAO.delete(null, scopeId, credentialId);
    }

    @Test
    public void deleteNullScopeIdTest() throws KapuaEntityNotFoundException {
        Mockito.when(entityManager.find(CredentialImpl.class, credentialId)).thenReturn(entityToFindOrDelete);

        Assert.assertTrue("True expected.", CredentialDAO.delete(entityManager, null, credentialId) instanceof Credential);
    }

    @Test(expected = NullPointerException.class)
    public void deleteNullMfaOptionIdTest() throws KapuaEntityNotFoundException {
        CredentialDAO.delete(entityManager, scopeId, null);
    }

    @Test(expected = KapuaEntityNotFoundException.class)
    public void deleteNullEntityToDeleteTest() throws KapuaEntityNotFoundException {
        Mockito.when(entityManager.find(CredentialImpl.class, credentialId)).thenReturn(null);

        CredentialDAO.delete(entityManager, scopeId, credentialId);
    }
}