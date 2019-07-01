/*******************************************************************************
 * Copyright (c) 2019 Eurotech and/or its affiliates and others
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

public final class JwtUtils {

    private JwtUtils() {
    }

    /**
     * Attempts to retrieve a URI from the Well-Known OpenId Configuration given a property
     *
     * @param property
     * @return
     * @throws IOException
     */
    public static Optional<URI> retrieveJwtUri(String property, String openIdConfPath) throws IOException {
        final JsonObject jsonObject;

        // Read .well-known resource
        try (final InputStream stream = new URL(openIdConfPath).openStream()) {
            // Parse json response
            jsonObject = Json.createReader(stream).readObject();
        }

        // Get jwt property
        final JsonValue uriJsonValue = jsonObject.get(property);

        // test result
        try {
            if (uriJsonValue instanceof JsonString) {
                return Optional.of(new URI(((JsonString) uriJsonValue).getString()));
            }
        } catch (URISyntaxException urise) {
            throw new IllegalAccessError("Unable to retrieve Jwt Uri");
        }

        // return
        return Optional.empty();
    }
}
