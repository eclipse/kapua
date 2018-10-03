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
import org.eclipse.hono.util.TenantConstants;
import org.eclipse.hono.util.TenantObject;
import org.eclipse.hono.util.TenantResult;
import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.broker.core.BrokerJAXBContextProvider;
import org.eclipse.kapua.commons.model.id.KapuaEid;
import org.eclipse.kapua.commons.security.KapuaSecurityUtils;
import org.eclipse.kapua.commons.util.xml.JAXBContextProvider;
import org.eclipse.kapua.commons.util.xml.XmlUtil;
import org.eclipse.kapua.locator.KapuaLocator;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.model.query.KapuaListResult;
import org.eclipse.kapua.service.account.Account;
import org.eclipse.kapua.service.account.AccountQuery;
import org.eclipse.kapua.service.account.AccountService;
import org.eclipse.kapua.service.account.internal.AccountQueryImpl;

import javax.security.auth.x500.X500Principal;
import java.math.BigInteger;
import java.net.HttpURLConnection;
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

        log.debug("Getting tenant:"+tenantId);
        try {
            Account account = KapuaSecurityUtils.doPrivileged(() -> accountService.findByName(tenantId));
            if (account == null) {
                resultHandler.handle(Future.succeededFuture(TenantResult.from(HttpURLConnection.HTTP_NOT_FOUND)));
                return;
            }

            TenantObject tenant = new TenantObject();
            tenant.setTenantId(tenantId);

            Properties accountProps = account.getEntityAttributes();
            if (accountProps.containsKey(TenantConstants.FIELD_ADAPTERS)){
                tenant.setAdapterConfigurations(new JsonArray()
                        .add(accountProps.getProperty(TenantConstants.FIELD_ADAPTERS)));
            }
            tenant.setEnabled(true);
            log.debug("returning tenant : "+account.getName());
            resultHandler.handle(Future.succeededFuture(TenantResult.from(HttpURLConnection.HTTP_OK,
                    JsonObject.mapFrom(tenant),
                    null)));

        } catch (KapuaException ke) {
            resultHandler.handle(Future.succeededFuture(TenantResult.from(HttpURLConnection.HTTP_INTERNAL_ERROR)));
            return;
        }

    }

    @Override
    public void get(X500Principal subjectDn, Handler<AsyncResult<TenantResult<JsonObject>>> resultHandler) {

        // prepare the kapua query
       KapuaId rootScopeId = new KapuaEid(BigInteger.ONE);
       AccountQuery query = new AccountQueryImpl(rootScopeId);
       try {
           KapuaListResult<Account> accountList = KapuaSecurityUtils.doPrivileged(() -> accountService.query(query));
            if (accountList.isEmpty()) {
                resultHandler.handle(Future.succeededFuture(TenantResult.from(HttpURLConnection.HTTP_NOT_FOUND)));
                return;
            }

           TenantObject tenant = new TenantObject();
           for(Account acc : accountList.getItems()) {

               Properties accountProps = acc.getEntityAttributes();
               if (accountProps.containsKey(TenantConstants.FIELD_PAYLOAD_TRUSTED_CA)) {

                   X500Principal accSubjectDn = new X500Principal(new JsonObject(accountProps
                           .getProperty(TenantConstants.FIELD_PAYLOAD_TRUSTED_CA))
                           .getString(TenantConstants.FIELD_PAYLOAD_SUBJECT_DN));
                   if (accSubjectDn.equals(subjectDn)) {
                       tenant.setProperty(TenantConstants.FIELD_PAYLOAD_TRUSTED_CA
                               ,new JsonObject(accountProps.getProperty(TenantConstants.FIELD_PAYLOAD_TRUSTED_CA)));
                       tenant.setTenantId(acc.getName());
                       tenant.setEnabled(true);

                       log.debug("returning tenant : "+tenant.getTenantId());
                       resultHandler.handle(Future.succeededFuture(TenantResult.from(HttpURLConnection.HTTP_OK,
                               JsonObject.mapFrom(tenant),
                               null)));
                       return;
                   }
               }
           }
           resultHandler.handle(Future.succeededFuture(TenantResult.from(HttpURLConnection.HTTP_NOT_FOUND)));
           return;
        } catch (KapuaException ke) {
            resultHandler.handle(Future.succeededFuture(TenantResult.from(HttpURLConnection.HTTP_INTERNAL_ERROR)));
            return;
        }

    }

}
