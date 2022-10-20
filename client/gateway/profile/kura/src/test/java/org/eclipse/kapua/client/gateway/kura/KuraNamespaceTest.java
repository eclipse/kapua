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

import org.eclipse.kapua.client.gateway.Topic;
import org.eclipse.kapua.client.gateway.kura.KuraNamespace.Builder;
import org.eclipse.kapua.qa.markers.junit.JUnitTests;
import org.hamcrest.core.IsInstanceOf;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;


@Category(JUnitTests.class)
public class KuraNamespaceTest {

    Builder builder;
    String accountName;
    String[] specialSymbols;
    String clientId;
    String applicationId;
    String path;
    Topic topic;
    KuraNamespace kuraNamespace;

    @Before
    public void initialize() {
        builder = new Builder();
        accountName = "Account Name";
        specialSymbols = new String[]{"", "#", "+", "/"};
        clientId = "Client Id";
        applicationId = "Application Id";
        path = "String to split";
        topic = Topic.split(path);
        kuraNamespace = new KuraNamespace.Builder().accountName(accountName).build();
    }

    @Test
    public void builderAccountNameStringParameterTest() {
        Assert.assertThat("Instance of Builder expected.", builder.accountName(accountName), IsInstanceOf.instanceOf(Builder.class));
    }

    @Test(expected = NullPointerException.class)
    public void builderAccountNameNullStringTest() {
        builder.accountName(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void builderAccountNameSpecialSymbolsTest() {
        for (String name : specialSymbols) {
            builder.accountName(name);
        }
    }

    @Test
    public void builderAccountNameTest() {
        Assert.assertEquals("Expected and actual values should be the same.", accountName, builder.accountName(accountName).accountName());
    }

    @Test
    public void builderBuildTest() {
        Assert.assertThat("Instance of KuraNamespace expected.", kuraNamespace, IsInstanceOf.instanceOf(KuraNamespace.class));
    }

    @Test(expected = IllegalArgumentException.class)
    public void builderBuildNullAccountNameTest() {
        builder.build();
    }

    @Test(expected = IllegalArgumentException.class)
    public void builderBuildEmptyAccountNameTest() {
        builder.accountName("").build();
    }

    @Test(expected = IllegalArgumentException.class)
    public void dataTopicNullClientIdTest() {
        kuraNamespace.dataTopic(null, applicationId, topic);
    }

    @Test(expected = IllegalArgumentException.class)
    public void dataTopicNullApplicationIdTest() {
        kuraNamespace.dataTopic(clientId, null, topic);
    }

    @Test(expected = NullPointerException.class)
    public void dataTopicNullTopicTest() {
        kuraNamespace.dataTopic(clientId, applicationId, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void dataTopicSpecialSymbolsInClientIdTest() {
        for (String idClient : specialSymbols) {
            kuraNamespace.dataTopic(idClient, applicationId, topic);
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void dataTopicSpecialSymbolsInApplicationIdTest() {
        for (String idApplication : specialSymbols) {
            kuraNamespace.dataTopic(clientId, idApplication, topic);
        }
    }

    @Test
    public void dataTopicTest() {
        String expectedString = accountName + "/" + clientId + "/" + applicationId + "/" + path;

        Assert.assertEquals("Expected and actual values should be the same.", expectedString, kuraNamespace.dataTopic(clientId, applicationId, topic));
    }
}
