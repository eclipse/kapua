/*******************************************************************************
 * Copyright (c) 2011, 2017 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.service.authentication.shiro.setting;

import org.eclipse.kapua.commons.setting.SettingKey;

/**
 * Authentication setting key
 *
 * @since 1.0
 *
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
    AUTHENTICATION_EVENT_ADDRESS("authentication.eventAddress");

    private String key;

    private KapuaAuthenticationSettingKeys(String key) {
        this.key = key;
    }

    public String key() {
        return key;
    }
}
