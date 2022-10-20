/*******************************************************************************
 * Copyright (c) 2017, 2022 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.client.gateway.spi;

import org.eclipse.kapua.client.gateway.ErrorHandler;
import org.eclipse.kapua.client.gateway.MessageHandler;
import org.eclipse.kapua.client.gateway.Payload;
import org.eclipse.kapua.client.gateway.Topic;
import org.eclipse.kapua.qa.markers.junit.JUnitTests;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;


@Category(JUnitTests.class)
public class DefaultDataTest {

    private DefaultApplication application;
    private List<String> segments;
    private Topic topic;
    private DefaultData data;

    @Before
    public void init() {
        application = Mockito.mock(DefaultApplication.class);
        segments = new ArrayList<>();
        segments.add("string");
        topic = Topic.of(segments);
        data = new DefaultData(application, topic);
    }

    @Test
    public void sendNullTest() {
        Assert.assertEquals("Expected and actual values should be the same.", application.publish(topic, null), data.send((Payload) null));
    }

    @Test
    public void sendTest() {
        Payload payload = Mockito.mock(Payload.class);
        Assert.assertEquals("Expected and actual values should be the same.", application.publish(topic, payload), data.send(payload));
    }

    @Test
    public void subscribeTest() throws Exception {
        final MessageHandler messageHandler = Mockito.mock(MessageHandler.class);
        final ErrorHandler<Exception> errorHandler = Mockito.mock(ErrorHandler.class);
        Assert.assertEquals("Expected and actual values should be the same.", application.subscribe(topic, messageHandler, errorHandler), data.subscribe(messageHandler, errorHandler));
    }

    @Test(expected = NullPointerException.class)
    public void subscribeMessageHandlerNullTest() throws Exception {
        final ErrorHandler<Exception> errorHandler = Mockito.mock(ErrorHandler.class);
        data.subscribe(null, errorHandler);
    }

    @Test(expected = NullPointerException.class)
    public void subscribeErrorHandlerNullTest() throws Exception {
        final MessageHandler messageHandler = Mockito.mock(MessageHandler.class);
        data.subscribe(messageHandler, null);
    }
}
