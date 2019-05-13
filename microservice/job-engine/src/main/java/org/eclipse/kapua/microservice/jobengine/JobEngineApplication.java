/*******************************************************************************
 * Copyright (c) 2019 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.microservice.jobengine;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Collection;

import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.service.authentication.shiro.KapuaAuthenticator;
import org.eclipse.kapua.service.authentication.shiro.realm.AccessTokenAuthenticatingRealm;
import org.eclipse.kapua.service.authentication.shiro.realm.ApiKeyAuthenticatingRealm;
import org.eclipse.kapua.service.authentication.shiro.realm.JwtAuthenticatingRealm;
import org.eclipse.kapua.service.authentication.shiro.realm.UserPassAuthenticatingRealm;
import org.eclipse.kapua.service.authorization.shiro.KapuaAuthorizingRealm;

import io.vertx.core.Vertx;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.mgt.DefaultSecurityManager;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.session.mgt.AbstractSessionManager;
import org.apache.shiro.session.mgt.AbstractValidatingSessionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan("org.eclipse.kapua.microservice")
public class JobEngineApplication {

    private static ApplicationContext applicationContext;

    @Autowired
    private JobEngineHttpServerVerticle jobEngineHttpServerVerticle;

    public static void main(String[] args) {
        applicationContext = SpringApplication.run(JobEngineApplication.class);
    }

    @PostConstruct
    public void init() {
        try {
            initShiro();
        } catch (KapuaException ex) {
            SpringApplication.exit(applicationContext, () -> -1);
        }
        Vertx vertx = Vertx.vertx();
        vertx.deployVerticle(jobEngineHttpServerVerticle);
    }

    private void initShiro() throws KapuaException {
        // Shiro
        // Make the SecurityManager instance available to the entire application:
        Collection<Realm> realms = new ArrayList<>();
        realms.add(new UserPassAuthenticatingRealm());
        realms.add(new ApiKeyAuthenticatingRealm());
        realms.add(new JwtAuthenticatingRealm());
        realms.add(new AccessTokenAuthenticatingRealm());
        realms.add(new KapuaAuthorizingRealm());

        DefaultSecurityManager defaultSecurityManager = new DefaultSecurityManager();
        defaultSecurityManager.setAuthenticator(new KapuaAuthenticator());
        defaultSecurityManager.setRealms(realms);

        SecurityUtils.setSecurityManager(defaultSecurityManager);

        if (defaultSecurityManager.getSessionManager() instanceof AbstractSessionManager) {
            ((AbstractSessionManager) defaultSecurityManager.getSessionManager()).setGlobalSessionTimeout(-1);
//            LOG.info("Shiro global session timeout set to indefinite.");
        } else {
//            LOG.warn("Cannot set Shiro global session timeout to indefinite.");
        }

        if (defaultSecurityManager.getSessionManager() instanceof AbstractValidatingSessionManager) {
            ((AbstractValidatingSessionManager) defaultSecurityManager.getSessionManager()).setSessionValidationSchedulerEnabled(false);
//            LOG.info("Shiro global session validator scheduler disabled.");
        } else {
//            LOG.warn("Cannot disable Shiro session validator scheduler.");
        }
    }

}
