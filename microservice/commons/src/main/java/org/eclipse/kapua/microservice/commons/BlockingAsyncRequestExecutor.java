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

import java.util.concurrent.Callable;

import org.eclipse.kapua.commons.security.KapuaSecurityUtils;
import org.eclipse.kapua.commons.security.KapuaSession;
import org.eclipse.kapua.commons.util.ThrowingRunnable;

import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.ThreadContext;

public class BlockingAsyncRequestExecutor {

    private BlockingAsyncRequestExecutor() { }

    public static <T> void executeAsyncRequest(Vertx vertx, KapuaSession kapuaSession, Subject shiroSubject, ThrowingRunnable throwingRunnable, Handler<AsyncResult<T>> resultHandler) {
        executeAsyncRequest(vertx, kapuaSession, shiroSubject, () -> {
            throwingRunnable.run();
            return null;
        }, resultHandler);
    }

    public static <T> void executeAsyncRequest(Vertx vertx, KapuaSession kapuaSession, Subject shiroSubject, Callable<T> callable, Handler<AsyncResult<T>> resultHandler) {
        vertx.executeBlocking(blockingFuture -> {
            KapuaSecurityUtils.setSession(kapuaSession);
            ThreadContext.bind(shiroSubject);
            ThreadContext.bind(SecurityUtils.getSecurityManager());
            try {
                blockingFuture.complete(callable.call());
            } catch (Exception ex) {
                blockingFuture.fail(ex);
            }
            KapuaSecurityUtils.clearSession();
            ThreadContext.unbindSubject();
            ThreadContext.unbindSecurityManager();
        }, resultHandler);
    }

}
