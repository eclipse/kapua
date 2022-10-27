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
package org.eclipse.kapua.commons.service.event.store.internal;

import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.model.id.KapuaIdImpl;
import org.eclipse.kapua.qa.markers.junit.JUnitTests;
import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import java.math.BigInteger;


@Category(JUnitTests.class)
public class EventStoreQueryImplTest {

    @Test
    public void eventStoreQueryImplTest1() {
        EventStoreQueryImpl eventStoreQueryImpl = new EventStoreQueryImpl();
        Assert.assertNull("query.sortCriteria", eventStoreQueryImpl.getSortCriteria());
        Assert.assertNotNull("query.defaultSortCriteria", eventStoreQueryImpl.getDefaultSortCriteria());
    }

    @Test
    public void eventStoreQueryImplTest2() {
        KapuaId scopeId = new KapuaIdImpl(BigInteger.ONE);

        EventStoreQueryImpl eventStoreQueryImpl1 = new EventStoreQueryImpl(null);
        Assert.assertNull("query.sortCriteria", eventStoreQueryImpl1.getSortCriteria());
        Assert.assertNotNull("query.defaultSortCriteria", eventStoreQueryImpl1.getDefaultSortCriteria());
        Assert.assertNull("Null expected.", eventStoreQueryImpl1.getScopeId());

        EventStoreQueryImpl eventStoreQueryImpl2 = new EventStoreQueryImpl(scopeId);
        Assert.assertNull("query.sortCriteria", eventStoreQueryImpl2.getSortCriteria());
        Assert.assertNotNull("query.defaultSortCriteria", eventStoreQueryImpl2.getDefaultSortCriteria());
        Assert.assertEquals("Expected and actual values should be the same.", scopeId, eventStoreQueryImpl2.getScopeId());
    }
}
