/*******************************************************************************
 * Copyright (c) 2017 Red Hat Inc and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Red Hat Inc - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.commons.util;

import java.io.IOException;
import java.io.Reader;
import java.net.URL;
import java.nio.charset.StandardCharsets;

import org.junit.Assert;
import org.junit.Test;

import com.google.common.io.CharStreams;

public class ResourceUtilsTest {

    /**
     * Test a successful resource resolve call and a successful read
     */
    @Test
    public void testRead1() throws IOException {
        final URL url = ResourceUtils.getResource("test.properties");
        Assert.assertNotNull(url);

        try (final Reader reader = ResourceUtils.openAsReader(url, StandardCharsets.UTF_8)) {
            final String string = CharStreams.toString(reader);
            Assert.assertNotNull(string);
            Assert.assertFalse(string.isEmpty());
        }
    }

    /**
     * Test locating a resource which does not exist
     */
    @Test
    public void testRead2() {
        final URL url = ResourceUtils.getResource("does-not-exist");
        Assert.assertNull(url);
    }

    /**
     * Test reading a resource which does not exists
     */
    @Test(expected = IOException.class)
    public void testRead3() throws IOException {
        ResourceUtils.openAsReader(new URL("file:/does-not-exists"), StandardCharsets.UTF_8);
    }
}
