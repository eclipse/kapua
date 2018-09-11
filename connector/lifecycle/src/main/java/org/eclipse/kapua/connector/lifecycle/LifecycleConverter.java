/*******************************************************************************
 * Copyright (c) 2011, 2018 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.connector.lifecycle;

import java.util.Map;

import org.eclipse.kapua.KapuaErrorCodes;
import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.commons.security.KapuaSecurityUtils;
import org.eclipse.kapua.connector.KapuaProcessorException;
import org.eclipse.kapua.locator.KapuaLocator;
import org.eclipse.kapua.message.device.lifecycle.KapuaAppsChannel;
import org.eclipse.kapua.message.device.lifecycle.KapuaAppsMessage;
import org.eclipse.kapua.message.device.lifecycle.KapuaAppsPayload;
import org.eclipse.kapua.message.device.lifecycle.KapuaBirthChannel;
import org.eclipse.kapua.message.device.lifecycle.KapuaBirthMessage;
import org.eclipse.kapua.message.device.lifecycle.KapuaBirthPayload;
import org.eclipse.kapua.message.device.lifecycle.KapuaDisconnectChannel;
import org.eclipse.kapua.message.device.lifecycle.KapuaDisconnectMessage;
import org.eclipse.kapua.message.device.lifecycle.KapuaDisconnectPayload;
import org.eclipse.kapua.message.device.lifecycle.KapuaMissingChannel;
import org.eclipse.kapua.message.device.lifecycle.KapuaMissingMessage;
import org.eclipse.kapua.message.device.lifecycle.KapuaMissingPayload;
import org.eclipse.kapua.message.internal.device.lifecycle.KapuaAppsChannelImpl;
import org.eclipse.kapua.message.internal.device.lifecycle.KapuaAppsMessageImpl;
import org.eclipse.kapua.message.internal.device.lifecycle.KapuaAppsPayloadImpl;
import org.eclipse.kapua.message.internal.device.lifecycle.KapuaBirthChannelImpl;
import org.eclipse.kapua.message.internal.device.lifecycle.KapuaBirthMessageImpl;
import org.eclipse.kapua.message.internal.device.lifecycle.KapuaBirthPayloadImpl;
import org.eclipse.kapua.message.internal.device.lifecycle.KapuaDisconnectChannelImpl;
import org.eclipse.kapua.message.internal.device.lifecycle.KapuaDisconnectMessageImpl;
import org.eclipse.kapua.message.internal.device.lifecycle.KapuaDisconnectPayloadImpl;
import org.eclipse.kapua.message.internal.device.lifecycle.KapuaMissingChannelImpl;
import org.eclipse.kapua.message.internal.device.lifecycle.KapuaMissingMessageImpl;
import org.eclipse.kapua.message.internal.device.lifecycle.KapuaMissingPayloadImpl;
import org.eclipse.kapua.message.transport.TransportMessage;
import org.eclipse.kapua.service.account.Account;
import org.eclipse.kapua.service.account.AccountService;
import org.eclipse.kapua.service.device.call.kura.KuraMetricsNames;
import org.eclipse.kapua.service.device.registry.Device;
import org.eclipse.kapua.service.device.registry.DeviceRegistryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

//TODO reorganize code
public class LifecycleConverter {

    private static final Logger logger = LoggerFactory.getLogger(LifecycleConverter.class);

    private static DeviceRegistryService deviceRegistryService = KapuaLocator.getInstance().getService(DeviceRegistryService.class);
    private static AccountService accountService = KapuaLocator.getInstance().getService(AccountService.class);

    private LifecycleConverter() {
    }

    public static KapuaAppsMessage getAppsMessage(TransportMessage tm) throws KapuaProcessorException {
        final String clientId = tm.getClientId();
        final String scopeName = tm.getScopeName();
        KapuaAppsMessage appsMessage = new KapuaAppsMessageImpl();
        //channel
        KapuaAppsChannel kapuaChannel = new KapuaAppsChannelImpl();
        kapuaChannel.setSemanticParts(tm.getChannel().getSemanticParts());
        appsMessage.setChannel(kapuaChannel);

        //TODO check why the client id is not already evaluated in the channel
        appsMessage.getChannel().setClientId(clientId);

        Map<String, Object> metrics = tm.getPayload().getMetrics();

        //TODO check this behavior (backward compatibility?)
        String applicationFrameworkVersion = (String) metrics.get(KuraMetricsNames.APPLICATION_FRAMEWORK_VERSION);
        if (applicationFrameworkVersion == null) {
            applicationFrameworkVersion = (String) metrics.get(KuraMetricsNames.KURA_VERSION);
            if (applicationFrameworkVersion==null) {
                applicationFrameworkVersion = (String) metrics.get(KuraMetricsNames.ESF_VERSION);
            }
        }
        //payload
        KapuaAppsPayload kapuaPayload = new KapuaAppsPayloadImpl(
                (String)metrics.get(KuraMetricsNames.UPTIME),
                (String)metrics.get(KuraMetricsNames.DISPLAY_NAME),
                (String)metrics.get(KuraMetricsNames.MODEL_NAME),
                (String)metrics.get(KuraMetricsNames.MODEL_ID),
                (String)metrics.get(KuraMetricsNames.PART_NUMBER),
                (String)metrics.get(KuraMetricsNames.SERIAL_NUMBER),
                (String)metrics.get(KuraMetricsNames.FIRMWARE_VERSION),
                (String)metrics.get(KuraMetricsNames.FIRMWARE_VERSION),
                (String)metrics.get(KuraMetricsNames.BIOS_VERSION),
                (String)metrics.get(KuraMetricsNames.BIOS_VERSION),
                (String)metrics.get(KuraMetricsNames.OS),
                (String)metrics.get(KuraMetricsNames.OS_VERSION),
                (String)metrics.get(KuraMetricsNames.JVM_NAME),
                (String)metrics.get(KuraMetricsNames.JVM_VERSION),
                (String)metrics.get(KuraMetricsNames.JVM_PROFILE),
                (String)metrics.get(KuraMetricsNames.OSGI_FRAMEWORK),//TODO check if it's containerFramework fallback to containerFramework????
                (String)metrics.get(KuraMetricsNames.OSGI_FRAMEWORK_VERSION),//TODO check if it's containerFrameworkVersion
                (String)metrics.get(KuraMetricsNames.APPLICATION_FRAMEWORK),
                (String)applicationFrameworkVersion,//ex kura_version (deprecated)
                (String)metrics.get(KuraMetricsNames.CONNECTION_INTERFACE),
                (String)metrics.get(KuraMetricsNames.CONNECTION_IP),
                (String)metrics.get(KuraMetricsNames.ACCEPT_ENCODING),
                (String)metrics.get(KuraMetricsNames.APPLICATION_IDS),//applicationIdentifiers
                (String)metrics.get(KuraMetricsNames.AVAILABLE_PROCESSORS),
                (String)metrics.get(KuraMetricsNames.TOTAL_MEMORY),
                (String)metrics.get(KuraMetricsNames.OS_ARCH),
                (String)metrics.get(KuraMetricsNames.MODEM_IMEI),
                (String)metrics.get(KuraMetricsNames.MODEM_IMSI),
                (String)metrics.get(KuraMetricsNames.MODEM_ICCID));
        kapuaPayload.setMetrics(tm.getPayload().getMetrics());
        kapuaPayload.setBody(tm.getPayload().getBody());
        appsMessage.setPayload(kapuaPayload);
        appsMessage.setClientId(clientId);
        appsMessage.setPosition(tm.getPosition());
        appsMessage.setReceivedOn(tm.getReceivedOn());
        appsMessage.setSentOn(tm.getSentOn());
        try {
            return KapuaSecurityUtils.doPrivileged(() -> {
                Account account = accountService.findByName(scopeName);
                if (account==null) {
                    throw new KapuaProcessorException(KapuaErrorCodes.ILLEGAL_ARGUMENT, "message.scopeName", scopeName);
                }
                appsMessage.setScopeId(account.getId());
                Device device = deviceRegistryService.findByClientId(account.getId(), clientId);
                if (device==null) {
                    throw new KapuaProcessorException(KapuaErrorCodes.ILLEGAL_ARGUMENT, "device.clientId", clientId);
                }
                appsMessage.setDeviceId(device.getId());
                logger.debug("Lifecycle birth... converting message... DONE");
                return appsMessage;
            });
        } catch (KapuaException e) {
            logger.info("Lifecycle birth... Error processing message {}", e.getMessage());
            throw new KapuaProcessorException(KapuaErrorCodes.INTERNAL_ERROR, e);
        }
    }

    public static KapuaBirthMessage getBirthMessage(TransportMessage tm) throws KapuaProcessorException {
        final String clientId = tm.getClientId();
        final String scopeName = tm.getScopeName();
        KapuaBirthMessage birthMessage = new KapuaBirthMessageImpl();
        //channel
        KapuaBirthChannel kapuaChannel = new KapuaBirthChannelImpl();
        kapuaChannel.setSemanticParts(tm.getChannel().getSemanticParts());
        birthMessage.setChannel(kapuaChannel);

        //TODO check why the client id is not already evaluated in the channel
        birthMessage.getChannel().setClientId(clientId);

        Map<String, Object> metrics = tm.getPayload().getMetrics();

        //TODO check this behavior (backward compatibility?)
        String applicationFrameworkVersion = (String) metrics.get(KuraMetricsNames.APPLICATION_FRAMEWORK_VERSION);
        if (applicationFrameworkVersion == null) {
            applicationFrameworkVersion = (String) metrics.get(KuraMetricsNames.KURA_VERSION);
            if (applicationFrameworkVersion==null) {
                applicationFrameworkVersion = (String) metrics.get(KuraMetricsNames.ESF_VERSION);
            }
        }
        //payload
        KapuaBirthPayload kapuaPayload = new KapuaBirthPayloadImpl(
                (String)metrics.get(KuraMetricsNames.UPTIME),//uptime
                (String)metrics.get(KuraMetricsNames.DISPLAY_NAME),//displayName
                (String)metrics.get(KuraMetricsNames.MODEL_NAME),//modelName
                (String)metrics.get(KuraMetricsNames.MODEL_ID),//modelId
                (String)metrics.get(KuraMetricsNames.PART_NUMBER),//partNumber
                (String)metrics.get(KuraMetricsNames.SERIAL_NUMBER),//serialNumber
                (String)metrics.get(KuraMetricsNames.FIRMWARE_VERSION),//firmware
                (String)metrics.get(KuraMetricsNames.FIRMWARE_VERSION),//firmwareVersion
                (String)metrics.get(KuraMetricsNames.BIOS_VERSION),//bios
                (String)metrics.get(KuraMetricsNames.BIOS_VERSION),//biosVersion
                (String)metrics.get(KuraMetricsNames.OS),//os
                (String)metrics.get(KuraMetricsNames.OS_VERSION),//osVersion
                (String)metrics.get(KuraMetricsNames.JVM_NAME),//jvm
                (String)metrics.get(KuraMetricsNames.JVM_VERSION),//jvmVersion
                (String)metrics.get(KuraMetricsNames.JVM_PROFILE),//jvmProfile
                (String)metrics.get(KuraMetricsNames.OSGI_FRAMEWORK),//containerFramework - TODO check if it's containerFramework fallback to containerFramework????
                (String)metrics.get(KuraMetricsNames.OSGI_FRAMEWORK_VERSION),//containerFrameworkVersion - TODO check if it's containerFrameworkVersion
                (String)metrics.get(KuraMetricsNames.APPLICATION_FRAMEWORK),//applicationFramework
                (String)applicationFrameworkVersion,//applicationFrameworkVersion - ex kura_version (deprecated)
                (String)metrics.get(KuraMetricsNames.CONNECTION_INTERFACE),//connectionInterface
                (String)metrics.get(KuraMetricsNames.CONNECTION_IP),//connectionIp
                (String)metrics.get(KuraMetricsNames.ACCEPT_ENCODING),//acceptEncoding
                (String)metrics.get(KuraMetricsNames.APPLICATION_IDS),//applicationIdentifiers
                (String)metrics.get(KuraMetricsNames.AVAILABLE_PROCESSORS),//availableProcessors
                (String)metrics.get(KuraMetricsNames.TOTAL_MEMORY),//totalMemory
                (String)metrics.get(KuraMetricsNames.OS_ARCH),//osArch
                (String)metrics.get(KuraMetricsNames.MODEM_IMEI),//modemImei
                (String)metrics.get(KuraMetricsNames.MODEM_IMSI),//modemImsi
                (String)metrics.get(KuraMetricsNames.MODEM_ICCID));//modemIccid
        kapuaPayload.setMetrics(tm.getPayload().getMetrics());
        kapuaPayload.setBody(tm.getPayload().getBody());
        birthMessage.setPayload(kapuaPayload);
        birthMessage.setClientId(clientId);
        birthMessage.setPosition(tm.getPosition());
        birthMessage.setReceivedOn(tm.getReceivedOn());
        birthMessage.setSentOn(tm.getSentOn());
        try {
            return KapuaSecurityUtils.doPrivileged(() -> {
                Account account = accountService.findByName(scopeName);
                if (account==null) {
                    throw new KapuaProcessorException(KapuaErrorCodes.ILLEGAL_ARGUMENT, "message.scopeName", scopeName);
                }
                birthMessage.setScopeId(account.getId());
                Device device = deviceRegistryService.findByClientId(account.getId(), clientId);
                birthMessage.setDeviceId(device != null ? device.getId() : null);
                logger.debug("Lifecycle birth... converting message... DONE");
                return birthMessage;
            });
        } catch (KapuaException e) {
            logger.info("Lifecycle birth... Error processing message {}", e.getMessage());
            throw new KapuaProcessorException(KapuaErrorCodes.INTERNAL_ERROR, e);
        }
    }

    public static KapuaDisconnectMessage getDisconnectMessage(TransportMessage tm) throws KapuaProcessorException {
        final String clientId = tm.getClientId();
        final String scopeName = tm.getScopeName();
        KapuaDisconnectMessage disconnectMessage = new KapuaDisconnectMessageImpl();
        //channel
        KapuaDisconnectChannel kapuaChannel = new KapuaDisconnectChannelImpl();
        kapuaChannel.setSemanticParts(tm.getChannel().getSemanticParts());
        disconnectMessage.setChannel(kapuaChannel);

        //TODO check why the client id is not already evaluated in the channel
        disconnectMessage.getChannel().setClientId(clientId);

        Map<String, Object> metrics = tm.getPayload().getMetrics();

        //payload
        KapuaDisconnectPayload kapuaPayload = new KapuaDisconnectPayloadImpl(
                (String)metrics.get(KuraMetricsNames.UPTIME),
                (String)metrics.get(KuraMetricsNames.DISPLAY_NAME));
        kapuaPayload.setMetrics(tm.getPayload().getMetrics());
        kapuaPayload.setBody(tm.getPayload().getBody());
        disconnectMessage.setPayload(kapuaPayload);
        disconnectMessage.setClientId(clientId);
        disconnectMessage.setPosition(tm.getPosition());
        disconnectMessage.setReceivedOn(tm.getReceivedOn());
        disconnectMessage.setSentOn(tm.getSentOn());
        try {
            return KapuaSecurityUtils.doPrivileged(() -> {
                Account account = accountService.findByName(scopeName);
                if (account==null) {
                    throw new KapuaProcessorException(KapuaErrorCodes.ILLEGAL_ARGUMENT, "message.scopeName", scopeName);
                }
                disconnectMessage.setScopeId(account.getId());
                Device device = deviceRegistryService.findByClientId(account.getId(), clientId);
                if (device==null) {
                    throw new KapuaProcessorException(KapuaErrorCodes.ILLEGAL_ARGUMENT, "device.clientId", clientId);
                }
                disconnectMessage.setDeviceId(device.getId());
                logger.debug("Lifecycle birth... converting message... DONE");
                return disconnectMessage;
            });
        } catch (KapuaException e) {
            logger.info("Lifecycle birth... Error processing message {}", e.getMessage());
            throw new KapuaProcessorException(KapuaErrorCodes.INTERNAL_ERROR, e);
        }
    }

    public static KapuaMissingMessage getMissingMessage(TransportMessage tm) throws KapuaProcessorException {
        final String clientId = tm.getClientId();
        final String scopeName = tm.getScopeName();
        KapuaMissingMessage missingMessage = new KapuaMissingMessageImpl();
        //channel
        KapuaMissingChannel kapuaChannel = new KapuaMissingChannelImpl();
        kapuaChannel.setSemanticParts(tm.getChannel().getSemanticParts());
        missingMessage.setChannel(kapuaChannel);

        //TODO check why the client id is not already evaluated in the channel
        missingMessage.getChannel().setClientId(clientId);

        //payload
        KapuaMissingPayload kapuaPayload = new KapuaMissingPayloadImpl();
        kapuaPayload.setMetrics(tm.getPayload().getMetrics());
        kapuaPayload.setBody(tm.getPayload().getBody());
        missingMessage.setPayload(kapuaPayload);
        missingMessage.setClientId(clientId);
        missingMessage.setPosition(tm.getPosition());
        missingMessage.setReceivedOn(tm.getReceivedOn());
        missingMessage.setSentOn(tm.getSentOn());
        try {
            return KapuaSecurityUtils.doPrivileged(() -> {
                Account account = accountService.findByName(scopeName);
                if (account==null) {
                    throw new KapuaProcessorException(KapuaErrorCodes.ILLEGAL_ARGUMENT, "message.scopeName", scopeName);
                }
                missingMessage.setScopeId(account.getId());
                Device device = deviceRegistryService.findByClientId(account.getId(), clientId);
                if (device==null) {
                    throw new KapuaProcessorException(KapuaErrorCodes.ILLEGAL_ARGUMENT, "device.clientId", clientId);
                }
                missingMessage.setDeviceId(device.getId());
                logger.debug("Lifecycle birth... converting message... DONE");
                return missingMessage;
            });
        } catch (KapuaException e) {
            logger.info("Lifecycle birth... Error processing message {}", e.getMessage());
            throw new KapuaProcessorException(KapuaErrorCodes.INTERNAL_ERROR, e);
        }
    }

}

