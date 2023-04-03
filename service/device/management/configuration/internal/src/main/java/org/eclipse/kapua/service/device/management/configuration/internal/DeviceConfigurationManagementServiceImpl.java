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

import com.google.common.base.Strings;
import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.KapuaIllegalArgumentException;
import org.eclipse.kapua.commons.util.ArgumentValidator;
import org.eclipse.kapua.commons.util.xml.XmlUtil;
import org.eclipse.kapua.model.domain.Actions;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.authorization.AuthorizationService;
import org.eclipse.kapua.service.authorization.permission.PermissionFactory;
import org.eclipse.kapua.service.device.management.DeviceManagementDomains;
import org.eclipse.kapua.service.device.management.commons.AbstractDeviceManagementTransactionalServiceImpl;
import org.eclipse.kapua.service.device.management.commons.call.DeviceCallBuilder;
import org.eclipse.kapua.service.device.management.configuration.DeviceComponentConfiguration;
import org.eclipse.kapua.service.device.management.configuration.DeviceConfiguration;
import org.eclipse.kapua.service.device.management.configuration.DeviceConfigurationFactory;
import org.eclipse.kapua.service.device.management.configuration.DeviceConfigurationManagementService;
import org.eclipse.kapua.service.device.management.configuration.message.internal.ConfigurationRequestChannel;
import org.eclipse.kapua.service.device.management.configuration.message.internal.ConfigurationRequestMessage;
import org.eclipse.kapua.service.device.management.configuration.message.internal.ConfigurationRequestPayload;
import org.eclipse.kapua.service.device.management.configuration.message.internal.ConfigurationResponseMessage;
import org.eclipse.kapua.service.device.management.configuration.store.DeviceConfigurationStoreService;
import org.eclipse.kapua.service.device.management.exception.DeviceManagementRequestContentException;
import org.eclipse.kapua.service.device.management.exception.DeviceNeverConnectedException;
import org.eclipse.kapua.service.device.management.message.KapuaMethod;
import org.eclipse.kapua.service.device.registry.DeviceRepository;
import org.eclipse.kapua.service.device.registry.event.DeviceEventFactory;
import org.eclipse.kapua.service.device.registry.event.DeviceEventRepository;
import org.eclipse.kapua.storage.TxManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXException;

import javax.inject.Singleton;
import javax.xml.bind.JAXBException;
import java.util.Date;

/**
 * {@link DeviceConfigurationManagementService} implementation.
 *
 * @since 1.0.0
 */
@Singleton
public class DeviceConfigurationManagementServiceImpl extends AbstractDeviceManagementTransactionalServiceImpl implements DeviceConfigurationManagementService {

    private static final Logger LOG = LoggerFactory.getLogger(DeviceConfigurationManagementServiceImpl.class);

    private final DeviceConfigurationFactory deviceConfigurationFactory;

    private static final String SCOPE_ID = "scopeId";
    private static final String DEVICE_ID = "deviceId";

    private final DeviceConfigurationStoreService deviceConfigurationStoreService;

    public DeviceConfigurationManagementServiceImpl(TxManager txManager,
                                                    AuthorizationService authorizationService,
                                                    PermissionFactory permissionFactory,
                                                    DeviceEventRepository deviceEventRepository,
                                                    DeviceEventFactory deviceEventFactory,
                                                    DeviceRepository deviceRepository,
                                                    DeviceConfigurationFactory deviceConfigurationFactory,
                                                    DeviceConfigurationStoreService deviceConfigurationStoreService) {
        super(txManager,
                authorizationService,
                permissionFactory,
                deviceEventRepository,
                deviceEventFactory,
                deviceRepository
        );
        this.deviceConfigurationFactory = deviceConfigurationFactory;
        this.deviceConfigurationStoreService = deviceConfigurationStoreService;
    }

    @Override
    public DeviceConfiguration get(KapuaId scopeId, KapuaId deviceId, String configurationId, String configurationComponentPid, Long timeout)
            throws KapuaException {
        // Argument Validation
        ArgumentValidator.notNull(scopeId, SCOPE_ID);
        ArgumentValidator.notNull(deviceId, DEVICE_ID);
        // Check Access
        authorizationService.checkPermission(permissionFactory.newPermission(DeviceManagementDomains.DEVICE_MANAGEMENT_DOMAIN, Actions.read, scopeId));
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

        // Build request
        DeviceCallBuilder<ConfigurationRequestChannel, ConfigurationRequestPayload, ConfigurationRequestMessage, ConfigurationResponseMessage> configurationDeviceCallBuilder =
                DeviceCallBuilder
                        .newBuilder()
                        .withRequestMessage(configurationRequestMessage)
                        .withTimeoutOrDefault(timeout);

        // Do get
        if (isDeviceConnected(scopeId, deviceId)) {
            ConfigurationResponseMessage responseMessage;
            try {
                responseMessage = configurationDeviceCallBuilder.send();
            } catch (Exception e) {
                LOG.error("Error while getting DeviceConfiguration with id {} and DeviceComponentConfiguration id {} for Device {}. Error: {}", configurationId, configurationComponentPid, deviceId, e.getMessage(), e);
                throw e;
            }

            // Create event
            createDeviceEvent(scopeId, deviceId, configurationRequestMessage, responseMessage);
            // Check response
            DeviceConfiguration onlineDeviceConfiguration = checkResponseAcceptedOrThrowError(responseMessage, () -> responseMessage.getPayload().getDeviceConfigurations());
            // Store config and return
            if (deviceConfigurationStoreService.isServiceEnabled(scopeId) &&
                    deviceConfigurationStoreService.isApplicationEnabled(scopeId, deviceId)) {
                if (Strings.isNullOrEmpty(configurationComponentPid)) {
                    // If all DeviceConfiguration has been requested, store it overriding any previous value
                    deviceConfigurationStoreService.storeConfigurations(scopeId, deviceId, onlineDeviceConfiguration);
                } else {
                    // If only one DeviceComponentConfiguration has been requested, store it overriding only the selected DeviceComponentConfiguration
                    deviceConfigurationStoreService.storeConfigurations(scopeId, deviceId, onlineDeviceConfiguration.getComponentConfigurations().get(0));
                }
            }

            return onlineDeviceConfiguration;
        } else {
            if (deviceConfigurationStoreService.isServiceEnabled(scopeId) &&
                    deviceConfigurationStoreService.isApplicationEnabled(scopeId, deviceId)) {
                return deviceConfigurationStoreService.getConfigurations(scopeId, deviceId);
            } else {
                throw new DeviceNeverConnectedException(deviceId);
            }
        }
    }

    @Override
    public void put(KapuaId scopeId, KapuaId deviceId, DeviceComponentConfiguration deviceComponentConfiguration, Long timeout)
            throws KapuaException {
        // Argument Validation
        ArgumentValidator.notNull(scopeId, SCOPE_ID);
        ArgumentValidator.notNull(deviceId, DEVICE_ID);
        ArgumentValidator.notNull(deviceComponentConfiguration, "componentConfiguration");
        ArgumentValidator.notEmptyOrNull(deviceComponentConfiguration.getId(), "componentConfiguration.componentId");
        // Check Access
        authorizationService.checkPermission(permissionFactory.newPermission(DeviceManagementDomains.DEVICE_MANAGEMENT_DOMAIN, Actions.write, scopeId));
        // Prepare the request
        ConfigurationRequestChannel configurationRequestChannel = new ConfigurationRequestChannel();
        configurationRequestChannel.setAppName(DeviceConfigurationAppProperties.APP_NAME);
        configurationRequestChannel.setVersion(DeviceConfigurationAppProperties.APP_VERSION);
        configurationRequestChannel.setMethod(KapuaMethod.WRITE);
        configurationRequestChannel.setComponentId(deviceComponentConfiguration.getId());

        ConfigurationRequestPayload configurationRequestPayload = new ConfigurationRequestPayload();

        DeviceConfiguration deviceConfiguration = deviceConfigurationFactory.newConfigurationInstance();
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

        // Build request
        DeviceCallBuilder<ConfigurationRequestChannel, ConfigurationRequestPayload, ConfigurationRequestMessage, ConfigurationResponseMessage> configurationDeviceCallBuilder =
                DeviceCallBuilder
                        .newBuilder()
                        .withRequestMessage(configurationRequestMessage)
                        .withTimeoutOrDefault(timeout);

        // Do put
        ConfigurationResponseMessage responseMessage;
        try {
            responseMessage = configurationDeviceCallBuilder.send();
        } catch (Exception e) {
            LOG.error("Error while putting DeviceComponentConfiguration {} for Device {}. Error: {}", deviceComponentConfiguration, deviceId, e.getMessage(), e);
            throw e;
        }

        // Create event
        createDeviceEvent(scopeId, deviceId, configurationRequestMessage, responseMessage);
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
        // Argument Validation
        ArgumentValidator.notNull(scopeId, SCOPE_ID);
        ArgumentValidator.notNull(deviceId, DEVICE_ID);
        ArgumentValidator.notNull(deviceConfiguration, "componentConfiguration");
        // Check Access
        authorizationService.checkPermission(permissionFactory.newPermission(DeviceManagementDomains.DEVICE_MANAGEMENT_DOMAIN, Actions.write, scopeId));
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

        // Build request
        DeviceCallBuilder<ConfigurationRequestChannel, ConfigurationRequestPayload, ConfigurationRequestMessage, ConfigurationResponseMessage> configurationDeviceCallBuilder =
                DeviceCallBuilder
                        .newBuilder()
                        .withRequestMessage(configurationRequestMessage)
                        .withTimeoutOrDefault(timeout);

        // Do put
        ConfigurationResponseMessage responseMessage;
        try {
            responseMessage = configurationDeviceCallBuilder.send();
        } catch (Exception e) {
            LOG.error("Error while putting DeviceConfiguration {} for Device {}. Error: {}", deviceConfiguration, deviceId, e.getMessage(), e);
            throw e;
        }

        // Create event
        createDeviceEvent(scopeId, deviceId, configurationRequestMessage, responseMessage);
        // Check response
        checkResponseAcceptedOrThrowError(responseMessage);
    }
}
