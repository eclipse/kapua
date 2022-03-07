/*******************************************************************************
 * Copyright (c) 2021, 2022 Eurotech and/or its affiliates and others
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.broker.artemis.plugin.security.context;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.security.auth.Subject;

import org.eclipse.kapua.KapuaIllegalArgumentException;
import org.eclipse.kapua.client.security.bean.AuthResponse;
import org.eclipse.kapua.client.security.context.SessionContext;
import org.eclipse.kapua.client.security.context.SessionContextContainer;
import org.eclipse.kapua.client.security.context.Utils;
import org.eclipse.kapua.service.authentication.KapuaPrincipal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class should be a singleton.
 * TODO move this under DI. Ask for a way to do so in Artemis (is still Artemis managed by Spring as ActiveMQ 5?)
 *
 */
public final class SecurityContextHandler {

    protected static Logger logger = LoggerFactory.getLogger(SecurityContextHandler.class);

    private static final SecurityContextHandler INSTANCE = new SecurityContextHandler();

    //use string as key since some method returns DefaultChannelId as connection id, some other a string
    //the string returned by some method as connection id is the asShortText of DefaultChannelId
    private final Map<String, SessionContext> sessionContextMapByClient = new ConcurrentHashMap<>();
    private final Map<String, SessionContext> sessionContextMapByConnection = new ConcurrentHashMap<>();
    private final Map<String, Acl> aclMap = new ConcurrentHashMap<>();
    //for performance reason we can remove this map since it's used only as match with the principal contained in acls. But since they are managed internally, they are the same
    private final Map<String, KapuaPrincipal> principalMap = new ConcurrentHashMap<>();

    private SecurityContextHandler() {
    }

    public static SecurityContextHandler getInstance() {
        return INSTANCE;
    }

    public KapuaPrincipal getPrincipal(String connectionId) {
        return principalMap.get(connectionId);
    }

    public void setSessionContext(SessionContext sessionContext) {
        principalMap.put(sessionContext.getConnectionId(), sessionContext.getPrincipal());
        sessionContextMapByConnection.put(sessionContext.getConnectionId(), sessionContext);
    }

    public void setSessionContext(SessionContext sessionContext, AuthResponse authResponse) throws KapuaIllegalArgumentException {
        setSessionContext(sessionContext);
        aclMap.put(sessionContext.getConnectionId(), new Acl(sessionContext.getPrincipal(), authResponse));
        sessionContextMapByClient.put(Utils.getFullClientId(sessionContext), sessionContext);
    }

    private SessionContext cleanSessionContext(SessionContext sessionContext) {
        principalMap.remove(sessionContext.getConnectionId());
        sessionContextMapByConnection.remove(sessionContext.getConnectionId());
        if (!sessionContext.isInternal()) {
            aclMap.remove(sessionContext.getConnectionId());
            //check for stealing link
            //remove object only if connection ids match
            String fullClientId = Utils.getFullClientId(sessionContext);
            SessionContext oldSessionContext = sessionContextMapByClient.get(fullClientId);
            if (oldSessionContext!=null && oldSessionContext.getConnectionId().equals(sessionContext.getConnectionId())) {
                return sessionContextMapByClient.remove(fullClientId);
            }
        }
        return null;
    }

    public SessionContext getSessionContextByClientId(String fullClientId) {
        return sessionContextMapByClient.get(fullClientId);
    }

    public SessionContextContainer getAndCleanSessionContextByConnectionId(String connectionId) {
        SessionContext sessionContext = sessionContextMapByConnection.get(connectionId);
        //oldSessionContext should be not null since it was updated by the new connection
        //so if the current connection id and that from sessionContet get by clientId are equals -> no stealing link
        SessionContext sessionContextOld = sessionContext!=null ? cleanSessionContext(sessionContext) : null;
        return new SessionContextContainer(sessionContext, sessionContextOld);
    }

    public SessionContext getSessionContextByConnectionId(String connectionId) {
        return sessionContextMapByConnection.get(connectionId);
    }

    public boolean checkPublisherAllowed(SessionContext sessionContext, String address) {
        KapuaPrincipal principal = principalMap.get(sessionContext.getConnectionId());
        Acl acl = aclMap.get(sessionContext.getConnectionId());
        if (acl==null || !acl.canWrite(principal, address)) {
            return false;
//            throw new SecurityException("User " + principal.getName() + " not allowed to publish to " + address);
        }
        else {
            return true;
        }
    }

    public boolean checkConsumerAllowed(SessionContext sessionContext, String address) {
        KapuaPrincipal principal = principalMap.get(sessionContext.getConnectionId());
        Acl acl = aclMap.get(sessionContext.getConnectionId());
        if (acl==null || !acl.canRead(principal, address)) {
            return false;
//            throw new SecurityException("User " + principal.getName() + " not allowed to consume from " + address);
        }
        else {
            return true;
        }
    }

    public boolean checkAdminAllowed(SessionContext sessionContext, String address) {
        KapuaPrincipal principal = principalMap.get(sessionContext.getConnectionId());
        Acl acl = aclMap.get(sessionContext.getConnectionId());
        if (acl==null || !acl.canManage(principal, address)) {
            return false;
//            throw new SecurityException("User " + principal.getName() + " not allowed to consume from " + address);
        }
        else {
            return true;
        }
    }

    public Subject buildFromPrincipal(KapuaPrincipal kapuaPrincipal) {
        Subject subject = new Subject();
        subject.getPrincipals().add(kapuaPrincipal);
        return subject;
    }

    public void printContent(String method, String connectionId) {
        StringBuilder builder = new StringBuilder();
        builder.append("\n============================").append(method).append(" - ").append(connectionId);
        builder.append("\nconnection info by client id\n");
        sessionContextMapByClient.forEach((key, sessionContext) -> builder.append("\tclientId: ").append(key).append(" - conId: ").append(sessionContext.getConnectionId()).append("\tinternal: ").append(sessionContext.isInternal()).append("\n"));
        builder.append("\nconnection info by connection id\n");
        sessionContextMapByConnection.forEach((key, sessionContext) -> builder.append("\tconId: ").append(key).append(" - clientId: ").append(sessionContext.getClientId()).append("\tinternal: ").append(sessionContext.isInternal()).append("\n"));
        builder.append("\nacl by connection id\n");
        aclMap.forEach((key, acl) -> builder.append("\tconnId: ").append(key).append("\n"));
        builder.append("\nprincipal by connection id\n");
        principalMap.forEach((key, principal) -> builder.append("\tconnId: ").append(key).append(" - name: ").append(principal.getName()).append(" - clientId: ").append(principal.getClientId()).append("\tinternal: ").append(principal.isInternal()).append("\n"));
        logger.info("{}", builder);
    }
}
