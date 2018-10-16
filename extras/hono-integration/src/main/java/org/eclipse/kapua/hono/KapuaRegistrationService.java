/*******************************************************************************
 * Copyright (c) 2018 Red Hat Inc and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Red Hat Inc - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.hono;

import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import org.eclipse.hono.config.SignatureSupportingConfigProperties;
import org.eclipse.hono.service.registration.BaseRegistrationService;
import org.eclipse.hono.service.registration.RegistrationAssertionHelperImpl;
import org.eclipse.hono.util.RegistrationResult;
import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.broker.core.BrokerJAXBContextProvider;
import org.eclipse.kapua.commons.security.KapuaSecurityUtils;
import org.eclipse.kapua.commons.util.xml.JAXBContextProvider;
import org.eclipse.kapua.commons.util.xml.XmlUtil;
import org.eclipse.kapua.locator.KapuaLocator;
import org.eclipse.kapua.service.account.Account;
import org.eclipse.kapua.service.account.AccountService;
import org.eclipse.kapua.service.device.registry.Device;
import org.eclipse.kapua.service.device.registry.DeviceRegistryService;
import org.eclipse.kapua.service.device.registry.DeviceStatus;
import org.eclipse.kapua.service.user.UserService;

import java.net.HttpURLConnection;

public class KapuaRegistrationService extends BaseRegistrationService<Object> {

    private final KapuaLocator locator = KapuaLocator.getInstance();

    UserService userService;
    AccountService accountService;
    DeviceRegistryService deviceRegistryService;

    public KapuaRegistrationService() {
        JAXBContextProvider brokerProvider = new BrokerJAXBContextProvider();
        XmlUtil.setContextProvider(brokerProvider);
        userService = locator.getService(UserService.class);
        accountService = locator.getService(AccountService.class);
        deviceRegistryService = locator.getService(DeviceRegistryService.class);

        SignatureSupportingConfigProperties props = new SignatureSupportingConfigProperties();
        // TODO configure key path
        props.setKeyPath("src/test/resources/certificates/jwt/test.key");

        // TODO remove vert.x dependency
        this.setRegistrationAssertionFactory(RegistrationAssertionHelperImpl.forSigning(Vertx.vertx(), props));

    }

    @Override
    public void setConfig(Object configuration) {

    }

    @Override
    public void assertRegistration(String tenantId, String deviceId, Handler<AsyncResult<RegistrationResult>> resultHandler){
         assertRegistration(tenantId, deviceId, null, resultHandler);
    }


    @Override
    public void assertRegistration(String tenantId, String deviceId, String gatewayId, Handler<AsyncResult<RegistrationResult>> resultHandler) {
        System.out.println("accessing registry.");
        System.out.println( "TenantId="+tenantId);
        System.out.println("deviceId="+deviceId);
        System.out.println("gatewawyId="+gatewayId);
        try {
            Account account = KapuaSecurityUtils.doPrivileged(() -> accountService.findByName(tenantId));
            if (account == null) {
                throw KapuaException.internalError("No account found for " + tenantId);
            }

            Device device = KapuaSecurityUtils.doPrivileged(() -> deviceRegistryService.findByClientId(account.getScopeId(), deviceId));
            if (device != null && device.getStatus() == DeviceStatus.ENABLED) {
                //TODO include more device data
                JsonObject deviceData = new JsonObject();

                resultHandler.handle(Future.succeededFuture(RegistrationResult.from(
                        HttpURLConnection.HTTP_OK,
                        getAssertionPayload(tenantId, deviceId, deviceData))));
            } else {
                resultHandler.handle(Future.succeededFuture(RegistrationResult.from(HttpURLConnection.HTTP_NOT_FOUND)));
            }

            //TODO check permissions and statuses
        } catch (KapuaException ke) {
            resultHandler.handle(Future.succeededFuture(RegistrationResult.from(HttpURLConnection.HTTP_INTERNAL_ERROR)));
            return;
        }
    }
}
