/*******************************************************************************
 * Copyright (c) 2020, 2022 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.plugin.sso.openid.provider;

import com.google.common.base.Strings;
import org.eclipse.kapua.commons.util.log.ConfigurationPrinter;
import org.eclipse.kapua.plugin.sso.openid.exception.OpenIDIllegalArgumentException;
import org.eclipse.kapua.plugin.sso.openid.exception.uri.OpenIDIllegalUriException;
import org.eclipse.kapua.plugin.sso.openid.exception.uri.OpenIDJwtUriException;
import org.eclipse.kapua.plugin.sso.openid.exception.uri.OpenIDUriException;
import org.eclipse.kapua.plugin.sso.openid.provider.setting.OpenIDSetting;
import org.eclipse.kapua.plugin.sso.openid.provider.setting.OpenIDSettingKeys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonString;
import javax.json.JsonValue;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Optional;

/**
 * Single Sign On utility class.
 */
public final class OpenIDUtils {

    private static final Logger logger = LoggerFactory.getLogger(OpenIDUtils.class);

    private static final String DEFAULT_SSO_OPENID_CONF_PATH = ".well-known/openid-configuration";

    private OpenIDUtils() {
    }

    /**
     * Attempts to retrieve a URI from the Well-Known OpenId Configuration using the given property.
     *
     * @param property the property to get from the JSON response.
     * @param openIdConfPath the OpendID Connect configuration path.
     * @return an Optional with a {@link URI} corresponding to the given property if everything is fine, otherwise
     * an empty Optional.
     * @throws OpenIDUriException if an {@link IOException}, a {@link MalformedURLException} or a {@link URISyntaxException} is caught.
     */
    public static Optional<URI> getConfigUri(String property, String openIdConfPath) throws OpenIDUriException {
        final JsonObject jsonObject;

        ConfigurationPrinter reqLogger =
                ConfigurationPrinter
                        .create()
                        .withLogger(logger)
                        .withLogLevel(ConfigurationPrinter.LogLevel.DEBUG)
                        .withTitle("OpenID Provider Configuration Information")
                        .addParameter("Requested property", property)
                        .addParameter("From well-known path", openIdConfPath);
        try {
            // Read .well-known resource
            try (final InputStream stream = new URL(openIdConfPath).openStream()) {
                // Parse json response
                jsonObject = Json.createReader(stream).readObject();
            }

            // Get property
            final JsonValue uriJsonValue = jsonObject.get(property);

            // test result
            if (uriJsonValue instanceof JsonString) {
                Optional<URI> optionalURI = Optional.of(new URI(((JsonString) uriJsonValue).getString()));
                reqLogger.addParameter("Result value", optionalURI.get());
                return optionalURI;
            }

            // return
            reqLogger.addHeader("No value found");
            return Optional.empty();
        } catch (MalformedURLException mue) {
            logger.error("openIdConfPath parameter is malformed: {}", mue.getLocalizedMessage(), mue);
            throw new OpenIDJwtUriException(mue);
        } catch (IOException ioe) {
            logger.error("IOException occurred while reading the well-known resource: {}", ioe.getLocalizedMessage(), ioe);
            throw new OpenIDJwtUriException(ioe);
        } catch (URISyntaxException urise) {
            logger.error("Unable to extract the required property from the openIdConfPath: {}", urise.getLocalizedMessage(), urise);
            throw new OpenIDJwtUriException(urise);
        } finally {
            reqLogger.printLog();
        }

    }

    /**
     * Retrieve the OpenID Connect discovery endpoint (the provider's Well-Known Configuration Endpoint).
     *
     * @param issuer the URI representing the JWT Issuer.
     * @return a String representing the discovery endpoint.
     * @throws OpenIDIllegalArgumentException if it cannot retrieve the OpenID configuration path or if the generated OpenID Connect discovery endpoint is a
     * malformed URL
     */
    public static String getOpenIdConfPath(final URI issuer) throws OpenIDIllegalArgumentException {
        return getOpenIdConfPath(issuer.toString());
    }

    /**
     * Retrieve the OpenID Connect discovery endpoint (the provider's Well-Known Configuration Endpoint).
     *
     * @param issuer the String representing the JWT Issuer URI.
     * @return a String representing the discovery endpoint.
     * @throws OpenIDIllegalArgumentException if it cannot retrieve the OpenID configuration path or if the generated OpenID Connect discovery endpoint is a
     * malformed URL
     */
    public static String getOpenIdConfPath(String issuer) throws OpenIDIllegalArgumentException {
        String openIDConfPathSuffix = OpenIDSetting.getInstance().getString(OpenIDSettingKeys.SSO_OPENID_CONF_PATH, DEFAULT_SSO_OPENID_CONF_PATH);
        if (Strings.isNullOrEmpty(openIDConfPathSuffix)) {
            throw new OpenIDIllegalArgumentException(OpenIDSettingKeys.SSO_OPENID_CONF_PATH.key(), openIDConfPathSuffix);
        }
        String openIdConfPath = issuer + "/" + openIDConfPathSuffix;
        try {
            new URL(openIdConfPath);
            return openIdConfPath;
        } catch (MalformedURLException mue) {
            throw new OpenIDIllegalUriException("openIdConfPath", openIdConfPath);
        }
    }
}
