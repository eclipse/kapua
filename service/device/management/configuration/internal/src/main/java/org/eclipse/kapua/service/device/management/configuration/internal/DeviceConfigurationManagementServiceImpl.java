/*******************************************************************************
 * Copyright (c) 2016, 2022 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.service.device.management.configuration.internal;

import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.KapuaIllegalArgumentException;
import org.eclipse.kapua.commons.util.ArgumentValidator;
import org.eclipse.kapua.commons.util.xml.XmlUtil;
import org.eclipse.kapua.locator.KapuaProvider;
import org.eclipse.kapua.model.domain.Actions;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.device.management.DeviceManagementDomains;
import org.eclipse.kapua.service.device.management.commons.AbstractDeviceManagementServiceImpl;
import org.eclipse.kapua.service.device.management.commons.call.DeviceCallExecutor;
import org.eclipse.kapua.service.device.management.commons.setting.DeviceManagementSetting;
import org.eclipse.kapua.service.device.management.commons.setting.DeviceManagementSettingKey;
import org.eclipse.kapua.service.device.management.configuration.DeviceComponentConfiguration;
import org.eclipse.kapua.service.device.management.configuration.DeviceConfiguration;
import org.eclipse.kapua.service.device.management.configuration.DeviceConfigurationFactory;
import org.eclipse.kapua.service.device.management.configuration.DeviceConfigurationManagementService;
import org.eclipse.kapua.service.device.management.configuration.message.internal.ConfigurationRequestChannel;
import org.eclipse.kapua.service.device.management.configuration.message.internal.ConfigurationRequestMessage;
import org.eclipse.kapua.service.device.management.configuration.message.internal.ConfigurationRequestPayload;
import org.eclipse.kapua.service.device.management.configuration.message.internal.ConfigurationResponseMessage;
import org.eclipse.kapua.service.device.management.exception.DeviceManagementRequestContentException;
import org.eclipse.kapua.service.device.management.message.KapuaMethod;
import org.xml.sax.SAXException;

import javax.xml.bind.JAXBException;
import java.util.Date;

/**
 * {@link DeviceConfigurationManagementService} implementation.
 *
 * @since 1.0.0
 */
@KapuaProvider
public class DeviceConfigurationManagementServiceImpl extends AbstractDeviceManagementServiceImpl implements DeviceConfigurationManagementService {

    private static final String CHAR_ENCODING = DeviceManagementSetting.getInstance().getString(DeviceManagementSettingKey.CHAR_ENCODING);

    private static final DeviceConfigurationFactory DEVICE_CONFIGURATION_FACTORY = LOCATOR.getFactory(DeviceConfigurationFactory.class);

    private static final String SCOPE_ID = "scopeId";
    private static final String DEVICE_ID = "deviceId";

    @Override
    public DeviceConfiguration get(KapuaId scopeId, KapuaId deviceId, String configurationId, String configurationComponentPid, Long timeout)
            throws KapuaException {
        //
        // Argument Validation
        ArgumentValidator.notNull(scopeId, SCOPE_ID);
        ArgumentValidator.notNull(deviceId, DEVICE_ID);

        //
        // Check Access
        AUTHORIZATION_SERVICE.checkPermission(PERMISSION_FACTORY.newPermission(DeviceManagementDomains.DEVICE_MANAGEMENT_DOMAIN, Actions.read, scopeId));

        //
        // Prepare the request
        ConfigurationRequestChannel configurationRequestChannel = new ConfigurationRequestChannel();
        configurationRequestChannel.setAppName(DeviceConfigurationAppProperties.APP_NAME);
        configurationRequestChannel.setVersion(DeviceConfigurationAppProperties.APP_VERSION);
        configurationRequestChannel.setMethod(KapuaMethod.READ);
        configurationRequestChannel.setConfigurationId(configurationId);
        configurationRequestChannel.setComponentId(configurationComponentPid);

        ConfigurationRequestPayload configurationRequestPayload = new ConfigurationRequestPayload();

        ConfigurationRequestMessage configurationRequestMessage = new ConfigurationRequestMessage();
        configurationRequestMessage.setScopeId(scopeId);
        configurationRequestMessage.setDeviceId(deviceId);
        configurationRequestMessage.setCapturedOn(new Date());
        configurationRequestMessage.setPayload(configurationRequestPayload);
        configurationRequestMessage.setChannel(configurationRequestChannel);

        //
        // Do get
        DeviceCallExecutor<?, ?, ?, ConfigurationResponseMessage> deviceApplicationCall = new DeviceCallExecutor<>(configurationRequestMessage, timeout);
        ConfigurationResponseMessage responseMessage = deviceApplicationCall.send();

        //
        // Create event
        createDeviceEvent(scopeId, deviceId, configurationRequestMessage, responseMessage);

        //
        // Check response
        return checkResponseAcceptedOrThrowError(responseMessage, () -> responseMessage.getPayload().getDeviceConfigurations());
    }

    @Override
    public void put(KapuaId scopeId, KapuaId deviceId, DeviceComponentConfiguration deviceComponentConfiguration, Long timeout)
            throws KapuaException {
        //
        // Argument Validation
        ArgumentValidator.notNull(scopeId, SCOPE_ID);
        ArgumentValidator.notNull(deviceId, DEVICE_ID);
        ArgumentValidator.notNull(deviceComponentConfiguration, "componentConfiguration");
        ArgumentValidator.notEmptyOrNull(deviceComponentConfiguration.getId(), "componentConfiguration.componentId");

        //
        // Check Access
        AUTHORIZATION_SERVICE.checkPermission(PERMISSION_FACTORY.newPermission(DeviceManagementDomains.DEVICE_MANAGEMENT_DOMAIN, Actions.write, scopeId));

        //
        // Prepare the request
        ConfigurationRequestChannel configurationRequestChannel = new ConfigurationRequestChannel();
        configurationRequestChannel.setAppName(DeviceConfigurationAppProperties.APP_NAME);
        configurationRequestChannel.setVersion(DeviceConfigurationAppProperties.APP_VERSION);
        configurationRequestChannel.setMethod(KapuaMethod.WRITE);
        configurationRequestChannel.setComponentId(deviceComponentConfiguration.getId());

        ConfigurationRequestPayload configurationRequestPayload = new ConfigurationRequestPayload();

        DeviceConfiguration deviceConfiguration = DEVICE_CONFIGURATION_FACTORY.newConfigurationInstance();
        try {
            deviceConfiguration.getComponentConfigurations().add(deviceComponentConfiguration);

            configurationRequestPayload.setDeviceConfigurations(deviceConfiguration);
        } catch (Exception e) {
            throw new DeviceManagementRequestContentException(e, deviceConfiguration);
        }

        ConfigurationRequestMessage configurationRequestMessage = new ConfigurationRequestMessage();
        configurationRequestMessage.setScopeId(scopeId);
        configurationRequestMessage.setDeviceId(deviceId);
        configurationRequestMessage.setCapturedOn(new Date());
        configurationRequestMessage.setPayload(configurationRequestPayload);
        configurationRequestMessage.setChannel(configurationRequestChannel);

        //
        // Do put
        DeviceCallExecutor<?, ?, ?, ConfigurationResponseMessage> deviceApplicationCall = new DeviceCallExecutor<>(configurationRequestMessage, timeout);
        ConfigurationResponseMessage responseMessage = deviceApplicationCall.send();

        //
        // Create event
        createDeviceEvent(scopeId, deviceId, configurationRequestMessage, responseMessage);

        //
        // Check response
        checkResponseAcceptedOrThrowError(responseMessage);
    }

    @Override
    public void put(KapuaId scopeId, KapuaId deviceId, String xmlDeviceConfig, Long timeout)
            throws KapuaException {
        try {
            put(scopeId,
                    deviceId,
                    XmlUtil.unmarshal(xmlDeviceConfig, DeviceConfigurationImpl.class),
                    timeout);
        } catch (JAXBException | SAXException e) {
            throw new KapuaIllegalArgumentException("xmlDeviceConfig", xmlDeviceConfig);
        }
    }

    @Override
    public void put(KapuaId scopeId, KapuaId deviceId, DeviceConfiguration deviceConfiguration, Long timeout)
            throws KapuaException {
        //
        // Argument Validation
        ArgumentValidator.notNull(scopeId, SCOPE_ID);
        ArgumentValidator.notNull(deviceId, DEVICE_ID);
        ArgumentValidator.notNull(deviceConfiguration, "componentConfiguration");

        //
        // Check Access
        AUTHORIZATION_SERVICE.checkPermission(PERMISSION_FACTORY.newPermission(DeviceManagementDomains.DEVICE_MANAGEMENT_DOMAIN, Actions.write, scopeId));

        //
        // Prepare the request
        ConfigurationRequestChannel configurationRequestChannel = new ConfigurationRequestChannel();
        configurationRequestChannel.setAppName(DeviceConfigurationAppProperties.APP_NAME);
        configurationRequestChannel.setVersion(DeviceConfigurationAppProperties.APP_VERSION);
        configurationRequestChannel.setMethod(KapuaMethod.WRITE);

        ConfigurationRequestPayload configurationRequestPayload = new ConfigurationRequestPayload();

        try {
            configurationRequestPayload.setDeviceConfigurations(deviceConfiguration);
        } catch (Exception e) {
            throw new DeviceManagementRequestContentException(e, deviceConfiguration);
        }

        ConfigurationRequestMessage configurationRequestMessage = new ConfigurationRequestMessage();
        configurationRequestMessage.setScopeId(scopeId);
        configurationRequestMessage.setDeviceId(deviceId);
        configurationRequestMessage.setCapturedOn(new Date());
        configurationRequestMessage.setPayload(configurationRequestPayload);
        configurationRequestMessage.setChannel(configurationRequestChannel);

        //
        // Do put
        DeviceCallExecutor<?, ?, ?, ConfigurationResponseMessage> deviceApplicationCall = new DeviceCallExecutor<>(configurationRequestMessage, timeout);
        ConfigurationResponseMessage responseMessage = deviceApplicationCall.send();

        //
        // Create event
        createDeviceEvent(scopeId, deviceId, configurationRequestMessage, responseMessage);

        //
        // Check response
        checkResponseAcceptedOrThrowError(responseMessage);
    }
}
