/*******************************************************************************
 * Copyright (c) 2020, 2022 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.client.gateway.kura;

import org.eclipse.kapua.client.gateway.mqtt.MqttModuleContext;
import org.eclipse.kapua.client.gateway.spi.ModuleContext;
import org.eclipse.kapua.qa.markers.junit.JUnitTests;
import org.hamcrest.core.IsInstanceOf;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.mockito.Mockito;

import java.util.Map;
import java.util.Set;
import java.util.Collection;
import java.util.HashSet;
import java.util.HashMap;
import java.util.Optional;


@Category(JUnitTests.class)
public class KuraBirthCertificateModuleTest {

    Map<String, String> values;
    Map<String, String> expectedValues;
    KuraBirthCertificateModule.Builder builder;
    Set<KuraBirthCertificateModule.Provider> expectedProviders;

    @Before
    public void initialize() {
        values = new HashMap<>();
        expectedValues = new HashMap<>();
        builder = KuraBirthCertificateModule.newBuilder("Account Name");
        expectedProviders = new HashSet<>();
    }

    @Test
    public void providerProvideDataJVMTest() {
        expectedValues.put("jvm_name", System.getProperty("java.vm.name"));
        expectedValues.put("jvm_version", System.getProperty("java.version"));
        expectedValues.put("os", System.getProperty("os.name"));
        expectedValues.put("os_version", System.getProperty("os.version"));
        expectedValues.put("os_arch", System.getProperty("os.arch"));

        KuraBirthCertificateModule.Provider.JVM.provideData(values);

        Assert.assertEquals("Expected and actual values should be the same.", expectedValues, values);
    }

    @Test(expected = NullPointerException.class)
    public void providerProvideDataJVMNullValuesTest() {
        KuraBirthCertificateModule.Provider.JVM.provideData(null);
    }

    @Test
    public void provideDataProviderRUNTIMETest() {
        expectedValues.put("available_processors", Integer.toString(Runtime.getRuntime().availableProcessors()));
        expectedValues.put("total_memory", Long.toString(Runtime.getRuntime().totalMemory()));

        KuraBirthCertificateModule.Provider.RUNTIME.provideData(values);

        Assert.assertEquals("Expected and actual values should be the same.", expectedValues, values);
    }

    @Test(expected = NullPointerException.class)
    public void providerProvideDataRUNTIMENullValuesTest() {
        KuraBirthCertificateModule.Provider.RUNTIME.provideData(null);
    }

    @Test
    public void builderDefaultProvidersTest() {
        expectedProviders.add(KuraBirthCertificateModule.Provider.JVM);
        expectedProviders.add(KuraBirthCertificateModule.Provider.RUNTIME);

        builder.defaultProviders();

        Assert.assertEquals("Expected and actual values should be the same.", expectedProviders, builder.providers());
    }

    @Test(expected = NullPointerException.class)
    public void builderProviderNullParameterTest() {
        builder.provider(null);
    }

    @Test
    public void builderProviderTest() {
        KuraBirthCertificateModule.Provider provider = Mockito.mock(KuraBirthCertificateModule.Provider.class);
        expectedProviders.add(provider);

        builder.provider(provider);

        Assert.assertEquals("Expected and actual values should be the same.", expectedProviders, builder.providers());
    }

    @Test(expected = NullPointerException.class)
    public void builderProvidersNullParameterTest() {
        builder.providers(null);
    }

    @Test
    public void builderProvidersTest() {
        KuraBirthCertificateModule.Provider provider1 = Mockito.mock(KuraBirthCertificateModule.Provider.class);
        KuraBirthCertificateModule.Provider provider2 = Mockito.mock(KuraBirthCertificateModule.Provider.class);
        KuraBirthCertificateModule.Provider provider3 = Mockito.mock(KuraBirthCertificateModule.Provider.class);

        Collection<KuraBirthCertificateModule.Provider> providers = new HashSet<>();
        providers.add(provider1);
        providers.add(provider2);
        providers.add(provider3);
        expectedProviders.add(provider1);
        expectedProviders.add(provider2);
        expectedProviders.add(provider3);

        builder.providers(providers);

        Assert.assertEquals("Expected and actual values should be the same.", expectedProviders, builder.providers());
    }

    @Test
    public void builderBuildTest() {
        Assert.assertThat("Instance of KuraBirthCertificateModule expected.", builder.build(), IsInstanceOf.instanceOf(KuraBirthCertificateModule.class));
    }

    @Test
    public void newBuilderNullTest() {
        Assert.assertThat("Instance of Builder expected.", KuraBirthCertificateModule.newBuilder(null), IsInstanceOf.instanceOf(KuraBirthCertificateModule.Builder.class));
    }

    @Test
    public void initializeTest() {
        ModuleContext moduleContext = Mockito.mock(ModuleContext.class);
        Optional<MqttModuleContext> optional = Optional.of(Mockito.mock(MqttModuleContext.class));
        Mockito.when(moduleContext.adapt(MqttModuleContext.class)).thenReturn(optional);
        builder.defaultProviders();
        try {
            builder.build().initialize(moduleContext);
        } catch (Exception e) {
            Assert.fail("Exception not expected.");
        }
    }

    @Test(expected = NullPointerException.class)
    public void initializeNullTest() {
        builder.build().initialize(null);
    }

    @Test(expected = IllegalStateException.class)
    public void initializeIllegalStateExceptionTest() {
        ModuleContext moduleContext = Mockito.mock(ModuleContext.class);
        Mockito.when(moduleContext.adapt(MqttModuleContext.class)).thenReturn(Optional.empty());
        builder.build().initialize(moduleContext);
    }
}
