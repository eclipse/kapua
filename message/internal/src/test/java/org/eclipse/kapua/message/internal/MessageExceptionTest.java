/*******************************************************************************
 * Copyright (c) 2011, 2017 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
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
public class MessageExceptionTest extends Assert {

    @Test
    public void invalidDestinationText() throws Exception {
        MessageException ex = new MessageException(MessageErrorCodes.INVALID_DESTINATION);
        String message = ex.getLocalizedMessage();

        assertEquals("Invalid destination", message);
    }

    @Test
    public void invalidMessageText() throws Exception {
        MessageException ex = new MessageException(MessageErrorCodes.INVALID_MESSAGE);
        String message = ex.getLocalizedMessage();

        assertEquals("Invalid message", message);
    }

    @Test
    public void invalidMetricTypeText() throws Exception {
        MessageException ex = new MessageException(MessageErrorCodes.INVALID_METRIC_TYPE, "int");
        String message = ex.getLocalizedMessage();

        assertEquals("Invalid metric type int", message);
    }

    @Test
    public void invalidMetricValueText() throws Exception {
        MessageException ex = new MessageException(MessageErrorCodes.INVALID_METRIC_VALUE,
                new NullPointerException(),
                0);
        String message = ex.getLocalizedMessage();

        assertEquals("Invalid metric value 0", message);
    }
}
