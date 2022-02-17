/*******************************************************************************
 * Copyright (c) 2016, 2022 Eurotech and/or its affiliates and others
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *     Red Hat Inc
 *******************************************************************************/
package org.eclipse.kapua.commons.security;

import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.commons.util.ThrowingRunnable;
import org.eclipse.kapua.model.id.KapuaId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Callable;

/**
 * Security utilities to handle the {@link KapuaSession}.
 *
 * @since 1.0.0
 */
public class KapuaSecurityUtils {

    private static final Logger LOG = LoggerFactory.getLogger(KapuaSecurityUtils.class);

    private static final ThreadLocal<KapuaSession> THREAD_SESSION = new ThreadLocal<>();

    private KapuaSecurityUtils() {
    }

    /**
     * Returns the {@link KapuaSession} associated to the current {@link ThreadLocal}.
     *
     * @return The {@link KapuaSession} associated to the current {@link ThreadLocal}.
     * @since 1.0.0
     */
    public static KapuaSession getSession() {
        return THREAD_SESSION.get();
    }

    /**
     * Bounds the {@link KapuaSession} to the current {@link ThreadLocal}.
     *
     * @param session The {@link KapuaSession} to the current {@link ThreadLocal}.
     * @since 1.0.0
     */
    public static void setSession(KapuaSession session) {
        THREAD_SESSION.set(session);
    }

    /**
     * Clears the {@link KapuaSession} from the current {@link ThreadLocal}.
     *
     * @since 1.0.0
     */
    public static void clearSession() {
        THREAD_SESSION.remove();
    }

    /**
     * Executes the {@link Runnable} in a privileged context.
     * <p>
     * Trusted mode means that checks for permissions and role will be skipped.
     *
     * @param runnable The {@link ThrowingRunnable} action to be executed.
     * @throws KapuaException
     * @since 1.0.0
     */
    public static void doPrivileged(final ThrowingRunnable runnable) throws KapuaException {
        doPrivileged((Callable<Void>) () -> {
            runnable.run();
            return null;
        });
    }

    /**
     * Execute the {@link Callable} in a privileged context.<br>
     * Trusted mode means that checks for permissions and role will pass.
     *
     * @param privilegedAction The {@link Callable} action to be executed.
     * @return The result of the {@link Callable} action.
     * @throws KapuaException
     * @since 1.0.0
     */
    public static <T> T doPrivileged(Callable<T> privilegedAction) throws KapuaException {
        // get (and keep) the current session
        KapuaSession previousSession = getSession();

        KapuaSession currentSession;
        if (previousSession == null) {
            currentSession = new KapuaSession(null, KapuaId.ONE, KapuaId.ONE);
            currentSession.setTrustedMode(true);
            LOG.debug("Created a new KapuaSession as ScopeId: {} - UserId: {} - Trusted: {} - Token: {}",
                    currentSession.getScopeId(),
                    currentSession.getUserId(),
                    currentSession.isTrustedMode(),
                    currentSession.getAccessToken() != null ? currentSession.getAccessToken().getTokenId() : null);
        } else {
            currentSession = KapuaSession.createFrom();
            LOG.debug("Cloning KapuaSession as ScopeId: {} - UserId: {} - Trusted: {} - Token: {}",
                    currentSession.getScopeId(),
                    currentSession.getUserId(),
                    currentSession.isTrustedMode(),
                    currentSession.getAccessToken() != null ? currentSession.getAccessToken().getTokenId() : null);
        }
        setSession(currentSession);

        try {
            return privilegedAction.call();
        } catch (KapuaException ke) {
            throw ke;
        } catch (Exception e) {
            throw KapuaException.internalError(e);
        } finally {
            // Restore the original session.
            setSession(previousSession);
        }
    }

}
