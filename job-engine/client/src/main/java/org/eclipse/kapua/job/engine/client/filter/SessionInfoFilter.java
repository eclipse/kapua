/*******************************************************************************
 * Copyright (c) 2021 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.job.engine.client.filter;

import com.google.common.net.HttpHeaders;
import org.eclipse.kapua.commons.security.KapuaSecurityUtils;
import org.eclipse.kapua.commons.security.KapuaSession;
import org.eclipse.kapua.job.engine.client.settings.JobEngineClientSetting;
import org.eclipse.kapua.job.engine.client.settings.JobEngineClientSettingKeys;

import javax.ws.rs.client.ClientRequestContext;
import javax.ws.rs.client.ClientRequestFilter;
import java.io.IOException;

/**
 * {@link ClientRequestFilter} used to populate HTTP request headers with information about the current {@link KapuaSession}.
 *
 * @since 1.5.0
 */
public class SessionInfoFilter implements ClientRequestFilter {

    @Override
    public void filter(ClientRequestContext requestContext) throws IOException {
        KapuaSession kapuaSession = KapuaSecurityUtils.getSession();
        String authMode = JobEngineClientSetting.getInstance().getString(JobEngineClientSettingKeys.JOB_ENGINE_CLIENT_AUTH_MODE, "access_token");
        switch (authMode) {
            case "trusted":
                requestContext.getHeaders().putSingle(SessionInfoHttpHeaders.SCOPE_ID_HTTP_HEADER, kapuaSession.getScopeId().toCompactId());
                requestContext.getHeaders().putSingle(SessionInfoHttpHeaders.USER_ID_HTTP_HEADER, kapuaSession.getUserId().toCompactId());
                break;
            case "access_token":
            default:
                requestContext.getHeaders().putSingle(HttpHeaders.AUTHORIZATION, String.format("Bearer %s", kapuaSession.getAccessToken().getTokenId()));
        }
        requestContext.getHeaders().putSingle(SessionInfoHttpHeaders.AUTH_MODE, authMode);
    }

}
