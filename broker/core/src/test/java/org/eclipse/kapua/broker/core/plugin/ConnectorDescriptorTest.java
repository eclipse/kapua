/*******************************************************************************
 * Copyright (c) 2017 Red Hat Inc and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Red Hat Inc - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.broker.core.plugin;

import org.eclipse.kapua.broker.core.plugin.ConnectorDescriptor.MessageType;
import org.eclipse.kapua.broker.core.setting.BrokerPluginSetting;
import org.eclipse.kapua.broker.core.setting.BrokerPluginSettingKey;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class ConnectorDescriptorTest {

    @Before
    public void resetSettings() {
        BrokerPluginSetting.resetInstance();
    }

    /**
     * Use a default provider, configuring a non-empty configuration
     */
    @Test
    public void testDefault6() throws Exception {
        final Map<String, String> properties = new HashMap<>();
        properties.put(BrokerPluginSettingKey.DISABLE_DEFAULT_CONNECTOR_DESCRIPTOR.key(), "true");
        properties.put(BrokerPluginSettingKey.CONFIGURATION_URI.key(), "file:src/test/resources/conector.descriptor/2.properties");

        Tests.runWithProperties(properties, () -> {
            DefaultConnectorDescriptionProvider provider = new DefaultConnectorDescriptionProvider();
            Assert.assertNull(provider.getDescriptor("foo"));

            ConnectorDescriptor descriptor = provider.getDescriptor("mqtt");
            Assert.assertNotNull(descriptor);

            Assert.assertEquals(org.eclipse.kapua.service.device.call.message.kura.lifecycle.KuraAppsMessage.class, descriptor.getDeviceClass(MessageType.APP));
            Assert.assertEquals(org.eclipse.kapua.message.device.lifecycle.KapuaAppsMessage.class, descriptor.getKapuaClass(MessageType.APP));

            Assert.assertNull(descriptor.getDeviceClass(MessageType.DATA));
            Assert.assertNull(descriptor.getKapuaClass(MessageType.DATA));
        });
    }

}
