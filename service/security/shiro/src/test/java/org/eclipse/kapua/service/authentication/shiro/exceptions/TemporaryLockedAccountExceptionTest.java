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
package org.eclipse.kapua.service.authentication.shiro.exceptions;

import java.util.Date;

import org.eclipse.kapua.service.authentication.shiro.exceptions.TemporaryLockedAccountException;
import org.junit.Assert;
import org.junit.Test;

public class TemporaryLockedAccountExceptionTest {

    @Test
    public void testDefault() {
        TemporaryLockedAccountException ex = new TemporaryLockedAccountException(new Date());
        Assert.assertNotNull(ex.getMessage());
    }
    
    @Test
    public void testNullTimestamp() {
        TemporaryLockedAccountException ex = new TemporaryLockedAccountException(null);
        Assert.assertNotNull(ex.getMessage());
    }
}
