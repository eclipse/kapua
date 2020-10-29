/*******************************************************************************
 * Copyright (c) 2018, 2020 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.service.authentication;

import org.eclipse.kapua.service.authentication.credential.CredentialDomain;
import org.eclipse.kapua.service.authentication.credential.mfa.MfaCredentialOptionDomain;
import org.eclipse.kapua.service.authentication.credential.mfa.ScratchCodeDomain;
import org.eclipse.kapua.service.authentication.token.AccessTokenDomain;

public class AuthenticationDomains {

    private AuthenticationDomains() { }

    public static final CredentialDomain CREDENTIAL_DOMAIN = new CredentialDomain();

    public static final AccessTokenDomain ACCESS_TOKEN_DOMAIN = new AccessTokenDomain();

    public static final MfaCredentialOptionDomain MFA_CREDENTIAL_OPTION_DOMAIN = new MfaCredentialOptionDomain();

    public static final ScratchCodeDomain SCRATCH_CODE_DOMAIN = new ScratchCodeDomain();
}
