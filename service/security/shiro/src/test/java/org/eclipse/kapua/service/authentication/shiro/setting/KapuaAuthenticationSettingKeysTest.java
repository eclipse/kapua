/*******************************************************************************
 * Copyright (c) 2021, 2022 Eurotech and/or its affiliates and others
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

import org.eclipse.kapua.qa.markers.junit.JUnitTests;
import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.categories.Category;


@Category(JUnitTests.class)
public class KapuaAuthenticationSettingKeysTest {

    @Test
    public void keyTest() {
        Assert.assertEquals("Expected and actual values should be the same.", "authentication.key", KapuaAuthenticationSettingKeys.AUTHENTICATION_KEY.key());
        Assert.assertEquals("Expected and actual values should be the same.", "authentication.token.expire.after", KapuaAuthenticationSettingKeys.AUTHENTICATION_TOKEN_EXPIRE_AFTER.key());
        Assert.assertEquals("Expected and actual values should be the same.", "authentication.refresh.token.expire.after", KapuaAuthenticationSettingKeys.AUTHENTICATION_REFRESH_TOKEN_EXPIRE_AFTER.key());
        Assert.assertEquals("Expected and actual values should be the same.", "authentication.session.jwt.issuer", KapuaAuthenticationSettingKeys.AUTHENTICATION_SESSION_JWT_ISSUER.key());
        Assert.assertEquals("Expected and actual values should be the same.", "authentication.session.jwt.cache.enabled", KapuaAuthenticationSettingKeys.AUTHENTICATION_SESSION_JWT_CACHE_ENABLE.key());
        Assert.assertEquals("Expected and actual values should be the same.", "authentication.session.jwt.cache.ttl", KapuaAuthenticationSettingKeys.AUTHENTICATION_SESSION_JWT_CACHE_CACHE_TTL.key());
        Assert.assertEquals("Expected and actual values should be the same.", "authentication.credential.userpass.cache.enabled", KapuaAuthenticationSettingKeys.AUTHENTICATION_CREDENTIAL_USERPASS_CACHE_ENABLE.key());
        Assert.assertEquals("Expected and actual values should be the same.", "authentication.credential.userpass.cache.ttl", KapuaAuthenticationSettingKeys.AUTHENTICATION_CREDENTIAL_USERPASS_CACHE_CACHE_TTL.key());
        Assert.assertEquals("Expected and actual values should be the same.", "authentication.credential.userpass.password.minlength", KapuaAuthenticationSettingKeys.AUTHENTICATION_CREDENTIAL_USERPASS_PASSWORD_MINLENGTH.key());
        Assert.assertEquals("Expected and actual values should be the same.", "authentication.credential.jwt.audience.allowed", KapuaAuthenticationSettingKeys.AUTHENTICATION_CREDENTIAL_AUDIENCE_ALLOWED.key());
        Assert.assertEquals("Expected and actual values should be the same.", "authentication.credential.jwt.cache.enabled", KapuaAuthenticationSettingKeys.AUTHENTICATION_CREDENTIAL_JWT_CACHE_ENABLE.key());
        Assert.assertEquals("Expected and actual values should be the same.", "authentication.credential.jwt.cache.ttl", KapuaAuthenticationSettingKeys.AUTHENTICATION_CREDENTIAL_JWT_CACHE_CACHE_TTL.key());
        Assert.assertEquals("Expected and actual values should be the same.", "authentication.credential.jwt.issuer.allowed", KapuaAuthenticationSettingKeys.AUTHENTICATION_CREDENTIAL_ISSUER_ALLOWED.key());
        Assert.assertEquals("Expected and actual values should be the same.", "authentication.credential.apiKey.pre.length", KapuaAuthenticationSettingKeys.AUTHENTICATION_CREDENTIAL_APIKEY_PRE_LENGTH.key());
        Assert.assertEquals("Expected and actual values should be the same.", "authentication.credential.apiKey.pre.separator", KapuaAuthenticationSettingKeys.AUTHENTICATION_CREDENTIAL_APIKEY_PRE_SEPARATOR.key());
        Assert.assertEquals("Expected and actual values should be the same.", "authentication.credential.apiKey.key.length", KapuaAuthenticationSettingKeys.AUTHENTICATION_CREDENTIAL_APIKEY_KEY_LENGTH.key());
        Assert.assertEquals("Expected and actual values should be the same.", "authentication.credential.apiKey.cache.enabled", KapuaAuthenticationSettingKeys.AUTHENTICATION_CREDENTIAL_APIKEY_CACHE_ENABLE.key());
        Assert.assertEquals("Expected and actual values should be the same.", "authentication.credential.apiKey.cache.ttl", KapuaAuthenticationSettingKeys.AUTHENTICATION_CREDENTIAL_APIKEY_CACHE_TTL.key());
        Assert.assertEquals("Expected and actual values should be the same.", "authentication.eventAddress", KapuaAuthenticationSettingKeys.AUTHENTICATION_EVENT_ADDRESS.key());
        Assert.assertEquals("Expected and actual values should be the same.", "authentication.registration.service.enabled", KapuaAuthenticationSettingKeys.AUTHENTICATION_REGISTRATION_SERVICE_ENABLED.key());
        Assert.assertEquals("Expected and actual values should be the same.", "authentication.mfa.time.step.size", KapuaAuthenticationSettingKeys.AUTHENTICATION_MFA_TIME_STEP_SIZE.key());
        Assert.assertEquals("Expected and actual values should be the same.", "authentication.mfa.window.size", KapuaAuthenticationSettingKeys.AUTHENTICATION_MFA_WINDOW_SIZE.key());
        Assert.assertEquals("Expected and actual values should be the same.", "authentication.mfa.scratch.codes.number", KapuaAuthenticationSettingKeys.AUTHENTICATION_MFA_SCRATCH_CODES_NUMBER.key());
        Assert.assertEquals("Expected and actual values should be the same.", "authentication.mfa.code.digits.number", KapuaAuthenticationSettingKeys.AUTHENTICATION_MFA_CODE_DIGITS_NUMBER.key());
    }
}