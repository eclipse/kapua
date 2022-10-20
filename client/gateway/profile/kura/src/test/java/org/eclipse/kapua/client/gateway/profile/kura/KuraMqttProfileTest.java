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
package org.eclipse.kapua.client.gateway.profile.kura;

import org.eclipse.kapua.client.gateway.Client;
import org.eclipse.kapua.client.gateway.Credentials;
import org.eclipse.kapua.client.gateway.mqtt.fuse.FuseChannel;
import org.eclipse.kapua.qa.markers.junit.JUnitTests;
import org.hamcrest.core.IsInstanceOf;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.mockito.Mockito;

import java.util.function.Consumer;
import java.util.function.Supplier;


@Category(JUnitTests.class)
public class KuraMqttProfileTest {

    Supplier supplier;
    KuraMqttProfile kuraMqttProfile;
    String accountName;
    String clientId;
    String brokerUrl;
    Consumer consumer;
    String username;
    String password;

    @Before
    public void initialize() {
        supplier = FuseChannel.Builder::new;
        kuraMqttProfile = KuraMqttProfile.newProfile(supplier);
        accountName = "kapua-sys";
        clientId = "clientId-1";
        brokerUrl = "tcp://localhost:1883";
        consumer = Mockito.mock(Consumer.class);
        username = "kapua-broker";
        password = "kapua-password";
    }

    @Test(expected = NullPointerException.class)
    public void newProfileNullTest() {
        KuraMqttProfile.newProfile(null);
    }

    @Test
    public void newProfileTest() {
        Assert.assertThat("Instance of KuraMqttProfile expected.", kuraMqttProfile, IsInstanceOf.instanceOf(KuraMqttProfile.class));
    }

    @Test
    public void accountNameTest() {
        Assert.assertThat("Instance of KuraMqttProfile expected.", kuraMqttProfile.accountName(accountName), IsInstanceOf.instanceOf(KuraMqttProfile.class));
    }

    @Test
    public void accountNameNullTest() {
        Assert.assertThat("Instance of KuraMqttProfile expected.", kuraMqttProfile.accountName(null), IsInstanceOf.instanceOf(KuraMqttProfile.class));
    }

    @Test
    public void brokerUrlTest() {
        Assert.assertThat("Instance of KuraMqttProfile expected.", kuraMqttProfile.brokerUrl(brokerUrl), IsInstanceOf.instanceOf(KuraMqttProfile.class));
    }

    @Test
    public void brokerUrlNullTest() {
        Assert.assertThat("Instance of KuraMqttProfile expected.", kuraMqttProfile.brokerUrl(null), IsInstanceOf.instanceOf(KuraMqttProfile.class));
    }

    @Test
    public void customizerTest() {
        kuraMqttProfile.customizer(consumer);
    }

    @Test
    public void customizerNullTest() {
        kuraMqttProfile.customizer(null);
    }

    @Test
    public void clientIdTest() {
        Assert.assertThat("Instance of KuraMqttProfile expected.", kuraMqttProfile.clientId(clientId), IsInstanceOf.instanceOf(KuraMqttProfile.class));
    }

    @Test
    public void clientIdNullTest() {
        Assert.assertThat("Instance of KuraMqttProfile expected.", kuraMqttProfile.clientId(null), IsInstanceOf.instanceOf(KuraMqttProfile.class));
    }

    @Test
    public void credentialsTest() {
        Credentials.UserAndPassword userAndPassword = Credentials.userAndPassword(username, password);
        Assert.assertThat("Instance of KuraMqttProfile expected.", kuraMqttProfile.credentials(userAndPassword), IsInstanceOf.instanceOf(KuraMqttProfile.class));
    }

    @Test
    public void credentialsNullTest() {
        Assert.assertThat("Instance of KuraMqttProfile expected.", kuraMqttProfile.credentials(null), IsInstanceOf.instanceOf(KuraMqttProfile.class));
    }


    @Test
    public void buildNullCustomizerTest() throws Exception {
        Assert.assertThat("Instance of Client expected.", kuraMqttProfile.accountName(accountName)
                .brokerUrl(brokerUrl)
                .clientId(clientId)
                .credentials(Credentials.userAndPassword(username, password))
                .build(), IsInstanceOf.instanceOf(Client.class));
    }

    @Test
    public void buildTest() throws Exception {
        Assert.assertThat("Instance of Client expected.", kuraMqttProfile.accountName(accountName)
                .brokerUrl(brokerUrl)
                .clientId(clientId)
                .credentials(Credentials.userAndPassword(username, password))
                .customizer(consumer)
                .build(), IsInstanceOf.instanceOf(Client.class));
    }

    @Test(expected = IllegalStateException.class)
    public void buildIllegalStateExceptionNullParameterTest() throws Exception {
        kuraMqttProfile.accountName(null)
                .brokerUrl(brokerUrl)
                .clientId(clientId)
                .credentials(Credentials.userAndPassword(username, password))
                .build();
    }

    @Test(expected = IllegalStateException.class)
    public void buildIllegalStateExceptionEmptyParameterTest() throws Exception {
        kuraMqttProfile.accountName("")
                .brokerUrl(brokerUrl)
                .clientId(clientId)
                .credentials(Credentials.userAndPassword(username, password))
                .build();
    }
}
