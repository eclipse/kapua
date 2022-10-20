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
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;


@Category(JUnitTests.class)
public class DefaultClientTest {

    @Test
    public void builderChannelIdTest() {
        Channel channel = Mockito.mock(Channel.class);
        DefaultClient.Builder builder = new DefaultClient.Builder(channel);
        Assert.assertEquals("Expected and actual values should be the same.", builder, builder.module(Mockito.mock(Module.class)));
    }

    @Test(expected = NullPointerException.class)
    public void builderChannelIdNullTest() {
        DefaultClient.Builder builder = new DefaultClient.Builder(null);
        Assert.assertEquals("Expected and actual values should be the same.", builder, builder.module(Mockito.mock(Module.class)));
    }

    @Test
    public void builderTest() {
        Channel channel = Mockito.mock(Channel.class);
        DefaultClient.Builder builder = new DefaultClient.Builder(channel);
        Assert.assertEquals("Expected and actual values should be the same.", builder, builder.builder());
    }

    @Test
    public void buildTest() throws Exception {
        Channel channel = Mockito.mock(Channel.class);
        DefaultClient.Builder builder = new DefaultClient.Builder(channel);
        DefaultClient defaultClientActual = (DefaultClient) builder.build();
        final ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
        final Set<Module> modules = new HashSet<>();
        modules.add(Mockito.mock(Module.class));
        DefaultClient client = new DefaultClient(channel, executorService, modules);
        client.init();
        Assert.assertEquals("Expected and actual values should be the same.", client.executor.isShutdown(), defaultClientActual.executor.isShutdown());
    }

    @Test
    public void defaultClientTest() {
        final Channel channel = Mockito.mock(Channel.class);
        final ScheduledExecutorService executorService = Mockito.mock(ScheduledExecutorService.class);
        final Set<Module> modules = new HashSet<>();
        modules.add(Mockito.mock(Module.class));
        DefaultClient client = new DefaultClient(channel, executorService, modules);
        Assert.assertEquals("Expected and actual values should be the same.", executorService, client.executor);
    }

    @Test(expected = NullPointerException.class)
    public void defaultClientChannelNullTest() {
        final ScheduledExecutorService executorService = Mockito.mock(ScheduledExecutorService.class);
        final Set<Module> modules = new HashSet<>();
        modules.add(Mockito.mock(Module.class));
        DefaultClient client = new DefaultClient(null, executorService, modules);
    }

    @Test
    public void defaultClientExecutorNullTest() {
        final Channel channel = Mockito.mock(Channel.class);
        final Set<Module> modules = new HashSet<>();
        modules.add(Mockito.mock(Module.class));
        DefaultClient client = new DefaultClient(channel, null, modules);
        Assert.assertNull("Expected null.", client.executor);
    }

    @Test(expected = NullPointerException.class)
    public void defaultClientModulesNullTest() {
        final Channel channel = Mockito.mock(Channel.class);
        final ScheduledExecutorService executorService = Mockito.mock(ScheduledExecutorService.class);
        DefaultClient client = new DefaultClient(channel, executorService, null);
        Assert.assertEquals("Expected and actual values should be the same.", executorService, client.executor);
    }

    @Test
    public void closeTest() throws Exception {
        final Channel channel = Mockito.mock(Channel.class);
        final ScheduledExecutorService executorService = Mockito.mock(ScheduledExecutorService.class);
        final Set<Module> modules = new HashSet<>();
        modules.add(Mockito.mock(Module.class));
        DefaultClient client = new DefaultClient(channel, executorService, modules);
        client.close();
        Assert.assertNull("Expected null.", client.executor.submit(Mockito.mock(Runnable.class)));
    }

    @Test(expected = ClassNotFoundException.class)
    public void adaptModuleContextTest() throws ClassNotFoundException {
        final Channel channel = Mockito.mock(Channel.class);
        final ScheduledExecutorService executorService = Mockito.mock(ScheduledExecutorService.class);
        final Set<Module> modules = new HashSet<>();
        modules.add(Mockito.mock(Module.class));
        DefaultClient client = new DefaultClient(channel, executorService, modules);
        final Class<Integer> clazz = (Class<Integer>) Class.forName(new String());
        client.adaptModuleContext(clazz);
    }

    @Test
    public void handleSubscribeTest() {
        final Channel channel = Mockito.mock(Channel.class);
        final ScheduledExecutorService executorService = Mockito.mock(ScheduledExecutorService.class);
        final Set<Module> modules = new HashSet<>();
        modules.add(Mockito.mock(Module.class));
        DefaultClient client = new DefaultClient(channel, executorService, modules);
        String applicationId = "id";
        List<String> segments = new ArrayList<>();
        segments.add("string");
        Topic topic = Topic.of(segments);
        MessageHandler messageHandler = Mockito.mock(MessageHandler.class);
        ErrorHandler<Exception> errorHandler = Mockito.mock(ErrorHandler.class);
        Assert.assertEquals("Expected and actual values should be the same.", channel.handleSubscribe(applicationId, topic, messageHandler, errorHandler), client.handleSubscribe(applicationId, topic, messageHandler, errorHandler));
    }

    @Test
    public void handleSubscribeApplicationIdNullTest() {
        final Channel channel = Mockito.mock(Channel.class);
        final ScheduledExecutorService executorService = Mockito.mock(ScheduledExecutorService.class);
        final Set<Module> modules = new HashSet<>();
        modules.add(Mockito.mock(Module.class));
        DefaultClient client = new DefaultClient(channel, executorService, modules);
        List<String> segments = new ArrayList<>();
        segments.add("string");
        Topic topic = Topic.of(segments);
        MessageHandler messageHandler = Mockito.mock(MessageHandler.class);
        ErrorHandler<Exception> errorHandler = Mockito.mock(ErrorHandler.class);
        Assert.assertEquals("Expected and actual values should be the same.", channel.handleSubscribe(null, topic, messageHandler, errorHandler), client.handleSubscribe(null, topic, messageHandler, errorHandler));
    }

    @Test
    public void handleSubscribeTopicNullTest() {
        final Channel channel = Mockito.mock(Channel.class);
        final ScheduledExecutorService executorService = Mockito.mock(ScheduledExecutorService.class);
        final Set<Module> modules = new HashSet<>();
        modules.add(Mockito.mock(Module.class));
        DefaultClient client = new DefaultClient(channel, executorService, modules);
        String applicationId = "id";
        MessageHandler messageHandler = Mockito.mock(MessageHandler.class);
        ErrorHandler<Exception> errorHandler = Mockito.mock(ErrorHandler.class);
        Assert.assertEquals("Expected and actual values should be the same.", channel.handleSubscribe(applicationId, null, messageHandler, errorHandler), client.handleSubscribe(applicationId, null, messageHandler, errorHandler));
    }

    @Test
    public void handleSubscribeMessageHandlerNullTest() {
        final Channel channel = Mockito.mock(Channel.class);
        final ScheduledExecutorService executorService = Mockito.mock(ScheduledExecutorService.class);
        final Set<Module> modules = new HashSet<>();
        modules.add(Mockito.mock(Module.class));
        DefaultClient client = new DefaultClient(channel, executorService, modules);
        String applicationId = "id";
        List<String> segments = new ArrayList<>();
        segments.add("string");
        Topic topic = Topic.of(segments);
        ErrorHandler<Exception> errorHandler = Mockito.mock(ErrorHandler.class);
        Assert.assertEquals("Expected and actual values should be the same.", channel.handleSubscribe(applicationId, topic, null, errorHandler), client.handleSubscribe(applicationId, topic, null, errorHandler));
    }

    @Test
    public void handleSubscribeErrorHandlerNullTest() {
        final Channel channel = Mockito.mock(Channel.class);
        final ScheduledExecutorService executorService = Mockito.mock(ScheduledExecutorService.class);
        final Set<Module> modules = new HashSet<>();
        modules.add(Mockito.mock(Module.class));
        DefaultClient client = new DefaultClient(channel, executorService, modules);
        String applicationId = "id";
        List<String> segments = new ArrayList<>();
        segments.add("string");
        Topic topic = Topic.of(segments);
        MessageHandler messageHandler = Mockito.mock(MessageHandler.class);
        Assert.assertEquals("Expected and actual values should be the same.", channel.handleSubscribe(applicationId, topic, messageHandler, null), client.handleSubscribe(applicationId, topic, messageHandler, null));
    }

    @Test
    public void handlePublishTest() {
        final Channel channel = Mockito.mock(Channel.class);
        final ScheduledExecutorService executorService = Mockito.mock(ScheduledExecutorService.class);
        final Set<Module> modules = new HashSet<>();
        modules.add(Mockito.mock(Module.class));
        DefaultClient client = new DefaultClient(channel, executorService, modules);
        String applicationId = "id";
        List<String> segments = new ArrayList<>();
        segments.add("string");
        Topic topic = Topic.of(segments);
        Payload payload = Mockito.mock(Payload.class);
        Assert.assertEquals("Expected and actual values should be the same.", channel.handlePublish(applicationId, topic, payload), client.handlePublish(applicationId, topic, payload));
    }

    @Test
    public void handlePublishApplicationIdNullTest() {
        final Channel channel = Mockito.mock(Channel.class);
        final ScheduledExecutorService executorService = Mockito.mock(ScheduledExecutorService.class);
        final Set<Module> modules = new HashSet<>();
        modules.add(Mockito.mock(Module.class));
        DefaultClient client = new DefaultClient(channel, executorService, modules);
        List<String> segments = new ArrayList<>();
        segments.add("string");
        Topic topic = Topic.of(segments);
        Payload payload = Mockito.mock(Payload.class);
        Assert.assertEquals("Expected and actual values should be the same.", channel.handlePublish(null, topic, payload), client.handlePublish(null, topic, payload));
    }

    @Test
    public void handlePublishTopicNullTest() {
        final Channel channel = Mockito.mock(Channel.class);
        final ScheduledExecutorService executorService = Mockito.mock(ScheduledExecutorService.class);
        final Set<Module> modules = new HashSet<>();
        modules.add(Mockito.mock(Module.class));
        DefaultClient client = new DefaultClient(channel, executorService, modules);
        String applicationId = "id";
        Payload payload = Mockito.mock(Payload.class);
        Assert.assertEquals("Expected and actual values should be the same.", channel.handlePublish(applicationId, null, payload), client.handlePublish(applicationId, null, payload));
    }

    @Test
    public void handlePublishPayloadNullTest() {
        final Channel channel = Mockito.mock(Channel.class);
        final ScheduledExecutorService executorService = Mockito.mock(ScheduledExecutorService.class);
        final Set<Module> modules = new HashSet<>();
        modules.add(Mockito.mock(Module.class));
        DefaultClient client = new DefaultClient(channel, executorService, modules);
        String applicationId = "id";
        List<String> segments = new ArrayList<>();
        segments.add("string");
        Topic topic = Topic.of(segments);
        Assert.assertEquals("Expected and actual values should be the same.", channel.handlePublish(applicationId, topic, null), client.handlePublish(applicationId, topic, null));
    }

    @Test
    public void handleUnsubscribeTest() throws Exception {
        final Channel channel = Mockito.mock(Channel.class);
        final ScheduledExecutorService executorService = Mockito.mock(ScheduledExecutorService.class);
        final Set<Module> modules = new HashSet<>();
        modules.add(Mockito.mock(Module.class));
        DefaultClient client = new DefaultClient(channel, executorService, modules);
        String applicationId = "id";
        List<String> segments = new ArrayList<>();
        segments.add("string");
        Topic topic = Topic.of(segments);
        Collection<Topic> topics = new ArrayList<>();
        topics.add(topic);
        client.handleUnsubscribe(applicationId, topics);
        Assert.assertEquals("Expected and actual values should be the same.", channel.handleSubscribe(applicationId, topic, Mockito.mock(MessageHandler.class), Mockito.mock(ErrorHandler.class)), client.handleSubscribe(applicationId, topic, Mockito.mock(MessageHandler.class), Mockito.mock(ErrorHandler.class)));
    }

    @Test
    public void handleUnsubscribeApplicationIdNullTest() throws Exception {
        final Channel channel = Mockito.mock(Channel.class);
        final ScheduledExecutorService executorService = Mockito.mock(ScheduledExecutorService.class);
        final Set<Module> modules = new HashSet<>();
        modules.add(Mockito.mock(Module.class));
        DefaultClient client = new DefaultClient(channel, executorService, modules);
        List<String> segments = new ArrayList<>();
        segments.add("string");
        Topic topic = Topic.of(segments);
        Collection<Topic> topics = new ArrayList<>();
        topics.add(topic);
        client.handleUnsubscribe(null, topics);
        Assert.assertEquals("Expected and actual values should be the same.", channel.handleSubscribe(null, topic, Mockito.mock(MessageHandler.class), Mockito.mock(ErrorHandler.class)), client.handleSubscribe(null, topic, Mockito.mock(MessageHandler.class), Mockito.mock(ErrorHandler.class)));
    }

    @Test
    public void handleUnsubscribeTopicsNullTest() throws Exception {
        final Channel channel = Mockito.mock(Channel.class);
        final ScheduledExecutorService executorService = Mockito.mock(ScheduledExecutorService.class);
        final Set<Module> modules = new HashSet<>();
        modules.add(Mockito.mock(Module.class));
        DefaultClient client = new DefaultClient(channel, executorService, modules);
        String applicationId = "id";
        client.handleUnsubscribe(applicationId, null);
        Assert.assertEquals("Expected and actual values should be the same.", channel.handleSubscribe(applicationId, null, Mockito.mock(MessageHandler.class), Mockito.mock(ErrorHandler.class)), client.handleSubscribe(applicationId, null, Mockito.mock(MessageHandler.class), Mockito.mock(ErrorHandler.class)));
    }
}
