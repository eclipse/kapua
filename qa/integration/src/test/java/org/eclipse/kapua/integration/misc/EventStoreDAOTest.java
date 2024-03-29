/*******************************************************************************
 * Copyright (c) 2020, 2022 Eurotech and/or its affiliates and others
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

import org.eclipse.kapua.qa.markers.junit.JUnitTests;
import org.junit.experimental.categories.Category;


//TODO: convert to repository test
@Category(JUnitTests.class)
public class EventStoreDAOTest {
//
//    @Test
//    public void createTest() throws Exception {
//        EntityManager entityManager = Mockito.mock(EntityManager.class);
//        EventStoreRecord eventStoreRecord = new EventStoreRecordImpl();
//        KapuaEid id = new KapuaEid(BigInteger.ONE);
//        eventStoreRecord.setId(id);
//        KapuaEntity kapuaEntity = Mockito.mock(KapuaEntity.class);
//        NullPointerException nullPointerException = new NullPointerException();
//        KapuaEntityExistsException kapuaEntityExistsException = new KapuaEntityExistsException(new EntityExistsException(), eventStoreRecord.getEntityId());
//        PersistenceException persistenceException = new PersistenceException("Error", new Throwable());
//        PersistenceException persistenceException2 = new PersistenceException("Error", new SQLException("Exception", "23505"));
//
//        Assert.assertEquals("Expected and actual values should be the same.", eventStoreRecord, EventStoreDAO.create(entityManager, eventStoreRecord));
//        Assert.assertNull("Null expected.", EventStoreDAO.create(entityManager, null));
//        try {
//            EventStoreDAO.create(null, null);
//        } catch (Exception e) {
//            Assert.assertEquals("NullPointerException expected.", nullPointerException.toString(), e.toString());
//        }
//        try {
//            EventStoreDAO.create(null, eventStoreRecord);
//        } catch (Exception e) {
//            Assert.assertEquals("NullPointerException expected.", nullPointerException.toString(), e.toString());
//        }
//
//        //EntityExistsException
//        Mockito.doThrow(new KapuaEntityExistsException(new EntityExistsException(), eventStoreRecord.getEntityId())).when(entityManager).persist(eventStoreRecord);
//        try {
//            EventStoreDAO.create(entityManager, eventStoreRecord);
//        } catch (KapuaEntityExistsException e) {
//            Assert.assertEquals("KapuaEntityExistsException expected.", kapuaEntityExistsException.toString(), e.toString());
//        }
//
//        //PersistenceException
//        try {
//            Mockito.doThrow(persistenceException).when(entityManager).persist(eventStoreRecord);
//            Mockito.doReturn(kapuaEntity).when(entityManager).find(eventStoreRecord.getClass(), eventStoreRecord.getId());
//            EventStoreDAO.create(entityManager, eventStoreRecord);
//        } catch (PersistenceException e) {
//            Assert.assertEquals("PersistenceException expected.", persistenceException.toString(), e.toString());
//        }
//        try {
//            Mockito.doThrow(persistenceException2).when(entityManager).persist(eventStoreRecord);
//            Mockito.doReturn(kapuaEntity).when(entityManager).find(eventStoreRecord.getClass(), eventStoreRecord.getId());
//            EventStoreDAO.create(entityManager, eventStoreRecord);
//        } catch (KapuaEntityExistsException e) {
//            Assert.assertEquals("KapuaEntityExistsException expected.", kapuaEntityExistsException.toString(), e.toString());
//        }
//        try {
//            Mockito.doThrow(persistenceException2).when(entityManager).persist(eventStoreRecord);
//            Mockito.doReturn(null).when(entityManager).find(eventStoreRecord.getClass(), eventStoreRecord.getId());
//            EventStoreDAO.create(entityManager, eventStoreRecord);
//        } catch (PersistenceException e) {
//            Assert.assertEquals("PersistenceException expected.", persistenceException2.toString(), e.toString());
//        }
//    }
//
//    @Test
//    public void updateTest() throws KapuaException {
//        EntityManager entityManager = Mockito.mock(EntityManager.class);
//        EventStoreRecord eventStoreRecord = new EventStoreRecordImpl();
//        KapuaEid id = new KapuaEid(BigInteger.ONE);
//        eventStoreRecord.setId(id);
//        EventStoreRecordImpl eventStoreRecordImpl = new EventStoreRecordImpl();
//        KapuaEntityNotFoundException kapuaEntityNotFoundException = new KapuaEntityNotFoundException("EventStoreRecordImpl", "1");
//        NullPointerException nullPointerException = new NullPointerException();
//
//        //entityToUpdate null
//        Mockito.when(entityManager.find(EventStoreRecordImpl.class, id)).thenReturn(null);
//        try {
//            EventStoreDAO.update(entityManager, eventStoreRecord);
//        } catch (Exception e) {
//            Assert.assertEquals("KapuaEntityNotFoundException expected.", kapuaEntityNotFoundException.toString(), e.toString());
//        }
//        try {
//            EventStoreDAO.update(null, null);
//        } catch (Exception e) {
//            Assert.assertEquals("NullPointerException expected.", nullPointerException.toString(), e.toString());
//        }
//        try {
//            EventStoreDAO.update(null, eventStoreRecord);
//        } catch (Exception e) {
//            Assert.assertEquals("NullPointerException expected.", nullPointerException.toString(), e.toString());
//        }
//
//        //entityToUpdate not null
//        Mockito.when(entityManager.find(EventStoreRecordImpl.class, id)).thenReturn(eventStoreRecordImpl);
//        Assert.assertEquals("Expected and actual values should be the same.", eventStoreRecordImpl, EventStoreDAO.update(entityManager, eventStoreRecord));
//        try {
//            EventStoreDAO.update(null, null);
//        } catch (Exception e) {
//            Assert.assertEquals("NullPointerException expected.", nullPointerException.toString(), e.toString());
//        }
//        try {
//            EventStoreDAO.update(null, eventStoreRecord);
//        } catch (Exception e) {
//            Assert.assertEquals("NullPointerException expected.", nullPointerException.toString(), e.toString());
//        }
//    }
//
//    @Test
//    public void findTest() {
//        EntityManager entityManager = Mockito.mock(EntityManager.class);
//        KapuaId scopeIdOne = new KapuaEid(BigInteger.ONE);
//        KapuaId scopeIdTen = new KapuaEid(BigInteger.TEN);
//        KapuaId[] eventIds = {null, new KapuaEid(BigInteger.ONE)};
//        EventStoreRecordImpl eventStoreRecordImpl = new EventStoreRecordImpl();
//        NullPointerException nullPointerException = new NullPointerException();
//
//        //entityToFind null
//        for (KapuaId event : eventIds) {
//            Mockito.when(entityManager.find(EventStoreRecordImpl.class, event)).thenReturn(null);
//            Assert.assertNull("Null expected", EventStoreDAO.find(entityManager, scopeIdOne, event));
//            Assert.assertNull("Null expected.", EventStoreDAO.find(entityManager, null, event));
//            try {
//                EventStoreDAO.find(null, scopeIdOne, event);
//            } catch (Exception e) {
//                Assert.assertEquals("NullPointerException expected.", nullPointerException.toString(), e.toString());
//            }
//            try {
//                EventStoreDAO.find(null, null, event);
//            } catch (Exception e) {
//                Assert.assertEquals("NullPointerException expected.", nullPointerException.toString(), e.toString());
//            }
//        }
//
//        //entityToFind not null
//        for (KapuaId eventId : eventIds) {
//            eventStoreRecordImpl.setScopeId(null);
//            Mockito.when(entityManager.find(EventStoreRecordImpl.class, eventId)).thenReturn(eventStoreRecordImpl);
//            Assert.assertEquals("Expected and actual values should be the same.", eventStoreRecordImpl, EventStoreDAO.find(entityManager, null, eventId));
//            Assert.assertNull("Expected and actual values should be the same.", EventStoreDAO.find(entityManager, scopeIdOne, eventId));
//
//            eventStoreRecordImpl.setScopeId(scopeIdOne);
//            Assert.assertEquals("Expected and actual values should be the same.", eventStoreRecordImpl, EventStoreDAO.find(entityManager, scopeIdOne, eventId));
//
//            eventStoreRecordImpl.setScopeId(scopeIdTen);
//            Assert.assertNull("Null expected", EventStoreDAO.find(entityManager, scopeIdOne, eventId));
//
//            try {
//                EventStoreDAO.find(null, scopeIdOne, eventId);
//            } catch (Exception e) {
//                Assert.assertEquals("NullPointerException expected.", nullPointerException.toString(), e.toString());
//            }
//            try {
//                EventStoreDAO.find(null, null, eventId);
//            } catch (Exception e) {
//                Assert.assertEquals("NullPointerException expected.", nullPointerException.toString(), e.toString());
//            }
//        }
//    }
//
//    @Test
//    public void queryTest() throws KapuaException {
//        EntityManager entityManager = Mockito.mock(EntityManager.class);
//        KapuaQuery kapuaQuery = Mockito.mock(KapuaQuery.class);
//
//        List<String> list = new ArrayList<>();
//        CriteriaBuilder criteriaBuilder = Mockito.mock(CriteriaBuilder.class);
//        CriteriaQuery<EventStoreRecordImpl> criteriaQuery1 = Mockito.mock(CriteriaQuery.class);
//        CriteriaQuery<EventStoreRecordImpl> criteriaQuery2 = Mockito.mock(CriteriaQuery.class);
//        CriteriaQuery<EventStoreRecordImpl> criteriaQuery3 = Mockito.mock(CriteriaQuery.class);
//        Root<EventStoreRecordImpl> entityRoot = Mockito.mock(Root.class);
//        EntityType<EventStoreRecordImpl> entityType = Mockito.mock(EntityType.class);
//        TypedQuery<EventStoreRecordImpl> query = Mockito.mock(TypedQuery.class);
//
//        Mockito.when(entityManager.getCriteriaBuilder()).thenReturn(criteriaBuilder);
//        Mockito.when(criteriaBuilder.createQuery(EventStoreRecordImpl.class)).thenReturn(criteriaQuery1);
//        Mockito.when(criteriaQuery1.from(EventStoreRecordImpl.class)).thenReturn(entityRoot);
//        Mockito.when(entityRoot.getModel()).thenReturn(entityType);
//        Mockito.when(criteriaQuery1.select(entityRoot)).thenReturn(criteriaQuery2);
//        Mockito.when(criteriaQuery2.distinct(true)).thenReturn(criteriaQuery3);
//        Mockito.when(kapuaQuery.getFetchAttributes()).thenReturn(list);
//        Mockito.when(entityManager.createQuery(criteriaQuery1)).thenReturn(query);
//
//        Assert.assertThat("EventStoreRecordListResult object expected.", EventStoreDAO.query(entityManager, kapuaQuery), IsInstanceOf.instanceOf(EventStoreRecordListResult.class));
//    }
//
//    @Test
//    public void countTest() throws KapuaException {
//        EntityManager entityManager = Mockito.mock(EntityManager.class);
//        KapuaQuery kapuaQuery = Mockito.mock(KapuaQuery.class);
//        CriteriaBuilder criteriaBuilder = Mockito.mock(CriteriaBuilder.class);
//        CriteriaQuery<Long> criteriaQuery1 = Mockito.mock(CriteriaQuery.class);
//        CriteriaQuery<Long> criteriaQuery2 = Mockito.mock(CriteriaQuery.class);
//        Root<EventStoreRecordImpl> entityRoot = Mockito.mock(Root.class);
//        TypedQuery<Long> query = Mockito.mock(TypedQuery.class);
//        Selection<Long> selection = Mockito.mock(Selection.class);
//        Expression<Long> expression = Mockito.mock(Expression.class);
//        long[] longNumberList = {0L, 10L, 100L, 1000000L, 9223372036854775807L, -100L, -9223372036854775808L};
//
//        Mockito.when(entityManager.getCriteriaBuilder()).thenReturn(criteriaBuilder);
//        Mockito.when(criteriaBuilder.createQuery(Long.class)).thenReturn(criteriaQuery1);
//        Mockito.when(criteriaQuery1.from(EventStoreRecordImpl.class)).thenReturn(entityRoot);
//        Mockito.when(criteriaBuilder.countDistinct(entityRoot)).thenReturn(expression);
//        Mockito.when(criteriaQuery1.select(selection)).thenReturn(criteriaQuery2);
//        Mockito.when(entityManager.createQuery(criteriaQuery1)).thenReturn(query);
//        for (long number : longNumberList) {
//            Mockito.doReturn(number).when(query).getSingleResult();
//            Assert.assertThat("Long object expected.", EventStoreDAO.count(entityManager, kapuaQuery), IsInstanceOf.instanceOf(Long.class));
//        }
//    }
//
//    @Test
//    public void deleteTest() throws KapuaException {
//        EventStoreRecordImpl eventStoreRecordImpl = new EventStoreRecordImpl();
//        EntityManager entityManager = Mockito.mock(EntityManager.class);
//        KapuaEid[] scopeIds = {null, new KapuaEid(BigInteger.ONE)};
//        KapuaEid[] eventIds = {null, new KapuaEid(BigInteger.ONE)};
//        KapuaEntityNotFoundException kapuaEntityNotFoundException = new KapuaEntityNotFoundException("EventStoreRecordImpl", "1");
//        NullPointerException nullPointerException = new NullPointerException();
//
//        //entityToDelete null
//        for (KapuaId scopeId : scopeIds) {
//            Mockito.when(entityManager.find(EventStoreRecordImpl.class, eventIds[1])).thenReturn(null);
//            try {
//                EventStoreDAO.delete(entityManager, scopeId, eventIds[1]);
//            } catch (Exception e) {
//                Assert.assertEquals("KapuaEntityNotFoundException expected", kapuaEntityNotFoundException.toString(), e.toString());
//            }
//            try {
//                EventStoreDAO.delete(null, scopeId, eventIds[1]);
//            } catch (Exception e) {
//                Assert.assertEquals("NullPointerException expected", nullPointerException.toString(), e.toString());
//            }
//        }
//    }
} 
