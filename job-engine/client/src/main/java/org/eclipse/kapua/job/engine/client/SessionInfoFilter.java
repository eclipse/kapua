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
package org.eclipse.kapua.job.engine.client;

import java.io.IOException;

import javax.ws.rs.client.ClientRequestContext;
import javax.ws.rs.client.ClientRequestFilter;

import org.eclipse.kapua.commons.security.KapuaSecurityUtils;
import org.eclipse.kapua.commons.security.KapuaSession;
import org.eclipse.kapua.job.engine.JobEngineHttpHeaders;
import org.eclipse.kapua.job.engine.client.settings.JobEngineClientSetting;
import org.eclipse.kapua.job.engine.client.settings.JobEngineClientSettingKeys;

import com.google.common.net.HttpHeaders;

public class SessionInfoFilter implements ClientRequestFilter {

    @Override
    public void filter(ClientRequestContext requestContext) throws IOException {
        KapuaSession kapuaSession = KapuaSecurityUtils.getSession();
        String authMode = JobEngineClientSetting.getInstance().getString(JobEngineClientSettingKeys.JOB_ENGINE_CLIENT_AUTH_MODE, "access_token");
        switch (authMode) {
            case "trusted":
                requestContext.getHeaders().putSingle(JobEngineHttpHeaders.SCOPE_ID_HTTP_HEADER, kapuaSession.getScopeId().toCompactId());
                requestContext.getHeaders().putSingle(JobEngineHttpHeaders.USER_ID_HTTP_HEADER, kapuaSession.getUserId().toCompactId());
                break;
            case "access_token":
            default:
                requestContext.getHeaders().putSingle(HttpHeaders.AUTHORIZATION, String.format("Bearer %s", kapuaSession.getAccessToken().getTokenId()));
        }
        requestContext.getHeaders().putSingle(JobEngineHttpHeaders.AUTH_MODE, authMode);
    }

}
