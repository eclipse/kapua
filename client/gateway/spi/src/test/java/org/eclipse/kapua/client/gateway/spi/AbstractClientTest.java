/*******************************************************************************
 * Copyright (c) 2021, 2022 Eurotech and/or its affiliates and others
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

import org.eclipse.kapua.client.gateway.Application;
import org.eclipse.kapua.client.gateway.ErrorHandler;
import org.eclipse.kapua.client.gateway.MessageHandler;
import org.eclipse.kapua.client.gateway.Payload;
import org.eclipse.kapua.client.gateway.Topic;
import org.eclipse.kapua.client.gateway.spi.util.TransportAsync;
import org.eclipse.kapua.qa.markers.junit.JUnitTests;
import org.hamcrest.core.IsInstanceOf;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import org.mockito.Mockito;

import java.util.Arrays;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.ScheduledExecutorService;

@Category(JUnitTests.class)
@RunWith(value = Parameterized.class)
public class AbstractClientTest {

    private ScheduledExecutorService executor;
    private Module module;
    private Set<Module> modules;
    private AbstractClient abstractClient;

    private final String applicationId;

    public AbstractClientTest(String applicationId) {
        this.applicationId = applicationId;
    }

    @Before
    public void init() {
        executor = Mockito.mock(ScheduledExecutorService.class);
        modules = new HashSet<>();
        modules.add(Mockito.mock(Module.class));
        modules.add(Mockito.mock(Module.class));
        module = Mockito.mock(Module.class);
        modules.add(module);
        abstractClient = new ActualClient(executor, modules);
    }

    @Parameters
    public static Iterable<Object[]> applicationId() {
        return Arrays.asList(
                new Object[]{null},
                new Object[]{""},
                new Object[]{"Application Id"},
                new Object[]{"APPLICATIONID"},
                new Object[]{"0123456789"},
                new Object[]{"!#$%&'()=?⁄@‹›€°·‚,.-;:_Èˇ¿<>«‘”’ÉØ∏{}|ÆæÒuF8FFÔÓÌÏÎÅ«»Ç◊Ñˆ¯Èˇ"},
                new Object[]{"abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789abcdefg"},
                new Object[]{"$EDC/string/string/MQTT/BIRTH"});
    }

    private class ActualClient extends AbstractClient {

        public ActualClient(ScheduledExecutorService executor, Set<Module> modules) {
            super(executor, modules);
        }

        @Override
        protected CompletionStage<?> handleSubscribe(String applicationId, Topic topic, MessageHandler messageHandler, ErrorHandler<? extends Throwable> errorHandler) {
            return null;
        }

        @Override
        protected CompletionStage<?> handlePublish(String applicationId, Topic topic, Payload payload) {
            return null;
        }

        @Override
        protected void handleUnsubscribe(String applicationId, Collection<Topic> topics) throws Exception {

        }

        @Override
        public void close() throws Exception {

        }
    }

    @Test
    public void abstractClientTest() {
        Assert.assertEquals("Expected and actual values should be the same.", executor, abstractClient.executor);
        Assert.assertThat("Instance of TransportAsync expected", abstractClient.transport(), IsInstanceOf.instanceOf(TransportAsync.class));
    }

    @Test
    public void abstractClientExecutorNullTest() {
        final Set<Module> modules = new HashSet<>();
        modules.add(Mockito.mock(Module.class));
        AbstractClient abstractClient = new ActualClient(null, modules);
        Assert.assertNull("Expected null value.", abstractClient.executor);
        Assert.assertThat("Instance of TransportAsync expected", abstractClient.transport(), IsInstanceOf.instanceOf(TransportAsync.class));
    }

    @Test(expected = NullPointerException.class)
    public void abstractClientModulesNullTest() {
        AbstractClient abstractClient = new ActualClient(executor, null);
    }

    @Test(expected = NullPointerException.class)
    public void abstractClientExecutorAndModulesNullTest() {
        AbstractClient abstractClient = new ActualClient(null, null);
        Assert.assertNull("Expected null value.", abstractClient.executor);
        Assert.assertThat("Instance of TransportAsync expected", abstractClient.transport(), IsInstanceOf.instanceOf(TransportAsync.class));
    }

    @Test(expected = ClassNotFoundException.class)
    public void adaptModuleContextTest() throws ClassNotFoundException {
        Class<Integer> clazz = (Class<Integer>) Class.forName("className");
        Assert.assertEquals("Expected and actual values should be the same.", Optional.empty(), abstractClient.adaptModuleContext(clazz));
    }

    @Test
    public void adaptModuleContextClazzNullTest() {
        Assert.assertEquals("Expected and actual values should be the same.", Optional.empty(), abstractClient.adaptModuleContext(null));
    }

    @Test
    public void transportTest() {
        Assert.assertNotNull("Expected not null value.", abstractClient.transport());
        Assert.assertThat("Instance of TransportAsync expected", abstractClient.transport(), IsInstanceOf.instanceOf(TransportAsync.class));
    }

    @Test
    public void notifyAddApplicationTest() {
        abstractClient.notifyAddApplication(applicationId);
        Mockito.verify(module, Mockito.times(1)).applicationAdded(applicationId);
    }

    @Test
    public void notifyRemoveApplicationTest() {
        abstractClient.notifyRemoveApplication(applicationId);
        Mockito.verify(module, Mockito.times(1)).applicationRemoved(applicationId);
    }

    @Test
    public void buildApplicationTest() {
        Assert.assertThat("Instance of Application.Builder expected", abstractClient.buildApplication(applicationId), IsInstanceOf.instanceOf(Application.Builder.class));
    }

    @Test
    public void buildApplicationBuildTest() {
        Assert.assertThat("Instance of Application expected", abstractClient.buildApplication(applicationId).build(), IsInstanceOf.instanceOf(Application.class));
    }

    @Test
    public void internalBuildApplicationTest() {
        final Application.Builder builder = Mockito.mock(Application.Builder.class);
        Assert.assertThat("Instance of DefaultApplication expected", abstractClient.internalBuildApplication(builder, applicationId), IsInstanceOf.instanceOf(DefaultApplication.class));
    }

    @Test(expected = IllegalStateException.class)
    public void internalBuildApplicationExceptionPartTest() {
        final Application.Builder builder = Mockito.mock(Application.Builder.class);
        abstractClient.buildApplication(applicationId).build();
        Assert.assertThat("Instance of DefaultApplication expected", abstractClient.internalBuildApplication(builder, applicationId), IsInstanceOf.instanceOf(DefaultApplication.class));
    }

    @Test
    public void internalBuildApplicationBuilderNullTest() {
        Assert.assertThat("Instance of DefaultApplication expected", abstractClient.internalBuildApplication(null, applicationId), IsInstanceOf.instanceOf(DefaultApplication.class));
    }

    @Test(expected = NullPointerException.class)
    public void internalSubscribeContextNullTest() {
        List<String> segments = new ArrayList<>();
        segments.add("string");
        final Topic topic = Topic.of(segments);
        final MessageHandler messageHandler = Mockito.mock(MessageHandler.class);
        final ErrorHandler<Exception> errorHandler = Mockito.mock(ErrorHandler.class);
        Assert.assertThat("Instance of CompletionStage expected", abstractClient.internalSubscribe(null, applicationId, topic, messageHandler, errorHandler), IsInstanceOf.instanceOf(CompletionStage.class));
    }

    @Test(expected = NullPointerException.class)
    public void internalSubscribeContextAndTopicNullTest() {
        final MessageHandler messageHandler = Mockito.mock(MessageHandler.class);
        final ErrorHandler<Exception> errorHandler = Mockito.mock(ErrorHandler.class);
        Assert.assertThat("Instance of CompletionStage expected", abstractClient.internalSubscribe(null, applicationId, null, messageHandler, errorHandler), IsInstanceOf.instanceOf(CompletionStage.class));
    }

    @Test(expected = NullPointerException.class)
    public void internalSubscribeContextAndMessageHandlerNullTest() {
        List<String> segments = new ArrayList<>();
        segments.add("string");
        final Topic topic = Topic.of(segments);
        final ErrorHandler<Exception> errorHandler = Mockito.mock(ErrorHandler.class);
        Assert.assertThat("Instance of CompletionStage expected", abstractClient.internalSubscribe(null, applicationId, topic, null, errorHandler), IsInstanceOf.instanceOf(CompletionStage.class));
    }

    @Test(expected = NullPointerException.class)
    public void internalSubscribeContextAndErrorHandlerNullTest() {
        List<String> segments = new ArrayList<>();
        segments.add("string");
        final Topic topic = Topic.of(segments);
        final MessageHandler messageHandler = Mockito.mock(MessageHandler.class);
        Assert.assertThat("Instance of CompletionStage expected", abstractClient.internalSubscribe(null, applicationId, topic, messageHandler, null), IsInstanceOf.instanceOf(CompletionStage.class));
    }

    @Test
    public void internalPublishTest() {
        final AbstractClient.Context context = Mockito.mock(AbstractClient.Context.class);
        List<String> segments = new ArrayList<>();
        segments.add("string");
        final Topic topic = Topic.of(segments);
        final Payload payload = Mockito.mock(Payload.class);
        Assert.assertThat("Instance of CompletionStage expected", abstractClient.internalPublish(context, applicationId, topic, payload), IsInstanceOf.instanceOf(CompletionStage.class));
    }

    @Test
    public void internalPublishContextNullTest() {
        List<String> segments = new ArrayList<>();
        segments.add("string");
        final Topic topic = Topic.of(segments);
        final Payload payload = Mockito.mock(Payload.class);
        Assert.assertNull(abstractClient.internalPublish(null, applicationId, topic, payload));
    }

    @Test
    public void internalPublishTopicNullTest() {
        final AbstractClient.Context context = Mockito.mock(AbstractClient.Context.class);
        final Payload payload = Mockito.mock(Payload.class);
        Assert.assertThat("Instance of CompletionStage expected", abstractClient.internalPublish(context, applicationId, null, payload), IsInstanceOf.instanceOf(CompletionStage.class));
    }

    @Test
    public void internalPublishPayloadNullTest() {
        final AbstractClient.Context context = Mockito.mock(AbstractClient.Context.class);
        List<String> segments = new ArrayList<>();
        segments.add("string");
        final Topic topic = Topic.of(segments);
        Assert.assertThat("Instance of CompletionStage expected", abstractClient.internalPublish(context, applicationId, topic,null), IsInstanceOf.instanceOf(CompletionStage.class));
    }

    @Test
    public void createApplicationTest() {
        final Application.Builder builder = Mockito.mock(Application.Builder.class);
        final AbstractClient.Context context = Mockito.mock(AbstractClient.Context.class);
        DefaultApplication defaultApplicationExpected = new DefaultApplication(context);
        Assert.assertEquals("Expected and actual values should be the same.", defaultApplicationExpected.transport(), abstractClient.createApplication(builder, context).transport());
    }

    @Test
    public void createApplicationBuilderNullTest() {
        final AbstractClient.Context context = Mockito.mock(AbstractClient.Context.class);
        DefaultApplication defaultApplicationExpected = new DefaultApplication(context);
        Assert.assertEquals("Expected and actual values should be the same.", defaultApplicationExpected.transport(), abstractClient.createApplication(null, context).transport());
    }

    @Test(expected = NullPointerException.class)
    public void createApplicationContextNullTest() {
        final Application.Builder builder = Mockito.mock(Application.Builder.class);
        DefaultApplication defaultApplicationExpected = new DefaultApplication(null);
        Assert.assertEquals("Expected and actual values should be the same.", defaultApplicationExpected.transport(), abstractClient.createApplication(builder, null).transport());
    }
}