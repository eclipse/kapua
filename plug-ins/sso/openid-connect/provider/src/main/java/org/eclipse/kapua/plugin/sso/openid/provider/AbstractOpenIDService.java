/*******************************************************************************
 * Copyright (c) 2017, 2021 Red Hat Inc and others.
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Red Hat Inc - initial API and implementation
 *     Eurotech
 *******************************************************************************/
package org.eclipse.kapua.plugin.sso.openid.provider;

import org.apache.http.HttpHeaders;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.message.BasicNameValuePair;
import org.eclipse.kapua.commons.util.log.ConfigurationPrinter;
import org.eclipse.kapua.plugin.sso.openid.OpenIDService;
import org.eclipse.kapua.plugin.sso.openid.exception.OpenIDTokenException;
import org.eclipse.kapua.plugin.sso.openid.exception.OpenIDException;
import org.eclipse.kapua.plugin.sso.openid.exception.uri.OpenIDLoginUriException;
import org.eclipse.kapua.plugin.sso.openid.exception.uri.OpenIDLogoutUriException;
import org.eclipse.kapua.plugin.sso.openid.provider.setting.OpenIDSetting;
import org.eclipse.kapua.plugin.sso.openid.provider.setting.OpenIDSettingKeys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.json.JsonValue;
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
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * This class represents an abstract OpenID Connect single sign on Service.
 * Each OpenID provider must provide its own concrete implementation by extending this class.
 */
public abstract class AbstractOpenIDService implements OpenIDService {


    private static final Logger logger = LoggerFactory.getLogger(AbstractOpenIDService.class);

    private static final String HIDDEN_SECRET = "****";
    private static final List<String> SECRET_TOKENS = Arrays.asList("access_token", "id_token", "refresh_token");

    protected OpenIDSetting openIDSettings;

    /**
     * Abstract OpenID constructor.
     *
     * @param openIDSettings the {@link OpenIDSetting} instance.
     */
    public AbstractOpenIDService(OpenIDSetting openIDSettings) {
        this.openIDSettings = openIDSettings;
    }

    /**
     * Get the endpoint URL to the authentication API.
     *
     * @return the URI representing the authentication endpoint in the form of a String.
     * @throws OpenIDException if it fails to get the authentication URI.
     */
    protected abstract String getAuthUri() throws OpenIDException;

    /**
     * Get the endpoint URL to the token API.
     *
     * @return the URI representing the token endpoint in the form of a String.
     * @throws OpenIDException if it fails to get the token URI.
     */
    protected abstract String getTokenUri() throws OpenIDException;

    /**
     * Get the endpoint URL for logging out.
     *
     * @return the URI representing the logout endpoint in the form of a String.
     * @throws OpenIDException if it fails to get the logout URI.
     */
    protected abstract String getLogoutUri() throws OpenIDException;

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
        return openIDSettings.getString(OpenIDSettingKeys.SSO_OPENID_CLIENT_ID);
    }

    /**
     * Get the "client secret" used when communicating with the OpenID Connect server.
     *
     * @return a String representing the OpenID Connect Client Secret.
     */
    protected String getClientSecret() {
        return openIDSettings.getString(OpenIDSettingKeys.SSO_OPENID_CLIENT_SECRET);
    }

    @Override
    public String getLoginUri(final String state, final URI redirectUri) throws OpenIDLoginUriException {
        ConfigurationPrinter reqLogger =
                ConfigurationPrinter
                        .create()
                        .withLogger(logger)
                        .withLogLevel(ConfigurationPrinter.LogLevel.DEBUG)
                        .withTitle("OpenID login URI");
        try {
            final String authUri = getAuthUri();
            final URIBuilder uri = new URIBuilder(authUri);
            reqLogger.addParameter("URI", authUri);

            uri.addParameter("scope", "openid");
            uri.addParameter("response_type", "code");
            uri.addParameter("client_id", getClientId());
            uri.addParameter("state", state);
            uri.addParameter("redirect_uri", redirectUri.toString());

            // logging parameters
            reqLogger.openSection("Parameters:");
            for (NameValuePair nameValuePair : uri.getQueryParams()) {
                reqLogger.addParameter(nameValuePair.getName(), nameValuePair.getValue());
            }

            return uri.toString();
        } catch (URISyntaxException use) {
            logger.error("Authentication URI is not a valid URI: {}", use.getLocalizedMessage(), use);
            throw new OpenIDLoginUriException(use);
        } catch (OpenIDException se) {
            logger.error("Error while retrieving the authentication URI {}", se.getLocalizedMessage(), se);
            throw new OpenIDLoginUriException(se);
        } finally {
            reqLogger.printLog();
        }
    }

    @Override
    public String getLogoutUri(final String idTokenHint, final URI postLogoutRedirectUri, final String state) throws OpenIDLogoutUriException {
        ConfigurationPrinter reqLogger =
                ConfigurationPrinter
                        .create()
                        .withLogger(logger)
                        .withLogLevel(ConfigurationPrinter.LogLevel.DEBUG)
                        .withTitle("OpenID logout URI");
        try {
            final String logoutUri = getLogoutUri();
            final URIBuilder uri = new URIBuilder(logoutUri);
            reqLogger.addParameter("URI", logoutUri);

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
            reqLogger.openSection("Parameters:");
            for (NameValuePair nameValuePair : uri.getQueryParams()) {
                String name = nameValuePair.getName();
                if (name.equals("id_token_hint")) {
                    reqLogger.addParameter(name, HIDDEN_SECRET);
                } else {
                    reqLogger.addParameter(name, nameValuePair.getValue());
                }
            }

            return uri.toString();
        } catch (URISyntaxException use) {
            logger.error("Logout URI is not a valid URI: {}", use.getLocalizedMessage(), use);
            throw new OpenIDLogoutUriException(use);
        } catch (OpenIDException se) {
            logger.error("Error while retrieving the logout URI {}", se.getLocalizedMessage(), se);
            throw new OpenIDLogoutUriException(se);
        } finally {
            reqLogger.printLog();
        }
    }

    /**
     * @throws OpenIDTokenException if an {@link IOException} is caught or the {@link #getTokenUri} method fails.
     */
    @Override
    public JsonObject getTokens(final String authCode, final URI redirectUri) throws OpenIDTokenException {
        // FIXME: switch to HttpClient implementation: better performance and connection caching

        ConfigurationPrinter reqLogger =
                ConfigurationPrinter
                        .create()
                        .withLogger(logger)
                        .withLogLevel(ConfigurationPrinter.LogLevel.DEBUG)
                        .withTitle("OpenID tokens HTTP request");
        try {
            URL url = new URL(getTokenUri());
            reqLogger.addParameter("URL", url);

            final HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

            urlConnection.setRequestMethod("POST");
            urlConnection.setRequestProperty(HttpHeaders.CONTENT_TYPE, URLEncodedUtils.CONTENT_TYPE);
            reqLogger.addParameter("HTTP request method", urlConnection.getRequestMethod());

            final List<NameValuePair> parameters = new ArrayList<>();
            parameters.add(new BasicNameValuePair("grant_type", "authorization_code"));
            parameters.add(new BasicNameValuePair("code", authCode));
            parameters.add(new BasicNameValuePair("client_id", getClientId()));

            final String clientSecret = getClientSecret();
            if (clientSecret != null && !clientSecret.isEmpty()) {
                parameters.add(new BasicNameValuePair("client_secret", clientSecret));
            }

            parameters.add(new BasicNameValuePair("redirect_uri", redirectUri.toString()));

            reqLogger.openSection("Parameters:");
            for (NameValuePair nameValuePair : parameters) {
                String name = nameValuePair.getName();
                if (name.equals("code") || name.equals("client_secret")) {
                    reqLogger.addParameter(name, HIDDEN_SECRET);
                } else {
                    reqLogger.addParameter(name, nameValuePair.getValue());
                }
            }
            reqLogger.closeSection();

            final UrlEncodedFormEntity entity = new UrlEncodedFormEntity(parameters);

            // Send post request
            urlConnection.setDoOutput(true);
            try (final OutputStream outputStream = urlConnection.getOutputStream()) {
                entity.writeTo(outputStream);
            }

            // parse result
            try (InputStream stream = urlConnection.getInputStream(); JsonReader jsonReader = Json.createReader(stream)) {
                reqLogger.addParameter("Response code", urlConnection.getResponseCode());
                JsonObject result = jsonReader.readObject();
                reqLogger.openSection("Response body:");
                for (Map.Entry<String, JsonValue> entry : result.entrySet()) {
                    if (SECRET_TOKENS.contains(entry.getKey())) {
                        reqLogger.addParameter(entry.getKey(), HIDDEN_SECRET);
                    } else {
                        reqLogger.addParameter(entry.getKey(), entry.getValue());
                    }
                }
                logger.debug("Successfully obtained tokens.");
                return result;
            }
        } catch (OpenIDException se) {
            logger.error("Error while retrieving the String of the token API endpoint {}", se.getLocalizedMessage(), se);
            throw new OpenIDTokenException(se);
        } catch (MalformedURLException mue) {
            logger.error("Malformed token API endpoint URL {}", mue.getLocalizedMessage(), mue);
            throw new OpenIDTokenException(mue);
        } catch (ProtocolException pe) {
            logger.error("Wrong HTTP request method {}", pe.getLocalizedMessage(), pe);
            throw new OpenIDTokenException(pe);
        } catch (UnsupportedEncodingException uee) {
            logger.error("Unsupported HTTP request default encoding {}", uee.getLocalizedMessage(), uee);
            throw new OpenIDTokenException(uee);
        } catch (IOException ioe) {
            logger.error("Error while getting the tokens {}", ioe.getLocalizedMessage(), ioe);
            throw new OpenIDTokenException(ioe);
        } finally {
            reqLogger.printLog();
        }
    }

}
