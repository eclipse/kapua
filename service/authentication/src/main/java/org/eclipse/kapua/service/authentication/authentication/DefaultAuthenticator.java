/*******************************************************************************
 * Copyright (c) 2018, 2022 Eurotech and/or its affiliates and others
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
import org.eclipse.kapua.client.security.metric.AuthLoginMetric;
import org.eclipse.kapua.client.security.metric.AuthMetric;
import org.eclipse.kapua.commons.event.ServiceEventBusManager;
import org.eclipse.kapua.commons.model.id.KapuaEid;
import org.eclipse.kapua.commons.security.KapuaSecurityUtils;
import org.eclipse.kapua.commons.setting.system.SystemSetting;
import org.eclipse.kapua.commons.setting.system.SystemSettingKey;
import org.eclipse.kapua.commons.util.KapuaDateUtils;
import org.eclipse.kapua.event.ServiceEvent;
import org.eclipse.kapua.event.ServiceEventBus;
import org.eclipse.kapua.event.ServiceEventBusException;
import org.eclipse.kapua.locator.KapuaLocator;
import org.eclipse.kapua.service.authentication.setting.ServiceAuthenticationSetting;
import org.eclipse.kapua.service.authentication.setting.ServiceAuthenticationSettingKey;
import org.eclipse.kapua.service.device.registry.Device;
import org.eclipse.kapua.service.device.registry.DeviceRegistryService;
import org.eclipse.kapua.service.device.registry.DeviceStatus;
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

    private DeviceRegistryService deviceRegistryService;
    private AuthMetric authenticationMetric = AuthMetric.getInstance();
    private boolean raiseLifecycleEvents;
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
     * @throws KapuaException
     */
    public DefaultAuthenticator() throws KapuaException {
        deviceRegistryService = KapuaLocator.getInstance().getService(DeviceRegistryService.class);
        adminUserName = SystemSetting.getInstance().getString(SystemSettingKey.SYS_ADMIN_USERNAME);
        raiseLifecycleEvents = ServiceAuthenticationSetting.getInstance().getBoolean(ServiceAuthenticationSettingKey.SERVICE_AUTHENTICATION_ENABLE_LIFECYCLE_EVENTS, false);
        if (raiseLifecycleEvents) {
            lifecycleEventAddress = ServiceAuthenticationSetting.getInstance().getString(ServiceAuthenticationSettingKey.SERVICE_AUTHENTICATION_LIFECYCLE_EVENTS_ADDRESS);
            serviceEventBus = ServiceEventBusManager.getInstance();
        }
        else {
            logger.info("Skipping AuthenticationService event bus initialization since the raise of connect/disconnect event is disabled!");
        }
    }

    @Override
    public List<AuthAcl> connect(AuthContext authContext) throws KapuaException {
        List<AuthAcl> authorizationEntries = null;
        Device device = KapuaSecurityUtils.doPrivileged(() ->
            deviceRegistryService.findByClientId(KapuaEid.parseCompactId(authContext.getScopeId()), authContext.getClientId()));
        if (device != null && DeviceStatus.DISABLED.equals(device.getStatus())) {
            logger.warn("Device {} is disabled", authContext.getClientId());
            throw new SecurityException("Device is disabled");
        }
        if (isAdminUser(authContext)) {
            updateMetricAndCheckForStealingLink(authContext, authenticationMetric.getAdminLogin());
            authorizationEntries = adminAuthenticationLogic.connect(authContext);
            authenticationMetric.getAdminLogin().getConnected().inc();
        }
        else {
            updateMetricAndCheckForStealingLink(authContext, authenticationMetric.getUserLogin());
            authorizationEntries = userAuthenticationLogic.connect(authContext);
            authenticationMetric.getUserLogin().getConnected().inc();
            Context loginRaiseLifecycleEventTimeContext = authenticationMetric.getExtConnectorTime().getRaiseLifecycleEvent().time();
            if (raiseLifecycleEvents) {
                logger.info("raising connect lifecycle event for clientId: {}", authContext.getClientId());
                raiseLifecycleEvent(authContext, DeviceConnectionStatus.CONNECTED);
            }
            loginRaiseLifecycleEventTimeContext.stop();
        }
        return authorizationEntries;
    }

    @Override
    public void disconnect(AuthContext authContext) throws KapuaException {
        if (isAdminUser(authContext)) {
            authenticationMetric.getAdminLogin().getDisconnected().inc();
            if (authContext.isStealingLink()) {
                authenticationMetric.getAdminLogin().getStealingLinkDisconnect().inc();
            }
            adminAuthenticationLogic.disconnect(authContext);
        }
        else {
            disconnectUser(authContext, authenticationMetric.getUserLogin());
        }
    }

    protected void disconnectUser(AuthContext authContext, AuthLoginMetric loginMetric) throws ServiceEventBusException {
        loginMetric.getDisconnected().inc();
        String error = authContext.getExceptionClass();
        logger.info("Disconnecting client: connection id: {} - error: {} - isStealingLink {} - isIllegalState: {}",
            authContext.getConnectionId(), error, authContext.isStealingLink(), authContext.isIllegalState());
        if (authContext.isStealingLink()) {
            loginMetric.getStealingLinkDisconnect().inc();
            logger.info("Stealing link: skip device connection status update. Client id: {} - Connection id: {}",
                authContext.getClientId(),
                authContext.getConnectionId());
        }
        else if (authContext.isIllegalState()) {
            loginMetric.getIllegalStateDisconnect().inc();
            logger.info("Illegal device connection status: skip device connection status update. Client id: {} - Connection id: {}",
                authContext.getClientId(),
                authContext.getConnectionId());
        }
        if (userAuthenticationLogic.disconnect(authContext)) {
            logger.info("raising disconnect lifecycle event for clientId: {}", authContext.getClientId());
            Context loginRaiseLifecycleEventTimeContext = authenticationMetric.getExtConnectorTime().getRaiseLifecycleEvent().time();
            raiseLifecycleEvent(authContext, DeviceConnectionStatus.DISCONNECTED);
            loginRaiseLifecycleEventTimeContext.stop();
        }
    }

    protected boolean isAdminUser(AuthContext authContext) {
        return adminUserName.equals(authContext.getUsername());
    }

    protected void raiseLifecycleEvent(AuthContext authContext, DeviceConnectionStatus deviceConnectionStatus) throws ServiceEventBusException {
        logger.debug("raising lifecycle events: clientId: {} - connection status: {}", authContext.getClientId(), deviceConnectionStatus);
        //internal connections with not registered user/account shouldn't raise connect/disconnect events
        if (authContext.getUserId()!=null && authContext.getScopeId()!=null) {
            serviceEventBus.publish(lifecycleEventAddress, getServiceEvent(authContext, deviceConnectionStatus));
        }
        else {
            logger.info("Skipping event raising for clientId {} (username: {} - clientIp: {}) since userId ({}) and/or scopeId ({}) are null",
                authContext.getClientId(), authContext.getUsername(), authContext.getClientIp(), authContext.getUserId(), authContext.getScopeId());
        }
    }

    protected void updateMetricAndCheckForStealingLink(AuthContext authContext, AuthLoginMetric loginMetric) {
        loginMetric.getAttempt().inc();
        if (authContext.isStealingLink()) {
            loginMetric.getStealingLinkConnect().inc();
            logger.warn("Detected Stealing link for clientId: {} - account: {} - current connectionId: {} - IP: {}",
                    authContext.getClientId(),
                    authContext.getAccountName(),
                    authContext.getConnectionId(),
                    authContext.getClientIp());
        }
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
