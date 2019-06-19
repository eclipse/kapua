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
package org.eclipse.kapua.message.internal;

import org.eclipse.kapua.message.device.data.KapuaDataChannel;
import org.eclipse.kapua.message.internal.device.data.KapuaDataChannelImpl;
import org.eclipse.kapua.qa.markers.junit.JUnitTests;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import java.util.ArrayList;
import java.util.List;

@Category(JUnitTests.class)
public class KapuaChannelTest extends Assert {

    KapuaDataChannel kapuaDataChannel;

    @Before
    public void before() throws Exception {
        kapuaDataChannel = new KapuaDataChannelImpl();
    }

    @Test
    public void semanticParts() throws Exception {
        List<String> semanticParts = new ArrayList<>();
        semanticParts.add("part1");
        semanticParts.add("part2");
        semanticParts.add("part3");
        kapuaDataChannel.setSemanticParts(semanticParts);
        List<String> getSemanticParts = kapuaDataChannel.getSemanticParts();

        assertEquals(semanticParts, getSemanticParts);
    }

    @Test
    public void semanticPartsToString() throws Exception {
        List<String> semanticParts = new ArrayList<>();
        semanticParts.add("part1");
        semanticParts.add("part2");
        semanticParts.add("part3");
        kapuaDataChannel.setSemanticParts(semanticParts);
        String toString = kapuaDataChannel.toString();

        assertEquals("part1/part2/part3", toString);
    }

    @Test
    public void semanticPartsToStringEmpty() throws Exception {
        List<String> semanticParts = new ArrayList<>();

        kapuaDataChannel.setSemanticParts(semanticParts);
        String toString = kapuaDataChannel.toString();

        assertEquals("", toString);
    }

    @Test
    public void semanticPartsToStringNull() throws Exception {

        kapuaDataChannel.setSemanticParts(null);
        String toString = kapuaDataChannel.toString();

        assertEquals("", toString);
    }

}
