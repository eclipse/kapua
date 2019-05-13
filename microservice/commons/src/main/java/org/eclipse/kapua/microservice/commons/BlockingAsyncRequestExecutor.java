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
package org.eclipse.kapua.microservice.commons;

import org.eclipse.kapua.commons.security.KapuaSecurityUtils;
import org.eclipse.kapua.commons.security.KapuaSession;

import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.ThreadContext;
import org.springframework.stereotype.Service;

@Service
public class BlockingAsyncRequestExecutor {

    public <T> void executeAsyncRequest(Vertx vertx, KapuaSession kapuaSession, Subject shiroSubject, Handler<Future<T>> handler, Handler<AsyncResult<T>> resultFuture) {
        vertx.executeBlocking(blockingFuture -> {
            KapuaSecurityUtils.setSession(kapuaSession);
            ThreadContext.bind(shiroSubject);
            ThreadContext.bind(SecurityUtils.getSecurityManager());
            handler.handle(blockingFuture);
            KapuaSecurityUtils.clearSession();
            ThreadContext.unbindSubject();
            ThreadContext.unbindSecurityManager();
        }, resultFuture);
    }

}
