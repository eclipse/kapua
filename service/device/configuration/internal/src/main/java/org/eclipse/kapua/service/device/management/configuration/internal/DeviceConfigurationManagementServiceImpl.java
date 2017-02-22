/*******************************************************************************
 * Copyright (c) 2011, 2016 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *
 *******************************************************************************/
package org.eclipse.kapua.service.device.management.configuration.internal;

import java.io.StringWriter;
import java.util.Date;

import javax.xml.bind.JAXBException;
import javax.xml.stream.FactoryConfigurationError;
import javax.xml.stream.XMLStreamException;

import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.KapuaIllegalArgumentException;
import org.eclipse.kapua.commons.util.ArgumentValidator;
import org.eclipse.kapua.commons.util.xml.XmlUtil;
import org.eclipse.kapua.locator.KapuaLocator;
import org.eclipse.kapua.locator.KapuaProvider;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.authorization.AuthorizationService;
import org.eclipse.kapua.service.authorization.domain.Domain;
import org.eclipse.kapua.service.authorization.permission.Actions;
import org.eclipse.kapua.service.authorization.permission.PermissionFactory;
import org.eclipse.kapua.service.device.management.KapuaMethod;
import org.eclipse.kapua.service.device.management.commons.DeviceManagementDomain;
import org.eclipse.kapua.service.device.management.commons.call.DeviceCallExecutor;
import org.eclipse.kapua.service.device.management.commons.exception.DeviceManagementErrorCodes;
import org.eclipse.kapua.service.device.management.commons.exception.DeviceManagementException;
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
import org.eclipse.kapua.service.device.management.configuration.message.internal.ConfigurationResponsePayload;
import org.eclipse.kapua.service.device.registry.event.DeviceEventCreator;
import org.eclipse.kapua.service.device.registry.event.DeviceEventFactory;
import org.eclipse.kapua.service.device.registry.event.DeviceEventService;
import org.xml.sax.SAXException;

/**
 * Device configuration service implementation.
 *
 * @since 1.0
 */
@KapuaProvider
public class DeviceConfigurationManagementServiceImpl implements DeviceConfigurationManagementService {

    private static final Domain deviceManagementDomain = new DeviceManagementDomain();

    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Override
    public DeviceConfiguration get(KapuaId scopeId, KapuaId deviceId, String configurationId, String configurationComponentPid, Long timeout)
            throws KapuaException {
        //
        // Argument Validation
        ArgumentValidator.notNull(scopeId, "scopeId");
        ArgumentValidator.notNull(deviceId, "deviceId");

        //
        // Check Access
        KapuaLocator locator = KapuaLocator.getInstance();
        AuthorizationService authorizationService = locator.getService(AuthorizationService.class);
        PermissionFactory permissionFactory = locator.getFactory(PermissionFactory.class);
        authorizationService.checkPermission(permissionFactory.newPermission(deviceManagementDomain, Actions.read, scopeId));

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
        DeviceCallExecutor deviceApplicationCall = new DeviceCallExecutor(configurationRequestMessage, timeout);
        ConfigurationResponseMessage responseMessage = (ConfigurationResponseMessage) deviceApplicationCall.send();

        //
        // Parse the response
        ConfigurationResponsePayload responsePayload = responseMessage.getPayload();

        DeviceManagementSetting config = DeviceManagementSetting.getInstance();
        String charEncoding = config.getString(DeviceManagementSettingKey.CHAR_ENCODING);

        DeviceConfiguration deviceConfiguration = null;
        if (responsePayload.getBody() != null) {
            String body = null;
            try {
                body = new String(responsePayload.getBody(), charEncoding);
            } catch (Exception e) {
                throw new DeviceManagementException(DeviceManagementErrorCodes.RESPONSE_PARSE_EXCEPTION, e, responsePayload.getBody());

            }

            try {
                deviceConfiguration = XmlUtil.unmarshal(body, DeviceConfigurationImpl.class);
            } catch (Exception e) {
                throw new DeviceManagementException(DeviceManagementErrorCodes.RESPONSE_PARSE_EXCEPTION, e, body);

            }
        }

        //
        // Create event
        DeviceEventService deviceEventService = locator.getService(DeviceEventService.class);
        DeviceEventFactory deviceEventFactory = locator.getFactory(DeviceEventFactory.class);

        DeviceEventCreator deviceEventCreator = deviceEventFactory.newCreator(scopeId, deviceId, responseMessage.getReceivedOn(), DeviceConfigurationAppProperties.APP_NAME.getValue());
        deviceEventCreator.setPosition(responseMessage.getPosition());
        deviceEventCreator.setSentOn(responseMessage.getSentOn());
        deviceEventCreator.setAction(KapuaMethod.READ);
        deviceEventCreator.setResponseCode(responseMessage.getResponseCode());
        deviceEventCreator.setEventMessage(responseMessage.getPayload().toDisplayString());

        deviceEventService.create(deviceEventCreator);

        return deviceConfiguration;
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Override
    public void put(KapuaId scopeId, KapuaId deviceId, DeviceComponentConfiguration deviceComponentConfiguration, Long timeout)
            throws KapuaException {
        //
        // Argument Validation
        ArgumentValidator.notNull(scopeId, "scopeId");
        ArgumentValidator.notNull(deviceId, "deviceId");
        ArgumentValidator.notNull(deviceComponentConfiguration, "componentConfiguration");
        ArgumentValidator.notEmptyOrNull(deviceComponentConfiguration.getId(), "componentConfiguration.componentId");

        //
        // Check Access
        KapuaLocator locator = KapuaLocator.getInstance();
        AuthorizationService authorizationService = locator.getService(AuthorizationService.class);
        PermissionFactory permissionFactory = locator.getFactory(PermissionFactory.class);
        authorizationService.checkPermission(permissionFactory.newPermission(deviceManagementDomain, Actions.write, scopeId));

        //
        // Prepare the request
        ConfigurationRequestChannel configurationRequestChannel = new ConfigurationRequestChannel();
        configurationRequestChannel.setAppName(DeviceConfigurationAppProperties.APP_NAME);
        configurationRequestChannel.setVersion(DeviceConfigurationAppProperties.APP_VERSION);
        configurationRequestChannel.setMethod(KapuaMethod.WRITE);
        configurationRequestChannel.setComponentId(deviceComponentConfiguration.getId());

        ConfigurationRequestPayload configurationRequestPayload = new ConfigurationRequestPayload();

        try {
            DeviceConfigurationFactory deviceConfigurationFactory = locator.getFactory(DeviceConfigurationFactory.class);
            DeviceConfiguration deviceConfiguration = deviceConfigurationFactory.newConfigurationInstance();
            deviceConfiguration.getComponentConfigurations().add(deviceComponentConfiguration);

            DeviceManagementSetting deviceManagementConfig = DeviceManagementSetting.getInstance();
            String charEncoding = deviceManagementConfig.getString(DeviceManagementSettingKey.CHAR_ENCODING);

            StringWriter sw = new StringWriter();
            XmlUtil.marshal(deviceConfiguration, sw);
            byte[] requestBody = sw.toString().getBytes(charEncoding);

            configurationRequestPayload.setBody(requestBody);
        } catch (Exception e) {
            throw new DeviceManagementException(DeviceManagementErrorCodes.REQUEST_EXCEPTION, e, deviceComponentConfiguration);
        }

        ConfigurationRequestMessage configurationRequestMessage = new ConfigurationRequestMessage();
        configurationRequestMessage.setScopeId(scopeId);
        configurationRequestMessage.setDeviceId(deviceId);
        configurationRequestMessage.setCapturedOn(new Date());
        configurationRequestMessage.setPayload(configurationRequestPayload);
        configurationRequestMessage.setChannel(configurationRequestChannel);

        //
        // Do put
        DeviceCallExecutor deviceApplicationCall = new DeviceCallExecutor(configurationRequestMessage, timeout);
        ConfigurationResponseMessage responseMessage = (ConfigurationResponseMessage) deviceApplicationCall.send();

        //
        // Create event
        DeviceEventService deviceEventService = locator.getService(DeviceEventService.class);
        DeviceEventFactory deviceEventFactory = locator.getFactory(DeviceEventFactory.class);

        DeviceEventCreator deviceEventCreator = deviceEventFactory.newCreator(scopeId, deviceId, responseMessage.getReceivedOn(), DeviceConfigurationAppProperties.APP_NAME.getValue());
        deviceEventCreator.setPosition(responseMessage.getPosition());
        deviceEventCreator.setSentOn(responseMessage.getSentOn());
        deviceEventCreator.setAction(KapuaMethod.WRITE);
        deviceEventCreator.setResponseCode(responseMessage.getResponseCode());
        deviceEventCreator.setEventMessage(responseMessage.getPayload().toDisplayString());

        deviceEventService.create(deviceEventCreator);

    }

    @Override
    public void put(KapuaId scopeId, KapuaId deviceId, String xmlDeviceConfig, Long timeout)
            throws KapuaException {
        try {
            put(scopeId,
                    deviceId,
                    XmlUtil.unmarshal(xmlDeviceConfig, DeviceConfigurationImpl.class),
                    timeout);
        } catch (JAXBException | XMLStreamException | FactoryConfigurationError | SAXException e) {
            // FIXME: rethrow or log this exception
            throw new KapuaIllegalArgumentException(xmlDeviceConfig, xmlDeviceConfig);
        }
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Override
    public void put(KapuaId scopeId, KapuaId deviceId, DeviceConfiguration deviceConfiguration, Long timeout)
            throws KapuaException {
        //
        // Argument Validation
        ArgumentValidator.notNull(scopeId, "scopeId");
        ArgumentValidator.notNull(deviceId, "deviceId");
        ArgumentValidator.notNull(deviceConfiguration, "componentConfiguration");

        //
        // Check Access
        KapuaLocator locator = KapuaLocator.getInstance();
        AuthorizationService authorizationService = locator.getService(AuthorizationService.class);
        PermissionFactory permissionFactory = locator.getFactory(PermissionFactory.class);
        authorizationService.checkPermission(permissionFactory.newPermission(deviceManagementDomain, Actions.write, scopeId));

        //
        // Prepare the request
        ConfigurationRequestChannel configurationRequestChannel = new ConfigurationRequestChannel();
        configurationRequestChannel.setAppName(DeviceConfigurationAppProperties.APP_NAME);
        configurationRequestChannel.setVersion(DeviceConfigurationAppProperties.APP_VERSION);
        configurationRequestChannel.setMethod(KapuaMethod.WRITE);

        ConfigurationRequestPayload configurationRequestPayload = new ConfigurationRequestPayload();

        try {
            DeviceManagementSetting deviceManagementConfig = DeviceManagementSetting.getInstance();
            String charEncoding = deviceManagementConfig.getString(DeviceManagementSettingKey.CHAR_ENCODING);

            StringWriter sw = new StringWriter();
            XmlUtil.marshal(deviceConfiguration, sw);
            byte[] requestBody = sw.toString().getBytes(charEncoding);

            configurationRequestPayload.setBody(requestBody);
        } catch (Exception e) {
            throw new DeviceManagementException(DeviceManagementErrorCodes.REQUEST_EXCEPTION, e, deviceConfiguration);
        }

        ConfigurationRequestMessage configurationRequestMessage = new ConfigurationRequestMessage();
        configurationRequestMessage.setScopeId(scopeId);
        configurationRequestMessage.setDeviceId(deviceId);
        configurationRequestMessage.setCapturedOn(new Date());
        configurationRequestMessage.setPayload(configurationRequestPayload);
        configurationRequestMessage.setChannel(configurationRequestChannel);

        //
        // Do put
        DeviceCallExecutor deviceApplicationCall = new DeviceCallExecutor(configurationRequestMessage, timeout);
        ConfigurationResponseMessage responseMessage = (ConfigurationResponseMessage) deviceApplicationCall.send();

        //
        // Create event
        DeviceEventService deviceEventService = locator.getService(DeviceEventService.class);
        DeviceEventFactory deviceEventFactory = locator.getFactory(DeviceEventFactory.class);

        DeviceEventCreator deviceEventCreator = deviceEventFactory.newCreator(scopeId, deviceId, responseMessage.getReceivedOn(), DeviceConfigurationAppProperties.APP_NAME.getValue());
        deviceEventCreator.setPosition(responseMessage.getPosition());
        deviceEventCreator.setSentOn(responseMessage.getSentOn());
        deviceEventCreator.setAction(KapuaMethod.WRITE);
        deviceEventCreator.setResponseCode(responseMessage.getResponseCode());
        deviceEventCreator.setEventMessage(responseMessage.getPayload().toDisplayString());

        deviceEventService.create(deviceEventCreator);
    }
}
