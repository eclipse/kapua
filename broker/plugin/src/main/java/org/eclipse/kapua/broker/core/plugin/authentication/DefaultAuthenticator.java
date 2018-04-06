/*******************************************************************************
 * Copyright (c) 2018 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.broker.core.plugin.authentication;

import com.codahale.metrics.Timer.Context;

import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.broker.core.message.system.DefaultSystemMessageCreator;
import org.eclipse.kapua.broker.core.message.system.SystemMessageCreator;
import org.eclipse.kapua.broker.core.message.system.SystemMessageCreator.SystemMessageType;
import org.eclipse.kapua.broker.core.plugin.KapuaConnectionContext;
import org.eclipse.kapua.broker.core.plugin.metric.ClientMetric;
import org.eclipse.kapua.broker.core.plugin.metric.LoginMetric;
import org.eclipse.kapua.broker.core.pool.JmsAssistantProducerPool;
import org.eclipse.kapua.broker.core.pool.JmsAssistantProducerPool.DESTINATIONS;
import org.eclipse.kapua.broker.core.pool.JmsAssistantProducerWrapper;
import org.eclipse.kapua.broker.core.setting.BrokerPluginSetting;
import org.eclipse.kapua.broker.core.setting.BrokerPluginSettingKey;
import org.eclipse.kapua.commons.setting.system.SystemSetting;
import org.eclipse.kapua.commons.setting.system.SystemSettingKey;
import org.eclipse.kapua.commons.util.ClassUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

/**
 * Default authenticator implementation
 *
 * @since 1.0
 */
public class DefaultAuthenticator implements Authenticator {

    protected static final Logger logger = LoggerFactory.getLogger(DefaultAuthenticator.class);

    private static final String SYSTEM_MESSAGE_CREATOR_CLASS_NAME;

    static {
        SYSTEM_MESSAGE_CREATOR_CLASS_NAME = BrokerPluginSetting.getInstance().getString(BrokerPluginSettingKey.SYSTEM_MESSAGE_CREATOR_CLASS_NAME);
    }

    private Map<String, Object> options;
    private ClientMetric clientMetric = ClientMetric.getInstance();
    private LoginMetric loginMetric = LoginMetric.getInstance();

    protected AdminAuthenticationLogic adminAuthenticationLogic;
    protected UserAuthenticationLogic userAuthenticationLogic;

    protected SystemMessageCreator systemMessageCreator;

    /**
     * Default constructor
     *
     * @param options thread safe options used to customize the authenticator behavior (please don't change the signature since the class is instantiated by reflection)
     * @throws KapuaException
     */
    public DefaultAuthenticator(Map<String, Object> options) throws KapuaException {
        this.options = options;
        adminAuthenticationLogic = new AdminAuthenticationLogic(options);
        userAuthenticationLogic = new UserAuthenticationLogic(options);
        logger.info(">>> Security broker filter: calling start... Initialize system message creator");
        systemMessageCreator = ClassUtil.newInstance(SYSTEM_MESSAGE_CREATOR_CLASS_NAME, DefaultSystemMessageCreator.class);
    }

    @Override
    public List<AuthorizationEntry> connect(KapuaConnectionContext kcc)
            throws KapuaException {
        List<AuthorizationEntry> authorizationEntries = null;
        if (isAdminUser(kcc)) {
            loginMetric.getKapuasysTokenAttempt().inc();
            authorizationEntries = adminAuthenticationLogic.connect(kcc);
            clientMetric.getConnectedKapuasys().inc();
        } else {
            loginMetric.getNormalUserAttempt().inc();
            authorizationEntries = userAuthenticationLogic.connect(kcc);
            clientMetric.getConnectedClient().inc();
            sendConnectMessage(kcc);
        }
        return authorizationEntries;
    }

    @Override
    public void disconnect(KapuaConnectionContext kcc, Throwable error) {
        if (isAdminUser(kcc)) {
            clientMetric.getDisconnectionKapuasys().inc();
            adminAuthenticationLogic.disconnect(kcc, error);
        } else {
            clientMetric.getDisconnectionClient().inc();
            if (userAuthenticationLogic.disconnect(kcc, error)) {
                sendDisconnectMessage(kcc);
            }
        }
    }

    @Override
    public void sendConnectMessage(KapuaConnectionContext kcc) {
        sendMessage(kcc, Authenticator.ADDRESS_CONNECT_PATTERN_KEY, SystemMessageType.CONNECT);
    }

    @Override
    public void sendDisconnectMessage(KapuaConnectionContext kcc) {
        sendMessage(kcc, Authenticator.ADDRESS_DISCONNECT_PATTERN_KEY, SystemMessageType.DISCONNECT);
    }

    private void sendMessage(KapuaConnectionContext kcc, String messageAddressPattern, SystemMessageType systemMessageType) {
        if (systemMessageType != null) {
            Context loginSendLogingUpdateMsgTimeContex = loginMetric.getSendLoginUpdateMsgTime().time();
            String message = systemMessageCreator.createMessage(systemMessageType, kcc);
            JmsAssistantProducerWrapper producerWrapper = null;
            try {
                producerWrapper = JmsAssistantProducerPool.getIOnstance(DESTINATIONS.NO_DESTINATION).borrowObject();
                producerWrapper.send(String.format((String) options.get(messageAddressPattern),
                        SystemSetting.getInstance().getMessageClassifier(), kcc.getAccountName(), kcc.getClientId()),
                        message,
                        kcc);
            } catch (Exception e) {
                logger.error("Exception sending the {} message: {}", systemMessageType.name().toLowerCase(), e.getMessage(), e);
            } finally {
                if (producerWrapper != null) {
                    JmsAssistantProducerPool.getIOnstance(DESTINATIONS.NO_DESTINATION).returnObject(producerWrapper);
                }
            }
            loginSendLogingUpdateMsgTimeContex.stop();
        }
        else {
            logger.warn("Cannot send system message for address pattern {} since the system message type is null!", messageAddressPattern);
        }
    }

    protected boolean isAdminUser(KapuaConnectionContext kcc) {
        String adminUsername = SystemSetting.getInstance().getString(SystemSettingKey.SYS_ADMIN_USERNAME);
        return kcc.getUserName().equals(adminUsername);
    }

}