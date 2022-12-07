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
 *******************************************************************************/
package org.eclipse.kapua.app.api.core.model;

import org.eclipse.kapua.app.api.core.exception.SessionNotPopulatedException;
import org.eclipse.kapua.app.api.core.settings.KapuaApiCoreSetting;
import org.eclipse.kapua.app.api.core.settings.KapuaApiCoreSettingKeys;
import org.eclipse.kapua.commons.security.KapuaSecurityUtils;
import org.eclipse.kapua.commons.security.KapuaSession;
import org.eclipse.kapua.model.id.KapuaId;

import javax.ws.rs.PathParam;
import java.math.BigInteger;
import java.util.Base64;

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

    private static final String SCOPE_ID_WILDCARD = KapuaApiCoreSetting.getInstance().getString(KapuaApiCoreSettingKeys.API_PATH_PARAM_SCOPEID_WILDCARD);

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

            if (session == null) {
                throw new SessionNotPopulatedException();
            }

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
        return KapuaId.toString(this);
    }

    @Override
    public int hashCode() {
        return KapuaId.hashCode(this);
    }

    @Override
    public boolean equals(Object obj) {
        return KapuaId.areEquals(this, obj);
    }
}
