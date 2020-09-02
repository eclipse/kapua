/*******************************************************************************
 * Copyright (c) 2020 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.sso.provider;

import com.google.common.base.Strings;
import org.eclipse.kapua.sso.exception.SsoIllegalArgumentException;
import org.eclipse.kapua.sso.exception.uri.SsoIllegalUriException;
import org.eclipse.kapua.sso.exception.uri.SsoJwtUriException;
import org.eclipse.kapua.sso.exception.uri.SsoUriException;
import org.eclipse.kapua.sso.provider.setting.SsoSetting;
import org.eclipse.kapua.sso.provider.setting.SsoSettingKeys;

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
public final class SingleSignOnUtils {

    private static final String DEFAULT_SSO_OPENID_CONF_PATH = ".well-known/openid-configuration";

    private SingleSignOnUtils() {
    }

    /**
     * Attempts to retrieve a URI from the Well-Known OpenId Configuration using the given proeprty.
     *
     * @param property the property to get from the JSON response.
     * @param openIdConfPath the OpendID Connect configuration path.
     * @return an Optional with a {@link URI} corresponding to the given property if everything is fine, otherwise
     * an empty Optional.
     * @throws SsoUriException if an {@link IOException} is caught.
     */
    public static Optional<URI> getConfigUri(String property, String openIdConfPath) throws SsoUriException {
        final JsonObject jsonObject;

        // Read .well-known resource
        try (final InputStream stream = new URL(openIdConfPath).openStream()) {
            // Parse json response
            jsonObject = Json.createReader(stream).readObject();
        } catch (IOException ioe) {
            throw new SsoJwtUriException(ioe);
        }

        // Get property
        final JsonValue uriJsonValue = jsonObject.get(property);

        // test result
        try {
            if (uriJsonValue instanceof JsonString) {
                return Optional.of(new URI(((JsonString) uriJsonValue).getString()));
            }
        } catch (URISyntaxException urise) {
            throw new IllegalAccessError("Unable to retrieve Config Uri");
        }

        // return
        return Optional.empty();
    }

    /**
     * Retrieve the OpenID Connect discovery endpoint (the provider's Well-Known Configuration Endpoint).
     *
     * @param issuer the URI representing the JWT Issuer.
     * @return a String representing the discovery endpoint.
     * @throws SsoIllegalArgumentException if it cannot retrieve the OpenID configuration path or if the generated OpenID Connect discovery endpoint is a
     * malformed URL
     */
    public static String getOpenIdConfPath(final URI issuer) throws SsoIllegalArgumentException {
        return getOpenIdConfPath(issuer.toString());
    }

    /**
     * Retrieve the OpenID Connect discovery endpoint (the provider's Well-Known Configuration Endpoint).
     *
     * @param issuer the String representing the JWT Issuer URI.
     * @return a String representing the discovery endpoint.
     * @throws SsoIllegalArgumentException if it cannot retrieve the OpenID configuration path or if the generated OpenID Connect discovery endpoint is a
     * malformed URL
     */
    public static String getOpenIdConfPath(String issuer) throws SsoIllegalArgumentException {
        String openIDConfPathSuffix = SsoSetting.getInstance().getString(SsoSettingKeys.SSO_OPENID_CONF_PATH, DEFAULT_SSO_OPENID_CONF_PATH);
        if (Strings.isNullOrEmpty(openIDConfPathSuffix)) {
            throw new SsoIllegalArgumentException(SsoSettingKeys.SSO_OPENID_CONF_PATH.key(), openIDConfPathSuffix);
        }
        String openIdConfPath = issuer + "/" + openIDConfPathSuffix;
        try {
            new URL(openIdConfPath);
            return openIdConfPath;
        } catch (MalformedURLException mue) {
            throw new SsoIllegalUriException("openIdConfPath", openIdConfPath);
        }
    }
}
