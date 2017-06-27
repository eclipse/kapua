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
package org.eclipse.kapua.client.gateway;

import java.util.Optional;

import org.eclipse.kapua.client.gateway.Errors;
import org.junit.Test;

public class ErrorsTest {

    @Test
    public void test1() {
        Errors.ignore().handleError(new Exception(), Optional.empty());
    }

    @Test
    public void test2() {
        Errors.handle((error, payload) -> {
        }).handleError(new Exception(), Optional.empty());
    }

    @Test
    public void test3() {
        Errors.handle(Errors::ignore).handleError(new Exception(), Optional.empty());
    }
}
