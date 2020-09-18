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
package org.eclipse.kapua.service.stream;

import org.eclipse.kapua.model.domain.Actions;
import org.eclipse.kapua.qa.markers.junit.JUnitTests;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@Category(JUnitTests.class)
public class StreamDomainTest extends Assert {

    StreamDomain streamDomain;

    @Before
    public void createInstanceOfClass() {
        streamDomain = new StreamDomain();
    }

    @Test
    public void getNameTest() {
        String expectedName = "stream";
        assertEquals("Expected and actual values should be the same.", expectedName, streamDomain.getName());
    }

    @Test
    public void getActionsTest() {
        Set expectedValue = new HashSet(Arrays.asList(Actions.write));
        assertEquals("Expected and actual values should be the same.", expectedValue, streamDomain.getActions());
    }

    @Test
    public void getGroupableTest() {
        assertFalse("False expected.", streamDomain.getGroupable());
    }
}