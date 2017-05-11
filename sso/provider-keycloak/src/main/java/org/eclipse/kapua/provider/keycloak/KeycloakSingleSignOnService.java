/*******************************************************************************
 * Copyright (c) 2017 Red Hat Inc and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Red Hat Inc - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.provider.keycloak;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.json.Json;
import javax.json.JsonObject;

import org.apache.http.HttpHeaders;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.message.BasicNameValuePair;
import org.eclipse.kapua.provider.keycloak.setting.KeycloakSsoSetting;
import org.eclipse.kapua.provider.keycloak.setting.KeycloakSsoSettingKeys;
import org.eclipse.kapua.sso.provider.AbstractSingleSignOnService;
import org.eclipse.kapua.sso.provider.setting.SsoSetting;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class KeycloakSingleSignOnService extends AbstractSingleSignOnService {

    private final static Logger logger = LoggerFactory.getLogger(KeycloakSingleSignOnService.class);

    private KeycloakSsoSetting keycloakSettings;

    public KeycloakSingleSignOnService() {
        this(SsoSetting.getInstance(), KeycloakSsoSetting.getInstance());
    }

    public KeycloakSingleSignOnService(final SsoSetting ssoSettings, final KeycloakSsoSetting keycloakSettings) {
        super(ssoSettings);
        this.keycloakSettings = keycloakSettings;
    }

    protected String getRealm() {
        return keycloakSettings.getString(KeycloakSsoSettingKeys.KEYCLOAK_REALM, "master");
    }

    private String getAuthUri() {
        return keycloakSettings.getString(KeycloakSsoSettingKeys.KEYCLOAK_URI) + "/auth/realms/" + getRealm() + "/protocol/openid-connect/auth";
    }

    private String getTokenUri() {
        return keycloakSettings.getString(KeycloakSsoSettingKeys.KEYCLOAK_URI) + "/auth/realms/" + getRealm() + "/protocol/openid-connect/token";
    }

    @Override
    public String getLoginUri(final String state) {
        try {
            final URIBuilder uri = new URIBuilder(getAuthUri());

            uri.addParameter("scope", "openid");
            uri.addParameter("response_type", "code");
            uri.addParameter("client_id", keycloakSettings.getString(KeycloakSsoSettingKeys.KEYCLOAK_CLIENT_ID));
            uri.addParameter("state", state);
            uri.addParameter("redirect_uri", getRedirectUri());

            return uri.toString();
        } catch (Exception e) {
            logger.warn("Failed to construct SSO URI", e);
        }
        return null;
    }

    @Override
    public JsonObject getAccessToken(final String authCode) throws IOException {
        // FIXME: switch to HttpClient implementation: better performance and connection caching

        final URL url = new URL(getTokenUri());
        final HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

        urlConnection.setRequestMethod("POST");
        urlConnection.setRequestProperty(HttpHeaders.CONTENT_TYPE, URLEncodedUtils.CONTENT_TYPE);

        final List<NameValuePair> parameters = new ArrayList<NameValuePair>();
        parameters.add(new BasicNameValuePair("grant_type", "authorization_code"));
        parameters.add(new BasicNameValuePair("code", authCode));
        parameters.add(new BasicNameValuePair("client_id", keycloakSettings.getString(KeycloakSsoSettingKeys.KEYCLOAK_CLIENT_ID)));

        final String clientSecret = keycloakSettings.getString(KeycloakSsoSettingKeys.KEYCLOAK_CLIENT_SECRET);
        if (clientSecret != null && !clientSecret.isEmpty()) {
            parameters.add(new BasicNameValuePair("client_secret", clientSecret));
        }

        parameters.add(new BasicNameValuePair("redirect_uri", getRedirectUri()));
        final UrlEncodedFormEntity entity = new UrlEncodedFormEntity(parameters);

        // Send post request

        urlConnection.setDoOutput(true);

        try (final OutputStream outputStream = urlConnection.getOutputStream()) {
            entity.writeTo(outputStream);
        }

        // parse result

        final JsonObject jsonObject;
        try (final InputStream stream = urlConnection.getInputStream()) {
            jsonObject = Json.createReader(stream).readObject();
        }
        return jsonObject;
    }

}
