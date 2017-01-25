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
 *
 *******************************************************************************/
package org.eclipse.kapua.message.internal;

import org.eclipse.kapua.message.KapuaChannel;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class KapuaChannelTest extends Assert {

    KapuaChannel kapuaChannel;

    @Before
    public void before() throws Exception {
        kapuaChannel = new KapuaChannelImpl();
    }

    @Test
    public void semanticParts() throws Exception {
        List<String> semanticParts = new ArrayList<>();
        semanticParts.add("part1");
        semanticParts.add("part2");
        semanticParts.add("part3");
        kapuaChannel.setSemanticParts(semanticParts);
        List<String> getSemanticParts = kapuaChannel.getSemanticParts();

        assertEquals(semanticParts, getSemanticParts);
    }

    @Test
    public void semanticPartsToString() throws Exception {
        List<String> semanticParts = new ArrayList<>();
        semanticParts.add("part1");
        semanticParts.add("part2");
        semanticParts.add("part3");
        kapuaChannel.setSemanticParts(semanticParts);
        String toString = kapuaChannel.toString();

        assertEquals("part1/part2/part3/", toString);
    }

    @Test
    public void semanticPartsToStringEmpty() throws Exception {
        List<String> semanticParts = new ArrayList<>();

        kapuaChannel.setSemanticParts(semanticParts);
        String toString = kapuaChannel.toString();

        assertEquals("NO semantic topic defined", toString);
    }

    @Test
    public void semanticPartsToStringNull() throws Exception {

        kapuaChannel.setSemanticParts(null);
        String toString = kapuaChannel.toString();

        assertEquals("NO semantic topic defined", toString);
    }

}
