/*******************************************************************************
 * Copyright (c) 2011, 2017 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *     Red Hat Inc
 *******************************************************************************/
package org.eclipse.kapua.commons.security;

import java.util.concurrent.Callable;

import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.commons.util.ThrowingRunnable;
import org.eclipse.kapua.model.id.KapuaId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Kapua security utility to handle the bind/unbind operation of the Kapua session into the thread context.
 *
 * @since 1.0
 */
public class KapuaSecurityUtils {

    private static final Logger logger = LoggerFactory.getLogger(KapuaSecurityUtils.class);

    public static final String MDC_USER_ID = "userId";

    private static final ThreadLocal<KapuaSession> THREAD_SESSION = new ThreadLocal<>();

    private KapuaSecurityUtils() {
    }

    /**
     * Return the {@link KapuaSession} associated to the current thread session.
     *
     * @return
     */
    public static KapuaSession getSession() {
        return THREAD_SESSION.get();
    }

    /**
     * Bound the {@link KapuaSession} to the current thread session.
     *
     * @param session
     */
    public static void setSession(KapuaSession session) {
        THREAD_SESSION.set(session);
    }

    /**
     * Clear the {@link KapuaSession} from the current thread session.
     */
    public static void clearSession() {
        THREAD_SESSION.remove();
    }

    /**
     * Execute the {@link Runnable} in a privileged context.<br>
     * Trusted mode means that checks for permissions and role will pass.
     *
     * @param runnable
     *            The {@link ThrowingRunnable} action to be executed.
     * @throws KapuaException
     */
    public static void doPrivileged(final ThrowingRunnable runnable) throws KapuaException {
        doPrivileged(new Callable<Void>() {

            @Override
            public Void call() throws Exception {
                runnable.run();
                return null;
            }

        });
    }

    /**
     * Execute the {@link Callable} in a privileged context.<br>
     * Trusted mode means that checks for permissions and role will pass.
     *
     * @param privilegedAction
     *            The {@link Callable} action to be executed.
     * @return The result of the {@link Callable} action.
     * @throws KapuaException
     * @since 1.0.0
     */
    public static <T> T doPrivileged(Callable<T> privilegedAction) throws KapuaException {
        // get (and keep) the current session
        KapuaSession previousSession = getSession();
        KapuaSession currentSession = null;

        if (previousSession == null) {
            logger.debug("==> create new session");
            currentSession = new KapuaSession(null, KapuaId.ONE, KapuaId.ONE);
            currentSession.setTrustedMode(true);
        } else {
            logger.debug("==> clone from previous session");
            currentSession = KapuaSession.createFrom();
        }
        setSession(currentSession);

        try {
            return privilegedAction.call();
        } catch (KapuaException ke) {
            throw ke;
        } catch (Exception e) {
            throw KapuaException.internalError(e);
        } finally {
            // restore the original session
            setSession(previousSession);
        }
    }

}
