/*******************************************************************************
 * Copyright (c) 2011, 2017 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.commons.model;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.Collection;
import java.util.Random;

import org.eclipse.kapua.commons.model.id.KapuaEid;
import org.eclipse.kapua.model.id.KapuaId;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

@RunWith(value = Parameterized.class)
public class KapuaEidTest {

    private final BigInteger eid;

    // bug in eclipse cannot convert this to a single paramater test
    // https://github.com/junit-team/junit4/wiki/Parameterized-tests
    @SuppressWarnings("unused")
    private final long notUsed;

    @Parameters
    public static Collection<Object[]> eids() {
        return Arrays.asList(new Object[][] {
                { new BigInteger(64, new Random()), 0L },
                { new BigInteger(64, new Random()), 0L },
        });
    }

    public KapuaEidTest(BigInteger eid, long notUsed) {
        this.eid = eid;
        this.notUsed = notUsed;
    }

    @Test
    public void test() {
        KapuaEid kid = new KapuaEid(eid);
        String sid = kid.toCompactId();

        Assert.assertEquals(eid, kid.getId());
        Assert.assertEquals(eid.toString(), kid.toString());

        KapuaEid kid1 = KapuaEid.parseCompactId(sid);
        Assert.assertEquals(eid, kid1.getId());
        Assert.assertEquals(kid.toString(), kid1.toString());
        Assert.assertEquals(kid.toCompactId(), kid1.toCompactId());
    }

    @Test
    public void testParseKapuaId() {
        KapuaEid kapuaEid = new KapuaEid(eid);
        KapuaEid parsedKapuaEid = KapuaEid.parseKapuaId(kapuaEid);

        Assert.assertTrue(kapuaEid == parsedKapuaEid);
        Assert.assertEquals(kapuaEid, parsedKapuaEid);
        Assert.assertEquals(eid, kapuaEid.getId());
        Assert.assertEquals(eid.toString(), kapuaEid.toString());

        KapuaId kapuaIdAny = KapuaId.ANY;
        KapuaEid parsedKapuaIdAny = KapuaEid.parseKapuaId(kapuaIdAny);

        Assert.assertTrue(parsedKapuaIdAny != kapuaIdAny);
        Assert.assertEquals(parsedKapuaIdAny.getId(), kapuaIdAny.getId());
    }
}
