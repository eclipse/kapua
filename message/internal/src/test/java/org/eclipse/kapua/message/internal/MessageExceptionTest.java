/*******************************************************************************
 * Copyright (c) 2016, 2022 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.message.internal;


import org.eclipse.kapua.qa.markers.junit.JUnitTests;
import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.categories.Category;


@Category(JUnitTests.class)
public class MessageExceptionTest {

    @Test
    public void invalidDestinationText() throws Exception {
        MessageException ex = new MessageException(MessageErrorCodes.INVALID_DESTINATION);
        String message = ex.getLocalizedMessage();

        Assert.assertEquals("Invalid destination", message);
    }

    @Test
    public void invalidMessageText() throws Exception {
        MessageException ex = new MessageException(MessageErrorCodes.INVALID_MESSAGE);
        String message = ex.getLocalizedMessage();

        Assert.assertEquals("Invalid message", message);
    }

    @Test
    public void invalidMetricTypeText() throws Exception {
        MessageException ex = new MessageException(MessageErrorCodes.INVALID_METRIC_TYPE, "int");
        String message = ex.getLocalizedMessage();

        Assert.assertEquals("Invalid metric type int", message);
    }

    @Test
    public void invalidMetricValueText() throws Exception {
        MessageException ex = new MessageException(MessageErrorCodes.INVALID_METRIC_VALUE,
                new NullPointerException(),
                0);
        String message = ex.getLocalizedMessage();

        Assert.assertEquals("Invalid metric value 0", message);
    }
}
