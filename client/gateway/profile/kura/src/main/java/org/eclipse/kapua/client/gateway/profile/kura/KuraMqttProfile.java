/*******************************************************************************
 * Copyright (c) 2017 Red Hat Inc and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Red Hat Inc - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.client.gateway.profile.kura;

import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Supplier;

import org.eclipse.kapua.client.gateway.Client;
import org.eclipse.kapua.client.gateway.Credentials.UserAndPassword;
import org.eclipse.kapua.client.gateway.kura.KuraBinaryPayloadCodec;
import org.eclipse.kapua.client.gateway.kura.KuraBirthCertificateModule;
import org.eclipse.kapua.client.gateway.kura.KuraNamespace;
import org.eclipse.kapua.client.gateway.mqtt.AbstractMqttChannel;
import org.eclipse.kapua.client.gateway.spi.DefaultClient;

public class KuraMqttProfile<B extends AbstractMqttChannel.Builder<B>> {

    public static <B extends AbstractMqttChannel.Builder<B>> KuraMqttProfile<B> newProfile(final Supplier<B> builderSupplier) {
        Objects.requireNonNull(builderSupplier);
        return new KuraMqttProfile<>(builderSupplier);
    }

    private final Supplier<B> builderSupplier;
    private String accountName;
    private String brokerUrl;
    private String clientId;
    private UserAndPassword userAndPassword;
    private Consumer<B> customizer;

    private KuraMqttProfile(final Supplier<B> builderSupplier) {
        this.builderSupplier = builderSupplier;
    }

    public KuraMqttProfile<B> accountName(final String accountName) {
        this.accountName = accountName;
        return this;
    }

    public KuraMqttProfile<B> brokerUrl(final String brokerUrl) {
        this.brokerUrl = brokerUrl;
        return this;
    }

    public KuraMqttProfile<B> customizer(final Consumer<B> customizer) {
        this.customizer = customizer;
        return this;
    }

    public KuraMqttProfile<B> clientId(final String clientId) {
        this.clientId = clientId;
        return this;
    }

    public KuraMqttProfile<B> credentials(final UserAndPassword userAndPassword) {
        this.userAndPassword = userAndPassword;
        return this;
    }

    public Client build() throws Exception {
        validate();

        final B builder = builderSupplier.get()
                .clientId(this.clientId)
                .broker(this.brokerUrl)
                .credentials(this.userAndPassword)
                .codec(new KuraBinaryPayloadCodec.Builder()
                        .build())
                .namespace(
                        new KuraNamespace.Builder()
                                .accountName(this.accountName)
                                .build());

        if (customizer != null) {
            customizer.accept(builder);
        }

        final DefaultClient.Builder client = new DefaultClient.Builder(builder.build())
                .module(
                        KuraBirthCertificateModule.newBuilder(this.accountName)
                                .defaultProviders()
                                .build());

        return client.build();
    }

    private void validate() {
        hasString("accountName", this.accountName);
        hasString("brokerUrl", this.brokerUrl);
        hasString("clientId", this.clientId);
    }

    private static void hasString(final String name, final String value) {
        if (value == null || value.isEmpty()) {
            throw new IllegalStateException(String.format("'%s' must be set", name));
        }
    }
}
