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

import org.eclipse.kapua.commons.jpa.EntityManagerFactory;
import org.eclipse.kapua.commons.model.id.KapuaEid;
import org.eclipse.kapua.commons.service.event.store.api.EventStoreRecordCreator;
import org.eclipse.kapua.commons.service.event.store.internal.EventStoreRecordCreatorImpl;
import org.eclipse.kapua.commons.service.event.store.internal.EventStoreServiceImpl;

import org.eclipse.kapua.qa.markers.junit.JUnitTests;

import org.hamcrest.core.IsInstanceOf;
import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.mockito.Mockito;

import java.math.BigInteger;


@Category(JUnitTests.class)
public class EventStoreServiceImplTest {

    @Test
    public void eventStoreServiceImplTest() {
        EntityManagerFactory entityManagerFactory = Mockito.mock(EntityManagerFactory.class);
        EventStoreServiceImpl eventStoreServiceImpl = new EventStoreServiceImpl(entityManagerFactory);
        Assert.assertThat("EventStoreServiceImpl object expected.", eventStoreServiceImpl, IsInstanceOf.instanceOf(EventStoreServiceImpl.class));
    }

    @Test
    public void createTest() {
        EntityManagerFactory entityManagerFactory = Mockito.mock(EntityManagerFactory.class);
        EventStoreServiceImpl eventStoreServiceImpl = new EventStoreServiceImpl(entityManagerFactory);
        EventStoreRecordCreator[] creator = {null, new EventStoreRecordCreatorImpl(new KapuaEid(BigInteger.ONE))};
        UnsupportedOperationException unsupportedOperationException = new UnsupportedOperationException();

        for (EventStoreRecordCreator eventStoreRecordCreator : creator) {
            try {
                eventStoreServiceImpl.create(eventStoreRecordCreator);
            } catch (Exception e) {
                Assert.assertEquals("Expected and actual values should be the same.", unsupportedOperationException.toString(), e.toString());
            }
        }
    }
} 
