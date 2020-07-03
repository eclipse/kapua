/*******************************************************************************
 * Copyright (c) 2020 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
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
        assertNotNull("Null not expected.", eventStoreQueryImpl.getSortCriteria());
    }

    @Test
    public void eventStoreQueryImplTest2() {
        KapuaId scopeId = new KapuaIdStatic(BigInteger.ONE);

        EventStoreQueryImpl eventStoreQueryImpl1 = new EventStoreQueryImpl(null);
        assertNotNull("Null not expected.", eventStoreQueryImpl1.getSortCriteria());
        assertNull("Null expected.", eventStoreQueryImpl1.getScopeId());

        EventStoreQueryImpl eventStoreQueryImpl2 = new EventStoreQueryImpl(scopeId);
        assertNotNull("Null not expected.", eventStoreQueryImpl2.getSortCriteria());
        assertEquals("Expected and actual values should be the same.", scopeId, eventStoreQueryImpl2.getScopeId());
    }
}
