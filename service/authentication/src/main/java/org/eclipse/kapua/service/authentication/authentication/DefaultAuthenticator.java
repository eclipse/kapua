/*******************************************************************************
 * Copyright (c) 2018, 2021 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.service.authentication.authentication;

import com.codahale.metrics.Timer.Context;

import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.client.security.bean.AuthAcl;
import org.eclipse.kapua.client.security.bean.AuthContext;
import org.eclipse.kapua.client.security.metric.LoginMetric;
import org.eclipse.kapua.commons.event.ServiceEventBusManager;
import org.eclipse.kapua.commons.model.id.KapuaEid;
import org.eclipse.kapua.commons.setting.system.SystemSetting;
import org.eclipse.kapua.commons.setting.system.SystemSettingKey;
import org.eclipse.kapua.commons.util.KapuaDateUtils;
import org.eclipse.kapua.event.ServiceEvent;
import org.eclipse.kapua.event.ServiceEventBus;
import org.eclipse.kapua.event.ServiceEventBusException;
import org.eclipse.kapua.service.device.registry.Device;
import org.eclipse.kapua.service.device.registry.connection.DeviceConnectionStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.List;

import javax.inject.Inject;

/**
 * Default authenticator implementation
 *
 * @since 1.0
 */
public class DefaultAuthenticator implements Authenticator {

    protected static final Logger logger = LoggerFactory.getLogger(DefaultAuthenticator.class);

    enum EventType {
        CONNECT,
        DISCONNECT
    }

    private LoginMetric loginMetric = LoginMetric.getInstance();
    private String lifecycleEventAddress;

    //TODO declare AuthenticationLogic interface and add parameters to help injector to inject the right instance
    @Inject
    protected AdminAuthenticationLogic adminAuthenticationLogic;
    @Inject
    protected UserAuthenticationLogic userAuthenticationLogic;

    //TODO inject this instance
    private ServiceEventBus serviceEventBus;

    protected String adminUserName;

    /**
     * Default constructor
     *
     * @param options thread safe options used to customize the authenticator behavior (please don't change the signature since the class is instantiated by reflection)
     * @throws KapuaException
     */
    public DefaultAuthenticator() throws KapuaException {
        adminUserName = SystemSetting.getInstance().getString(SystemSettingKey.SYS_ADMIN_USERNAME);
        lifecycleEventAddress = "lifecycleEvent";//TODO get from configuration
        serviceEventBus = ServiceEventBusManager.getInstance();
    }

    @Override
    public List<AuthAcl> connect(AuthContext authContext) throws KapuaException {
        List<AuthAcl> authorizationEntries = null;
        if (isAdminUser(authContext)) {
            loginMetric.getAdminAttempt().inc();
            authorizationEntries = adminAuthenticationLogic.connect(authContext);
            loginMetric.getAdminConnected().inc();
        }
        else {
            loginMetric.getAttempt().inc();
            authorizationEntries = userAuthenticationLogic.connect(authContext);
            loginMetric.getConnected().inc();
            Context loginSendLogingUpdateMsgTimeContext = loginMetric.getSendLoginUpdateMsgTime().time();
            raiseLifecycleEvent(authContext, DeviceConnectionStatus.CONNECTED);
            loginSendLogingUpdateMsgTimeContext.stop();
        }
        return authorizationEntries;
    }

    @Override
    public void disconnect(AuthContext authContext) throws KapuaException {
        if (isAdminUser(authContext)) {
            loginMetric.getAdminDisconnected().inc();
            adminAuthenticationLogic.disconnect(authContext);
        }
        else {
            loginMetric.getDisconnected().inc();
            if (userAuthenticationLogic.disconnect(authContext)) {
                Context loginSendLogingUpdateMsgTimeContext = loginMetric.getSendLoginUpdateMsgTime().time();
                raiseLifecycleEvent(authContext, DeviceConnectionStatus.DISCONNECTED);
                loginSendLogingUpdateMsgTimeContext.stop();
            }
        }
    }

    protected boolean isAdminUser(AuthContext authContext) {
        return authContext.getUsername().equals(adminUserName);
    }

    protected void raiseLifecycleEvent(AuthContext authContext, DeviceConnectionStatus deviceConnectionStatus) throws ServiceEventBusException {
        serviceEventBus.publish(lifecycleEventAddress, getServiceEvent(authContext, deviceConnectionStatus));
    }

    private ServiceEvent getServiceEvent(AuthContext authContext, DeviceConnectionStatus deviceConnectionStatus) {
        ServiceEvent serviceEvent = new ServiceEvent();
        serviceEvent.setEntityType(Device.class.getCanonicalName());
        serviceEvent.setInputs(authContext.getClientId());
        serviceEvent.setOperation(deviceConnectionStatus.name());
        serviceEvent.setService(this.getClass().getCanonicalName());
        serviceEvent.setUserId(KapuaEid.parseCompactId(authContext.getUserId()));
        serviceEvent.setScopeId(KapuaEid.parseCompactId(authContext.getScopeId()));
        serviceEvent.setTimestamp(Date.from(KapuaDateUtils.getKapuaSysDate()));
        return serviceEvent;
    }
}
