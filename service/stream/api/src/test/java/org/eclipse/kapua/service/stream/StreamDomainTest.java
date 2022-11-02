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
public class StreamDomainTest {

    StreamDomain streamDomain;

    @Before
    public void createInstanceOfClass() {
        streamDomain = new StreamDomain();
    }

    @Test
    public void getNameTest() {
        String expectedName = "stream";
        Assert.assertEquals("Expected and actual values should be the same.", expectedName, streamDomain.getName());
    }

    @Test
    public void getActionsTest() {
        Set expectedValue = new HashSet(Arrays.asList(Actions.write));
        Assert.assertEquals("Expected and actual values should be the same.", expectedValue, streamDomain.getActions());
    }

    @Test
    public void getGroupableTest() {
        Assert.assertFalse("False expected.", streamDomain.getGroupable());
    }
}
