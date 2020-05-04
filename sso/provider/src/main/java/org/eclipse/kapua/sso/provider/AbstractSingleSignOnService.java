/*******************************************************************************
 * Copyright (c) 2017, 2019 Red Hat Inc and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Red Hat Inc - initial API and implementation
 *     Eurotech
 *******************************************************************************/
package org.eclipse.kapua.sso.provider;

import org.apache.http.HttpHeaders;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.message.BasicNameValuePair;
import org.eclipse.kapua.sso.SingleSignOnService;
import org.eclipse.kapua.sso.exception.SsoJwtException;
import org.eclipse.kapua.sso.provider.setting.SsoSetting;
import org.eclipse.kapua.sso.provider.setting.SsoSettingKeys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.json.Json;
import javax.json.JsonObject;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * This class represents an abstract SingleSignOnService.
 * Each sso provider must provide its own SingleSignOnService concrete implementation by extending this class.
 */
public abstract class AbstractSingleSignOnService implements SingleSignOnService {


    private static final Logger logger = LoggerFactory.getLogger(AbstractSingleSignOnService.class);

    protected SsoSetting ssoSettings;

    /**
     * Abstract SingleSignOn constructor.
     *
     * @param ssoSettings the {@link SsoSetting} instance.
     */
    public AbstractSingleSignOnService(SsoSetting ssoSettings) {
        this.ssoSettings = ssoSettings;
    }

    /**
     * Get the endpoint URL to the authentication API.
     *
     * @return the URI representing the authentication endpoint in the form of a String.
     * @throws SsoJwtException if it fails to get the authentication URI.
     */
    protected abstract String getAuthUri() throws SsoJwtException;

    /**
     * Get the endpoint URL to the token API.
     *
     * @return the URI representing the token endpoint in the form of a String.
     * @throws SsoJwtException if it fails to get the token URI.
     */
    protected abstract String getTokenUri() throws SsoJwtException;

    /**
     * Check if the service is enabled.
     *
     * @return always <tt>true</tt>
     */
    @Override
    public boolean isEnabled() {
        return true;
    }

    /**
     * Get the "client id" used when communicating with the OpenID Connect server.
     *
     * @return a String representing the OpenID Connect Client ID.
     */
    protected String getClientId() {
        return ssoSettings.getString(SsoSettingKeys.SSO_OPENID_CLIENT_ID);
    }

    /**
     * Get the "client secret" used when communicating with the OpenID Connect server.
     *
     * @return a String representing the OpenID Connect Client Secret.
     */
    protected String getClientSecret() {
        return ssoSettings.getString(SsoSettingKeys.SSO_OPENID_CLIENT_SECRET);
    }

    @Override
    public String getLoginUri(final String state, final URI redirectUri) {
        try {
            final URIBuilder uri = new URIBuilder(getAuthUri());

            uri.addParameter("scope", "openid");
            uri.addParameter("response_type", "code");
            uri.addParameter("client_id", getClientId());
            uri.addParameter("state", state);
            uri.addParameter("redirect_uri", redirectUri.toString());

            return uri.toString();
        } catch (Exception e) {
            logger.warn("Failed to construct SSO URI", e);
        }
        return null;
    }

    /**
     *
     * @throws SsoJwtException if an {@link IOException} is caught or the {@link #getTokenUri} method fails.
     */
    @Override
    public JsonObject getAccessToken(final String authCode, final URI redirectUri) throws SsoJwtException {
        // FIXME: switch to HttpClient implementation: better performance and connection caching

        try {
            URL url = new URL(getTokenUri());

            final HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

            urlConnection.setRequestMethod("POST");
            urlConnection.setRequestProperty(HttpHeaders.CONTENT_TYPE, URLEncodedUtils.CONTENT_TYPE);

            final List<NameValuePair> parameters = new ArrayList<NameValuePair>();
            parameters.add(new BasicNameValuePair("grant_type", "authorization_code"));
            parameters.add(new BasicNameValuePair("code", authCode));
            parameters.add(new BasicNameValuePair("client_id", getClientId()));

            final String clientSecret = getClientSecret();
            if (clientSecret != null && !clientSecret.isEmpty()) {
                parameters.add(new BasicNameValuePair("client_secret", clientSecret));
            }

            parameters.add(new BasicNameValuePair("redirect_uri", redirectUri.toString()));

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
        } catch (IOException ioe) {
            throw new SsoJwtException(ioe);
        }
    }

}
