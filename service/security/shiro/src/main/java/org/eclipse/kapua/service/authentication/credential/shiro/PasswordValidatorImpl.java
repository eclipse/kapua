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
package org.eclipse.kapua.service.authentication.credential.shiro;

import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.commons.util.ArgumentValidator;
import org.eclipse.kapua.commons.util.CommonsValidationRegex;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.authentication.exception.PasswordLengthException;
import org.eclipse.kapua.service.authentication.shiro.CredentialServiceConfigurationManager;
import org.eclipse.kapua.service.authentication.shiro.CredentialServiceConfigurationManagerImpl;
import org.eclipse.kapua.storage.TxContext;

import java.util.Map;

public class PasswordValidatorImpl implements PasswordValidator {

    private final CredentialServiceConfigurationManager credentialServiceConfigurationManager;

    public PasswordValidatorImpl(CredentialServiceConfigurationManager credentialServiceConfigurationManager) {
        this.credentialServiceConfigurationManager = credentialServiceConfigurationManager;
    }

    @Override
    public void validatePassword(TxContext txContext, KapuaId scopeId, String plainPassword) throws KapuaException {
        // Argument Validation
        ArgumentValidator.notNull(scopeId, "scopeId");
        ArgumentValidator.notEmptyOrNull(plainPassword, "plainPassword");

        // Validate Password length
        int minPasswordLength = getMinimumPasswordLength(txContext, scopeId);
        if (plainPassword.length() < minPasswordLength || plainPassword.length() > CredentialServiceConfigurationManagerImpl.SYSTEM_MAXIMUM_PASSWORD_LENGTH) {
            throw new PasswordLengthException(minPasswordLength, CredentialServiceConfigurationManagerImpl.SYSTEM_MAXIMUM_PASSWORD_LENGTH);
        }
        // Validate Password regex
        ArgumentValidator.match(plainPassword, CommonsValidationRegex.PASSWORD_REGEXP, "plainPassword");
    }

    @Override
    public int getMinimumPasswordLength(TxContext txContext, KapuaId scopeId) throws KapuaException {
        // Argument Validation
        ArgumentValidator.notNull(scopeId, "scopeId");

        // Check access
        // None

        // Get system minimum password length
        int minPasswordLength = credentialServiceConfigurationManager.getSystemMinimumPasswordLength();
        Object minPasswordLengthConfigValue = getConfigValues(txContext, scopeId).get(CredentialServiceConfigurationManagerImpl.PASSWORD_MIN_LENGTH_ACCOUNT_CONFIG_KEY);

        if (!KapuaId.ANY.equals(scopeId)) {
            Object minPasswordLengthAccountConfigValue = getConfigValues(txContext, scopeId).get(minPasswordLengthConfigValue);
            if (minPasswordLengthAccountConfigValue != null) {
                minPasswordLength = Integer.parseInt(minPasswordLengthAccountConfigValue.toString());
            }
        }
        return minPasswordLength;
    }

    private Map<String, Object> getConfigValues(TxContext tx, KapuaId scopeId) throws KapuaException {
        return credentialServiceConfigurationManager.getConfigValues(tx, scopeId, true);
    }
}
