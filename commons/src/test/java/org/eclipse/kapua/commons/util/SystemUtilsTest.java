/*******************************************************************************
 * Copyright (c) 2017 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *     Red Hat Inc
 *******************************************************************************/
package org.eclipse.kapua.commons.util;

import java.lang.reflect.Constructor;
import java.net.URISyntaxException;

import org.junit.Assert;
import org.junit.Test;

public class SystemUtilsTest extends Assert {

    @Test
    public void testConstructor() throws Exception {
        Constructor<SystemUtils> systemUtils = SystemUtils.class.getDeclaredConstructor();
        systemUtils.setAccessible(true);
        systemUtils.newInstance();
    }
    
    @Test
    public void getBrokerURITest()
            throws URISyntaxException {
        Assert.assertNotNull(SystemUtils.getBrokerURI());
    }
}
