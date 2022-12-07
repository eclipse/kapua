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
import org.eclipse.kapua.service.authentication.credential.mfa.MfaOption;
import org.eclipse.kapua.service.authentication.credential.mfa.MfaOptionCreator;
import org.eclipse.kapua.service.authentication.credential.mfa.MfaOptionListResult;
import org.eclipse.kapua.service.authentication.credential.mfa.shiro.MfaOptionDAO;
import org.eclipse.kapua.service.authentication.credential.mfa.shiro.MfaOptionImpl;
import org.hamcrest.core.IsInstanceOf;
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
import java.math.BigInteger;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@Category(JUnitTests.class)
public class MfaOptionDAOTest {

    EntityManager entityManager;
    MfaOptionCreator mfaOptionCreator;
    MfaOptionImpl mfaOption, entityToFindOrDelete;
    Date createdOn;
    KapuaId createdBy, scopeId, mfaOptionId;
    KapuaQuery kapuaQuery;

    @Before
    public void initialize() {
        entityManager = Mockito.mock(EntityManager.class);
        mfaOptionCreator = Mockito.mock(MfaOptionCreator.class);
        mfaOption = Mockito.mock(MfaOptionImpl.class);
        entityToFindOrDelete = Mockito.mock(MfaOptionImpl.class);
        createdOn = new Date();
        createdBy = KapuaId.ONE;
        scopeId = KapuaId.ONE;
        mfaOptionId = KapuaId.ONE;
        kapuaQuery = Mockito.mock(KapuaQuery.class);
    }

    @Test
    public void createTest() throws KapuaException {
        Mockito.when(mfaOptionCreator.getMfaSecretKey()).thenReturn("mfa secret key");
        Mockito.when(mfaOptionCreator.getScopeId()).thenReturn(KapuaId.ONE);
        Mockito.when(mfaOptionCreator.getUserId()).thenReturn(new KapuaEid(BigInteger.ONE));

        Assert.assertThat("Instance of MfaOption object expected.", MfaOptionDAO.create(entityManager, mfaOptionCreator), IsInstanceOf.instanceOf(MfaOption.class));
        Assert.assertEquals("Expected and actual values should be the same.", "mfa secret key", MfaOptionDAO.create(entityManager, mfaOptionCreator).getMfaSecretKey());
        Assert.assertEquals("Expected and actual values should be the same.", KapuaId.ONE, MfaOptionDAO.create(entityManager, mfaOptionCreator).getScopeId());
        Assert.assertEquals("Expected and actual values should be the same.", KapuaId.ONE, MfaOptionDAO.create(entityManager, mfaOptionCreator).getUserId());
    }

    @Test(expected = KapuaEntityExistsException.class)
    public void createEntityExistsExceptionTest() throws KapuaException {
        Mockito.when(mfaOptionCreator.getMfaSecretKey()).thenReturn("mfa secret key");
        Mockito.when(mfaOptionCreator.getScopeId()).thenReturn(KapuaId.ONE);
        Mockito.when(mfaOptionCreator.getUserId()).thenReturn(new KapuaEid(BigInteger.ONE));
        Mockito.doThrow(new EntityExistsException()).when(entityManager).flush();

        MfaOptionDAO.create(entityManager, mfaOptionCreator);
    }

    @Test(expected = PersistenceException.class)
    public void createPersistenceExceptionTest() throws KapuaException {
        Mockito.when(mfaOptionCreator.getMfaSecretKey()).thenReturn("mfa secret key");
        Mockito.when(mfaOptionCreator.getScopeId()).thenReturn(KapuaId.ONE);
        Mockito.when(mfaOptionCreator.getUserId()).thenReturn(new KapuaEid(BigInteger.ONE));
        Mockito.doThrow(new PersistenceException()).when(entityManager).flush();

        MfaOptionDAO.create(entityManager, mfaOptionCreator);
    }

    @Test(expected = PersistenceException.class)
    public void createPersistenceExceptionWithCauseTest() throws KapuaException {
        Mockito.when(mfaOptionCreator.getMfaSecretKey()).thenReturn("mfa secret key");
        Mockito.when(mfaOptionCreator.getScopeId()).thenReturn(KapuaId.ONE);
        Mockito.when(mfaOptionCreator.getUserId()).thenReturn(new KapuaEid(BigInteger.ONE));
        Mockito.doThrow(new PersistenceException(new Exception(new Exception()))).when(entityManager).flush();

        MfaOptionDAO.create(entityManager, mfaOptionCreator);
    }

    @Test(expected = PersistenceException.class)
    public void createPersistenceExceptionWithSQLCauseTest() throws KapuaException {
        Mockito.when(mfaOptionCreator.getMfaSecretKey()).thenReturn("mfa secret key");
        Mockito.when(mfaOptionCreator.getScopeId()).thenReturn(KapuaId.ONE);
        Mockito.when(mfaOptionCreator.getUserId()).thenReturn(new KapuaEid(BigInteger.ONE));
        Mockito.doThrow(new PersistenceException(new SQLException("reason", "23505", new Exception()))).when(entityManager).flush();

        MfaOptionDAO.create(entityManager, mfaOptionCreator);
    }

    @Test(expected = NullPointerException.class)
    public void createNullEntityManagerTest() throws KapuaException {
        Mockito.when(mfaOptionCreator.getMfaSecretKey()).thenReturn("mfa secret key");
        Mockito.when(mfaOptionCreator.getScopeId()).thenReturn(KapuaId.ONE);
        Mockito.when(mfaOptionCreator.getUserId()).thenReturn(new KapuaEid());

        MfaOptionDAO.create(null, mfaOptionCreator);
    }

    @Test(expected = NullPointerException.class)
    public void createNullMfaOptionCreatorTest() throws KapuaException {
        MfaOptionDAO.create(entityManager, (MfaOptionCreator) null);
    }

    @Test
    public void updateTest() throws KapuaException {
        Mockito.when(mfaOption.getId()).thenReturn(KapuaId.ONE);
        Mockito.when(entityManager.find(MfaOptionImpl.class, KapuaId.ONE)).thenReturn(entityToFindOrDelete);
        Mockito.when(entityToFindOrDelete.getCreatedOn()).thenReturn(createdOn);
        Mockito.when(entityToFindOrDelete.getCreatedBy()).thenReturn(createdBy);

        Assert.assertThat("Instance of MfaOption object expected.", MfaOptionDAO.update(entityManager, mfaOption), IsInstanceOf.instanceOf(MfaOption.class));
        Assert.assertEquals("Expected and actual values should be the same.", createdBy, MfaOptionDAO.update(entityManager, mfaOption).getCreatedBy());
        Assert.assertEquals("Expected and actual values should be the same.", createdOn, MfaOptionDAO.update(entityManager, mfaOption).getCreatedOn());
    }

    @Test(expected = NullPointerException.class)
    public void updateNullEntityManagerTest() throws KapuaException {
        MfaOptionDAO.update(null, mfaOption);
    }

    @Test(expected = NullPointerException.class)
    public void updateNullMfaOptionTest() throws KapuaException {
        Mockito.when(entityManager.find(MfaOptionImpl.class, KapuaId.ONE)).thenReturn(entityToFindOrDelete);
        Mockito.when(entityToFindOrDelete.getCreatedOn()).thenReturn(createdOn);
        Mockito.when(entityToFindOrDelete.getCreatedBy()).thenReturn(createdBy);

        MfaOptionDAO.update(entityManager, null);
    }

    @Test
    public void findSameScopeIdsTest() {
        Mockito.when(entityManager.find(MfaOptionImpl.class, mfaOptionId)).thenReturn(entityToFindOrDelete);
        Mockito.when(entityToFindOrDelete.getScopeId()).thenReturn(KapuaId.ONE);

        Assert.assertThat("Instance of MfaOption object expected.", MfaOptionDAO.find(entityManager, scopeId, mfaOptionId), IsInstanceOf.instanceOf(MfaOption.class));
        Assert.assertEquals("Expected and actual values should be the same.", KapuaId.ONE, MfaOptionDAO.find(entityManager, scopeId, mfaOptionId).getScopeId());
    }

    @Test
    public void findDifferentScopeIdsTest() {
        Mockito.when(entityManager.find(MfaOptionImpl.class, mfaOptionId)).thenReturn(entityToFindOrDelete);
        Mockito.when(entityToFindOrDelete.getScopeId()).thenReturn(KapuaId.ANY);

        Assert.assertNull("Null expected.", MfaOptionDAO.find(entityManager, scopeId, mfaOptionId));
    }

    @Test
    public void findNullEntityToFindTest() {
        Mockito.when(entityManager.find(MfaOptionImpl.class, mfaOptionId)).thenReturn(null);

        Assert.assertNull("Null expected.", MfaOptionDAO.find(entityManager, scopeId, mfaOptionId));
    }

    @Test
    public void findNullEntityToFindScopeIdTest() {
        Mockito.when(entityManager.find(MfaOptionImpl.class, mfaOptionId)).thenReturn(entityToFindOrDelete);
        Mockito.when(entityToFindOrDelete.getScopeId()).thenReturn(null);

        Assert.assertThat("Instance of MfaOption object expected.", MfaOptionDAO.find(entityManager, KapuaId.ANY, mfaOptionId), IsInstanceOf.instanceOf(MfaOption.class));
    }

    @Test(expected = NullPointerException.class)
    public void findNullEntityManagerTest() {
        MfaOptionDAO.find(null, scopeId, mfaOptionId);
    }

    @Test
    public void findNullScopeIdTest() {
        Mockito.when(entityManager.find(MfaOptionImpl.class, mfaOptionId)).thenReturn(entityToFindOrDelete);
        Mockito.when(entityToFindOrDelete.getScopeId()).thenReturn(KapuaId.ONE);

        Assert.assertThat("Instance of MfaOption object expected.", MfaOptionDAO.find(entityManager, null, mfaOptionId), IsInstanceOf.instanceOf(MfaOption.class));
        Assert.assertEquals("Expected and actual values should be the same.", KapuaId.ONE, MfaOptionDAO.find(entityManager, null, mfaOptionId).getScopeId());
    }

    @Test
    public void findNullMfaOptionIdTest() {
        Mockito.when(entityManager.find(MfaOptionImpl.class, null)).thenReturn(entityToFindOrDelete);
        Mockito.when(entityToFindOrDelete.getScopeId()).thenReturn(KapuaId.ONE);

        Assert.assertThat("Instance of MfaOption object expected.", MfaOptionDAO.find(entityManager, scopeId, null), IsInstanceOf.instanceOf(MfaOption.class));
        Assert.assertEquals("Expected and actual values should be the same.", KapuaId.ONE, MfaOptionDAO.find(entityManager, scopeId, null).getScopeId());
    }

    @Test
    public void queryTest() throws KapuaException {
        CriteriaBuilder criteriaBuilder = Mockito.mock(CriteriaBuilder.class);
        CriteriaQuery<MfaOptionImpl> criteriaQuery1 = Mockito.mock(CriteriaQuery.class);
        CriteriaQuery<MfaOptionImpl> criteriaQuery2 = Mockito.mock(CriteriaQuery.class);
        CriteriaQuery<MfaOptionImpl> criteriaQuery3 = Mockito.mock(CriteriaQuery.class);
        Root<MfaOptionImpl> entityRoot = Mockito.mock(Root.class);
        EntityType<MfaOptionImpl> entityType = Mockito.mock(EntityType.class);
        List<String> list = new ArrayList<>();
        TypedQuery<MfaOptionImpl> query = Mockito.mock(TypedQuery.class);

        Mockito.when(entityManager.getCriteriaBuilder()).thenReturn(criteriaBuilder);
        Mockito.when(criteriaBuilder.createQuery(MfaOptionImpl.class)).thenReturn(criteriaQuery1);
        Mockito.when(criteriaQuery1.from(MfaOptionImpl.class)).thenReturn(entityRoot);
        Mockito.when(entityRoot.getModel()).thenReturn(entityType);
        Mockito.when(criteriaQuery1.select(entityRoot)).thenReturn(criteriaQuery2);
        Mockito.when(criteriaQuery2.distinct(true)).thenReturn(criteriaQuery3);
        Mockito.when(kapuaQuery.getFetchAttributes()).thenReturn(list);
        Mockito.when(entityManager.createQuery(criteriaQuery1)).thenReturn(query);

        Assert.assertThat("Instance of MfaOptionListResult object expected.", MfaOptionDAO.query(entityManager, kapuaQuery), IsInstanceOf.instanceOf(MfaOptionListResult.class));
    }

    @Test(expected = NullPointerException.class)
    public void queryNullEntityManagerTest() throws KapuaException {
        MfaOptionDAO.query(null, kapuaQuery);
    }

    @Test(expected = NullPointerException.class)
    public void queryNullKapuaQueryTest() throws KapuaException {
        CriteriaBuilder criteriaBuilder = Mockito.mock(CriteriaBuilder.class);
        CriteriaQuery<MfaOptionImpl> criteriaQuery1 = Mockito.mock(CriteriaQuery.class);
        CriteriaQuery<MfaOptionImpl> criteriaQuery2 = Mockito.mock(CriteriaQuery.class);
        CriteriaQuery<MfaOptionImpl> criteriaQuery3 = Mockito.mock(CriteriaQuery.class);
        Root<MfaOptionImpl> entityRoot = Mockito.mock(Root.class);
        EntityType<MfaOptionImpl> entityType = Mockito.mock(EntityType.class);
        TypedQuery<MfaOptionImpl> query = Mockito.mock(TypedQuery.class);

        Mockito.when(entityManager.getCriteriaBuilder()).thenReturn(criteriaBuilder);
        Mockito.when(criteriaBuilder.createQuery(MfaOptionImpl.class)).thenReturn(criteriaQuery1);
        Mockito.when(criteriaQuery1.from(MfaOptionImpl.class)).thenReturn(entityRoot);
        Mockito.when(entityRoot.getModel()).thenReturn(entityType);
        Mockito.when(criteriaQuery1.select(entityRoot)).thenReturn(criteriaQuery2);
        Mockito.when(criteriaQuery2.distinct(true)).thenReturn(criteriaQuery3);
        Mockito.when(entityManager.createQuery(criteriaQuery1)).thenReturn(query);

        MfaOptionDAO.query(entityManager, null);
    }

    @Test
    public void countTest() throws KapuaException {
        CriteriaBuilder criteriaBuilder = Mockito.mock(CriteriaBuilder.class);
        CriteriaQuery<Long> criteriaQuery1 = Mockito.mock(CriteriaQuery.class);
        CriteriaQuery<Long> criteriaQuery2 = Mockito.mock(CriteriaQuery.class);
        Root<MfaOptionImpl> entityRoot = Mockito.mock(Root.class);
        TypedQuery<Long> query = Mockito.mock(TypedQuery.class);
        Selection<Long> selection = Mockito.mock(Selection.class);
        Expression<Long> expression = Mockito.mock(Expression.class);
        long[] longNumberList = {0L, 10L, 100L, 1000000L, 9223372036854775807L, -100L, -9223372036854775808L};

        Mockito.when(entityManager.getCriteriaBuilder()).thenReturn(criteriaBuilder);
        Mockito.when(criteriaBuilder.createQuery(Long.class)).thenReturn(criteriaQuery1);
        Mockito.when(criteriaQuery1.from(MfaOptionImpl.class)).thenReturn(entityRoot);
        Mockito.when(criteriaBuilder.countDistinct(entityRoot)).thenReturn(expression);
        Mockito.when(criteriaQuery1.select(selection)).thenReturn(criteriaQuery2);
        Mockito.when(entityManager.createQuery(criteriaQuery1)).thenReturn(query);
        for (long number : longNumberList) {
            Mockito.doReturn(number).when(query).getSingleResult();

            Assert.assertThat("Long object expected.", MfaOptionDAO.count(entityManager, kapuaQuery), IsInstanceOf.instanceOf(Long.class));
            Assert.assertEquals("Expected and actual values should be the same.", number, MfaOptionDAO.count(entityManager, kapuaQuery));
        }
    }

    @Test(expected = NullPointerException.class)
    public void countNullEntityManagerTest() throws KapuaException {
        MfaOptionDAO.count(null, kapuaQuery);
    }

    @Test(expected = NullPointerException.class)
    public void countNullKapuaQueryTest() throws KapuaException {
        MfaOptionDAO.count(entityManager, null);
    }

    @Test
    public void deleteTest() throws KapuaEntityNotFoundException {
        Mockito.when(entityManager.find(MfaOptionImpl.class, mfaOptionId)).thenReturn(entityToFindOrDelete);
        Mockito.when(entityToFindOrDelete.getScopeId()).thenReturn(KapuaId.ONE);

        Assert.assertThat("Instance of MfaOption object expected.", MfaOptionDAO.delete(entityManager, scopeId, mfaOptionId), IsInstanceOf.instanceOf(MfaOption.class));
    }

    @Test(expected = NullPointerException.class)
    public void deleteNullEntityManagerTest() throws KapuaEntityNotFoundException {
        MfaOptionDAO.delete(null, scopeId, mfaOptionId);
    }

    @Test
    public void deleteNullScopeIdTest() throws KapuaEntityNotFoundException {
        Mockito.when(entityManager.find(MfaOptionImpl.class, mfaOptionId)).thenReturn(entityToFindOrDelete);

        Assert.assertThat("Instance of MfaOption object expected.", MfaOptionDAO.delete(entityManager, null, mfaOptionId), IsInstanceOf.instanceOf(MfaOption.class));
    }

    @Test(expected = NullPointerException.class)
    public void deleteNullMfaOptionIdTest() throws KapuaEntityNotFoundException {
        MfaOptionDAO.delete(entityManager, scopeId, null);
    }

    @Test(expected = KapuaEntityNotFoundException.class)
    public void deleteNullEntityToDeleteTest() throws KapuaEntityNotFoundException {
        Mockito.when(entityManager.find(MfaOptionImpl.class, mfaOptionId)).thenReturn(null);

        MfaOptionDAO.delete(entityManager, scopeId, mfaOptionId);
    }
}