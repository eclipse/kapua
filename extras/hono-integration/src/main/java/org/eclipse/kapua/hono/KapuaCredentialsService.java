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
import io.vertx.core.json.JsonObject;
import org.eclipse.hono.service.credentials.BaseCredentialsService;
import org.eclipse.hono.util.CacheDirective;
import org.eclipse.hono.util.CredentialsConstants;
import org.eclipse.hono.util.CredentialsObject;
import org.eclipse.hono.util.CredentialsResult;
import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.broker.core.BrokerJAXBContextProvider;
import org.eclipse.kapua.commons.security.KapuaSecurityUtils;
import org.eclipse.kapua.commons.util.xml.JAXBContextProvider;
import org.eclipse.kapua.commons.util.xml.XmlUtil;
import org.eclipse.kapua.locator.KapuaLocator;
import org.eclipse.kapua.service.account.Account;
import org.eclipse.kapua.service.account.AccountService;
import org.eclipse.kapua.service.authentication.credential.Credential;
import org.eclipse.kapua.service.authentication.credential.CredentialListResult;
import org.eclipse.kapua.service.authentication.credential.CredentialService;
import org.eclipse.kapua.service.device.registry.DeviceRegistryService;
import org.eclipse.kapua.service.user.User;
import org.eclipse.kapua.service.user.UserService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

import java.net.HttpURLConnection;

@Repository
@ConditionalOnProperty(name = "hono.app.type", havingValue = "kapua", matchIfMissing = true)
public class KapuaCredentialsService extends BaseCredentialsService<Object> {

    private final KapuaLocator locator = KapuaLocator.getInstance();

    UserService userService;
    AccountService accountService;
    CredentialService credentialService;
    DeviceRegistryService deviceRegistryService;


    public KapuaCredentialsService() {
        JAXBContextProvider brokerProvider = new BrokerJAXBContextProvider();
        XmlUtil.setContextProvider(brokerProvider);
        userService = locator.getService(UserService.class);
        accountService = locator.getService(AccountService.class);
        credentialService = locator.getService(CredentialService.class);
        deviceRegistryService = locator.getService(DeviceRegistryService.class);
    }

    public void setConfig(Object configuration) {

    }

    @Override
    public void get(String tenantId, String type, String authId, JsonObject clientContext, Handler<AsyncResult<CredentialsResult<JsonObject>>> resultHandler) {
        CredentialsObject result = null;

        try {
            User user = KapuaSecurityUtils.doPrivileged(() -> userService.findByName(authId));
            if (user == null) {
                throw KapuaException.internalError("No user found for " + authId);
            }

            Account account = KapuaSecurityUtils.doPrivileged(() -> accountService.find(user.getScopeId()));
            if (account == null) {
                throw KapuaException.internalError("No account found for " + authId);
            }

            String clientId = clientContext.getString("clientId");
            if (clientId == null) {
                clientId = authId;
            }

            CredentialListResult credentials = KapuaSecurityUtils.doPrivileged(() -> credentialService.findByUserId(user.getScopeId(), user.getId()));
            Credential credential = credentials.getFirstItem();

            result = new CredentialsObject(clientId, authId, CredentialsConstants.SECRETS_TYPE_HASHED_PASSWORD);
            //TODO handle secret expiry
            final JsonObject secret = CredentialsObject.emptySecret(null, null);
            secret.put(CredentialsConstants.FIELD_SECRETS_KEY, credential.getCredentialKey());
            result = result.addSecret(secret);

        } catch (KapuaException ke) {
            //TODO add logging
            resultHandler.handle(Future.succeededFuture(CredentialsResult.from(HttpURLConnection.HTTP_INTERNAL_ERROR)));
            return;
        }

        if (result == null) {
            resultHandler.handle(Future.succeededFuture(CredentialsResult.from(HttpURLConnection.HTTP_NOT_FOUND)));
        } else {
            resultHandler.handle(Future.succeededFuture(
                    CredentialsResult.from(HttpURLConnection.HTTP_OK, JsonObject.mapFrom(result), CacheDirective.noCacheDirective())));
        }
    }

    @Override
    public void getAll(String s, String s1, Handler<AsyncResult<CredentialsResult<JsonObject>>> handler) {
        //TODO implement
    }

}
