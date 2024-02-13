/*******************************************************************************
 * Copyright (c) 2017, 2022 Red Hat Inc and others.
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Red Hat Inc - initial API and implementation
 *     Eurotech
 *******************************************************************************/
package org.eclipse.kapua.plugin.sso.openid.provider.internal;

import org.eclipse.kapua.plugin.sso.openid.JwtProcessor;
import org.jose4j.jwt.consumer.JwtContext;

public class DisabledJwtProcessor implements JwtProcessor {

    @Override
    public void close() throws Exception {

    }

    @Override
    public boolean validate(String jwt) {
        return false;
    }

    @Override
    public JwtContext process(String jwt) {
        return null;
    }

    @Override
    public String getExternalIdClaimName() {
        return null;
    }

    @Override
    public String getExternalUsernameClaimName() {
        return null;
    }

    @Override
    public String getId() {
        return DisabledOpenIDService.DISABLED_ID;
    }
}
