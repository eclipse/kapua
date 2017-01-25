/*******************************************************************************
 * Copyright (c) 2011, 2016 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *
 *******************************************************************************/
package org.eclipse.kapua.commons.security;

import java.io.Serializable;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.authentication.token.AccessToken;
import org.eclipse.kapua.service.authorization.subject.Subject;

/**
 * Kapua session
 * 
 * @since 1.0
 */
public class KapuaSession implements Serializable {

    private static final long serialVersionUID = -3831904230950408142L;

    public final static String KAPUA_SESSION_KEY = "KapuaSession";

    private static List<String> trustedClasses = new ArrayList<String>();
    private static final String TRUST_CLASS_METHOD_PATTERN = "{0}.{1}";

    // TODO to be moved inside configuration service or something like that "fully.qualified.classname.methodname" (<init> for the constructor)
    static {
        trustedClasses.add("org.eclipse.kapua.broker.core.plugin.KapuaSecurityContext.<init>");
    }

    /**
     * Access token that identify the logged in session.
     */
    private AccessToken accessToken;

    /**
     * User scope identifier
     */
    private KapuaId scopeId;

    /**
     * Subject identifier
     */
    private Subject subject;

    /**
     * Trusted mode.<br>
     * If true every rights check will be skipped, in other word <b>the user is trusted so he is allowed to execute every operation</b> defined in the system.
     */
    private boolean trustedMode = false;

    /**
     * Default constructor
     */
    public KapuaSession() {
        super();
    }

    /**
     * Creates a {@link KapuaSession} copy with trusted mode flag set to true (to be used only from trusted classes)
     * 
     * @return
     */
    public static KapuaSession createFrom() {
        if (isCallerClassTrusted()) {
            KapuaSession kapuaSession = KapuaSecurityUtils.getSession();
            KapuaSession kapuaSessionCopy = new KapuaSession(kapuaSession.getAccessToken(),
                    kapuaSession.getScopeId(),
                    kapuaSession.getSubject());
            kapuaSessionCopy.trustedMode = true;
            return kapuaSessionCopy;
        } else {
            // TODO to be replaced with a security exception
            throw new RuntimeException("Method not allowed for the caller class");
        }
    }

    /**
     * Check if the caller is included in the caller list allowed to change the trusted mode flag.
     * 
     * @return
     */
    private final static boolean isCallerClassTrusted() {
        // the stack trace should be like
        // 0 ---> Thread
        // 1 ---> KapuaSession -> isCallerClassTrusted()
        // 2 ---> KapuaSession -> createFrom()
        // 3 ---> "outside" caller class that should be checked
        StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();
        if (stackTraceElements != null && stackTraceElements.length > 4) {
            return trustedClasses.contains(MessageFormat.format(TRUST_CLASS_METHOD_PATTERN, stackTraceElements[3].getClassName(), stackTraceElements[3].getMethodName()));
        } else {
            return false;
        }
    }

    /**
     * Constructs a {@link KapuaSession} with given parameters
     * 
     * @param accessToken
     * @param scopeId
     * @param subject
     */
    public KapuaSession(AccessToken accessToken,
            KapuaId scopeId,
            Subject subject) {
        this();
        this.accessToken = accessToken;
        this.scopeId = scopeId;
        this.subject = subject;
    }

    /**
     * Get the access token
     * 
     * @return
     */
    public AccessToken getAccessToken() {
        return accessToken;
    }

    /**
     * Get the scope identifier
     * 
     * @return
     */
    public KapuaId getScopeId() {
        return scopeId;
    }

    /**
     * Get the subject identifier
     * 
     * @return
     */
    public Subject getSubject() {
        return subject;
    }

    /**
     * Set the trusted mode status.<br>
     * If true every rights check will be skipped, in other word <b>the user is trusted so he is allowed to execute every operation</b> defined in the system.
     * 
     * @return
     */
    final void setTrustedMode(boolean trustedMode) {
        this.trustedMode = trustedMode;
    }

    /**
     * Return the trusted mode status.<br>
     * If true every rights check will be skipped, in other word <b>the user is trusted so he is allowed to execute every operation</b> defined in the system.
     * 
     * @return
     */
    public final boolean isTrustedMode() {
        return trustedMode;
    }
}
