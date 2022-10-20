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
import org.hamcrest.core.IsInstanceOf;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;


@Category(JUnitTests.class)
public class DefaultApplicationTest {

    private AbstractClient.Context context;
    private DefaultApplication defaultApplication;

    @Before
    public void init() {
        context = Mockito.mock(AbstractClient.Context.class);
        defaultApplication = new DefaultApplication(context);
    }

    @Test
    public void defaultApplicationContextIdTest() {
        Assert.assertThat("Instance of DefaultApplication expected.", defaultApplication, IsInstanceOf.instanceOf(DefaultApplication.class));
    }

    @Test
    public void transportTest() {
        Assert.assertEquals("Expected and actual values should be the same.", context.transport(), defaultApplication.transport());
    }

    @Test
    public void dataTest() {
        List<String> segments = new ArrayList<>();
        segments.add("string");
        Topic topic = Topic.of(segments);
        Assert.assertThat("Instance of DefaultData expected.", defaultApplication.data(topic), IsInstanceOf.instanceOf(DefaultData.class));
    }

    @Test
    public void dataNullTest() {
        Assert.assertThat("Instance of DefaultData expected.", defaultApplication.data(null), IsInstanceOf.instanceOf(DefaultData.class));
    }

    @Test
    public void closeTest() throws Exception {
        defaultApplication.close();
        Mockito.verify(context, Mockito.times(1)).close();
    }

    @Test
    public void publishTest() {
        List<String> segments = new ArrayList<>();
        segments.add("string");
        Topic topic = Topic.of(segments);
        Payload payload = Mockito.mock(Payload.class);
        Assert.assertEquals("Expected and actual values should be the same.", context.publish(topic, payload), defaultApplication.publish(topic, payload));
    }

    @Test
    public void publishTopicNullTest() {
        Payload payload = Mockito.mock(Payload.class);
        Assert.assertEquals("Expected and actual values should be the same.", context.publish(null, payload), defaultApplication.publish(null, payload));
    }

    @Test
    public void publishPayloadNullTest() {
        List<String> segments = new ArrayList<>();
        segments.add("string");
        Topic topic = Topic.of(segments);
        Assert.assertEquals("Expected and actual values should be the same.", context.publish(topic, null), defaultApplication.publish(topic, null));
    }

    @Test
    public void publishTopicAndPayloadNullTest() {
        Assert.assertEquals("Expected and actual values should be the same.", context.publish(null, null), defaultApplication.publish(null, null));
    }

    @Test
    public void subscribeTest() throws Exception {
        List<String> segments = new ArrayList<>();
        segments.add("string");
        final Topic topic = Topic.of(segments);
        final MessageHandler messageHandler = Mockito.mock(MessageHandler.class);
        final ErrorHandler<Exception> errorHandler = Mockito.mock(ErrorHandler.class);
        Assert.assertEquals("Expected and actual values should be the same.", context.subscribe(topic, messageHandler, errorHandler), defaultApplication.subscribe(topic, messageHandler, errorHandler));
    }

    @Test
    public void subscribeTopicNullTest() throws Exception {
        final MessageHandler messageHandler = Mockito.mock(MessageHandler.class);
        final ErrorHandler<Exception> errorHandler = Mockito.mock(ErrorHandler.class);
        Assert.assertEquals("Expected and actual values should be the same.", context.subscribe(null, messageHandler, errorHandler), defaultApplication.subscribe(null, messageHandler, errorHandler));
    }

    @Test
    public void subscribeMessageHandlerNullTest() throws Exception {
        List<String> segments = new ArrayList<>();
        segments.add("string");
        final Topic topic = Topic.of(segments);
        final ErrorHandler<Exception> errorHandler = Mockito.mock(ErrorHandler.class);
        Assert.assertEquals("Expected and actual values should be the same.", context.subscribe(topic, null, errorHandler), defaultApplication.subscribe(topic, null, errorHandler));
    }

    @Test
    public void subscribeErrorHandlerNullTest() throws Exception {
        List<String> segments = new ArrayList<>();
        segments.add("string");
        final Topic topic = Topic.of(segments);
        final MessageHandler messageHandler = Mockito.mock(MessageHandler.class);
        Assert.assertEquals("Expected and actual values should be the same.", context.subscribe(topic, messageHandler, null), defaultApplication.subscribe(topic, messageHandler, null));
    }

    @Test
    public void subscribeNullTest() throws Exception {
        Assert.assertEquals("Expected and actual values should be the same.", context.subscribe(null, null, null), defaultApplication.subscribe(null, null, null));
    }
}
