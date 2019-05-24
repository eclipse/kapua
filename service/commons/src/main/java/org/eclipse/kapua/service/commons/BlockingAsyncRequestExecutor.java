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
package org.eclipse.kapua.service.commons;

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

/**
 * Factory class to encapsulate one or more statements in a Vert.x blocking call, where the {@link KapuaSession},
 * the Shiro {@link Subject} and the Shiro {@link SecurityManager} are correctly set in the executing thread before the
 * call and cleaned after.
 */
public class BlockingAsyncRequestExecutor {

    private BlockingAsyncRequestExecutor() { }

    /**
     * Factory method to execute an arbitrary piece of code that returns null. Due to the nature of {@link Void}, the
     * code must be wrapped in a {@link ThrowingRunnable} instead of a {@link Callable}
     * @param vertx             The Vert.x instance
     * @param kapuaSession      The {@link KapuaSession}
     * @param shiroSubject      The Shiro {@link Subject}
     * @param throwingRunnable  The {@link ThrowingRunnable} that will wrap the statement to be executed
     * @param resultHandler     The {@link Handler&lt;AsyncResult&lt;T&gt;&gt;} to be executed once the blocking code is complete
     * @param <T>               The type contained in the {@link AsyncResult}
     */
    public static <T> void executeAsyncRequest(Vertx vertx, KapuaSession kapuaSession, Subject shiroSubject, ThrowingRunnable throwingRunnable, Handler<AsyncResult<T>> resultHandler) {
        executeAsyncRequest(vertx, kapuaSession, shiroSubject, () -> {
            throwingRunnable.run();
            return null;
        }, resultHandler);
    }

    /**
     * Factory method to execute an arbitrary piece of code that returns null. Due to the nature of {@link Void}, the
     * code must be wrapped in a {@link ThrowingRunnable} instead of a {@link Callable}
     * @param vertx             The Vert.x instance
     * @param kapuaSession      The {@link KapuaSession}
     * @param shiroSubject      The Shiro {@link Subject}
     * @param callable          The {@link Callable} that will wrap the statement to be executed
     * @param resultHandler     The {@link Handler&lt;AsyncResult&lt;T&gt;&gt;} to be executed once the blocking code is complete
     * @param <T>               The type both resulting from the call to the {@link Callable} and contained in the {@link AsyncResult}
     */
    public static <T> void executeAsyncRequest(Vertx vertx, KapuaSession kapuaSession, Subject shiroSubject, Callable<T> callable, Handler<AsyncResult<T>> resultHandler) {
        vertx.executeBlocking(blockingFuture -> {
            try {
                KapuaSecurityUtils.setSession(kapuaSession);
                ThreadContext.bind(shiroSubject);
                ThreadContext.bind(SecurityUtils.getSecurityManager());
                blockingFuture.complete(callable.call());
            } catch (Exception ex) {
                blockingFuture.fail(ex);
            } finally {
                KapuaSecurityUtils.clearSession();
                ThreadContext.unbindSubject();
                ThreadContext.unbindSecurityManager();
            }
        }, resultHandler);
    }

}
