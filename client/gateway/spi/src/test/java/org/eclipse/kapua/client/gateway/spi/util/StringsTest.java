/*******************************************************************************
 * Copyright (c) 2017, 2020 Red Hat Inc and others.
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
package org.eclipse.kapua.client.gateway.spi.util;

import org.eclipse.kapua.qa.markers.junit.JUnitTests;
import org.junit.Test;
import org.junit.experimental.categories.Category;

@Category(JUnitTests.class)
public class StringsTest {

    @Test(expected = IllegalArgumentException.class)
    public void test1() {
        Strings.nonEmptyText(null, "foo");
    }

    @Test(expected = IllegalArgumentException.class)
    public void test2() {
        Strings.nonEmptyText("", "foo");
    }

    @Test
    public void test3() {
        Strings.nonEmptyText("foo", "foo");
    }
}
