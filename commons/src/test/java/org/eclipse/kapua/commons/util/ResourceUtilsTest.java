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

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.io.IOException;
import java.net.URL;

import org.junit.Test;

public class ResourceUtilsTest {

    /**
     * Test a successful resource resolve call and a successful read
     */
    @Test
    public void testRead1() throws IOException {
        final URL url = ResourceUtils.getResource("test.properties");
        assertNotNull(url);
        
        final String string = ResourceUtils.readResource(url);
        assertNotNull(string);
        assertFalse(string.isEmpty());
    }

    /**
     * Test locating a resource which does not exist
     */
    @Test
    public void testRead2() {
        final URL url = ResourceUtils.getResource("does-not-exist");
        assertNull(url);
    }
    
    /**
     * Test reading a resource which does not exists
     */
    @Test(expected=IOException.class)
    public void testRead3 () throws IOException {
        ResourceUtils.readResource(new URL("file:/does-not-exists"));
    }
}
