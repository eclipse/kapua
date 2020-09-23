/*******************************************************************************
 * Copyright (c) 2017, 2020 Red Hat Inc and others.
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

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;

import org.eclipse.kapua.sso.SingleSignOnService;
import org.eclipse.kapua.sso.exception.SsoAccessTokenException;
import org.eclipse.kapua.sso.exception.SsoException;
import org.eclipse.kapua.sso.exception.uri.SsoLoginUriException;
import org.eclipse.kapua.sso.exception.uri.SsoLogoutUriException;
import org.eclipse.kapua.sso.provider.setting.SsoSetting;
import org.eclipse.kapua.sso.provider.setting.SsoSettingKeys;

import org.apache.http.HttpHeaders;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.message.BasicNameValuePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class represents an abstract SingleSignOnService.
 * Each sso provider must provide its own SingleSignOnService concrete implementation by extending this class.
 */
public abstract class AbstractSingleSignOnService implements SingleSignOnService {


    private static final Logger logger = LoggerFactory.getLogger(AbstractSingleSignOnService.class);

    private static final String HIDDEN_SECRET = "****";

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
     * @throws SsoException if it fails to get the authentication URI.
     */
    protected abstract String getAuthUri() throws SsoException;

    /**
     * Get the endpoint URL to the token API.
     *
     * @return the URI representing the token endpoint in the form of a String.
     * @throws SsoException if it fails to get the token URI.
     */
    protected abstract String getTokenUri() throws SsoException;

    /**
     * Get the endpoint URL for logging out.
     *
     * @return the URI representing the logout endpoint in the form of a String.
     * @throws SsoException if it fails to get the logout URI.
     */
    protected abstract String getLogoutUri() throws SsoException;

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
    public String getLoginUri(final String state, final URI redirectUri) throws SsoLoginUriException {
        StringBuilder logStr = new StringBuilder();
        logStr.append("Requested SSO login URI:");
        try {
            final String authUri = getAuthUri();
            final URIBuilder uri = new URIBuilder(authUri);
            logStr.append("\n\tURI: ").append(authUri);

            uri.addParameter("scope", "openid");
            uri.addParameter("response_type", "code");
            uri.addParameter("client_id", getClientId());
            uri.addParameter("state", state);
            uri.addParameter("redirect_uri", redirectUri.toString());

            // logging parameters
            logStr.append("\n\tParameters:");
            for (NameValuePair nameValuePair : uri.getQueryParams()) {
                logStr.append("\n\t\t").append(nameValuePair);
            }

            return uri.toString();
        } catch (URISyntaxException use) {
            logger.error("Authentication URI is not a valid URI: {}", use.getLocalizedMessage(), use);
            throw new SsoLoginUriException(use);
        } catch (SsoException se) {
            logger.error("Error while retrieving the authentication URI {}", se.getLocalizedMessage(), se);
            throw new SsoLoginUriException(se);
        } finally {
            logger.debug("{}", logStr);
        }
    }

    @Override
    public String getLogoutUri(final String idTokenHint, final URI postLogoutRedirectUri, final String state) throws SsoLogoutUriException {
        StringBuilder logStr = new StringBuilder();
        logStr.append("Requested SSO logout URI:");
        try {
            final String logoutUri = getLogoutUri();
            final URIBuilder uri = new URIBuilder(logoutUri);
            logStr.append("\n\tURI: ").append(logoutUri);

            if (idTokenHint != null) { // idTokenHint is recommended
                uri.addParameter("id_token_hint", idTokenHint);
            }
            if (postLogoutRedirectUri != null) { // post_logout_redirect_uri is optional
                uri.addParameter("post_logout_redirect_uri", postLogoutRedirectUri.toString());
            }
            if (state != null) { // state is optional
                uri.addParameter("state", state);
            }

            // logging parameters
            logStr.append("\n\tParameters:");
            for (NameValuePair nameValuePair : uri.getQueryParams()) {
                String name = nameValuePair.getName();
                if (name.equals("id_token_hint")) {
                    logStr.append("\n\t\t").append(name).append("=").append(HIDDEN_SECRET);
                } else {
                    logStr.append("\n\t\t").append(nameValuePair);
                }
            }

            return uri.toString();
        } catch (URISyntaxException use) {
            logger.error("Logout URI is not a valid URI: {}", use.getLocalizedMessage(), use);
            throw new SsoLogoutUriException(use);
        } catch (SsoException se) {
            logger.error("Error while retrieving the logout URI {}", se.getLocalizedMessage(), se);
            throw new SsoLogoutUriException(se);
        } finally {
            logger.debug("{}", logStr);
        }
    }

    /**
     * @throws SsoAccessTokenException if an {@link IOException} is caught or the {@link #getTokenUri} method fails.
     */
    @Override
    public JsonObject getAccessToken(final String authCode, final URI redirectUri) throws SsoAccessTokenException {
        // FIXME: switch to HttpClient implementation: better performance and connection caching

        StringBuilder logStr = new StringBuilder();
        logStr.append("SSO access token HTTP request:");
        try {
            URL url = new URL(getTokenUri());
            logStr.append("\n\tUrl: ").append(url);

            final HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

            urlConnection.setRequestMethod("POST");
            urlConnection.setRequestProperty(HttpHeaders.CONTENT_TYPE, URLEncodedUtils.CONTENT_TYPE);
            logStr.append("\n\tHTTP request method: ").append(urlConnection.getRequestMethod());

            final List<NameValuePair> parameters = new ArrayList<NameValuePair>();
            parameters.add(new BasicNameValuePair("grant_type", "authorization_code"));
            parameters.add(new BasicNameValuePair("code", authCode));
            parameters.add(new BasicNameValuePair("client_id", getClientId()));

            final String clientSecret = getClientSecret();
            if (clientSecret != null && !clientSecret.isEmpty()) {
                parameters.add(new BasicNameValuePair("client_secret", clientSecret));
            }

            parameters.add(new BasicNameValuePair("redirect_uri", redirectUri.toString()));

            logStr.append("\n\tParameters:");
            for (NameValuePair nameValuePair : parameters) {
                String name = nameValuePair.getName();
                if (name.equals("code") || name.equals("client_secret")) {
                    logStr.append("\n\t\t").append(name).append("=").append(HIDDEN_SECRET);
                } else {
                    logStr.append("\n\t\t").append(nameValuePair);
                }
            }

            final UrlEncodedFormEntity entity = new UrlEncodedFormEntity(parameters);

            // Send post request
            urlConnection.setDoOutput(true);
            try (final OutputStream outputStream = urlConnection.getOutputStream()) {
                entity.writeTo(outputStream);
            }

            // parse result
            try (InputStream stream = urlConnection.getInputStream(); JsonReader jsonReader = Json.createReader(stream)) {
                logStr.append("\n\tResponse code: ").append(urlConnection.getResponseCode());
                JsonObject result = jsonReader.readObject();
                logStr.append("\n\tResponse body:");
                logStr.append("\n\t\taccess_token: ").append(HIDDEN_SECRET);
                logStr.append("\n\t\tid_token: ").append(HIDDEN_SECRET);
                logger.debug("Successfully obtained access token.");
                return result;
            }
        } catch (SsoException se) {
            logger.error("Error while retrieving the String of the token API endpoint {}", se.getLocalizedMessage(), se);
            throw new SsoAccessTokenException(se);
        } catch (MalformedURLException mue) {
            logger.error("Malformed token API endpoint URL {}", mue.getLocalizedMessage(), mue);
            throw new SsoAccessTokenException(mue);
        } catch (ProtocolException pe) {
            logger.error("Wrong HTTP request method {}", pe.getLocalizedMessage(), pe);
            throw new SsoAccessTokenException(pe);
        } catch (UnsupportedEncodingException uee) {
            logger.error("Unsupported HTTP request default encoding {}", uee.getLocalizedMessage(), uee);
            throw new SsoAccessTokenException(uee);
        } catch (IOException ioe) {
            logger.error("Error while getting the access token {}", ioe.getLocalizedMessage(), ioe);
            throw new SsoAccessTokenException(ioe);
        } finally {
            logger.debug("{}", logStr);
        }
    }

}
