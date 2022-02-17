/*******************************************************************************
 * Copyright (c) 2017, 2022 Red Hat Inc and others.
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Red Hat Inc - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.client.gateway.util;

import java.util.Optional;

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
