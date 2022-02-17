/*******************************************************************************
 * Copyright (c) 2019, 2022 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.app.console.module.api.test;

import org.eclipse.kapua.app.console.module.api.client.util.SplitTooltipStringUtil;
import org.eclipse.kapua.qa.markers.junit.JUnitTests;
import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.categories.Category;

@Category(JUnitTests.class)
public class SplitTooltipStringTest {

    @Test
    public void testStringSplit() {
        String inputString = "testtesttesttest";
        String result = SplitTooltipStringUtil.splitTooltipString(inputString, 10);
        Assert.assertTrue(result.contains("</br>"));
    }
}
