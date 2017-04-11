/*******************************************************************************
 * Copyright (c) 2017 Red Hat Inc and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Red Hat Inc - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.service.datastore.internal.elasticsearch;

import java.math.BigInteger;
import java.time.Instant;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;

import org.eclipse.kapua.commons.model.id.KapuaEid;
import org.eclipse.kapua.model.id.KapuaId;
import org.junit.Assert;
import org.junit.Test;

public class EsUtilsIndexNameTest {

    private static final KapuaId ONE = new KapuaEid(BigInteger.ONE);

    @Test
    public void test1() {
        final Instant instant = ZonedDateTime.of(2017, 1, 1, 0, 0, 0, 0, ZoneOffset.UTC).toInstant();
        final String name = EsUtils.getDataIndexName(ONE, instant.toEpochMilli());
        Assert.assertEquals("1-2017-01", name);
    }
    
    @Test
    public void test2() {
        final Instant instant = ZonedDateTime.of(2017, 1, 8, 0, 0, 0, 0, ZoneOffset.UTC).toInstant();
        final String name = EsUtils.getDataIndexName(ONE, instant.toEpochMilli());
        Assert.assertEquals("1-2017-02", name);
    }
}
