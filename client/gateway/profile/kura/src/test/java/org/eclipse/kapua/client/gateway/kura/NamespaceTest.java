/*******************************************************************************
 * Copyright (c) 2017 Red Hat Inc and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Red Hat Inc - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.client.gateway.kura;

import org.eclipse.kapua.client.gateway.Topic;
import org.eclipse.kapua.client.gateway.kura.KuraNamespace.Builder;
import org.junit.Assert;
import org.junit.Test;

public class NamespaceTest {

    @Test(expected = IllegalArgumentException.class)
    public void testNullAccount() {
        new KuraNamespace.Builder().build();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEmptyAccount() {
        new KuraNamespace.Builder().accountName("").build();
    }

    @Test
    public void testSetAndGetAccount() {
        final Builder builder = new KuraNamespace.Builder().accountName("foo");
        Assert.assertEquals("foo", builder.accountName());
    }

    @Test
    public void testSegment1() {
        final KuraNamespace namespace = new KuraNamespace.Builder()
                .accountName("account")
                .build();
        Assert.assertEquals("account/c1/a1/seg1", namespace.dataTopic("c1", "a1", Topic.of("seg1")));
    }

    @Test
    public void testSegment2() {
        final KuraNamespace namespace = new KuraNamespace.Builder()
                .accountName("account")
                .build();
        Assert.assertEquals("account/c1/a1/seg1/seg2", namespace.dataTopic("c1", "a1", Topic.of("seg1", "seg2")));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidAccountId1() {
        new KuraNamespace.Builder()
                .accountName("account/1")
                .build();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidAccountId2() {
        new KuraNamespace.Builder()
                .accountName("#")
                .build();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidAccountId3() {
        new KuraNamespace.Builder()
                .accountName("+")
                .build();
    }
}
