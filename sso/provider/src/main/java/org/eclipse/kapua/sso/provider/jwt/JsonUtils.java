/*******************************************************************************
 * Copyright (c) 2019, 2020 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.sso.provider.jwt;

import org.eclipse.kapua.sso.exception.uri.SsoJwtUriException;
import org.eclipse.kapua.sso.exception.uri.SsoUriException;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonString;
import javax.json.JsonValue;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Optional;

/**
 * Json utility class.
 */
public final class JsonUtils {

    private JsonUtils() {
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
}
