/*******************************************************************************
 * Copyright (c) 2026 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.service.elasticsearch.client.rest.lowlevel;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.google.inject.TypeLiteral;
import com.google.inject.name.Named;
import com.google.inject.name.Names;
import org.assertj.core.api.Assertions;
import org.eclipse.kapua.qa.markers.junit.JUnitTests;
import org.junit.Test;
import org.junit.experimental.categories.Category;

/**
 * Verifies the DI wiring that lets {@code DatastoreModule} (and, symmetrically, a downstream project's own module) pick a {@link DeviceStoreClientBuilder}
 * out of a Guice-multibound {@link Set}, by {@link DeviceStoreClientBuilder#getId()} - instead of hardcoding the concrete vendor class.
 * <p>
 * The one non-obvious property under test: two independent "flows" (e.g. Message Store vs. Log Store), each resolving the <em>same</em> engine id from the
 * <em>same</em> shared {@link Set}, must each get their <em>own</em> {@link DeviceStoreClientBuilder} instance - never the same mutable instance - because
 * {@link DeviceStoreClientBuilder#initializeAndSetHosts} mutates state on it. That only holds because {@link ElasticsearchDeviceStoreClientBuilderModule} and
 * {@link OpensearchDeviceStoreClientBuilderModule} deliberately do NOT scope their {@code @ProvidesIntoSet} methods as {@code @Singleton}.
 */
@Category(JUnitTests.class)
public class DeviceStoreClientBuilderSetBindingTest {

    private static final Key<Set<DeviceStoreClientBuilder>> DEVICE_STORE_CLIENT_BUILDER_SET_KEY = Key.get(new TypeLiteral<Set<DeviceStoreClientBuilder>>() {
    });

    @Test
    public void bothVendorsAreContributedToTheSharedSetWithTheExpectedIds() {
        Injector injector = Guice.createInjector(new ElasticsearchDeviceStoreClientBuilderModule(), new OpensearchDeviceStoreClientBuilderModule());

        Set<DeviceStoreClientBuilder> available = injector.getInstance(DEVICE_STORE_CLIENT_BUILDER_SET_KEY);

        Assertions.assertThat(available).hasSize(2);
        Assertions.assertThat(available)
                .extracting(DeviceStoreClientBuilder::getId)
                .containsExactlyInAnyOrder(ElasticsearchDeviceStoreClientBuilder.ID, OpensearchDeviceStoreClientBuilder.ID);
    }

    @Test
    public void locateResolvesTheMatchingVendorCaseInsensitively() {
        DeviceStoreClientBuilder elasticsearch = new ElasticsearchDeviceStoreClientBuilder();
        DeviceStoreClientBuilder opensearch = new OpensearchDeviceStoreClientBuilder();
        Set<DeviceStoreClientBuilder> candidates = new HashSet<>(Arrays.asList(elasticsearch, opensearch));

        DeviceStoreClientBuilder picked = new DeviceStoreClientBuilderLocator().locate("OpenSearch", candidates);

        Assertions.assertThat(picked).isSameAs(opensearch);
    }

    @Test
    public void locateThrowsWhenNoContributedBuilderMatches() {
        Assertions.assertThatThrownBy(() -> new DeviceStoreClientBuilderLocator().locate("solr", Collections.emptySet()))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    public void twoIndependentFlowsPickingTheSameEngineGetTheirOwnInstance() {
        // Simulates DatastoreModule and DeviceLogstoreModule: two independent modules, each with their own "resolvedBuilder" @Provides @Singleton method,
        // both reading the SAME shared multibound Set, both configured for the same engine id.
        Injector injector = Guice.createInjector(
                new ElasticsearchDeviceStoreClientBuilderModule(),
                new OpensearchDeviceStoreClientBuilderModule(),
                new AbstractModule() {
                    @Provides
                    @Singleton
                    @Named("messageStore")
                    DeviceStoreClientBuilder messageStoreBuilder(Set<DeviceStoreClientBuilder> availableDeviceStoreClientBuilders, DeviceStoreClientBuilderLocator locator) {
                        return locator.locate(ElasticsearchDeviceStoreClientBuilder.ID, availableDeviceStoreClientBuilders);
                    }

                    @Provides
                    @Singleton
                    @Named("logStore")
                    DeviceStoreClientBuilder logStoreBuilder(Set<DeviceStoreClientBuilder> availableDeviceStoreClientBuilders, DeviceStoreClientBuilderLocator locator) {
                        return locator.locate(ElasticsearchDeviceStoreClientBuilder.ID, availableDeviceStoreClientBuilders);
                    }
                });

        DeviceStoreClientBuilder messageStoreBuilder = injector.getInstance(Key.get(DeviceStoreClientBuilder.class, Names.named("messageStore")));
        DeviceStoreClientBuilder logStoreBuilder = injector.getInstance(Key.get(DeviceStoreClientBuilder.class, Names.named("logStore")));

        // Both flows chose "elasticsearch" ...
        Assertions.assertThat(messageStoreBuilder).isInstanceOf(ElasticsearchDeviceStoreClientBuilder.class);
        Assertions.assertThat(logStoreBuilder).isInstanceOf(ElasticsearchDeviceStoreClientBuilder.class);
        // ... but each flow must hold its OWN mutable instance, not share one.
        Assertions.assertThat(messageStoreBuilder).isNotSameAs(logStoreBuilder);

        // Within a single flow, the @Singleton on the resolving method still means repeated lookups return the same cached instance.
        DeviceStoreClientBuilder messageStoreBuilderAgain = injector.getInstance(Key.get(DeviceStoreClientBuilder.class, Names.named("messageStore")));
        Assertions.assertThat(messageStoreBuilderAgain).isSameAs(messageStoreBuilder);
    }
}
