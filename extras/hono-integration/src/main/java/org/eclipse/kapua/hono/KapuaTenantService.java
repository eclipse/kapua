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
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import org.eclipse.hono.service.tenant.BaseTenantService;
import org.eclipse.hono.util.TenantObject;
import org.eclipse.hono.util.TenantResult;
import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.broker.core.BrokerJAXBContextProvider;
import org.eclipse.kapua.commons.security.KapuaSecurityUtils;
import org.eclipse.kapua.commons.util.xml.JAXBContextProvider;
import org.eclipse.kapua.commons.util.xml.XmlUtil;
import org.eclipse.kapua.locator.KapuaLocator;
import org.eclipse.kapua.service.account.Account;
import org.eclipse.kapua.service.account.AccountService;

import javax.security.auth.x500.X500Principal;
import java.net.HttpURLConnection;
import java.security.PublicKey;
import java.util.Properties;

public class KapuaTenantService extends BaseTenantService<Object> {

    private final KapuaLocator locator = KapuaLocator.getInstance();

    AccountService accountService;

    public KapuaTenantService(){
        JAXBContextProvider brokerProvider = new BrokerJAXBContextProvider();
        XmlUtil.setContextProvider(brokerProvider);
        accountService = locator.getService(AccountService.class);
    }

    @Override
    public void setConfig(Object configuration) {

    }


    @Override
    public void get(String tenantId, Handler<AsyncResult<TenantResult<JsonObject>>> resultHandler) {

        System.out.println("Getting tenant !");
        try {
            Account account = KapuaSecurityUtils.doPrivileged(() -> accountService.findByName(tenantId));
            if (account == null) {
                resultHandler.handle(Future.succeededFuture(TenantResult.from(HttpURLConnection.HTTP_NOT_FOUND)));
                return;
            }

            TenantObject tenant = new TenantObject();
            tenant.setTenantId(tenantId);
            Properties accountProps = account.getEntityProperties();
            if (accountProps.containsKey("adapters")){
                tenant.setAdapterConfigurations(new JsonArray()
                        .add(accountProps.getProperty("adapters")));
            }
            if (accountProps.containsKey("trusted-ca")){
                tenant.setTrustAnchor(
                        (new JsonObject(accountProps.getProperty("trusted-ca"))).getString("public-key"),
                        (new JsonObject(accountProps.getProperty("trusted-ca"))).getString("subject-dn"));
            }
            tenant.setEnabled(true);
            System.out.println("returning tenant : "+tenant);
            resultHandler.handle(Future.succeededFuture(TenantResult.from(HttpURLConnection.HTTP_OK,
                    JsonObject.mapFrom(tenant),
                    null)));

        } catch (KapuaException ke) {
            resultHandler.handle(Future.succeededFuture(TenantResult.from(HttpURLConnection.HTTP_INTERNAL_ERROR)));
            return;
        }

    }

   /* @Override
    public void get(X500Principal subjectDn, Handler<AsyncResult<TenantResult<JsonObject>>> resultHandler) {
        try {
            AccountListResult accountList = KapuaSecurityUtils.doPrivileged(() -> accountService.query(
                    new AccountQuery(){

                        @Override

                    }
                    )
            )
            if (accountList.isEmpty()) {
                resultHandler.handle(Future.succeededFuture(TenantResult.from(HttpURLConnection.HTTP_NOT_FOUND)));
                return;
            }

            TenantObject tenant = new TenantObject();
            tenant.setTenantId(tenantId);
            //tenant.setAdapterConfigurations(account.getEntityProperties().)
            tenant.setEnabled(true);
            System.out.println("returning tenant : "+tenant);
            resultHandler.handle(Future.succeededFuture(TenantResult.from(HttpURLConnection.HTTP_OK,
                    JsonObject.mapFrom(tenant),
                    null)));

        } catch (KapuaException ke) {
            resultHandler.handle(Future.succeededFuture(TenantResult.from(HttpURLConnection.HTTP_INTERNAL_ERROR)));
            return;
        }

    }
    */
}
