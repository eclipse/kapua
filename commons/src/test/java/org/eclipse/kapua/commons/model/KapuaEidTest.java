/*******************************************************************************
 * Copyright (c) 2011, 2016 Eurotech and/or its affiliates and others
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

import static org.junit.Assert.assertEquals;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.Collection;
import java.util.Random;

import org.eclipse.kapua.commons.model.id.KapuaEid;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

@RunWith(value = Parameterized.class)
public class KapuaEidTest 
{
	private BigInteger eid;
	
	// bug in eclipse cannot convert this to a single paramater test
	// https://github.com/junit-team/junit4/wiki/Parameterized-tests
	@SuppressWarnings("unused")
	private long notUsed; 

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
	public void test() 
	{
		KapuaEid kid = new KapuaEid(eid);
        String sid = kid.toCompactId();
		System.out.println(eid+"="+sid);

		assertEquals(eid, kid.getId());
		assertEquals(eid.toString(), kid.toString());

        KapuaEid kid1 = KapuaEid.parseCompactId(sid);
		assertEquals(eid, kid1.getId());
		assertEquals(kid.toString(), kid1.toString());
        assertEquals(kid.toCompactId(), kid1.toCompactId());
	}
}
