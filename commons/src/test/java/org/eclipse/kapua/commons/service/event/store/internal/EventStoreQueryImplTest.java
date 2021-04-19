/*******************************************************************************
 * Copyright (c) 2020, 2021 Eurotech and/or its affiliates and others
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
import org.eclipse.kapua.model.id.KapuaIdStatic;
import org.eclipse.kapua.qa.markers.junit.JUnitTests;
import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import java.math.BigInteger;

@Category(JUnitTests.class)
public class EventStoreQueryImplTest extends Assert {

    @Test
    public void eventStoreQueryImplTest1() {
        EventStoreQueryImpl eventStoreQueryImpl = new EventStoreQueryImpl();
        assertNull("query.sortCriteria", eventStoreQueryImpl.getSortCriteria());
        assertNotNull("query.defaultSortCriteria", eventStoreQueryImpl.getDefaultSortCriteria());
    }

    @Test
    public void eventStoreQueryImplTest2() {
        KapuaId scopeId = new KapuaIdStatic(BigInteger.ONE);

        EventStoreQueryImpl eventStoreQueryImpl1 = new EventStoreQueryImpl(null);
        assertNull("query.sortCriteria", eventStoreQueryImpl1.getSortCriteria());
        assertNotNull("query.defaultSortCriteria", eventStoreQueryImpl1.getDefaultSortCriteria());
        assertNull("Null expected.", eventStoreQueryImpl1.getScopeId());

        EventStoreQueryImpl eventStoreQueryImpl2 = new EventStoreQueryImpl(scopeId);
        assertNull("query.sortCriteria", eventStoreQueryImpl2.getSortCriteria());
        assertNotNull("query.defaultSortCriteria", eventStoreQueryImpl2.getDefaultSortCriteria());
        assertEquals("Expected and actual values should be the same.", scopeId, eventStoreQueryImpl2.getScopeId());
    }
}
