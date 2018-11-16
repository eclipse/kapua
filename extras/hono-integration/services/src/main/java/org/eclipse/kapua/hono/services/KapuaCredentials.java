/*******************************************************************************
 * Copyright (c) 2018 Red Hat Inc and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Red Hat Inc - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.hono.services;

import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import org.eclipse.hono.service.auth.device.DeviceCredentials;
import org.eclipse.hono.util.CredentialsConstants;
import org.eclipse.hono.util.CredentialsObject;
import org.springframework.security.crypto.bcrypt.BCrypt;

import java.time.Instant;
import java.util.Objects;

public class KapuaCredentials implements DeviceCredentials {



    private final String tenantId;
    private final String authId;

    private String password;

    /**
     * Creates credentials for a tenant and authentication identifier.
     *
     * @param tenantId The tenant that the device belongs to.
     * @param authId The identifier that the device uses for authentication.
     */
    protected KapuaCredentials(final String tenantId, final String authId) {
        this.tenantId = tenantId;
        this.authId = authId;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final String getAuthId() {
        return authId;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final String getTenantId() {
        return tenantId;
    }

    @Override
    public final boolean validate(final CredentialsObject credentialsOnRecord) {
        Objects.requireNonNull(credentialsOnRecord);
        if (!getAuthId().equals(credentialsOnRecord.getAuthId())) {
            return false;
        } else if (!getType().equals(credentialsOnRecord.getType())) {
            return false;
        } else if (!credentialsOnRecord.isEnabled()) {
            return false;
        } else {

            final JsonArray secrets = credentialsOnRecord.getSecrets();

            if (secrets == null) {
                throw new IllegalArgumentException("credentials do not contain any secret");
            } else {
                return validate(secrets);
            }
        }
    }

    private boolean validate(final JsonArray secretsOnRecord) {
        return secretsOnRecord.stream().filter(obj -> obj instanceof JsonObject).anyMatch(obj -> {
            final JsonObject candidateSecret = (JsonObject) obj;
            return isInValidityPeriod(candidateSecret, Instant.now()) && matchesCredentials(candidateSecret);
        });
    }

    private boolean isInValidityPeriod(final JsonObject secret, final Instant instant) {
        final Instant notBefore = CredentialsObject.getNotBefore(secret);
        final Instant notAfter = CredentialsObject.getNotAfter(secret);
        return (notBefore == null || instant.isAfter(notBefore)) && (notAfter == null || instant.isBefore(notAfter));
    }

    /**
     * {@inheritDoc}
     *
     * @return Always {@link CredentialsConstants#SECRETS_TYPE_HASHED_PASSWORD}
     */
    @Override
    public final String getType() {
        return CredentialsConstants.SECRETS_TYPE_HASHED_PASSWORD;
    }

    /**
     * Checks if the credentials provided by the device match a secret on record for the device.
     *
     * @param candidateSecret The secret to match against.
     * @return {@code true} if the credentials match.
     */
    public boolean matchesCredentials(JsonObject candidateSecret) {
        String pwdHash = candidateSecret.getString(CredentialsConstants.FIELD_SECRETS_KEY);
        if (pwdHash == null) {
            return false;
        }

       return BCrypt.checkpw(password, pwdHash);
    }

    public static DeviceCredentials create(String username, String password, boolean singleTenant) {
        Objects.requireNonNull(username);
        Objects.requireNonNull(password);

        KapuaCredentials credentials;

        String[] userComponents = username.split("@", 2);
        if (userComponents.length != 2) {
            //LOG.trace("username does not comply with expected pattern [<authId>@<tenantId>]", username);
            return null;
        } else {
            credentials = new KapuaCredentials(userComponents[1], userComponents[0]);
        }
        credentials.password = password;
        return credentials;
    }
}
