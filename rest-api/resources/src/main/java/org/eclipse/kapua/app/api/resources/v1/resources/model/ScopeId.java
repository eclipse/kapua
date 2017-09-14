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
 *******************************************************************************/
package org.eclipse.kapua.app.api.resources.v1.resources.model;

import java.math.BigInteger;
import java.util.Base64;

import javax.ws.rs.PathParam;

import org.eclipse.kapua.app.api.core.settings.KapuaApiSetting;
import org.eclipse.kapua.app.api.core.settings.KapuaApiSettingKeys;
import org.eclipse.kapua.commons.security.KapuaSecurityUtils;
import org.eclipse.kapua.commons.security.KapuaSession;
import org.eclipse.kapua.model.id.KapuaId;

/**
 * {@link KapuaId} implementation to be used on REST API to parse the {@link PathParam} scopeId.
 * <p>
 * If the {@link PathParam} is equals to "_" the scopeId used will be set to {@link KapuaSession#getScopeId()} of {@link KapuaSecurityUtils#getSession()},
 * which means that the scope of the current request will be the same of the current session scope.
 *
 * @since 1.0.0
 */
public class ScopeId implements KapuaId {

    private static final long serialVersionUID = 6893262093856905182L;

    private static final String SCOPE_ID_WILDCARD = KapuaApiSetting.getInstance().getString(KapuaApiSettingKeys.API_PATH_PARAM_SCOPEID_WILDCARD);

    private BigInteger id;

    /**
     * Builds the {@link KapuaId} from the given {@link String} compact scopeId.
     * If the given parameter equals to "_" the current session scope will be used.
     *
     * @param compactScopeId The compact scopeId to parse.
     * @since 1.0.0
     */
    public ScopeId(String compactScopeId) {

        if (SCOPE_ID_WILDCARD.equals(compactScopeId)) {
            KapuaSession session = KapuaSecurityUtils.getSession();
            setId(session.getScopeId().getId());
        } else {
            byte[] bytes = Base64.getUrlDecoder().decode(compactScopeId);
            setId(new BigInteger(bytes));
        }
    }

    @Override
    public BigInteger getId() {
        return id;
    }

    protected void setId(BigInteger id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return getId().toString();
    }
}
