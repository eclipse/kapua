/*******************************************************************************
 * Copyright (c) 2016, 2022 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.service.authentication.shiro.setting;

import org.eclipse.kapua.commons.setting.SettingKey;
import org.eclipse.kapua.service.authentication.JwtCredentials;
import org.eclipse.kapua.service.user.User;

/**
 * Authentication setting key
 *
 * @since 1.0
 */
public enum KapuaAuthenticationSettingKeys implements SettingKey {
    AUTHENTICATION_KEY("authentication.key"), //

    AUTHENTICATION_TOKEN_EXPIRE_AFTER("authentication.token.expire.after"),//
    AUTHENTICATION_REFRESH_TOKEN_EXPIRE_AFTER("authentication.refresh.token.expire.after"),//

    // AUTHENTICATION_TOKEN_JWT_SECRET("authentication.token.jwt.secret"),//
    AUTHENTICATION_SESSION_JWT_ISSUER("authentication.session.jwt.issuer"),//
    AUTHENTICATION_SESSION_JWT_CACHE_ENABLE("authentication.session.jwt.cache.enabled"), //
    AUTHENTICATION_SESSION_JWT_CACHE_CACHE_TTL("authentication.session.jwt.cache.ttl"), //

    AUTHENTICATION_CREDENTIAL_USERPASS_CACHE_ENABLE("authentication.credential.userpass.cache.enabled"), //
    AUTHENTICATION_CREDENTIAL_USERPASS_CACHE_CACHE_TTL("authentication.credential.userpass.cache.ttl"), //
    AUTHENTICATION_CREDENTIAL_USERPASS_PASSWORD_MINLENGTH("authentication.credential.userpass.password.minlength"), //

    AUTHENTICATION_CREDENTIAL_AUDIENCE_ALLOWED("authentication.credential.jwt.audience.allowed"), //
    AUTHENTICATION_CREDENTIAL_JWT_CACHE_ENABLE("authentication.credential.jwt.cache.enabled"), //
    AUTHENTICATION_CREDENTIAL_JWT_CACHE_CACHE_TTL("authentication.credential.jwt.cache.ttl"), //
    AUTHENTICATION_CREDENTIAL_ISSUER_ALLOWED("authentication.credential.jwt.issuer.allowed"),

    AUTHENTICATION_CREDENTIAL_APIKEY_PRE_LENGTH("authentication.credential.apiKey.pre.length"),//
    AUTHENTICATION_CREDENTIAL_APIKEY_PRE_SEPARATOR("authentication.credential.apiKey.pre.separator"),//
    AUTHENTICATION_CREDENTIAL_APIKEY_KEY_LENGTH("authentication.credential.apiKey.key.length"), //
    AUTHENTICATION_CREDENTIAL_APIKEY_CACHE_ENABLE("authentication.credential.apiKey.cache.enabled"), //
    AUTHENTICATION_CREDENTIAL_APIKEY_CACHE_TTL("authentication.credential.apiKey.cache.ttl"), //

    //event queues
    AUTHENTICATION_EVENT_ADDRESS("authentication.eventAddress"),

    // to enable the registration service
    AUTHENTICATION_REGISTRATION_SERVICE_ENABLED("authentication.registration.service.enabled"),

    // SSO
    /**
     * Whether populate the {@link User#getExternalId()} when authenticating {@link JwtCredentials}, if not populated.
     *
     * @since 2.0.0
     */
    AUTHENTICATION_SSO_USER_EXTERNAL_ID_AUTOFILL("authentication.sso.user.external.id.autofill"),
    /**
     * Whether populate the {@link User#getExternalUsername()} when authenticating {@link JwtCredentials}, if not populated.
     *
     * @since 2.0.0
     */
    AUTHENTICATION_SSO_USER_EXTERNAL_USERNAME_AUTOFILL("authentication.sso.user.external.username.autofill"),

    // MFA authenticator service configuration
    AUTHENTICATION_MFA_TIME_STEP_SIZE("authentication.mfa.time.step.size"),  // the time step size, in seconds, min > 0
    AUTHENTICATION_MFA_WINDOW_SIZE("authentication.mfa.window.size"),  // number of windows of size timeStepSizeInMillis checked during the validation, min > 0
    AUTHENTICATION_MFA_SCRATCH_CODES_NUMBER("authentication.mfa.scratch.codes.number"),  // number of scratch codes, min is 0 max is 1000
    AUTHENTICATION_MFA_CODE_DIGITS_NUMBER("authentication.mfa.code.digits.number"),  // the number of digits in the generated code, min is 6 max is 8
    AUTHENTICATION_MFA_TRUST_KEY_DURATION("authentication.mfa.trust.key.duration");  // machine trust key duration in days

    private String key;

    private KapuaAuthenticationSettingKeys(String key) {
        this.key = key;
    }

    @Override
    public String key() {
        return key;
    }
}
